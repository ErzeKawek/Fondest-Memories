package net.erzekawek.fondestmemories.client;

import net.erzekawek.fondestmemories.FondestMemories;
import net.erzekawek.fondestmemories.registry.FondestMemoriesBlockEntityRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class FondestMemoriesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRenderers.register(FondestMemoriesBlockEntityRegistry.SKY_WINDOW, context -> new SkyWindowRenderer());
    }
}
