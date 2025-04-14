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
import dan200.computercraft.shared.util.BlockPos;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class TurtleSpeaker extends AbstractTurtleUpgrade {
    public TurtleSpeaker(int id) {
        super(id, TurtleUpgradeType.PERIPHERAL, ComputerCraftItems.SPEAKER);
    }

    @Override
    public IPeripheral createPeripheral(@Nonnull ITurtleAccess turtle, @Nonnull TurtleSide side) {
        return new TurtleSpeaker.Peripheral(turtle);
    }

    @Override
    public void drawTileUpgrade(Tessellator tessellator, TextureManager textureManager, TileTurtle tileEntity, float angle, @NotNull TurtleSide side, float partialTick) {
        textureManager.loadTexture("/assets/computercraft/textures/block/turtle_speaker_face.png").bind();
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
        textureManager.loadTexture("/assets/computercraft/textures/block/turtle_speaker_face.png").bind();
        tessellator.startDrawingQuads();
        if (side == TurtleSide.LEFT) {
            drawUpgradeLeft(tessellator);
        } else {
            drawUpgradeRight(tessellator);
        }
        tessellator.draw();
    }

    @Override
    public void update(@Nonnull ITurtleAccess turtle, @Nonnull TurtleSide turtleSide) {
        IPeripheral turtlePeripheral = turtle.getPeripheral(turtleSide);
        if (turtlePeripheral instanceof Peripheral) {
            Peripheral peripheral = (Peripheral) turtlePeripheral;
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
        public Vec3 getPosition() {
            BlockPos pos = turtle.getPosition();
            return Vec3.getPermanentVec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        }

        @Override
        public boolean equals(IPeripheral other) {
            return this == other || (other instanceof Peripheral && turtle == ((Peripheral) other).turtle);
        }
    }
}
