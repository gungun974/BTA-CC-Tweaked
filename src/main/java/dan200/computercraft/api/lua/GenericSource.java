/*
 * This file is part of the public ComputerCraft API - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. This API may be redistributed unmodified and in full only.
 * For help using the API, and posting your mods, visit the forums at computercraft.info.
 */
package dan200.computercraft.api.lua;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import dan200.computercraft.core.asm.LuaMethod;
import org.jetbrains.annotations.NotNull;

/**
 * A generic source of {@link LuaMethod} functions.
 * <p>
 * Unlike normal objects ({@link IDynamicLuaObject} or {@link IPeripheral}), methods do not target this object but
 * instead are defined as {@code static} and accept their target as the first parameter. This allows you to inject
 * methods onto objects you do not own, as well as declaring methods for a specific "trait".
 * <p>
 * Currently the "generic peripheral" system is incompatible with normal peripherals. Normal {@link IPeripheralProvider}
 * or {@link IPeripheral} implementations take priority. Tile entities which use this system are given a peripheral name
 * determined by their id, rather than any peripheral provider. This will hopefully change in the future, once a suitable
 * design has been established.
 * <p>
 * For example, the main CC: Tweaked mod defines a generic source for inventories, which works on
 * {@link net.minecraft.core.player.inventory.container.Container}s:
 *
 * <pre>{@code
 * public class InventoryMethods implements GenericSource {
 *     \@LuaFunction( mainThread = true )
 *     public static int size(Container inventory) {
 *         return inventory.getSlots();
 *     }
 *
 *     // ...
 * }
 * }</pre>
 *
 * @see ComputerCraftAPI#registerGenericSource(GenericSource)
 */
public interface GenericSource {
    /**
     * A unique identifier for this generic source.
     * <p>
     * This is currently unused, but may be used in the future to allow disabling specific sources. It is recommended
     * to return an identifier using your mod's ID.
     *
     * @return This source's identifier.
     */
    @NotNull
    String id();
}
