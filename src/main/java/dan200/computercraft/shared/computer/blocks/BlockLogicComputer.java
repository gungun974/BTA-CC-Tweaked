package dan200.computercraft.shared.computer.blocks;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.BlockPos;
import dan200.computercraft.core.computer.ComputerSide;
import dan200.computercraft.shared.common.ComputerCraftBlocks;
import dan200.computercraft.shared.common.IBundledRedstoneBlock;
import dan200.computercraft.shared.computer.core.ServerComputer;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import org.jetbrains.annotations.Nullable;

public class BlockLogicComputer extends BlockLogicRotatable implements IBundledRedstoneBlock {
    public BlockLogicComputer(Block<?> block) {
        super(block, Material.stone);
        block.withEntity(TileEntityComputer::new);
    }

    @Nullable
    private ItemStack getItemStack(TileEntity entity) {
        if( !(entity instanceof TileComputerBase) )
        {
            return null;
        }

        final TileComputerBase computerEntity = (TileComputerBase) entity;

        final CompoundTag tags = new CompoundTag();

        computerEntity.writeDescription(tags);

        final ItemStack item = new ItemStack(ComputerCraftBlocks.COMPUTER_NORMAL, 1 ,0, tags);

        if (computerEntity.getLabel() != null) {
            item.setCustomName(computerEntity.getLabel());
        }

        return item;

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

                return new ItemStack[]{item};
            default:
                return null;
        }
    }

    @Override
    public void onBlockRemoved(World world, int x, int y, int z, int data) {
        TileEntity entity = (world.getTileEntity(x, y, z));
        if( !(entity instanceof TileComputerBase) )
        {
            return;
        }

        TileComputerBase computerEntity = (TileComputerBase) entity;

        computerEntity.destroy();
    }

    public boolean isSignalSource() {
        return true;
    }

    public boolean getDirectSignal(World world, int x, int y, int z, Side side) {
        return this.getSignal(world, x, y, z, side);
    }

    public boolean getSignal(WorldSource world, int x, int y, int z, Side side) {
        TileEntity entity = (world.getTileEntity(x, y, z));
        if( !(entity instanceof TileComputerBase) )
        {
            return false;
        }

        TileComputerBase computerEntity = (TileComputerBase) entity;
        ServerComputer computer = computerEntity.getServerComputer();
        if( computer == null )
        {
            return false;
        }

        ComputerSide localSide = computerEntity.remapToLocalSide( side.getOpposite().getDirection() );
        return computer.getRedstoneOutput( localSide ) > 0;
    }

    @Override
    public boolean getBundledRedstoneConnectivity(World world, BlockPos pos, Direction side )
    {
        return true;
    }

    @Override
    public int getBundledRedstoneOutput( World world, BlockPos pos, Direction side )
    {
        TileEntity entity = (world.getTileEntity(pos.x, pos.y, pos.z));
        if( !(entity instanceof TileComputerBase) )
        {
            return 0;
        }

        TileComputerBase computerEntity = (TileComputerBase) entity;
        ServerComputer computer = computerEntity.getServerComputer();
        if( computer == null )
        {
            return 0;
        }

        ComputerSide localSide = computerEntity.remapToLocalSide( side );
        return computer.getBundledRedstoneOutput( localSide );
    }


    public boolean onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xPlaced, double yPlaced) {
        return ((TileComputerBase)world.getTileEntity(x, y, z)).onBlockRightClicked(player, side, xPlaced, yPlaced);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
        TileEntity entity = (world.getTileEntity(x, y, z));
        if( !(entity instanceof TileComputerBase) )
        {
            return;
        }

        TileComputerBase computerEntity = (TileComputerBase) entity;

        computerEntity.onNeighbourChange(new BlockPos(x, y, z));
    }
}
