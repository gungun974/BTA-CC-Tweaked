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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public BlockPos offset(Direction dir) {
        return offset(dir, 1);
    }

    public BlockPos offset(Direction dir, int mul) {
        return new BlockPos(x + dir.getOffsetX() * mul, y + dir.getOffsetY() * mul, z + dir.getOffsetZ() * mul);
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
