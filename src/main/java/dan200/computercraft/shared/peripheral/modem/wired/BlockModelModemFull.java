package dan200.computercraft.shared.peripheral.modem.wired;

import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;

public class BlockModelModemFull<T extends BlockLogic> extends BlockModelStandard<T> {
    public BlockModelModemFull(Block<T> block) {
        super(block);
    }

    public IconCoordinate getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        int metaId = blockAccess.getBlockMetadata(x, y, z);

        boolean isModemOn = (metaId & 0b1) == 1;
        boolean isPeripheralOn = (metaId & 0b10) == 2;

        IconCoordinate originalFront = this.blockTextures.get(Side.NORTH);

        if (isModemOn) {
            if (isPeripheralOn) {
                return TextureRegistry.getTexture(originalFront.namespaceId.namespace() + ":block/wired_modem_face_peripheral_on");
            } else {
                return TextureRegistry.getTexture(originalFront.namespaceId.namespace() + ":block/wired_modem_face_on");
            }
        }


        if (isPeripheralOn) {
            return TextureRegistry.getTexture(originalFront.namespaceId.namespace() + ":block/wired_modem_face_peripheral");
        }

        return originalFront;
    }
}
