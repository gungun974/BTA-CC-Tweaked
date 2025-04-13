/*
 * This file is part of the public ComputerCraft API - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. This API may be redistributed unmodified and in full only.
 * For help using the API, and posting your mods, visit the forums at computercraft.info.
 */
package dan200.computercraft.api.turtle;

import dan200.computercraft.shared.turtle.blocks.BlockAORenderer;
import dan200.computercraft.shared.turtle.blocks.TileTurtle;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;

import javax.annotation.Nonnull;

/**
 * A base class for {@link ITurtleUpgrade}s.
 * <p>
 * One does not have to use this, but it does provide a convenient template.
 */
public abstract class AbstractTurtleUpgrade implements ITurtleUpgrade {
    private final int id;
    private final TurtleUpgradeType type;
    private final ItemStack stack;

    protected AbstractTurtleUpgrade(int id, TurtleUpgradeType type, IItemConvertible item) {
        this(id, type, new ItemStack(item));
    }

    protected AbstractTurtleUpgrade(int id, TurtleUpgradeType type, ItemStack stack) {
        this.id = id;
        this.type = type;
        this.stack = stack;
    }

    protected static void drawUpgradeLeft(Tessellator tessellator, TileTurtle tileEntity, float angle) {
        (new BlockAORenderer(AABB.getTemporaryBB(0.5 / 16f, 4.5 / 16f, 3.5 / 16f, 2 / 16f, 12.5 / 16f, 11.5 / 16f)))
            .setBottomUV(14 / 16f, 14 / 16f, 16 / 16f, 2 / 16f)
            .setTopUV(3 / 16f, 2 / 16f, 0, 14 / 16f)
            .setNorthUV(0, 2 / 16f, 3 / 16f, 14 / 16f)
            .setSouthUV(13 / 16f, 2 / 16f, 16 / 16f, 14 / 16f)
            .setWestUV(2 / 16f, 2 / 16f, 14 / 16f, 14 / 16f)
            .render(tessellator, tileEntity, angle, 1f, 1f, 1f);
    }

    protected static void drawUpgradeRight(Tessellator tessellator, TileTurtle tileEntity, float angle) {
        (new BlockAORenderer(AABB.getTemporaryBB(14 / 16f, 4.5 / 16f, 3.5 / 16f, 15.5 / 16f, 12.5 / 16f, 11.5 / 16f)))
            .setBottomUV(16 / 16f, 2 / 16f, 14 / 16f, 14 / 16f)
            .setTopUV(0, 14 / 16f, 3 / 16f, 2 / 16f)
            .setNorthUV(13 / 16f, 2 / 16f, 16 / 16f, 14 / 16f)
            .setSouthUV(0 / 16f, 2 / 16f, 3 / 16f, 14 / 16f)
            .setEastUV(2 / 16f, 2 / 16f, 14 / 16f, 14 / 16f)
            .render(tessellator, tileEntity, angle, 1f, 1f, 1f);
    }

    protected static void drawUpgradeLeft(Tessellator tessellator) {
        (new BlockAORenderer(AABB.getTemporaryBB(0.5 / 16f, 4.5 / 16f, 3.5 / 16f, 2 / 16f, 12.5 / 16f, 11.5 / 16f)))
            .setBottomUV(14 / 16f, 14 / 16f, 16 / 16f, 2 / 16f)
            .setTopUV(3 / 16f, 2 / 16f, 0, 14 / 16f)
            .setNorthUV(0, 2 / 16f, 3 / 16f, 14 / 16f)
            .setSouthUV(13 / 16f, 2 / 16f, 16 / 16f, 14 / 16f)
            .setWestUV(2 / 16f, 2 / 16f, 14 / 16f, 14 / 16f)
            .render(tessellator, Side.NORTH);
    }

    protected static void drawUpgradeRight(Tessellator tessellator) {
        (new BlockAORenderer(AABB.getTemporaryBB(14 / 16f, 4.5 / 16f, 3.5 / 16f, 15.5 / 16f, 12.5 / 16f, 11.5 / 16f)))
            .setBottomUV(16 / 16f, 2 / 16f, 14 / 16f, 14 / 16f)
            .setTopUV(0, 14 / 16f, 3 / 16f, 2 / 16f)
            .setNorthUV(13 / 16f, 2 / 16f, 16 / 16f, 14 / 16f)
            .setSouthUV(0 / 16f, 2 / 16f, 3 / 16f, 14 / 16f)
            .setEastUV(2 / 16f, 2 / 16f, 14 / 16f, 14 / 16f)
            .render(tessellator, Side.NORTH);
    }

    @Nonnull
    @Override
    public final int getUpgradeID() {
        return id;
    }

    @Nonnull
    @Override
    public final TurtleUpgradeType getType() {
        return type;
    }

    @Nonnull
    @Override
    public final ItemStack getCraftingItem() {
        return stack;
    }
}
