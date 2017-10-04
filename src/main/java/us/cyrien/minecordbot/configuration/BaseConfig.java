package us.cyrien.minecordbot.configuration;

import us.cyrien.mcutils.config.Config;
import us.cyrien.mcutils.config.ConfigManager;
import us.cyrien.mcutils.logger.Logger;

import java.io.File;

public abstract class BaseConfig {

    protected ConfigManager configManager;
    protected Config config;

    public BaseConfig(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public boolean init() {
        File f = configManager.getConfigFile(this.getClass().getSimpleName() + ".yml");
        if(!f.exists()) {
            Logger.warn(this.getClass().getSimpleName() + ".yml" + " have been generated or new fields have been added. Please make sure to fill in all config fields correctly. Server will be stopped for safety.");
            config = configManager.getNewConfig(this.getClass().getSimpleName() + ".yml");
            initialize();
            return false;
        }
        config = configManager.getNewConfig(this.getClass().getSimpleName() + ".yml");
        initialize();
        return true;
    }

    public abstract void initialize();

    public Config getConfig() {
        return config;
    }
}
