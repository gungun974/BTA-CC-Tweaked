/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.computer.blocks;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.BlockPos;
import dan200.computercraft.ComputerCraft;
import dan200.computercraft.core.computer.ComputerSide;
import dan200.computercraft.fabric.IComputerPlayer;
import dan200.computercraft.shared.common.TileGeneric;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.ComputerState;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.computer.inventory.ContainerComputer;
import dan200.computercraft.shared.util.DirectionUtil;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;

import javax.annotation.Nonnull;

public abstract class TileComputerBase extends TileGeneric implements IComputerTile//, IPeripheralTile
{
    private static final String NBT_ID = "ComputerId";
    private static final String NBT_LABEL = "Label";
    private static final String NBT_ON = "On";
    private final ComputerFamily family;
    protected String label = null;
    boolean startOn = false;
    private int instanceID = -1;
    private int computerID = -1;
    private boolean on = false;
    private boolean fresh = false;

    public TileComputerBase( ComputerFamily family )
    {
        super();
        this.family = family;
    }

    @Override
    public void destroy()
    {
        unload();
        for( Direction dir : DirectionUtil.FACINGS )
        {
            //RedstoneUtil.propagateRedstoneOutput( getWorld(), getPos(), dir );
        }
    }

    @Override
    public void onChunkUnloaded()
    {
        unload();
    }

    protected void unload()
    {
        if( instanceID >= 0 )
        {
            if( true || !worldObj.isClientSide )
            {
                ComputerCraft.serverComputerRegistry.remove( instanceID );
            }
            instanceID = -1;
        }
    }

    public boolean onBlockRightClicked(Player player, Side side, double xPlaced, double yPlaced) {
//        ItemStack currentItem = player.getStackInHand( hand );
//        if( !currentItem.isEmpty() && currentItem.getItem() == Items.NAME_TAG && canNameWithTag( player ) && currentItem.hasCustomName() )
//        {
//            // Label to rename computer
//            if( !getWorld().isClient )
//            {
//                setLabel( currentItem.getName()
//                    .getString() );
//                currentItem.decrement( 1 );
//            }
//            return ActionResult.SUCCESS;
//        }
//        else if( !player.isInSneakingPose() )
//        {
            // Regular right click to activate computer
            if( !worldObj.isClientSide && isUsable( player, false ) )
            {
                createServerComputer().turnOn();
                createServerComputer().sendTerminalState( player );
                ((IComputerPlayer) player).setCurrentContainerComputer(new ContainerComputer(this));
                createServerComputer().sendOpenComputerGui( player );
            }

            return true;
//            return ActionResult.SUCCESS;
//        }
//        return ActionResult.PASS;
    }

    protected boolean canNameWithTag( Player player )
    {
        return false;
    }


    public ServerComputer createServerComputer()
    {
        if( false && worldObj.isClientSide )
        {
            return null;
        }

        boolean changed = false;
        if( instanceID < 0 )
        {
            instanceID = ComputerCraft.serverComputerRegistry.getUnusedInstanceID();
            changed = true;
        }
        if( !ComputerCraft.serverComputerRegistry.contains( instanceID ) )
        {
            ServerComputer computer = createComputer( instanceID, computerID );
            ComputerCraft.serverComputerRegistry.add( instanceID, computer );
            fresh = true;
            changed = true;
        }
        if( changed )
        {
            //updateBlock();
            //updateInput();
        }
        return ComputerCraft.serverComputerRegistry.get( instanceID );
    }

    public ServerComputer getServerComputer()
    {
        return false && worldObj.isClientSide ? null : ComputerCraft.serverComputerRegistry.get( instanceID );
    }

    protected abstract ServerComputer createComputer( int instanceID, int id );

    /*
    public void updateInput()
    {
        if( getWorld() == null || getWorld().isClient )
        {
            return;
        }

        // Update all sides
        ServerComputer computer = getServerComputer();
        if( computer == null )
        {
            return;
        }

        BlockPos pos = computer.getPosition();
        for( Direction dir : DirectionUtil.FACINGS )
        {
            updateSideInput( computer, dir, pos.offset( dir ) );
        }
    }
     */

    /*
    private void updateSideInput( ServerComputer computer, Direction dir, BlockPos offset )
    {
        Direction offsetSide = dir.getOpposite();
        ComputerSide localDir = remapToLocalSide( dir );

        computer.setRedstoneInput( localDir, getRedstoneInput( world, offset, dir ) );
        computer.setBundledRedstoneInput( localDir, BundledRedstone.getOutput( getWorld(), offset, offsetSide ) );
        if( !isPeripheralBlockedOnSide( localDir ) )
        {
            IPeripheral peripheral = Peripherals.getPeripheral( getWorld(), offset, offsetSide );
            computer.setPeripheral( localDir, peripheral );
        }
    }
     */

    protected ComputerSide remapToLocalSide( Direction globalSide )
    {
        return remapLocalSide( DirectionUtil.toLocal( getDirection(), globalSide ) );
    }

    /*
     * Gets the redstone input for an adjacent block.
     *
     * @param world The world we exist in
     * @param pos   The position of the neighbour
     * @param side  The side we are reading from
     * @return The effective redstone power
     */
    /*
    protected static int getRedstoneInput( World world, BlockPos pos, Direction side )
    {
        int power = world.getEmittedRedstonePower( pos, side );
        if( power >= 15 )
        {
            return power;
        }

        BlockState neighbour = world.getBlockState( pos );
        return neighbour.getBlock() == Blocks.REDSTONE_WIRE ? Math.max( power, neighbour.get( RedstoneWireBlock.POWER ) ) : power;
    }
     */

    protected boolean isPeripheralBlockedOnSide( ComputerSide localSide )
    {
        return false;
    }

    protected ComputerSide remapLocalSide( ComputerSide localSide )
    {
        return localSide;
    }

    protected abstract Direction getDirection();

    @Override
    public void onNeighbourChange( @Nonnull BlockPos neighbour )
    {
        //updateInput( neighbour );
    }

    @Override
    public void onNeighbourTileEntityChange( @Nonnull BlockPos neighbour )
    {
        //updateInput( neighbour );
    }

    /*
    @Override
    protected void readDescription( @Nonnull CompoundTag nbt )
    {
        super.readDescription( nbt );
        label = nbt.contains( NBT_LABEL ) ? nbt.getString( NBT_LABEL ) : null;
        computerID = nbt.contains( NBT_ID ) ? nbt.getInt( NBT_ID ) : -1;
    }

    @Override
    protected void writeDescription( @Nonnull CompoundTag nbt )
    {
        super.writeDescription( nbt );
        if( label != null )
        {
            nbt.putString( NBT_LABEL, label );
        }
        if( computerID >= 0 )
        {
            nbt.putInt( NBT_ID, computerID );
        }
    }

     */

    @Override
    public void tick()
    {
        if( true || !worldObj.isClientSide )
        {
            ServerComputer computer = createServerComputer();
            if( computer == null )
            {
                return;
            }

            // If the computer isn't on and should be, then turn it on
            if( startOn || (fresh && on) )
            {
                computer.turnOn();
                startOn = false;
            }

            computer.keepAlive();

            fresh = false;
            computerID = computer.getID();
            label = computer.getLabel();
            on = computer.isOn();

            if( computer.hasOutputChanged() )
            {
                updateOutput();
            }

            // Update the block state if needed. We don't fire a block update intentionally,
            // as this only really is needed on the client side.
            updateBlockState( computer.getState() );

            if( computer.hasOutputChanged() )
            {
                updateOutput();
            }
        }
    }

    public void updateOutput()
    {
        // Update redstone
        //updateBlock();
        for( Direction dir : DirectionUtil.FACINGS )
        {
            //RedstoneUtil.propagateRedstoneOutput( getWorld(), getPos(), dir );
        }
    }

    protected abstract void updateBlockState( ComputerState newState );

    @Override
    public void readFromNBT( CompoundTag nbt )
    {
        super.readFromNBT( nbt );

        // Load ID, label and power state
        computerID = nbt.containsKey( NBT_ID ) ? nbt.getInteger( NBT_ID ) : -1;
        label = nbt.containsKey( NBT_LABEL ) ? nbt.getString( NBT_LABEL ) : null;
        on = startOn = nbt.getBoolean( NBT_ON );
    }

    @Override
    public void writeToNBT( CompoundTag nbt )
    {
        // Save ID, label and power state
        if( computerID >= 0 )
        {
            nbt.putInt( NBT_ID, computerID );
        }
        if( label != null )
        {
            nbt.putString( NBT_LABEL, label );
        }
        nbt.putBoolean( NBT_ON, on );

        super.writeToNBT( nbt );
    }

    /*
    @Override
    public void markRemoved()
    {
        unload();
        super.markRemoved();
    }
     */

    /*
    private void updateInput( BlockPos neighbour )
    {
        if( getWorld() == null || getWorld().isClient )
        {
            return;
        }

        ServerComputer computer = getServerComputer();
        if( computer == null )
        {
            return;
        }

        for( Direction dir : DirectionUtil.FACINGS )
        {
            BlockPos offset = pos.offset( dir );
            if( offset.equals( neighbour ) )
            {
                updateSideInput( computer, dir, offset );
                return;
            }
        }

        // If the position is not any adjacent one, update all inputs.
        updateInput();
    }
     */

    /*
    private void updateInput( Direction dir )
    {
        if( getWorld() == null || getWorld().isClient )
        {
            return;
        }

        ServerComputer computer = getServerComputer();
        if( computer == null )
        {
            return;
        }

        updateSideInput( computer, dir, pos.offset( dir ) );
    }
     */

    @Override
    public final int getComputerID()
    {
        return computerID;
    }

    @Override
    public final void setComputerID( int id )
    {
        if( false && worldObj.isClientSide || computerID == id )
        {
            return;
        }

        computerID = id;
        ServerComputer computer = getServerComputer();
        if( computer != null )
        {
            computer.setID( computerID );
        }
        //markDirty();
    }

    @Override
    public final String getLabel()
    {
        return label;
    }

    // Networking stuff

    @Override
    public final void setLabel( String label )
    {
//        if( getWorld().isClient || Objects.equals( this.label, label ) )
//        {
//            return;
//        }

        this.label = label;
        ServerComputer computer = getServerComputer();
        if( computer != null )
        {
            computer.setLabel( label );
        }
        //markDirty();
    }

    @Override
    public ComputerFamily getFamily()
    {
        return family;
    }

    /*
    protected void transferStateFrom( TileComputerBase copy )
    {
        if( copy.computerID != computerID || copy.instanceID != instanceID )
        {
            unload();
            instanceID = copy.instanceID;
            computerID = copy.computerID;
            label = copy.label;
            on = copy.on;
            startOn = copy.startOn;
            updateBlock();
        }
        copy.instanceID = -1;
    }
     */

    /*
    @Nonnull
    @Override
    public IPeripheral getPeripheral( Direction side )
    {
        return new ComputerPeripheral( "computer", createProxy() );
    }

    public abstract ComputerProxy createProxy();
     */

    /*
    @Nonnull
    @Override
    public Text getName()
    {
        return hasCustomName() ? new LiteralText( label ) : new TranslatableText( getCachedState().getBlock()
            .getTranslationKey() );
    }
     */

    /*
    @Override
    public boolean hasCustomName()
    {
        return !Strings.isNullOrEmpty( label );
    }

    @Nonnull
    @Override
    public Text getDisplayName()
    {
        return Nameable.super.getDisplayName();
    }

    @Nullable
    @Override
    public Text getCustomName()
    {
        return hasCustomName() ? new LiteralText( label ) : null;
    }

    @Override
    public void writeScreenOpeningData( ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf )
    {
        packetByteBuf.writeInt( getServerComputer().getInstanceID() );
        packetByteBuf.writeEnumConstant( getServerComputer().getFamily() );
    }
     */
}
