package dan200.computercraft.shared.computer.items;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.shared.computer.blocks.TileComputerBase;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class ItemBlockComputer extends ItemComputerBase {
    public ItemBlockComputer(@NotNull Block block) {
        super(block);
    }

    public ItemStack create( int id, String label )
    {
        ItemStack result = new ItemStack( this );

       CompoundTag tag = result.getData();

        if( id >= 0 )
        {
            tag
                .putInt( NBT_ID, id );
        }

       result.setData(tag);

        if( label != null )
        {
            result.setCustomName( label );
        }
        return result;
    }

    @Override
    public ItemStack withFamily(@Nonnull ItemStack stack, @Nonnull ComputerFamily family )
    {
        ItemStack result = ComputerItemFactory.create( getComputerID( stack ), null, family );
        if (stack.hasCustomName() && result != null) {
            result.setCustomName(stack.getCustomName());
        }
        return result;
    }

    public boolean onUseItemOnBlock(ItemStack stack, @Nullable Player player, World world, int x, int y, int z, Side side, double xPlaced, double yPlaced) {
        if (stack.stackSize <= 0) {
            return false;
        } else {
            if (!world.canPlaceInsideBlock(x, y, z)) {
                x += side.getOffsetX();
                y += side.getOffsetY();
                z += side.getOffsetZ();
            }

            if (y >= 0 && y < world.getHeightBlocks()) {
                if (world.canBlockBePlacedAt(this.block.id(), x, y, z, false, side) && stack.consumeItem(player)) {
                    int meta = this.getPlacedBlockMetadata(player, stack, world, x, y, z, side, xPlaced, yPlaced);
                    if (world.setBlockAndMetadataWithNotify(x, y, z, this.block.id(), meta)) {
                        if (player == null) {
                            this.block.onBlockPlacedOnSide(world, x, y, z, side, xPlaced, yPlaced);
                        } else {
                            this.block.onBlockPlacedByMob(world, x, y, z, side, player, xPlaced, yPlaced);
                        }

                        world.playBlockSoundEffect(player, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this.block, EnumBlockSoundEffectType.PLACE);

                        TileEntity entity = (world.getTileEntity(x, y, z));
                        if( !(entity instanceof TileComputerBase) )
                        {
                            return false;
                        }

                        TileComputerBase computerEntity = (TileComputerBase) entity;

                        computerEntity.readDescription(stack.getData());

                        return true;
                    }

                    if (player == null || player.getGamemode().consumeBlocks()) {
                        ++stack.stackSize;
                    }
                }

                return false;
            } else {
                return false;
            }
        }
    }
}
