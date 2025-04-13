package dan200.computercraft.fabric.mixin;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.shared.common.ComputerCraftItems;
import dan200.computercraft.shared.media.items.ItemTreasureDisk;
import dan200.computercraft.shared.media.items.TreasureDisk;
import net.minecraft.core.WeightedRandomBag;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.generate.feature.WorldFeatureLabyrinth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(value = WorldFeatureLabyrinth.class, remap = false)
public class WorldFeatureLabyrinthMixin {
    @Shadow
    public WeightedRandomBag<WeightedRandomLootObject> chestLoot;

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void addTreasuresDisks(CallbackInfo ci) {
        this.chestLoot.addEntry(new WeightedRandomLootObject(new ItemStack(ComputerCraftItems.TREASURE_DISK, 1, 0)), 100.0);
    }


    @Inject(method = "pickCheckLootItem", at = @At("RETURN"), cancellable = true)
    private void makeRandomTreasureDisk(Random random, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack itemStack = cir.getReturnValue();

        if (itemStack != null && itemStack.getItem().equals(ComputerCraftItems.TREASURE_DISK)) {
            CompoundTag tag = itemStack.getData();

            int randomIndex = random.nextInt(TreasureDisk.DISKS.length);
            TreasureDisk randomDisk = TreasureDisk.DISKS[randomIndex];

            tag.putString(ItemTreasureDisk.NBT_TITLE, randomDisk.getTitle());
            tag.putString(ItemTreasureDisk.NBT_SUB_PATH, randomDisk.getSubPath());
            tag.putInt(ItemTreasureDisk.NBT_COLOUR, randomDisk.getColour());

            itemStack.setData(tag);
        }

        cir.setReturnValue(itemStack);
    }
}
