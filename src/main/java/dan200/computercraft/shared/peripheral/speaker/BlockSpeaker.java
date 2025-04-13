/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.speaker;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicRotatable;
import net.minecraft.core.block.material.Material;

public class BlockSpeaker extends BlockLogicRotatable {
    public BlockSpeaker(Block<?> block) {
        super(block, Material.stone);
        block.withEntity(TileSpeaker::new);
    }
}
