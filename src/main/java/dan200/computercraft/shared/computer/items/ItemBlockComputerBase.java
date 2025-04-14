package dan200.computercraft.shared.computer.items;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.filesystem.IMount;
import dan200.computercraft.api.media.IMedia;
import dan200.computercraft.shared.common.ComputerCraftBlocks;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.world.World;

import javax.annotation.Nonnull;

public abstract class ItemBlockComputerBase<T extends BlockLogic> extends ItemBlock<T> implements IComputerItem, IMedia {
    private final ComputerFamily family;

    public ItemBlockComputerBase(Block<T> block) {
        super(block);

        if (block.equals(ComputerCraftBlocks.COMPUTER_ADVANCED)) {
            family = ComputerFamily.ADVANCED;
            return;
        }
        if (block.equals(ComputerCraftBlocks.TURTLE_ADVANCED)) {
            family = ComputerFamily.ADVANCED;
            return;
        }
        family = ComputerFamily.NORMAL;
    }

    @Override
    public String getTranslatedDescription(ItemStack stack) {
        I18n i18n = I18n.getInstance();

        String text = super.getTranslatedDescription(stack);

        if (getLabel(stack) == null) {
            int id = getComputerID(stack);
            if (id >= 0) {
                text += "\n" + i18n.translateKeyAndFormat("gui.computercraft.tooltip.computer_id", id);
            }
        }

        return text;
    }

    @Override
    public String getLabel(@Nonnull ItemStack stack) {
        return IComputerItem.super.getLabel(stack);
    }

    @Override
    public final ComputerFamily getFamily() {
        return family;
    }

    // IMedia implementation

    @Override
    public boolean setLabel(@Nonnull ItemStack stack, String label) {
        if (label != null) {
            stack.setCustomName(label);
        } else {
            stack.removeCustomName();
        }
        return true;
    }

    @Override
    public IMount createDataMount(@Nonnull ItemStack stack, @Nonnull World world) {
        ComputerFamily family = getFamily();
        if (family != ComputerFamily.COMMAND) {
            int id = getComputerID(stack);
            if (id >= 0) {
                return ComputerCraftAPI.createSaveDirMount(world, "computer/" + id, ComputerCraft.computerSpaceLimit);
            }
        }
        return null;
    }
}
