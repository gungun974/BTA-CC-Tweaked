/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.core.filesystem;

import dan200.computercraft.api.filesystem.IMount;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

public class SubMount implements IMount {
    private final IMount parent;
    private final String subPath;

    public SubMount(IMount parent, String subPath) {
        this.parent = parent;
        this.subPath = subPath;
    }

    @Override
    public boolean exists(@NotNull String path) throws IOException {
        return parent.exists(getFullPath(path));
    }

    @Override
    public boolean isDirectory(@NotNull String path) throws IOException {
        return parent.isDirectory(getFullPath(path));
    }

    @Override
    public void list(@NotNull String path, @NotNull List<String> contents) throws IOException {
        parent.list(getFullPath(path), contents);
    }

    @Override
    public long getSize(@NotNull String path) throws IOException {
        return parent.getSize(getFullPath(path));
    }

    @NotNull
    @Override
    public ReadableByteChannel openForRead(@NotNull String path) throws IOException {
        return parent.openForRead(getFullPath(path));
    }

    @NotNull
    @Override
    public BasicFileAttributes getAttributes(@NotNull String path) throws IOException {
        return parent.getAttributes(getFullPath(path));
    }

    private String getFullPath(String path) {
        return path.isEmpty() ? subPath : subPath + "/" + path;
    }
}
