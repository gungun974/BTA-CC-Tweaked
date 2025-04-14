/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.monitor;

import dan200.computercraft.BlockPos;
import dan200.computercraft.client.FrameInfo;
import dan200.computercraft.client.gui.FixedWidthFontRenderer;
import dan200.computercraft.core.terminal.Terminal;
import dan200.computercraft.shared.util.DirectionUtil;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.util.helper.Direction;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import static dan200.computercraft.client.gui.FixedWidthFontRenderer.FONT_HEIGHT;
import static dan200.computercraft.client.gui.FixedWidthFontRenderer.FONT_WIDTH;

public class TileEntityMonitorRenderer extends TileEntityRenderer<TileMonitor> {
    /**
     * {@link TileMonitor#RENDER_MARGIN}, but a tiny bit of additional padding to ensure that there is no space between the monitor frame and contents.
     */
    private static final float MARGIN = (float) (TileMonitor.RENDER_MARGIN * 1.1);

    public TileEntityMonitorRenderer() {
    }

    private static void renderTerminal(ClientMonitor monitor, float xMargin, float yMargin) {
        MonitorRenderer renderType = MonitorRenderer.current();
        boolean redraw = monitor.pollTerminalChanged();
        if (redraw) {
            monitor.displayListCompiled = false;
        }

        monitor.createBuffer(renderType, xMargin, yMargin);

        switch (renderType) {
            case DisplayList:
                if (monitor.displayListCompiled) {
                    GL11.glCallList(monitor.displayList);
                }
                break;
        }
    }

    @Override
    public void doRender(Tessellator tessellator, TileMonitor monitor, double x, double y, double z, float partialTick) {
        // Render from the origin monitor
        ClientMonitor originTerminal = monitor.getClientMonitor();

        if (originTerminal == null) return;
        TileMonitor origin = originTerminal.getOrigin();
        BlockPos monitorPos = monitor.getPos();

        // Ensure each monitor terminal is rendered only once. We allow rendering a specific tile
        // multiple times in a single frame to ensure compatibility with shaders which may run a
        // pass multiple times.
        long renderFrame = FrameInfo.getRenderFrame();
        if (originTerminal.lastRenderFrame == renderFrame && !monitorPos.equals(originTerminal.lastRenderPos)) {
            return;
        }

        originTerminal.lastRenderFrame = renderFrame;
        originTerminal.lastRenderPos = monitorPos;

        BlockPos originPos = origin.getPos();

        // Determine orientation
        Direction dir = origin.getDirection();
        Direction front = origin.getFront();
        float yaw = DirectionUtil.getRotationYaw(dir);
        float pitch = DirectionUtil.toPitchAngle(front);

        // Setup initial transform
        GL11.glPushMatrix();

        if (LightmapHelper.isLightmapEnabled()) {
            LightmapHelper.setLightmapCoord(LightmapHelper.getLightmapCoord(15, 15));
        }

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_LIGHTING);

        GL11.glColor4f(1, 1, 1, 1);

        GL11.glTranslatef((float) x, (float) y, (float) z);

        GL11.glTranslatef(
            originPos.getX() - monitorPos.getX() + 0.5f,
            originPos.getY() - monitorPos.getY() + 0.5f,
            originPos.getZ() - monitorPos.getZ() + 0.5f
        );


        GL11.glRotatef(-yaw, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(pitch, 1.0f, 0.0f, 0.0f);

        GL11.glTranslatef(
            (float) (-0.5f + TileMonitor.RENDER_BORDER + TileMonitor.RENDER_MARGIN),
            (float) (origin.getHeight() - 0.5f - (TileMonitor.RENDER_BORDER + TileMonitor.RENDER_MARGIN)),
            0.50f
        );

        double xSize = origin.getWidth() - 2.0 * (TileMonitor.RENDER_MARGIN + TileMonitor.RENDER_BORDER);
        double ySize = origin.getHeight() - 2.0 * (TileMonitor.RENDER_MARGIN + TileMonitor.RENDER_BORDER);

        // Draw the background blocker
        FixedWidthFontRenderer.drawBlocker(tessellator,
            (float) -TileMonitor.RENDER_MARGIN,
            (float) TileMonitor.RENDER_MARGIN,
            (float) (xSize + 2 * TileMonitor.RENDER_MARGIN),
            (float) -(ySize + TileMonitor.RENDER_MARGIN * 2));


        // Set the contents slightly off the surface to prevent z-fighting
        GL11.glTranslatef(0.0f, 0.0f, 0.001f);

        // Draw the contents
        Terminal terminal = originTerminal.getTerminal();
        if (terminal != null) {
            // Draw a terminal
            int width = terminal.getWidth(), height = terminal.getHeight();
            int pixelWidth = width * FONT_WIDTH, pixelHeight = height * FONT_HEIGHT;
            double xScale = xSize / pixelWidth;
            double yScale = ySize / pixelHeight;
            GL11.glPushMatrix();
            GL11.glScalef((float) xScale, (float) -yScale, 1.0f);

            GL11.glDepthMask(false);
            renderTerminal(originTerminal, (float) (MARGIN / xScale), (float) (MARGIN / yScale));
            GL11.glDepthMask(true);

            GL11.glTranslatef(0.0f, 0.0f, 0.001f);

            // We don't draw the cursor with the VBO/DisplayList, as it's dynamic and so we'll end up refreshing far more than is
            // reasonable.
            FixedWidthFontRenderer.drawCursor(0, 0, terminal, !originTerminal.isColour());

            GL11.glPopMatrix();
        } else {
            FixedWidthFontRenderer.drawEmptyTerminal(tessellator,
                -MARGIN,
                MARGIN,
                (float) (xSize + 2 * MARGIN),
                (float) -(ySize + MARGIN * 2));
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
}
