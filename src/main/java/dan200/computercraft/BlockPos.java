package dan200.computercraft;

import net.minecraft.core.util.helper.Direction;

import java.util.Objects;

public class BlockPos {
    public int x;
    public int y;
    public int z;

    public BlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public BlockPos offset(Direction dir) {
        return new BlockPos(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ());
    }

    public BlockPos down() {
        return new BlockPos(x, y - 1, z);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BlockPos blockPos = (BlockPos) o;
        return x == blockPos.x && y == blockPos.y && z == blockPos.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
