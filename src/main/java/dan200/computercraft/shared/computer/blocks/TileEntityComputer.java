package dan200.computercraft.shared.computer.blocks;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.core.computer.ComputerSide;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.ComputerState;
import dan200.computercraft.shared.computer.core.ServerComputer;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;

public class TileEntityComputer extends TileComputerBase {
    private ComputerProxy proxy;

    public TileEntityComputer() {
        super(ComputerFamily.NORMAL);
    }

    public TileEntityComputer(ComputerFamily family) {
        super(family);
    }

    public boolean canBeCarried(World world, Entity potentialHolder) {
        return false;
    }


    public boolean isUsableByPlayer(Player player) {
        return isUsable(player, false);
    }

    @Override
    protected void updateBlockState(ComputerState newState) {
        final int currentMetadata = getBlockMeta();

        final ComputerState currentState = ComputerState.class.getEnumConstants()[(currentMetadata >> 3) & 0b11];

        if (currentState != newState) {
            final int newMetadata = (currentMetadata & ~0b11000) | (newState.ordinal() << 3);

            if (worldObj != null) {
                worldObj.setBlockDataNotify(tilePos, newMetadata);
            }
        }
    }

    @Override
    public Direction getDirection() {
        return BlockLogicComputer.getDirectionFromMeta(getBlockMeta());
    }

    @Override
    protected ComputerSide remapLocalSide(ComputerSide localSide) {
        // For legacy reasons, computers invert the meaning of "left" and "right". A computer's front is facing
        // towards you, but a turtle's front is facing the other way.
        if (localSide == ComputerSide.RIGHT) {
            return ComputerSide.LEFT;
        }
        if (localSide == ComputerSide.LEFT) {
            return ComputerSide.RIGHT;
        }
        return localSide;
    }

    @Override
    protected ServerComputer createComputer(int instanceID, int id) {
        ComputerFamily family = getFamily();
        ServerComputer computer = new ServerComputer(worldObj,
            id, label,
            instanceID,
            family,
            ComputerCraft.computerTermWidth,
            ComputerCraft.computerTermHeight);
        computer.setPosition(tilePos);
        return computer;
    }

    @Override
    public ComputerProxy createProxy() {
        if (proxy == null) {
            proxy = new ComputerProxy(() -> this) {
                @Override
                protected TileComputerBase getTile() {
                    return TileEntityComputer.this;
                }
            };
        }
        return proxy;
    }
}
