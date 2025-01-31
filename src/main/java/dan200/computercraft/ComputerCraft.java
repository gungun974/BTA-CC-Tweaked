/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft;

//import dan200.computercraft.api.turtle.event.TurtleAction;
import dan200.computercraft.core.apis.http.options.Action;
import dan200.computercraft.core.apis.http.options.AddressRule;
//import dan200.computercraft.shared.computer.core.ClientComputerRegistry;
//import dan200.computercraft.shared.computer.core.ServerComputerRegistry;
//import dan200.computercraft.shared.peripheral.monitor.MonitorRenderer;
//import org.apache.logging.log4j.LogManager;

import java.util.*;
import java.util.concurrent.TimeUnit;

//import static dan200.computercraft.shared.ComputerCraftRegistry.ModBlocks;
//import static dan200.computercraft.shared.ComputerCraftRegistry.init;

import dan200.computercraft.shared.computer.blocks.BlockLogicComputer;
import dan200.computercraft.shared.computer.blocks.TileEntityComputer;
import dan200.computercraft.shared.computer.core.ClientComputerRegistry;
import dan200.computercraft.shared.computer.core.ServerComputerRegistry;
import dan200.computercraft.shared.network.NetworkHandler;
import net.minecraft.core.util.collection.NamespaceID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.EntityHelper;

public final class ComputerCraft implements ModInitializer
{
    public static final String MOD_ID = "computercraft";

    // Configuration fields
    public static int computerSpaceLimit = 1000 * 1000;
    public static int floppySpaceLimit = 125 * 1000;
    public static int maximumFilesOpen = 128;
    public static boolean disableLua51Features = false;
    public static String defaultComputerSettings = "";
    public static boolean debugEnable = true;
    public static boolean logComputerErrors = true;
    public static boolean commandRequireCreative = true;

    public static int computerThreads = 1;
    public static long maxMainGlobalTime = TimeUnit.MILLISECONDS.toNanos( 10 );
    public static long maxMainComputerTime = TimeUnit.MILLISECONDS.toNanos( 5 );

    public static boolean httpEnabled = true;
    public static boolean httpWebsocketEnabled = true;
    public static List<AddressRule> httpRules = Collections.unmodifiableList( Arrays.asList(
        AddressRule.parse( "$private", null, Action.DENY.toPartial() ),
        AddressRule.parse( "*", null, Action.ALLOW.toPartial() )
    ) );
    public static int httpMaxRequests = 16;
    public static int httpMaxWebsockets = 4;

    public static boolean enableCommandBlock = false;
    public static int modemRange = 64;
    public static int modemHighAltitudeRange = 384;
    public static int modemRangeDuringStorm = 64;
    public static int modemHighAltitudeRangeDuringStorm = 384;
    public static int maxNotesPerTick = 8;
    //public static MonitorRenderer monitorRenderer = MonitorRenderer.BEST;
    public static double monitorDistanceSq = 4096;
    public static long monitorBandwidth = 1_000_000;

    public static boolean turtlesNeedFuel = true;
    public static int turtleFuelLimit = 20000;
    public static int advancedTurtleFuelLimit = 100000;
    public static boolean turtlesObeyBlockProtection = true;
    public static boolean turtlesCanPush = true;
    //public static EnumSet<TurtleAction> turtleDisabledActions = EnumSet.noneOf( TurtleAction.class );

    public static int computerTermWidth = 51;
    public static int computerTermHeight = 19;

    public static final int turtleTermWidth = 39;
    public static final int turtleTermHeight = 13;

    public static int pocketTermWidth = 26;
    public static int pocketTermHeight = 20;
    public static int monitorWidth = 8;
    public static int monitorHeight = 6;

    // Registries
    public static final ClientComputerRegistry clientComputerRegistry = new ClientComputerRegistry();
    public static final ServerComputerRegistry serverComputerRegistry = new ServerComputerRegistry();

    // Logging
    public static final Logger log = LoggerFactory.getLogger(MOD_ID);;

    //public static ItemGroup MAIN_GROUP = FabricItemGroupBuilder.build( new Identifier( MOD_ID, "main" ), () -> new ItemStack( ModBlocks.COMPUTER_NORMAL ) );

    @Override
    public void onInitialize()
    {
        EntityHelper.createTileEntity(TileEntityComputer.class, new NamespaceID(MOD_ID, "computer"));

        new BlockBuilder(MOD_ID)
            .setTextures("computercraft:block/computer_normal_side")
            .setTopTexture("computercraft:block/computer_normal_top")
            .setBottomTexture("computercraft:block/computer_normal_top")
            .setNorthTexture("computercraft:block/computer_normal_front")
            .setHardness(1f)
            .setTileEntity(TileEntityComputer::new)
            .build("computer_normal", 10000, b -> new BlockLogicComputer(b));

        NetworkHandler.setup();
        /*
        ComputerCraftProxyCommon.init();
        Registry.register( Registry.RECIPE_SERIALIZER, new Identifier( ComputerCraft.MOD_ID, "colour" ), ColourableRecipe.SERIALIZER );
        Registry.register( Registry.RECIPE_SERIALIZER, new Identifier( ComputerCraft.MOD_ID, "computer_upgrade" ), ComputerUpgradeRecipe.SERIALIZER );
        Registry.register( Registry.RECIPE_SERIALIZER,
            new Identifier( ComputerCraft.MOD_ID, "pocket_computer_upgrade" ),
            PocketComputerUpgradeRecipe.SERIALIZER );
        Registry.register( Registry.RECIPE_SERIALIZER, new Identifier( ComputerCraft.MOD_ID, "disk" ), DiskRecipe.SERIALIZER );
        Registry.register( Registry.RECIPE_SERIALIZER, new Identifier( ComputerCraft.MOD_ID, "printout" ), PrintoutRecipe.SERIALIZER );
        Registry.register( Registry.RECIPE_SERIALIZER, new Identifier( ComputerCraft.MOD_ID, "turtle" ), TurtleRecipe.SERIALIZER );
        Registry.register( Registry.RECIPE_SERIALIZER, new Identifier( ComputerCraft.MOD_ID, "turtle_upgrade" ), TurtleUpgradeRecipe.SERIALIZER );
        Registry.register( Registry.RECIPE_SERIALIZER, new Identifier( ComputerCraft.MOD_ID, "impostor_shaped" ), ImpostorRecipe.SERIALIZER );
        Registry.register( Registry.RECIPE_SERIALIZER, new Identifier( ComputerCraft.MOD_ID, "impostor_shapeless" ), ImpostorShapelessRecipe.SERIALIZER );
        Registry.register( Registry.LOOT_CONDITION_TYPE, new Identifier( ComputerCraft.MOD_ID, "block_named" ), BlockNamedEntityLootCondition.TYPE );
        Registry.register( Registry.LOOT_CONDITION_TYPE, new Identifier( ComputerCraft.MOD_ID, "player_creative" ), PlayerCreativeLootCondition.TYPE );
        Registry.register( Registry.LOOT_CONDITION_TYPE, new Identifier( ComputerCraft.MOD_ID, "has_id" ), HasComputerIdLootCondition.TYPE );
        init();
        FabricLoader.getInstance().getModContainer( MOD_ID ).ifPresent( modContainer -> {
            ResourceManagerHelper.registerBuiltinResourcePack( new Identifier( MOD_ID, "classic" ), modContainer, ResourcePackActivationType.NORMAL );
            ResourceManagerHelper.registerBuiltinResourcePack( new Identifier( MOD_ID, "overhaul" ), modContainer, ResourcePackActivationType.NORMAL );
        } );
         */
    }
}
