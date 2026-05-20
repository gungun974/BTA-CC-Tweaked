/*
 * This file is part of the public ComputerCraft API - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. This API may be redistributed unmodified and in full only.
 * For help using the API, and posting your mods, visit the forums at computercraft.info.
 */
package dan200.computercraft.api.turtle;

import dan200.computercraft.shared.turtle.blocks.TileTurtle;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;

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
        drawUpgradeLeft(tessellator);
    }

    protected static void drawUpgradeRight(Tessellator tessellator, TileTurtle tileEntity, float angle) {
        drawUpgradeRight(tessellator);
    }

    protected static void drawUpgradeLeft(Tessellator tessellator) {
        TessellatorGeneral tess = (TessellatorGeneral) tessellator;
        float x = -0.46875f;
        float yMin = -0.21875f, yMax = 0.28125f;
        float zMin = -0.28125f, zMax = 0.21875f;
        float uMin = 0.125f, uMax = 0.875f;
        float vMin = 0.125f, vMax = 0.875f;
        tess.setNormal(-1f, 0f, 0f);
        tess.addVertexWithUV(x, yMax, zMax, uMin, vMin);
        tess.addVertexWithUV(x, yMin, zMax, uMin, vMax);
        tess.addVertexWithUV(x, yMin, zMin, uMax, vMax);
        tess.addVertexWithUV(x, yMax, zMin, uMax, vMin);
    }

    protected static void drawUpgradeRight(Tessellator tessellator) {
        TessellatorGeneral tess = (TessellatorGeneral) tessellator;
        float x = 0.46875f;
        float yMin = -0.21875f, yMax = 0.28125f;
        float zMin = -0.28125f, zMax = 0.21875f;
        float uMin = 0.125f, uMax = 0.875f;
        float vMin = 0.125f, vMax = 0.875f;
        tess.setNormal(1f, 0f, 0f);
        tess.addVertexWithUV(x, yMax, zMin, uMin, vMin);
        tess.addVertexWithUV(x, yMin, zMin, uMin, vMax);
        tess.addVertexWithUV(x, yMin, zMax, uMax, vMax);
        tess.addVertexWithUV(x, yMax, zMax, uMax, vMin);
    }

    @NotNull
    @Override
    public final int getUpgradeID() {
        return id;
    }

    @NotNull
    @Override
    public final TurtleUpgradeType getType() {
        return type;
    }

    @NotNull
    @Override
    public final ItemStack getCraftingItem() {
        return stack;
    }
}
