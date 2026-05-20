/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.monitor;

import dan200.computercraft.client.FrameInfo;
import dan200.computercraft.client.gui.FixedWidthFontRenderer;
import dan200.computercraft.core.terminal.Terminal;
import dan200.computercraft.core.terminal.TextBuffer;
import dan200.computercraft.shared.util.Colour;
import dan200.computercraft.shared.util.DirectionUtil;
import net.minecraft.client.render.renderer.CompareFunc;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.Shaders;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.pos.TilePosc;
import org.joml.Matrix4f;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static dan200.computercraft.client.gui.FixedWidthFontRenderer.*;

public class TileEntityMonitorRenderer extends TileEntityRenderer<TileMonitor> {
    /**
     * {@link TileMonitor#RENDER_MARGIN}, but a tiny bit of additional padding to ensure that there is no space between the monitor frame and contents.
     */
    private static final float MARGIN = (float) (TileMonitor.RENDER_MARGIN * 1.1);

    private static ByteBuffer tboContents;

    private static int quadVao = 0;
    private static int quadVbo = 0;
    private static ByteBuffer quadVertexBuffer;

    public TileEntityMonitorRenderer() {
    }

    public static synchronized ByteBuffer allocateByteBuffer(int size) {
        return ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
    }

    private static void renderTerminal(Matrix4f matrix, ClientMonitor monitor, float xMargin, float yMargin) {
        Terminal terminal = monitor.getTerminal();

        MonitorRenderer renderType = MonitorRenderer.current();
        boolean redraw = monitor.pollTerminalChanged();
        if (monitor.createBuffer(renderType)) {
            redraw = true;
        }

        switch (renderType) {
            case TBO: {
                if (!MonitorTextureBufferShader.use()) {
                    return;
                }

                int width = terminal.getWidth(), height = terminal.getHeight();
                int pixelWidth = width * FONT_WIDTH, pixelHeight = height * FONT_HEIGHT;

                if (redraw) {
                    int size = width * height * 3;
                    if (tboContents == null || tboContents.capacity() < size) {
                        tboContents = allocateByteBuffer(size);
                    }

                    ByteBuffer monitorBuffer = tboContents;
                    monitorBuffer.clear();
                    for (int y = 0; y < height; y++) {
                        TextBuffer text = terminal.getLine(y), textColour = terminal.getTextColourLine(y), background = terminal.getBackgroundColourLine(y);
                        for (int x = 0; x < width; x++) {
                            monitorBuffer.put((byte) (text.charAt(x) & 0xFF));
                            monitorBuffer.put((byte) getColour(textColour.charAt(x), Colour.WHITE));
                            monitorBuffer.put((byte) getColour(background.charAt(x), Colour.BLACK));
                        }
                    }
                    monitorBuffer.flip();

                    GL15.glBindBuffer(GL31.GL_TEXTURE_BUFFER, monitor.tboBuffer);
                    GL15.glBufferData(GL31.GL_TEXTURE_BUFFER, monitorBuffer, GL20.GL_STATIC_DRAW);
                    GL15.glBindBuffer(GL31.GL_TEXTURE_BUFFER, 0);
                }

                GL13.glActiveTexture(GL13.GL_TEXTURE0);
                FixedWidthFontRenderer.bindFont();

                GL13.glActiveTexture(MonitorTextureBufferShader.TEXTURE_INDEX);
                GL11.glBindTexture(GL31.GL_TEXTURE_BUFFER, monitor.tboTexture);
                GL13.glActiveTexture(GL13.GL_TEXTURE0);

                MonitorTextureBufferShader.setupUniform(matrix, width, height, terminal.getPalette(), !monitor.isColour());

                if (quadVao == 0) {
                    quadVao = GL30.glGenVertexArrays();
                    quadVbo = GL15.glGenBuffers();
                    GL30.glBindVertexArray(quadVao);
                    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, quadVbo);
                    GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 12, 0L);
                    GL20.glEnableVertexAttribArray(0);
                    GL30.glBindVertexArray(0);
                }

                if (quadVertexBuffer == null) {
                    quadVertexBuffer = allocateByteBuffer(4 * 3 * 4);
                }

                quadVertexBuffer.clear();
                quadVertexBuffer.putFloat(-xMargin).putFloat(-yMargin).putFloat(0f);
                quadVertexBuffer.putFloat(-xMargin).putFloat(pixelHeight + yMargin).putFloat(0f);
                quadVertexBuffer.putFloat(pixelWidth + xMargin).putFloat(-yMargin).putFloat(0f);
                quadVertexBuffer.putFloat(pixelWidth + xMargin).putFloat(pixelHeight + yMargin).putFloat(0f);
                quadVertexBuffer.flip();

                GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, quadVbo);
                GL15.glBufferData(GL15.GL_ARRAY_BUFFER, quadVertexBuffer, GL15.GL_DYNAMIC_DRAW);
                GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

                GL30.glBindVertexArray(quadVao);
                GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
                GL30.glBindVertexArray(0);

                GL20.glUseProgram(0);
                break;
            }

//            case VBO:
//                VertexBuffer vbo = monitor.buffer;
//                if( redraw )
//                {
//                    Tessellator tessellator = Tessellator.getInstance();
//                    BufferBuilder builder = tessellator.getBuffer();
//                    builder.begin( FixedWidthFontRenderer.TYPE.getDrawMode(), FixedWidthFontRenderer.TYPE.getVertexFormat() );
//                    FixedWidthFontRenderer.drawTerminalWithoutCursor( IDENTITY,
//                        builder,
//                        0,
//                        0,
//                        terminal,
//                        !monitor.isColour(),
//                        yMargin,
//                        yMargin,
//                        xMargin,
//                        xMargin );
//
//                    builder.end();
//                    vbo.upload( builder );
//                }
//
//                vbo.bind();
//                FixedWidthFontRenderer.TYPE.getVertexFormat()
//                    .startDrawing( 0L );
//                vbo.draw( matrix, FixedWidthFontRenderer.TYPE.getDrawMode() );
//                VertexBuffer.unbind();
//                FixedWidthFontRenderer.TYPE.getVertexFormat()
//                    .endDrawing();
//                break;
        }
    }

    @Override
    public void doRender(TessellatorGeneral tessellator, TileMonitor monitor, double x, double y, double z, float partialTick) {
        // Render from the origin monitor
        ClientMonitor originTerminal = monitor.getClientMonitor();

        if (originTerminal == null) return;
        TileMonitor origin = originTerminal.getOrigin();
        TilePosc monitorPos = monitor.getPos();

        // Ensure each monitor terminal is rendered only once. We allow rendering a specific tile
        // multiple times in a single frame to ensure compatibility with shaders which may run a
        // pass multiple times.
        long renderFrame = FrameInfo.getRenderFrame();
        if (originTerminal.lastRenderFrame == renderFrame && !monitorPos.equals(originTerminal.lastRenderPos)) {
            return;
        }

        originTerminal.lastRenderFrame = renderFrame;
        originTerminal.lastRenderPos = monitorPos;

        TilePosc originPos = origin.getPos();

        // Determine orientation
        Direction dir = origin.getDirection();
        Direction front = origin.getFront();
        float yaw = DirectionUtil.getRotationYaw(dir);
        float pitch = DirectionUtil.toPitchAngle(front);

        // Setup initial transform
        GLRenderer.pushFrame();
        Matrix4f transform = GLRenderer.modelM4f();

        transform.translate(
            (float) x, (float) y, (float) z
        );

        transform.translate(
            originPos.x() - monitorPos.x() + 0.5f,
            originPos.y() - monitorPos.y() + 0.5f,
            originPos.z() - monitorPos.z() + 0.5f
        );

        transform.rotateY((float) Math.toRadians(-yaw));
        transform.rotateX((float) Math.toRadians(pitch));

        transform.translate(
            -0.5f + TileMonitor.RENDER_BORDER + TileMonitor.RENDER_MARGIN,
            origin.getHeight() - 0.5f - (TileMonitor.RENDER_BORDER + TileMonitor.RENDER_MARGIN),
            0.50f
        );

        double xSize = origin.getWidth() - 2.0 * (TileMonitor.RENDER_MARGIN + TileMonitor.RENDER_BORDER);
        double ySize = origin.getHeight() - 2.0 * (TileMonitor.RENDER_MARGIN + TileMonitor.RENDER_BORDER);
        GLRenderer.setDepthFunc(CompareFunc.LESS_EQUAL);
        GLRenderer.setShader(Shaders.INTERFACE);

        transform.translate(0.0f, 0.0f, -0.010f);

        // Draw the background blocker
        FixedWidthFontRenderer.drawBlocker(
            GLRenderer.modelM4f(),
            -TileMonitor.RENDER_MARGIN,
            TileMonitor.RENDER_MARGIN,
            (float) (xSize + 2 * TileMonitor.RENDER_MARGIN),
            (float) -(ySize + TileMonitor.RENDER_MARGIN * 2)
        );

        // Set the contents slightly off the surface to prevent z-fighting

        // Draw the contents
        Terminal terminal = originTerminal.getTerminal();
        if (terminal != null) {
            // Draw a terminal
            int width = terminal.getWidth(), height = terminal.getHeight();
            int pixelWidth = width * FONT_WIDTH, pixelHeight = height * FONT_HEIGHT;
            double xScale = xSize / pixelWidth;
            double yScale = ySize / pixelHeight;

            GLRenderer.pushFrame();
            transform.translate(0.0f, 0.0f, 0.010f);

            GLRenderer.modelM4f().scale((float) xScale, (float) -yScale, 1.0f);

            renderTerminal(GLRenderer.modelM4f(), originTerminal, (float) (MARGIN / xScale), (float) (MARGIN / yScale));

            GLRenderer.modelM4f().translate(
                0,
                0,
                0.001f
            );

            // We don't draw the cursor with the VBO, as it's dynamic and so we'll end up refreshing far more than is
            // reasonable.
            FixedWidthFontRenderer.drawCursor(GLRenderer.modelM4f(), 0, 0, terminal, !originTerminal.isColour());

            GLRenderer.popFrame();
        } else {
            FixedWidthFontRenderer.drawEmptyTerminal(
                GLRenderer.modelM4f(),
                -MARGIN,
                MARGIN,
                (float) (xSize + 2 * MARGIN),
                (float) -(ySize + MARGIN * 2)
            );
        }

        GLRenderer.popFrame();
    }
}
