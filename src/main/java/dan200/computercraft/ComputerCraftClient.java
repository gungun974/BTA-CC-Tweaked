package dan200.computercraft;

import dan200.computercraft.shared.common.ComputerCraftModels;
import net.fabricmc.api.ClientModInitializer;
import turniplabs.halplibe.event.defs.ClientEvents;
import turniplabs.halplibe.util.dependency.Key;

import static dan200.computercraft.ComputerCraft.*;

public class ComputerCraftClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ComputerCraft.log.info("Binding to client events...");

        ClientEvents.BLOCK_MODEL_RELOAD.listen(Key.of(MOD_ID), (t) -> new ComputerCraftModels().initBlockModels(t));
        ClientEvents.ITEM_MODEL_RELOAD.listen(Key.of(MOD_ID), (t) -> new ComputerCraftModels().initItemModels(t));
        ClientEvents.TILE_ENTITY_RENDERER_RELOAD.listen(Key.of(MOD_ID), (t) -> new ComputerCraftModels().initTileEntityModels(t));
        ClientEvents.ENTITY_RENDERER_RELOAD.listen(Key.of(MOD_ID), (t) -> new ComputerCraftModels().initEntityModels(t));
    }
}
