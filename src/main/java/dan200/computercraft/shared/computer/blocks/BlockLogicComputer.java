package dan200.computercraft.shared.computer.blocks;

import com.mojang.logging.LogUtils;
import dan200.computercraft.BlockPos;
import dan200.computercraft.ComputerCraft;
import dan200.computercraft.client.gui.GuiComputer;
import dan200.computercraft.core.computer.Computer;
import dan200.computercraft.core.computer.ComputerSide;
import dan200.computercraft.shared.common.IBundledRedstoneBlock;
import dan200.computercraft.shared.computer.core.ClientComputer;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.ServerComputer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.container.ScreenFurnace;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicRotatable;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityFurnace;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class BlockLogicComputer extends BlockLogicRotatable implements IBundledRedstoneBlock {
    public BlockLogicComputer(Block<?> block) {
        super(block, Material.stone);
        block.withEntity(TileEntityComputer::new);
    }

    public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
        switch (dropCause) {
            case PICK_BLOCK:
            case EXPLOSION:
            case PROPER_TOOL:
            case SILK_TOUCH:
            case PISTON_CRUSH:
                return new ItemStack[]{new ItemStack(Blocks.FURNACE_STONE_IDLE)};
            default:
                return null;
        }
    }

    public boolean isSignalSource() {
        return true;
    }

//    public void onBlockPlacedByWorld(World world, int x, int y, int z) {
//        Side[] var5 = Side.sides;
//        int var6 = var5.length;
//
//        for(int var7 = 0; var7 < var6; ++var7) {
//            Side s = var5[var7];
//            world.notifyBlocksOfNeighborChange(x + s.getOffsetX(), y + s.getOffsetY(), z + s.getOffsetZ(), this.id());
//        }
//
//    }
//
//    public void onBlockRemoved(World world, int x, int y, int z, int data) {
//        Side[] var6 = Side.sides;
//        int var7 = var6.length;
//
//        for(int var8 = 0; var8 < var7; ++var8) {
//            Side s = var6[var8];
//            world.notifyBlocksOfNeighborChange(x + s.getOffsetX(), y + s.getOffsetY(), z + s.getOffsetZ(), this.id());
//        }
//
//    }

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
        return ((TileEntityComputer)world.getTileEntity(x, y, z)).onBlockRightClicked(player, side, xPlaced, yPlaced);
    }
}
