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
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.generic.BlockModelGeneric;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TurtleCraftingTable extends AbstractTurtleUpgrade {
    @Nullable
    @Environment(EnvType.CLIENT)
    private BlockModelGeneric modelLeft;
    @Nullable
    @Environment(EnvType.CLIENT)
    private BlockModelGeneric modelRight;

    @Environment(EnvType.CLIENT)
    private void ensureModels() {
        if (modelLeft == null) {
            modelLeft = new BlockModelGeneric(Blocks.STONE, BlockModelDispatcher.loadDataModel("computercraft:item/turtle_crafting_table_left").asModel());
            modelRight = new BlockModelGeneric(Blocks.STONE, BlockModelDispatcher.loadDataModel("computercraft:item/turtle_crafting_table_right").asModel());
        }
    }

    public TurtleCraftingTable(int id) {
        super(id, TurtleUpgradeType.PERIPHERAL, Blocks.WORKBENCH);
    }

    @Override
    public IPeripheral createPeripheral(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side) {
        return new CraftingTablePeripheral(turtle);
    }

    @Override
    public void drawTileUpgrade(Tessellator tessellator, TextureManager textureManager, TileTurtle tileEntity, float angle, @NotNull TurtleSide side, float partialTick) {
        ensureModels();

        byte lightIndex = tileEntity.worldObj.getLightIndex(tileEntity.tilePos, 0);
        float toolAngle = tileEntity.getToolRenderAngle(side, partialTick);

        BlockModelGeneric model = side == TurtleSide.LEFT ? modelLeft : modelRight;

        TextureRegistry.worldAtlas.bind();
        GLRenderer.pushFrame();
        GLRenderer.modelM4f().rotateX((float) Math.toRadians(-toolAngle));
        model.renderStandalone((TessellatorGeneral) tessellator, 0, lightIndex);
        GLRenderer.popFrame();
    }

    @Override
    public void drawItemUpgrade(TessellatorGeneral tessellator, byte lightIndex, @NotNull TurtleSide side) {
        ensureModels();
        TextureRegistry.worldAtlas.bind();
        BlockModelGeneric model = side == TurtleSide.LEFT ? modelLeft : modelRight;
        GLRenderer.pushFrame();
        model.renderStandalone(tessellator, 0, lightIndex);
        GLRenderer.popFrame();
    }

    @Override
    public @NotNull String getUnlocalisedAdjective() {
        return "upgrade.minecraft.crafting_table.adjective";
    }
}
