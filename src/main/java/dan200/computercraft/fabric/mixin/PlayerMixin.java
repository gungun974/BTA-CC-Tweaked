package dan200.computercraft.fabric.mixin;

import dan200.computercraft.fabric.IComputerPlayer;
import dan200.computercraft.shared.computer.core.IContainerComputer;
import net.minecraft.core.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = Player.class, remap = false)
public class PlayerMixin implements IComputerPlayer {
    @Nullable
    private IContainerComputer containerComputer;

    @Nullable
    @Override
    public IContainerComputer getCurrentContainerComputer() {
        return this.containerComputer;
    }

    @Override
    public void setCurrentContainerComputer(IContainerComputer container) {
        this.containerComputer = container;
    }
}
