/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.generic;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IDynamicPeripheral;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityDispatcher;
import net.minecraft.core.util.collection.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class GenericPeripheral implements IDynamicPeripheral {
    private final String type;
    private final TileEntity tile;
    private final List<SaturatedMethod> methods;

    GenericPeripheral(TileEntity tile, List<SaturatedMethod> methods) {
        NamespaceID namespaceID = TileEntityDispatcher.getIDFromClass(tile.getClass());
        this.tile = tile;
        this.type = namespaceID == null ? "unknown" : namespaceID.toString();
        this.methods = methods;
    }

    @NotNull
    @Override
    public String[] getMethodNames() {
        String[] names = new String[methods.size()];
        for (int i = 0; i < methods.size(); i++) names[i] = methods.get(i).getName();
        return names;
    }

    @NotNull
    @Override
    public MethodResult callMethod(@NotNull IComputerAccess computer, @NotNull ILuaContext context, int method, @NotNull IArguments arguments) throws LuaException {
        return methods.get(method).apply(context, computer, arguments);
    }

    @NotNull
    @Override
    public String getType() {
        return type;
    }

    @Override
    public Set<String> getAdditionalTypes() {
        return new HashSet<>(Collections.singletonList("inventory"));
    }

    @Nullable
    @Override
    public Object getTarget() {
        return tile;
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        if (other == this) return true;
        if (!(other instanceof GenericPeripheral generic)) return false;

        return tile == generic.tile && methods.equals(generic.methods);
    }
}
