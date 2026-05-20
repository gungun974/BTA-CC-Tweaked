/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft;

import dan200.computercraft.api.ComputerCraftAPI.IComputerCraftAPI;
import dan200.computercraft.api.filesystem.IMount;
import dan200.computercraft.api.filesystem.IWritableMount;
import dan200.computercraft.api.lua.GenericSource;
import dan200.computercraft.api.lua.ILuaAPIFactory;
import dan200.computercraft.api.media.IMediaProvider;
import dan200.computercraft.api.network.IPacketNetwork;
import dan200.computercraft.api.network.wired.IWiredElement;
import dan200.computercraft.api.network.wired.IWiredNode;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.redstone.IBundledRedstoneProvider;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.core.apis.ApiFactories;
import dan200.computercraft.core.asm.GenericMethod;
import dan200.computercraft.core.filesystem.FileMount;
import dan200.computercraft.core.filesystem.ResourceMount;
import dan200.computercraft.shared.BundledRedstone;
import dan200.computercraft.shared.MediaProviders;
import dan200.computercraft.shared.PocketUpgrades;
import dan200.computercraft.shared.TurtleUpgrades;
import dan200.computercraft.shared.peripheral.modem.wired.TileCable;
import dan200.computercraft.shared.peripheral.modem.wired.TileWiredModemFull;
import dan200.computercraft.shared.peripheral.modem.wireless.WirelessNetwork;
import dan200.computercraft.shared.util.IDAssigner;
import dan200.computercraft.shared.util.ResourceManager;
import dan200.computercraft.shared.wired.WiredNode;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;

public final class ComputerCraftAPIImpl implements IComputerCraftAPI {
    public static final ComputerCraftAPIImpl INSTANCE = new ComputerCraftAPIImpl();
    static final ResourceManager manager = new ResourceManager();
    private String version;

    private ComputerCraftAPIImpl() {
    }

    public static InputStream getResourceFile(String domain, String subPath) {
        {
            return manager.getResource(new ResourceManager.Identifier(domain, subPath))
                .getInputStream();
        }
    }

    @NotNull
    @Override
    public String getInstalledVersion() {
        if (version != null) {
            return version;
        }
        return version = FabricLoader.getInstance()
            .getModContainer(ComputerCraft.MOD_ID)
            .map(x -> x.getMetadata()
                .getVersion()
                .toString())
            .orElse("unknown");
    }

    @Override
    public int createUniqueNumberedSaveDir(@NotNull World world, @NotNull String parentSubPath) {
        return IDAssigner.getNextId(parentSubPath);
    }

    @Override
    public IWritableMount createSaveDirMount(@NotNull World world, @NotNull String subPath, long capacity) {
        try {
            return new FileMount(
                new File(
                    IDAssigner.getWorldDir(), subPath
                ), capacity
            );
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public IMount createResourceMount(@NotNull String domain, @NotNull String subPath) {
        {
            ResourceMount mount = ResourceMount.get(domain, subPath, manager);
            return mount.exists("") ? mount : null;
        }
    }

    @Override
    public void registerPeripheralProvider(@NotNull IPeripheralProvider provider) {
        Peripherals.register(provider);
    }

    @Override
    public void registerTurtleUpgrade(@NotNull ITurtleUpgrade upgrade) {
        TurtleUpgrades.register(upgrade);
    }

    @Override
    public void registerBundledRedstoneProvider(@NotNull IBundledRedstoneProvider provider) {
        BundledRedstone.register(provider);
    }

    @Override
    public int getBundledRedstoneOutput(@NotNull World world, @NotNull TilePosc pos, @NotNull Direction side) {
        return BundledRedstone.getDefaultOutput(world, pos, side);
    }

    @Override
    public void registerMediaProvider(@NotNull IMediaProvider provider) {
        MediaProviders.register(provider);
    }

    @Override
    public void registerPocketUpgrade(@NotNull IPocketUpgrade upgrade) {
        PocketUpgrades.register(upgrade);
    }

    @Override
    public void registerGenericSource(@NotNull GenericSource source) {
        GenericMethod.register(source);
    }

    @NotNull
    @Override
    public IPacketNetwork getWirelessNetwork() {
        return WirelessNetwork.getUniversal();
    }

    @Override
    public void registerAPIFactory(@NotNull ILuaAPIFactory factory) {
        ApiFactories.register(factory);
    }

    @NotNull
    @Override
    public IWiredNode createWiredNodeForElement(@NotNull IWiredElement element) {
        return new WiredNode(element);
    }

    @Nullable
    @Override
    public IWiredElement getWiredElementAt(@NotNull World world, @NotNull TilePosc pos, @NotNull Direction side) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileCable) {
            return ((TileCable) tile).getElement(side);
        } else if (tile instanceof TileWiredModemFull) {
            return ((TileWiredModemFull) tile).getElement();
        }
        return null;
    }
}
