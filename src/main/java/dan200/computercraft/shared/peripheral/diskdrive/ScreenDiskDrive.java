/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.diskdrive;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class ScreenDiskDrive extends ScreenContainerAbstract {
	private final TileDiskDrive furnaceInventory;

	public ScreenDiskDrive(ContainerInventory inventory, TileDiskDrive tileEntityFurnace) {
		super(new MenuDiskDrive(inventory, tileEntityFurnace));
		this.furnaceInventory = tileEntityFurnace;
	}

	@Override
	protected void drawGuiContainerForegroundLayer() {
		I18n i18n = I18n.getInstance();
//		if (this.furnaceInventory instanceof TileEntityFurnaceBlast) {
//			this.font.drawString(i18n.translateKey("gui.furnace.blast.label.blast_furnace"), 60, 6, 4210752);
//		} else {
			this.font.drawString(i18n.translateKey("gui.furnace.label.furnace"), 60, 6, 4210752);
//		}

		this.font.drawString(i18n.translateKey("gui.furnace.label.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f) {
//		if (this.furnaceInventory instanceof TileEntityFurnaceBlast) {
//			this.mc.textureManager.loadTexture("/assets/minecraft/textures/gui/container/blastfurnace.png").bind();
//		} else {
			this.mc.textureManager.loadTexture("/assets/minecraft/textures/gui/container/furnace.png").bind();
//		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
//		if (this.furnaceInventory.isBurning()) {
//			if (this.furnaceInventory instanceof TileEntityFurnaceBlast) {
//				int fireHeight = this.furnaceInventory.getBurnTimeRemainingScaled(14);
//				this.drawTexturedModalRect(x + 56, y + 35 + 14 - fireHeight, 176, 14 - fireHeight, 14, fireHeight + 2);
//				int arrowWidth = this.furnaceInventory.getCookProgressScaled(24);
//				this.drawTexturedModalRect(x + 79, y + 34, 176, 15, arrowWidth + 1, 16);
//			} else {
//				int fireHeight = this.furnaceInventory.getBurnTimeRemainingScaled(12);
//				this.drawTexturedModalRect(x + 56, y + 36 + 12 - fireHeight, 176, 12 - fireHeight, 14, fireHeight + 2);
//				int arrowWidth = this.furnaceInventory.getCookProgressScaled(24);
//				this.drawTexturedModalRect(x + 79, y + 34, 176, 14, arrowWidth + 1, 16);
//			}
//		}
	}


//    private final Inventory inventory;
//
//    public ContainerDiskDrive( int id, PlayerInventory player )
//    {
//        this( id, player, new SimpleInventory( 1 ) );
//    }
//
//    public ContainerDiskDrive( int id, PlayerInventory player, Inventory inventory )
//    {
//        super( ComputerCraftRegistry.ModContainers.DISK_DRIVE, id );
//
//        this.inventory = inventory;
//
//        addSlot( new Slot( this.inventory, 0, 8 + 4 * 18, 35 ) );
//
//        for( int y = 0; y < 3; y++ )
//        {
//            for( int x = 0; x < 9; x++ )
//            {
//                addSlot( new Slot( player, x + y * 9 + 9, 8 + x * 18, 84 + y * 18 ) );
//            }
//        }
//
//        for( int x = 0; x < 9; x++ )
//        {
//            addSlot( new Slot( player, x, 8 + x * 18, 142 ) );
//        }
//    }
//
//    @Nonnull
//    @Override
//    public ItemStack transferSlot( @Nonnull PlayerEntity player, int slotIndex )
//    {
//        Slot slot = slots.get( slotIndex );
//        if( slot == null || !slot.hasStack() )
//        {
//            return ItemStack.EMPTY;
//        }
//
//        ItemStack existing = slot.getStack()
//            .copy();
//        ItemStack result = existing.copy();
//        if( slotIndex == 0 )
//        {
//            // Insert into player inventory
//            if( !insertItem( existing, 1, 37, true ) )
//            {
//                return ItemStack.EMPTY;
//            }
//        }
//        else
//        {
//            // Insert into drive inventory
//            if( !insertItem( existing, 0, 1, false ) )
//            {
//                return ItemStack.EMPTY;
//            }
//        }
//
//        if( existing.isEmpty() )
//        {
//            slot.setStack( ItemStack.EMPTY );
//        }
//        else
//        {
//            slot.markDirty();
//        }
//
//        if( existing.getCount() == result.getCount() )
//        {
//            return ItemStack.EMPTY;
//        }
//
//        slot.onTakeItem( player, existing );
//        return result;
//    }
//
//    @Override
//    public boolean canUse( @Nonnull PlayerEntity player )
//    {
//        return inventory.canPlayerUse( player );
//    }
}
