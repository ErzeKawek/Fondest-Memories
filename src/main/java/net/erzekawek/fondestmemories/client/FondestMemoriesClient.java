package net.erzekawek.fondestmemories.client;

import net.erzekawek.fondestmemories.client.entityrendering.NullRenderer;
import net.erzekawek.fondestmemories.client.skyrendering.SkyPanelRenderer;
import net.erzekawek.fondestmemories.client.skyrendering.SkyWindowRenderer;
import net.erzekawek.fondestmemories.client.skyrendering.SkyWindowRendering;
import net.erzekawek.fondestmemories.misc.InterspaceUtil;
import net.erzekawek.fondestmemories.registry.FondestMemoriesBlockEntityRegistry;
import net.erzekawek.fondestmemories.registry.FondestMemoriesDimensionRegistry;
import net.erzekawek.fondestmemories.registry.FondestMemoriesMobs;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class FondestMemoriesClient implements ClientModInitializer {

    private static ResourceKey<Level> lastDimension = null;

    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.register(FondestMemoriesMobs.NULL_TYPE, NullRenderer::new);

        BlockEntityRenderers.register(FondestMemoriesBlockEntityRegistry.SKY_WINDOW, context -> new SkyWindowRenderer());
        BlockEntityRenderers.register(FondestMemoriesBlockEntityRegistry.SKY_PANEL_NOON, context -> new SkyPanelRenderer());

        ClientTickEvents.END_CLIENT_TICK.register(client -> SkyWindowRendering.ensureRegistered());

        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            if (mc.level == null) {
                lastDimension = null;
                return;
            }

            ResourceKey<Level> cur = mc.level.dimension();
            if (cur != lastDimension) {
                lastDimension = cur;
                if (InterspaceUtil.FMLVL.contains(cur)) {
                    InterspaceUtil.showDimensionToast(mc, cur);
                }
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.level == null) {
                lastDimension = null;
                return;
            }

            ResourceKey<Level> current = client.level.dimension();
            if (current.equals(lastDimension)) return;
            lastDimension = current;

            if (current.equals(FondestMemoriesDimensionRegistry.CARDINAL_LEVEL)) {
                client.options.renderDistance().set(8);
                client.options.gamma().set(0.0);
                client.options.vignette().set(false);
            }
        });
    }
}
