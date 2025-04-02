/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.upgrades;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.BlockPos;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import dan200.computercraft.fabric.Helper;
import dan200.computercraft.shared.common.ComputerCraftItems;
import dan200.computercraft.shared.peripheral.modem.ModemState;
import dan200.computercraft.shared.peripheral.modem.wireless.WirelessModemPeripheral;
import dan200.computercraft.shared.turtle.blocks.TileTurtle;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class TurtleModem extends AbstractTurtleUpgrade
{
    private final boolean advanced;

    public TurtleModem( boolean advanced, int id )
    {
        super( id,
            TurtleUpgradeType.PERIPHERAL,
            advanced ? ComputerCraftItems.WIRELESS_MODEM_ADVANCED : ComputerCraftItems.WIRELESS_MODEM_NORMAL );
        this.advanced = advanced;
    }

    @Override
    public IPeripheral createPeripheral( @Nonnull ITurtleAccess turtle, @Nonnull TurtleSide side )
    {
        return new Peripheral( turtle, advanced );
    }

    @Nonnull
    @Override
    public TurtleCommandResult useTool( @Nonnull ITurtleAccess turtle, @Nonnull TurtleSide side, @Nonnull TurtleVerb verb, @Nonnull Direction dir )
    {
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

        if (advanced) {
            if (active) {
                textureManager.loadTexture("/assets/computercraft/textures/block/wireless_modem_advanced_face_on.png").bind();
            } else {
                textureManager.loadTexture("/assets/computercraft/textures/block/wireless_modem_advanced_face.png").bind();
            }
        } else {
            if (active) {
                textureManager.loadTexture("/assets/computercraft/textures/block/wireless_modem_normal_face_on.png").bind();
            } else {
                textureManager.loadTexture("/assets/computercraft/textures/block/wireless_modem_normal_face.png").bind();
            }
        }

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
        if (!advanced) {
            textureManager.loadTexture("/assets/computercraft/textures/block/wireless_modem_normal_face.png").bind();
        } else {
            textureManager.loadTexture("/assets/computercraft/textures/block/wireless_modem_advanced_face.png").bind();
        }

        tessellator.startDrawingQuads();
        if (side == TurtleSide.LEFT) {
            drawUpgradeLeft(tessellator);
        } else {
            drawUpgradeRight(tessellator);
        }
        tessellator.draw();
    }

    @Override
    public void update( @Nonnull ITurtleAccess turtle, @Nonnull TurtleSide side )
    {
        // Advance the modem
        if( !Helper.isClientWorld())
        {
            IPeripheral peripheral = turtle.getPeripheral( side );
            if( peripheral instanceof Peripheral )
            {
                ModemState state = ((Peripheral) peripheral).getModemState();
                if( state.pollChanged() )
                {
                    turtle.getUpgradeNBTData( side )
                        .putBoolean( "active", state.isOpen() );
                    turtle.updateUpgradeNBTData( side );
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

    private static class Peripheral extends WirelessModemPeripheral
    {
        private final ITurtleAccess turtle;

        Peripheral( ITurtleAccess turtle, boolean advanced )
        {
            super( new ModemState(), advanced );
            this.turtle = turtle;
        }

        @Nonnull
        @Override
        public World getWorld()
        {
            return turtle.getWorld();
        }

        @Nonnull
        @Override
        public Vec3 getPosition()
        {
            BlockPos turtlePos = turtle.getPosition();
            return Vec3.getPermanentVec3(turtlePos.getX(), turtlePos.getY(), turtlePos.getZ());
        }

        @Override
        public boolean equals( IPeripheral other )
        {
            return this == other || (other instanceof Peripheral && ((Peripheral) other).turtle == turtle);
        }
    }
}
