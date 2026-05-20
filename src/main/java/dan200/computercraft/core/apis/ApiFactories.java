/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.core.apis;

import dan200.computercraft.api.lua.ILuaAPIFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;

public final class ApiFactories {
    private static final Collection<ILuaAPIFactory> factories = new LinkedHashSet<>();
    private static final Collection<ILuaAPIFactory> factoriesView = Collections.unmodifiableCollection(factories);

    private ApiFactories() {
    }

    public static synchronized void register(@NotNull ILuaAPIFactory factory) {
        Objects.requireNonNull(factory, "provider cannot be null");
        factories.add(factory);
    }

    public static Iterable<ILuaAPIFactory> getAll() {
        return factoriesView;
    }
}
