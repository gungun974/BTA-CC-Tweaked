package dan200.computercraft.shared.computer.blocks;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import java.util.List;
import java.util.Random;

import dan200.computercraft.BlockPos;
import dan200.computercraft.ComputerCraft;
import dan200.computercraft.core.computer.ComputerSide;
import dan200.computercraft.fabric.Helper;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.ComputerState;
import dan200.computercraft.shared.computer.core.ServerComputer;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicFurnace;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.motion.CarriedBlock;
import net.minecraft.core.crafting.LookupFuelFurnace;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryFurnace;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.PacketTileEntityData;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;

public class TileEntityComputer extends TileComputerBase  {
    public TileEntityComputer() {
        super(ComputerFamily.NORMAL);
    }

    public boolean canBeCarried(World world, Entity potentialHolder) {
        return false;
    }


    public boolean isUsableByPlayer( Player player )
    {
        return isUsable( player, false );
    }

    @Override
    protected void updateBlockState( ComputerState newState )
    {
        final int currentMetadata = getBlockMeta();

        final ComputerState currentState = ComputerState.class.getEnumConstants()[(currentMetadata >> 3) & 0b11];

        if( currentState != newState )
        {
            final int newMetadata = (currentMetadata & ~0b11000) | (newState.ordinal() << 3);

            if (worldObj != null) {
                worldObj.setBlockMetadataWithNotify(this.x, this.y, this.z, newMetadata);
            }
        }
    }

    @Override
    public Direction getDirection()
    {
        return BlockLogicComputer.getDirectionFromMeta(getBlockMeta());
    }

    @Override
    protected ComputerSide remapLocalSide( ComputerSide localSide )
    {
        // For legacy reasons, computers invert the meaning of "left" and "right". A computer's front is facing
        // towards you, but a turtle's front is facing the other way.
        if( localSide == ComputerSide.RIGHT )
        {
            return ComputerSide.LEFT;
        }
        if( localSide == ComputerSide.LEFT )
        {
            return ComputerSide.RIGHT;
        }
        return localSide;
    }

    @Override
    protected ServerComputer createComputer( int instanceID, int id )
    {
        ComputerFamily family = getFamily();
        ServerComputer computer = new ServerComputer( worldObj,
            id, label,
            instanceID,
            family,
            ComputerCraft.computerTermWidth,
            ComputerCraft.computerTermHeight );
        computer.setPosition( new BlockPos(x, y, z) );
        return computer;
    }

//    @Override
//    public ComputerProxy createProxy()
//    {
//        if( proxy == null )
//        {
//            proxy = new ComputerProxy( () -> this )
//            {
//                @Override
//                protected TileComputerBase getTile()
//                {
//                    return TileComputer.this;
//                }
//            };
//        }
//        return proxy;
//    }

//    @Nullable
//    @Override
//    public ScreenHandler createMenu( int id, @Nonnull PlayerInventory inventory, @Nonnull PlayerEntity player )
//    {
//        return new ContainerComputer( id, this );
//    }
}
