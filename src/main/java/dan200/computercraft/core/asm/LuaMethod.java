/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.core.asm;

import dan200.computercraft.api.lua.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public interface LuaMethod {
    Generator<LuaMethod> GENERATOR = new Generator<>(LuaMethod.class, Collections.singletonList(ILuaContext.class),
        m -> (target, context, args) -> TaskCallback.make(context, () -> TaskCallback.checkUnwrap(m.apply(target, context, args)))
    );

    IntCache<LuaMethod> DYNAMIC = new IntCache<>(
        method -> (instance, context, args) -> ((IDynamicLuaObject) instance).callMethod(context, method, args)
    );

    String[] EMPTY_METHODS = new String[0];

    @NotNull
    MethodResult apply(@NotNull Object target, @NotNull ILuaContext context, @NotNull IArguments args) throws LuaException;
}
