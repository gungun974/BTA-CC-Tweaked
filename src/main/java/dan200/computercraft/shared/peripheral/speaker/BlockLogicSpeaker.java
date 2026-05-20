/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.speaker;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicRotatable;
import net.minecraft.core.block.material.Materials;

public class BlockLogicSpeaker extends BlockLogicRotatable {
    public BlockLogicSpeaker(Block<?> block) {
        super(block, Materials.STONE);
        block.withEntity(TileSpeaker::new);
    }
}
