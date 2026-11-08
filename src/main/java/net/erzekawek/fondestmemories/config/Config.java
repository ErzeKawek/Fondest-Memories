package net.erzekawek.fondestmemories.config;

import net.erzekawek.fondestmemories.FondestMemories;
import net.fabricmc.loader.api.FabricLoader;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Config {
    private static final Path config_path = FabricLoader.getInstance().getConfigDir()
            .resolve(FondestMemories.MOD_ID + ".yaml").toAbsolutePath();

    /**
     * Enable FM Post-effects
     */
    protected boolean enablePostEffects;

    public boolean enablePostEffects() {
        return this.enablePostEffects;
    }

    /**
     * Whether to disable F3
     */
    protected boolean disableF3;

    public boolean disableF3() {
        return this.disableF3;
    }

    /**
     * Default values
     */
    private static final boolean DEFAULT_ENABLE_POST_EFFECTS = true;
    private static final boolean DEFAULT_DISABLE_F3 = true;

    public static Config loadConfiguration() {
        Config config;
        try {
            config = fromConfigFile();
            FondestMemories.LOGGER.debug("Loaded " + FondestMemories.MOD_NAME + " configuration.");
        } catch (FileNotFoundException e) {
            FondestMemories.LOGGER.warn("No " + FondestMemories.MOD_NAME + " configuration file found.");
            try {
                writeDefaultConfigFile();
            } catch (ConfigException writee) {
                FondestMemories.LOGGER.error(
                        "Could not write " + FondestMemories.MOD_NAME + " configuration:\n" + writee);
            }
            config = getDefaultConfiguration();
        } catch (ConfigException e) {
            FondestMemories.LOGGER.error(FondestMemories.MOD_NAME + " configuration is invalid:\n" + e);
            config = getDefaultConfiguration();
        }
        return config;
    }

    private static final DumperOptions configFormat = new DumperOptions() {{
        this.setIndent(2);
        this.setPrettyFlow(true);
        this.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    }};

    /**
     * Write the configuration to file
     *
     * @throws ConfigException If the writer encounters any sort of IO error (permissions?)
     */
    protected void writeConfigToFile() throws ConfigException {
        FileWriter configWriter;
        try {
            configWriter = new FileWriter(config_path.toFile());
        } catch (IOException e) {
            throw new ConfigException(
                    "Could not open " + config_path + " for writing.", e
            );
        }
        Map<String, Object> writeme = new LinkedHashMap<>();
        writeme.put("enable_post_effects", this.enablePostEffects);
        writeme.put("disable_f3", this.disableF3);

        (new Yaml(configFormat)).dump(writeme, configWriter);
        FondestMemories.LOGGER.info(
                "Wrote " + FondestMemories.MOD_NAME + " configuration file to " + config_path);
    }

    private static Config getDefaultConfiguration() {
        FondestMemories.LOGGER.info("Loading default " + FondestMemories.MOD_NAME + " configuration");
        Config config = new Config();
        config.enablePostEffects = DEFAULT_ENABLE_POST_EFFECTS;
        config.disableF3 = DEFAULT_DISABLE_F3;
        return config;
    }

    private static void writeDefaultConfigFile() throws ConfigException {
        getDefaultConfiguration().writeConfigToFile();
    }

    private static Config fromConfigFile() throws FileNotFoundException, ConfigException {

        FondestMemories.LOGGER.debug(
                "Reading " + FondestMemories.MOD_NAME + " configuration from " + config_path
        );
        FileInputStream configReader = new FileInputStream(config_path.toFile());
        HashMap<String, Object> settings = new HashMap<>((new Yaml()).load(configReader));

        try {
            Config config = new Config();

            // Now we actually construct the thing
            config.enablePostEffects = Boolean.parseBoolean(
                    settings.getOrDefault(
                            "enable_post_effects",
                            DEFAULT_ENABLE_POST_EFFECTS
                    ).toString()
            );

            config.disableF3 = Boolean.parseBoolean(
                    settings.getOrDefault(
                            "disable_f3",
                            DEFAULT_DISABLE_F3
                    ).toString()
            );

            return config;

        } catch (Exception e) {
            throw new ConfigException(
                    config_path + " is not a valid " + FondestMemories.MOD_NAME + " configuration.",
                    e
            );
        }
    }

    protected static class ConfigException extends Exception {

        ConfigException(String message, Exception e) {
            super(message, e);
        }

        ConfigException(String message) {
            super(message);
        }
    }
}
