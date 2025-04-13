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
import net.minecraft.client.render.model.ModelSign;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.util.helper.Direction;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.nio.ByteBuffer;

import static dan200.computercraft.client.gui.FixedWidthFontRenderer.FONT_HEIGHT;
import static dan200.computercraft.client.gui.FixedWidthFontRenderer.FONT_WIDTH;

public class TileEntityMonitorRenderer extends TileEntityRenderer<TileMonitor> {
    /**
     * {@link TileMonitor#RENDER_MARGIN}, but a tiny bit of additional padding to ensure that there is no space between the monitor frame and contents.
     */
    private static final float MARGIN = (float) (TileMonitor.RENDER_MARGIN * 1.1);
    private static ByteBuffer tboContents;

    private final ModelSign modelSign = new ModelSign();

    public TileEntityMonitorRenderer() {
    }

    private static void renderTerminal(ClientMonitor monitor, float xMargin, float yMargin) {
        Terminal terminal = monitor.getTerminal();

        MonitorRenderer renderType = MonitorRenderer.current();
        boolean redraw = monitor.pollTerminalChanged();
        if (redraw) {
            monitor.displayListCompiled = false;
        }

        if (monitor.createBuffer(renderType, xMargin, yMargin)) {
            redraw = true;
        }

        switch (renderType) {
//            case TBO:
//                if (MonitorTextureBufferShader.use()) {
//                    int width = terminal.getWidth(), height = terminal.getHeight();
//                    int pixelWidth = width * FONT_WIDTH, pixelHeight = height * FONT_HEIGHT;
//
//                    if (redraw) {
//                        int size = width * height * 3;
//                        if (tboContents == null || tboContents.capacity() < size) {
//                            tboContents = ByteBuffer.allocateDirect(size);
//                        }
//
//                        ByteBuffer monitorBuffer = tboContents;
//                        monitorBuffer.clear();
//                        for (int y = 0; y < height; y++) {
//                            TextBuffer text = terminal.getLine(y);
//                            TextBuffer textColour = terminal.getTextColourLine(y);
//                            TextBuffer background = terminal.getBackgroundColourLine(y);
//                            for (int x = 0; x < width; x++) {
//                                monitorBuffer.put((byte) (text.charAt(x) & 0xFF));
//                                monitorBuffer.put((byte) getColour(textColour.charAt(x), Colour.WHITE));
//                                monitorBuffer.put((byte) getColour(background.charAt(x), Colour.BLACK));
//                            }
//                        }
//                        monitorBuffer.flip();
//
//                        // Liaison du buffer et chargement des données
//                        GL15.glBindBuffer(GL31.GL_TEXTURE_BUFFER, monitor.tboBuffer);
//                        GL15.glBufferData(GL31.GL_TEXTURE_BUFFER, monitorBuffer, GL15.GL_STATIC_DRAW);
//                        GL15.glBindBuffer(GL31.GL_TEXTURE_BUFFER, 0);
//                    }
//
////                    GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, monitor.tboTexture);
////                    GL31.glTexBuffer(GL31.GL_TEXTURE_BUFFER, GL11.GL_RGB8, monitor.tboBuffer);
////                    GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, 0);
//
//                    GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, monitor.tboTexture);
//                    GL31.glBindBuffer(GL31.GL_TEXTURE_BUFFER, monitor.tboBuffer);
//                    GL31.glTexBuffer(GL31.GL_TEXTURE_BUFFER, GL11.GL_RGBA8, monitor.tboBuffer);
////
//                    GL13.glActiveTexture(MonitorTextureBufferShader.TEXTURE_INDEX);
//                    GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, monitor.tboTexture);
//                    GL13.glActiveTexture(GL13.GL_TEXTURE0);
//
//                    MonitorTextureBufferShader.setupUniform(/*matrix,*/ width, height, terminal.getPalette(), !monitor.isColour());
//
//                    Tessellator tessellator = Tessellator.instance;
//
//                    tessellator.startDrawing(GL11.GL_QUAD_STRIP);
//
//                    tessellator.setColorRGBA_F(1,1,1, 1f);
//
//                    tessellator.addVertexWithUV(-xMargin, -yMargin, 0, 0, 0);
//                    tessellator.addVertexWithUV(-xMargin, pixelHeight + yMargin, 0, 0, 1);
//                    tessellator.addVertexWithUV(pixelWidth + xMargin, -yMargin, 0, 1, 0);
//                    tessellator.addVertexWithUV(pixelWidth + xMargin, pixelHeight + yMargin, 0, 1, 1);
//                    tessellator.draw();
//
//                    GL20.glUseProgram(0);
//                }
//
//                break;
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

        //ComputerCraft.log.info("Width: {}", origin.getWidth());

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
