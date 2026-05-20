/*
 * This file is part of the public ComputerCraft API - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. This API may be redistributed unmodified and in full only.
 * For help using the API, and posting your mods, visit the forums at computercraft.info.
 */
package dan200.computercraft.api.lua;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ILuaObject {
    @NotNull
    String[] getMethodNames();

    @Nullable
    Object[] callMethod(@NotNull ILuaContext context, int method, @NotNull Object[] arguments) throws LuaException, InterruptedException;
}
