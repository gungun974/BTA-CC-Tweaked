/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.upgrades;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import dan200.computercraft.shared.common.ComputerCraftItems;
import dan200.computercraft.shared.peripheral.modem.ModemState;
import dan200.computercraft.shared.peripheral.modem.wireless.WirelessModemPeripheral;
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
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import turniplabs.halplibe.helper.EnvironmentHelper;

public class TurtleModem extends AbstractTurtleUpgrade {
    private final boolean advanced;

    @Nullable
    @Environment(EnvType.CLIENT)
    private BlockModelGeneric modelOffLeft;
    @Nullable
    @Environment(EnvType.CLIENT)
    private BlockModelGeneric modelOffRight;
    @Nullable
    @Environment(EnvType.CLIENT)
    private BlockModelGeneric modelOnLeft;
    @Nullable
    @Environment(EnvType.CLIENT)
    private BlockModelGeneric modelOnRight;

    @Environment(EnvType.CLIENT)
    private void ensureModels() {
        if (modelOffLeft == null) {
            String prefix = advanced ? "advanced" : "normal";
            modelOffLeft = new BlockModelGeneric(Blocks.STONE, BlockModelDispatcher.loadDataModel("computercraft:item/turtle_modem_" + prefix + "_off_left").asModel());
            modelOffRight = new BlockModelGeneric(Blocks.STONE, BlockModelDispatcher.loadDataModel("computercraft:item/turtle_modem_" + prefix + "_off_right").asModel());
            modelOnLeft = new BlockModelGeneric(Blocks.STONE, BlockModelDispatcher.loadDataModel("computercraft:item/turtle_modem_" + prefix + "_on_left").asModel());
            modelOnRight = new BlockModelGeneric(Blocks.STONE, BlockModelDispatcher.loadDataModel("computercraft:item/turtle_modem_" + prefix + "_on_right").asModel());
        }
    }

    public TurtleModem(boolean advanced, int id) {
        super(id,
            TurtleUpgradeType.PERIPHERAL,
            advanced ? ComputerCraftItems.WIRELESS_MODEM_ADVANCED : ComputerCraftItems.WIRELESS_MODEM_NORMAL);
        this.advanced = advanced;
    }

    @Override
    public IPeripheral createPeripheral(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side) {
        return new Peripheral(turtle, advanced);
    }

    @NotNull
    @Override
    public TurtleCommandResult useTool(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side, @NotNull TurtleVerb verb, @NotNull Direction dir) {
        return TurtleCommandResult.failure();
    }

    @Override
    public void drawTileUpgrade(Tessellator tessellator, TextureManager textureManager, TileTurtle tileEntity, float angle, @NotNull TurtleSide side, float partialTick) {
        boolean active = false;
        ITurtleAccess turtle = tileEntity.getAccess();
        if (turtle != null) {
            CompoundTag turtleNBT = turtle.getUpgradeNBTData(side);
            active = turtleNBT.containsKey("active") && turtleNBT.getBoolean("active");
        }

        ensureModels();

        byte lightIndex = tileEntity.worldObj.getLightIndex(tileEntity.tilePos, 0);
        float toolAngle = tileEntity.getToolRenderAngle(side, partialTick);

        BlockModelGeneric model = side == TurtleSide.LEFT
            ? (active ? modelOnLeft : modelOffLeft)
            : (active ? modelOnRight : modelOffRight);

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
        BlockModelGeneric model = side == TurtleSide.LEFT ? modelOffLeft : modelOffRight;
        GLRenderer.pushFrame();
        model.renderStandalone(tessellator, 0, lightIndex);
        GLRenderer.popFrame();
    }

    @Override
    public void update(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side) {
        // Advance the modem
        if (!EnvironmentHelper.isClientWorld()) {
            IPeripheral peripheral = turtle.getPeripheral(side);
            if (peripheral instanceof Peripheral) {
                ModemState state = ((Peripheral) peripheral).getModemState();
                if (state.pollChanged()) {
                    turtle.getUpgradeNBTData(side)
                        .putBoolean("active", state.isOpen());
                    turtle.updateUpgradeNBTData(side);
                }
            }
        }
    }

    @Override
    public @NotNull String getUnlocalisedAdjective() {
        if (advanced) {
            return "upgrade.computercraft.wireless_modem_advanced.adjective";
        }
        return "upgrade.computercraft.wireless_modem_normal.adjective";
    }

    private static class Peripheral extends WirelessModemPeripheral {
        private final ITurtleAccess turtle;

        Peripheral(ITurtleAccess turtle, boolean advanced) {
            super(new ModemState(), advanced);
            this.turtle = turtle;
        }

        @NotNull
        @Override
        public World getWorld() {
            return turtle.getWorld();
        }

        @NotNull
        @Override
        public Vector3dc getPosition() {
            TilePosc turtlePos = turtle.getPosition();
            return new Vector3d(turtlePos.x(), turtlePos.y(), turtlePos.z());
        }

        @Override
        public boolean equals(IPeripheral other) {
            return this == other || (other instanceof Peripheral && ((Peripheral) other).turtle == turtle);
        }
    }
}
