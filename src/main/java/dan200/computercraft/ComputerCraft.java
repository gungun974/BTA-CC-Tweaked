/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.media.IMedia;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.api.turtle.event.TurtleAction;
import dan200.computercraft.api.turtle.event.TurtleEvent;
import dan200.computercraft.core.apis.http.options.Action;
import dan200.computercraft.core.apis.http.options.AddressRule;
import dan200.computercraft.fabric.Helper;
import dan200.computercraft.shared.TurtlePermissions;
import dan200.computercraft.shared.common.*;
import dan200.computercraft.shared.computer.core.ClientComputerRegistry;
import dan200.computercraft.shared.computer.core.ServerComputerRegistry;
import dan200.computercraft.shared.media.items.RecordMedia;
import dan200.computercraft.shared.network.client.*;
import dan200.computercraft.shared.network.server.*;
import dan200.computercraft.shared.peripheral.generic.methods.InventoryMethods;
import dan200.computercraft.shared.peripheral.monitor.MonitorRenderer;
import dan200.computercraft.shared.turtle.FurnaceRefuelHandler;
import dan200.computercraft.shared.turtle.SignInspectHandler;
import dan200.computercraft.shared.util.Config;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemDiscMusic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.network.NetworkHandler;
import turniplabs.halplibe.util.GameStartEntrypoint;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class ComputerCraft implements ModInitializer, GameStartEntrypoint {
    public static final String MOD_ID = "computercraft";
    // Registries
    public static final ClientComputerRegistry clientComputerRegistry = new ClientComputerRegistry();
    public static final ServerComputerRegistry serverComputerRegistry = new ServerComputerRegistry();
    // Logging
    public static final Logger log = LoggerFactory.getLogger(MOD_ID);
    // Configuration fields

    public static int startBlockID = 1800;
    public static int startItemID = 19000;

    public static int computerSpaceLimit = 1000 * 1000;
    public static int floppySpaceLimit = 125 * 1000;
    public static int maximumFilesOpen = 128;
    public static boolean disableLua51Features = false;
    public static String defaultComputerSettings = "";
    public static boolean debugEnable = true;
    public static boolean logComputerErrors = true;
    public static boolean commandRequireCreative = true;
    public static int computerThreads = 1;
    public static long maxMainGlobalTime = TimeUnit.MILLISECONDS.toNanos(10);
    public static long maxMainComputerTime = TimeUnit.MILLISECONDS.toNanos(5);
    public static boolean httpEnabled = true;
    public static boolean httpWebsocketEnabled = true;
    public static List<AddressRule> httpRules = Collections.unmodifiableList(Arrays.asList(
        AddressRule.parse("$private", null, Action.DENY.toPartial()),
        AddressRule.parse("*", null, Action.ALLOW.toPartial())
    ));
    public static int httpMaxRequests = 16;
    public static int httpMaxWebsockets = 4;
    public static boolean enableCommandBlock = false;
    public static int modemRange = 64;
    public static int modemHighAltitudeRange = 384;
    public static int modemRangeDuringStorm = 64;
    public static int modemHighAltitudeRangeDuringStorm = 384;
    public static int maxNotesPerTick = 8;
    public static MonitorRenderer monitorRenderer = MonitorRenderer.BEST;
    public static double monitorDistanceSq = 4096;
    public static long monitorBandwidth = 1_000_000;
    public static boolean turtlesNeedFuel = true;
    public static int turtleFuelLimit = 20000;
    public static int advancedTurtleFuelLimit = 100000;
    public static boolean turtlesObeyBlockProtection = true;
    public static boolean turtlesCanPush = true;
    public static EnumSet<TurtleAction> turtleDisabledActions = EnumSet.noneOf(TurtleAction.class);
    public static int computerTermWidth = 51;
    public static int computerTermHeight = 19;
    public static final int turtleTermWidth = 39;
    public static final int turtleTermHeight = 13;
    public static int pocketTermWidth = 26;
    public static int pocketTermHeight = 20;
    public static int monitorWidth = 8;
    public static int monitorHeight = 6;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onInitialize() {
        ComputerCraftBlocks.RegisterBlocks();
        ComputerCraftItems.RegisterItems();

        NetworkHandler.registerNetworkMessage(ComputerActionServerMessage::new);
        NetworkHandler.registerNetworkMessage(QueueEventServerMessage::new);
        NetworkHandler.registerNetworkMessage(RequestComputerMessage::new);
        NetworkHandler.registerNetworkMessage(KeyEventServerMessage::new);
        NetworkHandler.registerNetworkMessage(MouseEventServerMessage::new);

        // Client messages
        NetworkHandler.registerNetworkMessage(OpenGuiDiskDriveClientMessage::new);
        NetworkHandler.registerNetworkMessage(OpenGuiPrinterClientMessage::new);
        NetworkHandler.registerNetworkMessage(OpenGuiTurtleClientMessage::new);
        NetworkHandler.registerNetworkMessage(OpenGuiComputerClientMessage::new);
        NetworkHandler.registerNetworkMessage(OpenGuiPrintoutClientMessage::new);
        NetworkHandler.registerNetworkMessage(ComputerDataClientMessage::new);
        NetworkHandler.registerNetworkMessage(ComputerDeletedClientMessage::new);
        NetworkHandler.registerNetworkMessage(ComputerTerminalClientMessage::new);
        NetworkHandler.registerNetworkMessage(MonitorClientMessage::new);

        ComputerCraftAPI.registerPeripheralProvider((world, pos, side) -> {
            TileEntity tile = world.getTileEntity(pos.x, pos.y, pos.z);
            return tile instanceof IPeripheralTile ? ((IPeripheralTile) tile).getPeripheral(side) : null;
        });

        // Register bundled power providers
        ComputerCraftAPI.registerBundledRedstoneProvider(new DefaultBundledRedstoneProvider());

        // Register media providers
        ComputerCraftAPI.registerMediaProvider(stack -> {
            Item item = stack.getItem();
            if (item instanceof IMedia) {
                return (IMedia) item;
            }
            if (item instanceof ItemDiscMusic) {
                return RecordMedia.INSTANCE;
            }
            return null;
        });

        // Config
//        ServerLifecycleEvents.SERVER_STARTING.register( Config::serverStarting );
//        ServerLifecycleEvents.SERVER_STOPPING.register( Config::serverStopping );
//        ServerPlayConnectionEvents.JOIN.register( ( listener, sender, server ) ->
//        {
//            NetworkHandler.sendToPlayer( listener.player, new TerminalDimensionsClientMessage() );
//        } );
//
//        ServerBlockEntityEvents.BLOCK_ENTITY_UNLOAD.register( ( blockEntity, world ) -> {
//            if( blockEntity instanceof TileGeneric )
//            {
//                ((TileGeneric) blockEntity).onChunkUnloaded();
//            }
//        } );

        TurtleEvent.EVENT_BUS.register(FurnaceRefuelHandler.INSTANCE);
        TurtleEvent.EVENT_BUS.register(new TurtlePermissions());
        TurtleEvent.EVENT_BUS.register(new SignInspectHandler());

        ComputerCraftAPI.registerGenericSource(new InventoryMethods());

        //ResourceManagerHelper.get( ResourceType.SERVER_DATA ).registerReloadListener( ResourceMount.RELOAD_LISTENER );
    }


    @Override
    public void beforeGameStart() {
        if (Helper.isServerEnvironment()) {
            Config.serverStarting();
        } else {
            Config.clientStarted();
        }
    }

    @Override
    public void afterGameStart() {
        ComputerCraftPocketUpgrades.registerPocketUpgrades();
        ComputerCraftTurtleUpgrades.registerTurtleUpgrades();
    }
}
