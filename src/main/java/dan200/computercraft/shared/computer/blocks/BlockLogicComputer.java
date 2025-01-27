package dan200.computercraft.shared.computer.blocks;

import com.mojang.logging.LogUtils;
import dan200.computercraft.ComputerCraft;
import dan200.computercraft.client.gui.GuiComputer;
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
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.slf4j.Logger;

import java.util.Random;

public class BlockLogicComputer extends BlockLogicRotatable {
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

    public boolean onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xPlaced, double yPlaced) {
        if (!world.isClientSide) {
            TileEntityComputer tileEntityComputer = (TileEntityComputer)world.getTileEntity(x, y, z);
            ServerComputer server = tileEntityComputer.createServerComputer();

            ClientComputer client = new ClientComputer(server.getInstanceID());


            Thread thread2 = new Thread(() -> {
                while (true) {
                    client.read(server.write());
                }
            });

            thread2.start();

server.turnOn();

            Minecraft.getMinecraft().displayScreen(new GuiComputer(client, server.getTerminal().getWidth(), server.getTerminal().getHeight()));
        }

        return true;
    }
}
