/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.core.computer;

import dan200.computercraft.api.filesystem.IMount;
import dan200.computercraft.api.filesystem.IWritableMount;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;

public interface IComputerEnvironment {
    int getDay();

    double getTimeOfDay();

    boolean isColour();

    long getComputerSpaceLimit();

    @NotNull
    String getHostString();

    @NotNull
    String getUserAgent();

    int assignNewID();

    @Nullable
    IWritableMount createSaveDirMount(String subPath, long capacity);

    @Nullable
    IMount createResourceMount(String domain, String subPath);

    @Nullable
    InputStream createResourceFile(String domain, String subPath);
}
