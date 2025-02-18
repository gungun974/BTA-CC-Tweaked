/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.diskdrive;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.BlockPos;
import dan200.computercraft.api.filesystem.IMount;
import dan200.computercraft.api.filesystem.IWritableMount;
import dan200.computercraft.api.media.IMedia;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.fabric.Helper;
import dan200.computercraft.shared.MediaProviders;
import dan200.computercraft.shared.common.TileGeneric;
import dan200.computercraft.shared.network.client.OpenGuiContainerMessage;
import dan200.computercraft.shared.util.InventoryUtil;
import dan200.computercraft.shared.util.RecordUtil;
import net.minecraft.client.sound.SoundEvent;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class TileDiskDrive extends TileGeneric implements IPeripheralTile, Container
{
    private static final String NBT_NAME = "CustomName";
    private static final String NBT_ITEM = "Item";
    private final Map<IComputerAccess, MountInfo> computers = new HashMap<>();
    String customName;
    private ItemStack diskStack = null;
    private IMount diskMount = null;
    private boolean recordQueued = false;
    private boolean recordPlaying = false;
    private boolean restartRecord = false;
    private boolean ejectQueued;

    public TileDiskDrive()
    {
    }

//    @Override
//    public void destroy()
//    {
//        ejectContents( true );
//        if( recordPlaying )
//        {
//            stopRecord();
//        }
//    }

    public boolean onBlockRightClicked(Player player, Side side, double xPlaced, double yPlaced) {
        if( player.isSneaking() )
        {
            // Try to put a disk into the drive
            ItemStack disk = player.getHeldItem();
            if( disk == null )
            {
                return true;
            }
            if( !Helper.isClientWorld() && getItem( 0 ) == null && MediaProviders.get( disk ) != null )
            {
                setDiskStack( disk );
                player.setHeldObject( null );
            }
            return true;
        }
        else
        {
            // Open the GUI
            if( !Helper.isClientWorld() )
            {
                OpenGuiContainerMessage.SendToPlayer(player, this, ScreenDiskDrive.class, MenuDiskDrive::new);
            }
            return true;
        }
    }

    public Direction getDirection()
    {
        return BlockDiskDrive.getDirectionFromMeta(getBlockMeta());
    }

    @Override
    public void readFromNBT(CompoundTag nbt) {
        super.readFromNBT(nbt);
        customName = nbt.containsKey( NBT_NAME ) ?  nbt.getString( NBT_NAME ) : null;
        if( nbt.containsKey( NBT_ITEM ) )
        {
            CompoundTag item = nbt.getCompound( NBT_ITEM );
            diskStack = ItemStack.readItemStackFromNbt( item );
            diskMount = null;
        }
    }

    @Override
    public void writeToNBT(CompoundTag nbt) {
        if( customName != null )
        {
            nbt.putString( NBT_NAME,  customName );
        }

        if( diskStack != null )
        {
            CompoundTag item = new CompoundTag();
            diskStack.writeToNBT( item );
            nbt.put( NBT_ITEM, item );
        }

        super.writeToNBT(nbt);
    }

    public void markDirty()
    {
        if( !Helper.isClientWorld() )
        {
            updateBlockState();
        }
    }

    @Override
    public void tick()
    {
        // Ejection
        if( ejectQueued )
        {
            ejectContents( false );
            ejectQueued = false;
        }

        // Music
        synchronized( this )
        {
            if( !Helper.isClientWorld() && recordPlaying != recordQueued || restartRecord )
            {
                restartRecord = false;
                if( recordQueued )
                {
                    IMedia contents = getDiskMedia();
                    SoundEvent record = contents != null ? contents.getAudio( diskStack ) : null;
                    if( record != null )
                    {
                        recordPlaying = true;
                        playRecord();
                    }
                    else
                    {
                        recordQueued = false;
                    }
                }
                else
                {
                    stopRecord();
                    recordPlaying = false;
                }
            }
        }
    }

    // Container implementation

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public @Nullable ItemStack getItem(int i) {
        return diskStack;
    }

    @Override
    public @Nullable ItemStack removeItem(int slot, int count) {
        if( diskStack == null )
        {
            return null;
        }

        if( diskStack.stackSize <= count )
        {
            ItemStack disk = diskStack;
            setItem( slot, null );
            return disk;
        }

        ItemStack part = diskStack.splitStack( count );
        setItem( slot, diskStack == null ? null : diskStack );
        return part;
    }

    @Override
    public void setItem(int i, @Nullable ItemStack stack) {
        if( Helper.isClientWorld() )
        {
            diskStack = stack;
            diskMount = null;
            markDirty();
            return;
        }

        synchronized( this )
        {
            if( InventoryUtil.areItemsStackable( stack, diskStack ) )
            {
                diskStack = stack;
                return;
            }

            // Unmount old disk
            if( diskStack != null )
            {
                // TODO: Is this iteration thread safe?
                Set<IComputerAccess> computers = this.computers.keySet();
                for( IComputerAccess computer : computers )
                {
                    unmountDisk( computer );
                }
            }

            // Stop music
            if( recordPlaying )
            {
                stopRecord();
                recordPlaying = false;
                recordQueued = false;
            }

            // Swap disk over
            diskStack = stack;
            diskMount = null;
            markDirty();

            // Mount new disk
            if( diskStack != null )
            {
                Set<IComputerAccess> computers = this.computers.keySet();
                for( IComputerAccess computer : computers )
                {
                    mountDisk( computer );
                }
            }
        }

    }

    @Override
    public String getNameTranslationKey() {
        return "";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
        //return isUsable( player, false );
    }

    @Override
    public void sortContainer() {}

    @Nonnull
    @Override
    public IPeripheral getPeripheral( Direction side )
    {
        return new DiskDrivePeripheral( this );
    }

    String getDiskMountPath( IComputerAccess computer )
    {
        synchronized( this )
        {
            MountInfo info = computers.get( computer );
            return info != null ? info.mountPath : null;
        }
    }

    void mount( IComputerAccess computer )
    {
        synchronized( this )
        {
            computers.put( computer, new MountInfo() );
            mountDisk( computer );
        }
    }

    private synchronized void mountDisk( IComputerAccess computer )
    {
        if( diskStack != null )
        {
            MountInfo info = computers.get( computer );
            IMedia contents = getDiskMedia();
            if( contents != null )
            {
                if( diskMount == null )
                {
                    diskMount = contents.createDataMount( diskStack, worldObj );
                }
                if( diskMount != null )
                {
                    if( diskMount instanceof IWritableMount )
                    {
                        // Try mounting at the lowest numbered "disk" name we can
                        int n = 1;
                        while( info.mountPath == null )
                        {
                            info.mountPath = computer.mountWritable( n == 1 ? "disk" : "disk" + n, (IWritableMount) diskMount );
                            n++;
                        }
                    }
                    else
                    {
                        // Try mounting at the lowest numbered "disk" name we can
                        int n = 1;
                        while( info.mountPath == null )
                        {
                            info.mountPath = computer.mount( n == 1 ? "disk" : "disk" + n, diskMount );
                            n++;
                        }
                    }
                }
                else
                {
                    info.mountPath = null;
                }
            }
            computer.queueEvent( "disk", computer.getAttachmentName() );
        }
    }

    private IMedia getDiskMedia()
    {
        return MediaProviders.get( getDiskStack() );
    }

    @Nullable
    ItemStack getDiskStack()
    {
        return getItem( 0 );
    }

    void setDiskStack( @Nullable ItemStack stack )
    {
        setItem( 0, stack );
    }

    void unmount( IComputerAccess computer )
    {
        synchronized( this )
        {
            unmountDisk( computer );
            computers.remove( computer );
        }
    }

    private synchronized void unmountDisk( IComputerAccess computer )
    {
        if( diskStack != null )
        {
            MountInfo info = computers.get( computer );
            assert info != null;
            if( info.mountPath != null )
            {
                computer.unmount( info.mountPath );
                info.mountPath = null;
            }
            computer.queueEvent( "disk_eject", computer.getAttachmentName() );
        }
    }

    void playDiskAudio()
    {
        synchronized( this )
        {
            IMedia media = getDiskMedia();
            if( media != null && media.getAudioTitle( diskStack ) != null )
            {
                recordQueued = true;
                restartRecord = recordPlaying;
            }
        }
    }

    void stopDiskAudio()
    {
        synchronized( this )
        {
            recordQueued = false;
            restartRecord = false;
        }
    }

    // private methods

    void ejectDisk()
    {
        synchronized( this )
        {
            ejectQueued = true;
        }
    }

    private void updateBlockState()
    {
//        if( removed )
//        {
//            return;
//        }

        if( diskStack != null )
        {
            IMedia contents = getDiskMedia();
            updateBlockState( contents != null ? DiskDriveState.FULL : DiskDriveState.INVALID );
        }
        else
        {
            updateBlockState( DiskDriveState.EMPTY );
        }
    }

    private void updateBlockState( DiskDriveState newState )
    {
        final int currentMetadata = getBlockMeta();

        final DiskDriveState currentState = DiskDriveState.class.getEnumConstants()[(currentMetadata >> 3) & 0b11];

        if( currentState != newState )
        {
            final int newMetadata = (currentMetadata & ~0b11000) | (newState.ordinal() << 3);

            if (worldObj != null) {
                worldObj.setBlockMetadataWithNotify(this.x, this.y, this.z, newMetadata);
            }
        }
    }

    private synchronized void ejectContents( boolean destroyed )
    {
        if( Helper.isClientWorld() || diskStack == null )
        {
            return;
        }

        // Remove the disks from the inventory
        ItemStack disks = diskStack;
        setDiskStack( null );

        // Spawn the item in the world
        int xOff = 0;
        int zOff = 0;
        if( !destroyed )
        {
            Direction dir = getDirection();
            xOff = dir.getOffsetX();
            zOff = dir.getOffsetZ();
        }

        BlockPos pos = getPos();
        double x = pos.getX() + 0.5 + xOff * 0.5;
        double y = pos.getY() + 0.75;
        double z = pos.getZ() + 0.5 + zOff * 0.5;
        EntityItem entityitem = new EntityItem( worldObj, x, y, z, disks );
        entityitem.xd = xOff * 0.15;
        entityitem.yd = 0;
        entityitem.zd = zOff * 0.15 ;

        worldObj.entityJoinedWorld( entityitem );
        if( !destroyed )
        {
            //worldObj.syncGlobalEvent( 1000, getPos(), 0 );
        }
    }

    private BlockPos getPos() {
        return new BlockPos(x, y, z);
    }

    private void playRecord()
    {
        IMedia contents = getDiskMedia();
        SoundEvent record = contents != null ? contents.getAudio( diskStack ) : null;
        if( record != null )
        {
            RecordUtil.playRecord( record, contents.getAudioTitle( diskStack ), worldObj, new BlockPos(x, y, z) );
        }
        else
        {
            RecordUtil.playRecord( null, null, worldObj, new BlockPos(x, y, z) );
        }
    }

    // Private methods

    private void stopRecord()
    {
        RecordUtil.playRecord( null, null, worldObj, new BlockPos(x, y, z) );
    }

    @Nonnull
    public String getName()
    {
        return customName != null ? customName : "A name";
    }

//    @Nonnull
//    @Override
//    public ScreenHandler createMenu( int id, @Nonnull PlayerInventory inventory, @Nonnull PlayerEntity player )
//    {
//        return new ContainerDiskDrive( id, inventory, this );
//    }

    private static class MountInfo
    {
        String mountPath;
    }
}
