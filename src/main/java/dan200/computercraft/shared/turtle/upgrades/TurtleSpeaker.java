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
import dan200.computercraft.shared.common.ComputerCraftItems;
import dan200.computercraft.shared.peripheral.speaker.SpeakerPeripheral;
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
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class TurtleSpeaker extends AbstractTurtleUpgrade {
    @Nullable
    @Environment(EnvType.CLIENT)
    private BlockModelGeneric modelLeft;
    @Nullable
    @Environment(EnvType.CLIENT)
    private BlockModelGeneric modelRight;

    @Environment(EnvType.CLIENT)
    private void ensureModels() {
        if (modelLeft == null) {
            modelLeft = new BlockModelGeneric(Blocks.STONE, BlockModelDispatcher.loadDataModel("computercraft:item/turtle_speaker_upgrade_left").asModel());
            modelRight = new BlockModelGeneric(Blocks.STONE, BlockModelDispatcher.loadDataModel("computercraft:item/turtle_speaker_upgrade_right").asModel());
        }
    }

    public TurtleSpeaker(int id) {
        super(id, TurtleUpgradeType.PERIPHERAL, ComputerCraftItems.SPEAKER);
    }

    @Override
    public IPeripheral createPeripheral(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side) {
        return new TurtleSpeaker.Peripheral(turtle);
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
    public void update(@NotNull ITurtleAccess turtle, @NotNull TurtleSide turtleSide) {
        IPeripheral turtlePeripheral = turtle.getPeripheral(turtleSide);
        if (turtlePeripheral instanceof Peripheral peripheral) {
            peripheral.update();
        }
    }

    @Override
    public @NotNull String getUnlocalisedAdjective() {
        return "upgrade.computercraft.speaker.adjective";
    }

    private static class Peripheral extends SpeakerPeripheral {
        ITurtleAccess turtle;

        Peripheral(ITurtleAccess turtle) {
            this.turtle = turtle;
        }

        @Override
        public World getWorld() {
            return turtle.getWorld();
        }

        @Override
        public Vector3dc getPosition() {
            TilePosc pos = turtle.getPosition();
            return new Vector3d(pos.x() + 0.5, pos.y() + 0.5, pos.z() + 0.5);
        }

        @Override
        public boolean equals(IPeripheral other) {
            return this == other || (other instanceof Peripheral && turtle == ((Peripheral) other).turtle);
        }
    }
}
