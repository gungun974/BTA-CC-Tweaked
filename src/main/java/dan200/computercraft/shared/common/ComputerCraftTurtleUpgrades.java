package dan200.computercraft.shared.common;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.shared.turtle.upgrades.*;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.item.Items;

public final class ComputerCraftTurtleUpgrades {
    public static TurtleModem wirelessModemNormal = new TurtleModem(false, ComputerCraftBlocks.WIRELESS_MODEM_NORMAL.id());
    public static TurtleModem wirelessModemAdvanced = new TurtleModem(true, ComputerCraftBlocks.WIRELESS_MODEM_ADVANCED.id());
    public static TurtleSpeaker speaker = new TurtleSpeaker(ComputerCraftBlocks.SPEAKER.id());

    public static TurtleCraftingTable craftingTable = new TurtleCraftingTable(Blocks.WORKBENCH.id());
    public static TurtleSword diamondSword = new TurtleSword(Items.TOOL_SWORD_DIAMOND.id, Items.TOOL_SWORD_DIAMOND);
    public static TurtleShovel diamondShovel = new TurtleShovel(Items.TOOL_SHOVEL_DIAMOND.id, Items.TOOL_SHOVEL_DIAMOND);
    public static TurtleTool diamondPickaxe = new TurtleTool(Items.TOOL_PICKAXE_DIAMOND.id, Items.TOOL_PICKAXE_DIAMOND);
    public static TurtleAxe diamondAxe = new TurtleAxe(Items.TOOL_AXE_DIAMOND.id, Items.TOOL_AXE_DIAMOND);
    public static TurtleHoe diamondHoe = new TurtleHoe(Items.TOOL_HOE_DIAMOND.id, Items.TOOL_HOE_DIAMOND);
    public static TurtleSilkTouch goldenPickaxe = new TurtleSilkTouch(Items.TOOL_PICKAXE_GOLD.id, Items.TOOL_PICKAXE_GOLD);

    public static void registerTurtleUpgrades() {
        ComputerCraftAPI.registerTurtleUpgrade(wirelessModemNormal);
        ComputerCraftAPI.registerTurtleUpgrade(wirelessModemAdvanced);
        ComputerCraftAPI.registerTurtleUpgrade(speaker);

        ComputerCraftAPI.registerTurtleUpgrade(craftingTable);
        ComputerCraftAPI.registerTurtleUpgrade(diamondSword);
        ComputerCraftAPI.registerTurtleUpgrade(diamondShovel);
        ComputerCraftAPI.registerTurtleUpgrade(diamondPickaxe);
        ComputerCraftAPI.registerTurtleUpgrade(diamondAxe);
        ComputerCraftAPI.registerTurtleUpgrade(diamondHoe);
        ComputerCraftAPI.registerTurtleUpgrade(goldenPickaxe);
    }
}
