package dan200.computercraft;

import net.minecraft.core.util.helper.Direction;

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
}
