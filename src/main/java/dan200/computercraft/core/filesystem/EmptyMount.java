/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.core.filesystem;

import dan200.computercraft.api.filesystem.FileOperationException;
import dan200.computercraft.api.filesystem.IMount;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

public class EmptyMount implements IMount {
    @Override
    public boolean exists(@NotNull String path) {
        return path.isEmpty();
    }

    @Override
    public boolean isDirectory(@NotNull String path) {
        return path.isEmpty();
    }

    @Override
    public void list(@NotNull String path, @NotNull List<String> contents) {
    }

    @Override
    public long getSize(@NotNull String path) {
        return 0;
    }

    @NotNull
    @Override
    public ReadableByteChannel openForRead(@NotNull String path) throws IOException {
        throw new FileOperationException(path, "No such file");
    }
}
