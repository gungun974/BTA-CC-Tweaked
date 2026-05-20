/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle;

import com.google.common.eventbus.Subscribe;
import dan200.computercraft.api.turtle.event.TurtleBlockEvent;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntitySign;

import java.util.HashMap;
import java.util.Map;

public class SignInspectHandler {
    @Subscribe
    public void onTurtleInspect(TurtleBlockEvent.Inspect event) {
        TileEntity be = event.getWorld().getTileEntity(event.getPos());
        if (be instanceof TileEntitySign sbe) {
            Map<Integer, String> textTable = new HashMap<>();
            for (int k = 0; k < 4; k++) {
                textTable.put(k + 1, sbe.signText[k]);
            }
            event.getData().put("text", textTable);
        }
    }
}
