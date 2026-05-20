package dan200.computercraft.shared.computer.blocks;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.core.computer.ComputerSide;
import dan200.computercraft.shared.common.IBundledRedstoneBlock;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.computer.items.ComputerItemFactory;
import dan200.computercraft.shared.util.DirectionUtil;
import dan200.computercraft.shared.util.RedstoneUtil;
import net.minecraft.core.block.*;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.tool.ItemToolShears;
import net.minecraft.core.player.gamemode.Gamemodes;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.Nullable;

public class BlockLogicComputer extends BlockLogicRotatable implements IBundledRedstoneBlock {
    public BlockLogicComputer(Block<?> block, ComputerFamily family) {
        super(block, Materials.STONE);
        block.withEntity(() -> new TileEntityComputer(family));
    }

    @Nullable
    protected ItemStack getItemStack(TileEntity entity) {
        if (!(entity instanceof TileComputerBase)) {
            return null;
        }

        return ComputerItemFactory.create((TileComputerBase) entity);

    }

    public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity entity) {
        switch (dropCause) {
            case PICK_BLOCK:
            case EXPLOSION:
            case PROPER_TOOL:
            case SILK_TOUCH:
            case PISTON_CRUSH:
                final ItemStack item = getItemStack(entity);
                if (item == null) {
                    return null;
                }

                if (dropCause == EnumDropCause.PICK_BLOCK) {
                    item.setData(new CompoundTag());
                }

                return new ItemStack[]{item};
            default:
                return null;
        }
    }

    @Override
    public void onHarvest(World world, Player player, TilePosc tilePos, int meta, TileEntity tileEntity) {
        player.addStat(this.block.getStat("stat_mined"), 1);
        ItemStack heldItemStack = player.inventory.getCurrentItem();
        Item heldItem = heldItemStack != null ? Item.itemsList[heldItemStack.itemID] : null;
        if (heldItem != null) {
            if (heldItem.isSilkTouch() && player.canHarvestBlock(this.block)) {
                this.dropWithCause(world, EnumDropCause.SILK_TOUCH, tilePos, meta, tileEntity, player);
                return;
            }

            if (heldItem instanceof ItemToolShears heldShears && (this.block.hasTag(BlockTags.SHEARS_DO_SILK_TOUCH) || this.block.hasTag(BlockTags.MINEABLE_BY_SHEARS))) {
                this.dropWithCause(world, EnumDropCause.SILK_TOUCH, tilePos, meta, tileEntity, player);
                heldShears.onBlockSheared(heldItemStack, player);
                return;
            }
        }


        if ((tileEntity instanceof TileComputerBase computerEntity)) {

            if (player.getGamemode() == Gamemodes.CREATIVE && computerEntity.getComputerID() == -1) {
                return;
            }
        }

        if (player.canHarvestBlock(this.block) || player.getGamemode() == Gamemodes.CREATIVE) {
            this.dropWithCause(world, EnumDropCause.PROPER_TOOL, tilePos, meta, tileEntity, player);
        } else {
            this.dropWithCause(world, EnumDropCause.IMPROPER_TOOL, tilePos, meta, tileEntity, player);
        }
    }

    @Override
    public void onRemoved(World world, TilePosc tilePos, int data) {
        TileEntity entity = (world.getTileEntity(tilePos));
        if (!(entity instanceof TileComputerBase computerEntity)) {
            return;
        }

        computerEntity.destroy();

        for (Direction dir : DirectionUtil.FACINGS) {
            RedstoneUtil.propagateRedstoneOutput(world, tilePos, dir);
        }
    }

    public static boolean[] ENABLE_DOOR_PROTECTION = {true, true, true, true, true, true};
    public static boolean TURTLE_MOVING = false;

    public boolean isSignalSource() {
        return true;
    }

    @Override
    public boolean isEmittingDirectSignal(World world, TilePosc tilePos, Side side) {
        return this.isEmittingSignal(world, tilePos, side);
    }

    @Override
    public boolean isEmittingSignal(WorldSource world, TilePosc tilePos, Side side) {
        TileEntity entity = (world.getTileEntity(tilePos));
        if (!(entity instanceof TileComputerBase computerEntity)) {
            return redstoneDoorProtection(world, tilePos, side);
        }

        ServerComputer computer = computerEntity.getServerComputer();
        if (computer == null) {
            return redstoneDoorProtection(world, tilePos, side);
        }

        ComputerSide localSide = computerEntity.remapToLocalSide(side.opposite().direction());

        if (computer.getRedstoneOutput(localSide) <= 0) {
            return redstoneDoorProtection(world, tilePos, side);
        }

        return !TURTLE_MOVING;
    }

    public boolean redstoneDoorProtection(WorldSource world, TilePosc tilePos, Side side) {
        if (!ENABLE_DOOR_PROTECTION[side.ordinal()]) {
            return false;
        }

        TilePosc pos = tilePos.add(side.opposite(), new TilePos());

        BlockLogic logic = world.getBlockType(pos).getLogic();

        if (logic instanceof BlockLogicTrapDoor) {
            return BlockLogicTrapDoor.isTrapdoorOpen(world.getBlockData(pos));
        }

        if (logic instanceof BlockLogicDoor) {
            return BlockLogicDoor.isOpen(world.getBlockData(pos));
        }

        return false;
    }

    @Override
    public boolean getBundledRedstoneConnectivity(World world, TilePosc pos, Direction side) {
        return true;
    }

    @Override
    public int getBundledRedstoneOutput(World world, TilePosc pos, Direction side) {
        TileEntity entity = (world.getTileEntity(pos));
        if (!(entity instanceof TileComputerBase computerEntity)) {
            return 0;
        }

        ServerComputer computer = computerEntity.getServerComputer();
        if (computer == null) {
            return 0;
        }

        ComputerSide localSide = computerEntity.remapToLocalSide(side);
        return computer.getBundledRedstoneOutput(localSide);
    }


    @Override
    public boolean onInteracted(World world, TilePosc tilePos, Player player, Side side, double xPlaced, double yPlaced) {
        return ((TileComputerBase) world.getTileEntity(tilePos)).onInteracted(player, side, xPlaced, yPlaced);
    }

    @Override
    public void onNeighborChanged(World world, TilePosc tilePos, Block block) {
        TileEntity entity = (world.getTileEntity(tilePos));
        if (!(entity instanceof TileComputerBase computerEntity)) {
            return;
        }

        computerEntity.onNeighbourChange(tilePos);
    }
}
