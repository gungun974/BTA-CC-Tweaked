package dan200.computercraft.shared.computer.items;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.shared.computer.blocks.TileComputerBase;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemBlockComputer<T extends BlockLogic> extends ItemBlockComputerBase<T> {
    public ItemBlockComputer(@NotNull Block<T> block) {
        super(block);
    }

    public ItemStack create(int id, String label) {
        ItemStack result = new ItemStack(this);

        CompoundTag tag = result.getData();

        if (id >= 0) {
            tag
                .putInt(NBT_ID, id);
        }

        result.setData(tag);

        if (label != null) {
            result.setCustomName(label);
        }
        return result;
    }

    @Override
    public ItemStack withFamily(@NotNull ItemStack stack, @NotNull ComputerFamily family) {
        ItemStack result = ComputerItemFactory.create(getComputerID(stack), null, family);
        if (stack.hasCustomName() && result != null) {
            result.setCustomName(stack.getCustomName());
        }
        return result;
    }

    public boolean onUseOnBlock(@NotNull ItemStack selfStack, @NotNull World world, @Nullable Player player, @NotNull TilePosc blockPos, @NotNull Side side, double xHit, double yHit) {
        TilePos tilePos = new TilePos(blockPos);

        if (selfStack.stackSize <= 0) {
            return false;
        } else {
            if (!world.canPlaceInsideBlock(tilePos)) {
                tilePos.x += side.offsetX();
                tilePos.y += side.offsetY();
                tilePos.z += side.offsetZ();
            }

            if (tilePos.y >= 0 && tilePos.y < world.getHeightBlocks()) {
                if (world.canBlockIdBePlacedAt(this.block.id(), tilePos, false, side) && selfStack.consumeItem(player)) {
                    int meta = this.getPlacedData(selfStack, world, player, tilePos, side, xHit, yHit);
                    if (world.setBlockTypeDataNotify(tilePos, this.block, meta)) {
                        if (player == null) {
                            this.block.onPlacedOnSide(world, tilePos, side, xHit, yHit);
                        } else {
                            this.block.onPlacedByMob(world, tilePos, side, player, xHit, yHit);
                        }

                        world.playBlockSoundEffect(player, (float) tilePos.x + 0.5F, (float) tilePos.y + 0.5F, (float) tilePos.z + 0.5F, this.block, EnumBlockSoundEffectType.PLACE);

                        TileEntity entity = (world.getTileEntity(tilePos));
                        if (!(entity instanceof TileComputerBase computerEntity)) {
                            return false;
                        }

                        computerEntity.readDescription(selfStack.getData());

                        // Set label
                        String label = selfStack.getCustomName();
                        if (label != null) {
                            computerEntity.setLabel(label);
                        }

                        return true;
                    }

                    if (player == null || player.getGamemode().hasBlockConsumption()) {
                        ++selfStack.stackSize;
                    }
                }

                return false;
            } else {
                return false;
            }
        }
    }
}
