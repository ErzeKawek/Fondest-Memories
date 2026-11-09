package net.erzekawek.fondestmemories.config;

import net.erzekawek.fondestmemories.FondestMemories;
import net.erzekawek.fondestmemories.config.Config.ConfigException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;

public class ConfigurationScreen extends OptionsSubScreen {

    private OptionsList widgets;

    private static String REQUIRES_RESTART = "Restart Minecraft to apply changes";

    public ConfigurationScreen(Screen previous) {
        super(previous, Minecraft.getInstance().options, Component.nullToEmpty(FondestMemories.MOD_NAME));
    }


    @Override
    protected void addOptions() {
        if (this.list != null) {
            this.list.addBig(
                    OptionInstance.createBoolean(
                            "Enable Post Effects",
                            OptionInstance.cachedConstantTooltip(Component.nullToEmpty("Whether to enable Post-Effects this mod Has")),
                            FondestMemories.CONFIG.enablePostEffects,
                            (value) -> {
                                FondestMemories.CONFIG.enablePostEffects = value;
                            }
                    )
            );

            this.list.addBig(
                    OptionInstance.createBoolean(
                            "Disable F3 In Dimensions",
                            OptionInstance.cachedConstantTooltip(Component.nullToEmpty("Some of the dimensions in this mod Disable F3, toggle off this to be able to use F3.")),
                            FondestMemories.CONFIG.disableF3,
                            (value) -> {
                                FondestMemories.CONFIG.disableF3 = value;
                            }
                    )
            );
        }
    }

    @Override
    public void removed() {
        try {
            FondestMemories.CONFIG.writeConfigToFile();
        } catch (ConfigException e) {
            FondestMemories.LOGGER.error(String.valueOf(e));
        }
    }
}
