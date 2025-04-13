/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.monitor;


import dan200.computercraft.client.gui.FixedWidthFontRenderer;
import dan200.computercraft.shared.util.Palette;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import java.io.InputStream;
import java.nio.FloatBuffer;

class MonitorTextureBufferShader {
    static final int TEXTURE_INDEX = GL13.GL_TEXTURE3;

    private static final FloatBuffer PALETTE_BUFFER = BufferUtils.createFloatBuffer(16 * 3);

    private static int uniformMv;
    private static int uniformFont;
    private static int uniformWidth;
    private static int uniformHeight;
    private static int uniformTbo;
    private static int uniformPalette;

    private static boolean initialised;
    private static boolean ok;
    private static int program;

    static void setupUniform(int width, int height, Palette palette, boolean greyscale) {
        GL20.glUniform1i(uniformWidth, width);
        GL20.glUniform1i(uniformHeight, height);

        PALETTE_BUFFER.clear();
        for (int i = 0; i < 16; i++) {
            double[] colour = palette.getColour(i);
            if (greyscale) {
                float f = FixedWidthFontRenderer.toGreyscale(colour);
                PALETTE_BUFFER.put(f).put(f).put(f);
            } else {
                PALETTE_BUFFER.put((float) colour[0]).put((float) colour[1]).put((float) colour[2]);
            }
        }
        PALETTE_BUFFER.flip();
        GL20.glUniform3fv(uniformPalette, PALETTE_BUFFER);
    }

    static boolean use() {
        if (initialised) {
            if (ok) {
                GL20.glUseProgram(program);
            }
            return ok;
        }

        ok = load();

        if (ok) {
            GL20.glUseProgram(program);
            GL20.glUniform1i(uniformFont, 0);
            GL20.glUniform1i(uniformTbo, TEXTURE_INDEX - GL13.GL_TEXTURE0);
        }
        return ok;
    }

    private static boolean load() {
        initialised = true;
        try {
            int vertexShader = loadShader(GL20.GL_VERTEX_SHADER, "assets/computercraft/shaders/monitor.vert");
            int fragmentShader = loadShader(GL20.GL_FRAGMENT_SHADER, "assets/computercraft/shaders/monitor.frag");

            program = GL20.glCreateProgram();
            GL20.glAttachShader(program, vertexShader);
            GL20.glAttachShader(program, fragmentShader);
            GL20.glBindAttribLocation(program, 0, "v_pos");

            GL20.glLinkProgram(program);
            boolean ok = GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) != 0;
            String log = GL20.glGetProgramInfoLog(program, Short.MAX_VALUE).trim();
            if (!log.isEmpty()) {
                System.out.println("Problems when linking monitor shader: " + log);
            }

            GL20.glDetachShader(program, vertexShader);
            GL20.glDetachShader(program, fragmentShader);
            GL20.glDeleteShader(vertexShader);
            GL20.glDeleteShader(fragmentShader);

            if (!ok) {
                return false;
            }

            uniformMv = getUniformLocation(program, "u_mv");
            uniformFont = getUniformLocation(program, "u_font");
            uniformWidth = getUniformLocation(program, "u_width");
            uniformHeight = getUniformLocation(program, "u_height");
            uniformTbo = getUniformLocation(program, "u_tbo");
            uniformPalette = getUniformLocation(program, "u_palette");

            System.out.println("Loaded monitor shader.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static int loadShader(int kind, String path) {
        try {
            InputStream stream = MonitorTextureBufferShader.class.getResourceAsStream("/" + path);
            if (stream == null) {
                throw new IllegalArgumentException("Cannot find " + path);
            }
            java.util.Scanner scanner = new java.util.Scanner(stream, "UTF-8");
            String contents = scanner.useDelimiter("\\A").next();
            scanner.close();

            int shader = GL20.glCreateShader(kind);
            GL20.glShaderSource(shader, contents);
            GL20.glCompileShader(shader);

            boolean ok = GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) != 0;
            String log = GL20.glGetShaderInfoLog(shader, Short.MAX_VALUE).trim();
            if (!log.isEmpty()) {
                System.out.println("Problems when loading monitor shader " + path + ": " + log);
            }

            if (!ok) {
                throw new IllegalStateException("Cannot compile shader " + path);
            }
            return shader;
        } catch (Exception e) {
            throw new RuntimeException("Error loading shader: " + path, e);
        }
    }

    private static int getUniformLocation(int program, String name) {
        int uniform = GL20.glGetUniformLocation(program, name);
        if (uniform == -1) {
            throw new IllegalStateException("Cannot find uniform " + name);
        }
        return uniform;
    }
}

