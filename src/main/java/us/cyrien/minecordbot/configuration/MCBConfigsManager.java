package us.cyrien.minecordbot.configuration;

import us.cyrien.mcutils.config.Config;
import us.cyrien.mcutils.config.ConfigManager;

public class MCBConfigsManager {

    private ConfigManager manager;
    private BaseConfig[] configs;

    public MCBConfigsManager(ConfigManager manager) {
        this.manager = manager;
        configs = new BaseConfig[] {new BotConfig(manager), new ChatConfig(manager), new BroadcastConfig(manager), new ModChannelConfig(manager)};
    }

    public boolean setupConfigurations() {
        boolean shutdown = true;
        for(BaseConfig bCfg : configs) {
            if(!bCfg.init())
                shutdown = false;
        }
        return shutdown;
    }

    public void reloadAllConfig() {
        for(BaseConfig bCfg : configs) {
            bCfg.getConfig().reloadConfig();
        }
    }

    public ConfigManager getManager() {
        return manager;
    }

    public Config getBotConfig() {
        return configs[0].getConfig();
    }

    public Config getChatConfig() {
        return configs[1].getConfig();
    }

    public Config getModChannelConfig() {
        return configs[2].getConfig();
    }

    public Config getBroadcastConfig() {
        return configs[3].getConfig();
    }
}
