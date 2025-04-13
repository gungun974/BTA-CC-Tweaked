/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.upgrades;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.AbstractTurtleUpgrade;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;
import dan200.computercraft.shared.turtle.blocks.TileTurtle;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Blocks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class TurtleCraftingTable extends AbstractTurtleUpgrade {
    public TurtleCraftingTable(int id) {
        super(id, TurtleUpgradeType.PERIPHERAL, Blocks.WORKBENCH);
    }

    @Override
    public IPeripheral createPeripheral(@Nonnull ITurtleAccess turtle, @Nonnull TurtleSide side) {
        return new CraftingTablePeripheral(turtle);
    }

    @Override
    public void drawTileUpgrade(Tessellator tessellator, TextureManager textureManager, TileTurtle tileEntity, float angle, @NotNull TurtleSide side, float partialTick) {
        textureManager.loadTexture("/assets/computercraft/textures/block/turtle_crafty_face.png").bind();
        tessellator.startDrawingQuads();
        if (side == TurtleSide.LEFT) {
            drawUpgradeLeft(tessellator, tileEntity, angle);
        } else {
            drawUpgradeRight(tessellator, tileEntity, angle);
        }
        tessellator.draw();
    }

    @Override
    public void drawItemUpgrade(Tessellator tessellator, TextureManager textureManager, @NotNull TurtleSide side) {
        textureManager.loadTexture("/assets/computercraft/textures/block/turtle_crafty_face.png").bind();
        tessellator.startDrawingQuads();
        if (side == TurtleSide.LEFT) {
            drawUpgradeLeft(tessellator);
        } else {
            drawUpgradeRight(tessellator);
        }
        tessellator.draw();
    }

    @Override
    public @NotNull String getUnlocalisedAdjective() {
        return "upgrade.minecraft.crafting_table.adjective";
    }
}
