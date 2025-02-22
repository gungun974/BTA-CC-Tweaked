package dan200.computercraft.shared.common;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.shared.turtle.upgrades.TurtleTool;
import net.minecraft.core.item.Items;

public final class ComputerCraftTurtleUpgrades
{
//    public static TurtleModem wirelessModemNormal = new TurtleModem( false, new Identifier( ComputerCraft.MOD_ID, "wireless_modem_normal" ) );
//    public static TurtleModem wirelessModemAdvanced = new TurtleModem( true, new Identifier( ComputerCraft.MOD_ID, "wireless_modem_advanced" ) );
//    public static TurtleSpeaker speaker = new TurtleSpeaker( new Identifier( ComputerCraft.MOD_ID, "speaker" ) );

//    public static TurtleCraftingTable craftingTable = new TurtleCraftingTable( new Identifier( "minecraft", "crafting_table" ) );
//    public static TurtleSword diamondSword = new TurtleSword( new Identifier( "minecraft", "diamond_sword" ), Items.DIAMOND_SWORD );
//    public static TurtleShovel diamondShovel = new TurtleShovel( new Identifier( "minecraft", "diamond_shovel" ), Items.DIAMOND_SHOVEL );
    public static TurtleTool diamondPickaxe = new TurtleTool( Items.TOOL_PICKAXE_DIAMOND.id, Items.TOOL_PICKAXE_DIAMOND );
//    public static TurtleAxe diamondAxe = new TurtleAxe( new Identifier( "minecraft", "diamond_axe" ), Items.DIAMOND_AXE );
//    public static TurtleHoe diamondHoe = new TurtleHoe( new Identifier( "minecraft", "diamond_hoe" ), Items.DIAMOND_HOE );

    public static void registerTurtleUpgrades()
    {
//        ComputerCraftAPI.registerTurtleUpgrade( wirelessModemNormal );
//        ComputerCraftAPI.registerTurtleUpgrade( wirelessModemAdvanced );
//        ComputerCraftAPI.registerTurtleUpgrade( speaker );

//        ComputerCraftAPI.registerTurtleUpgrade( craftingTable );
//        ComputerCraftAPI.registerTurtleUpgrade( diamondSword );
//        ComputerCraftAPI.registerTurtleUpgrade( diamondShovel );
        ComputerCraftAPI.registerTurtleUpgrade( diamondPickaxe );
//        ComputerCraftAPI.registerTurtleUpgrade( diamondAxe );
//        ComputerCraftAPI.registerTurtleUpgrade( diamondHoe );
    }
}
