package us.cyrien.minecordbot.configuration;

import us.cyrien.mcutils.config.Config;
import us.cyrien.mcutils.config.ConfigManager;

public class MCBConfigsManager {

    private final String[] BOTCONFIG_HEADER = new String[]{"Bot Configuration", "All bot configuration"};
    private final String[] CHATCONFIG_HEADER = new String[]{"Chat Configuration", "All chat configurations"};
    private final String[] BROADCASTCONFIG_HEADER = new String[]{"Broadcast Configuration", "All broadcast configuration",
            "Some nodes below will also be", "effective to Mod Channels"};
    private final String[] MODCHANNELCONFIG_HEADER = new String[]{"Mod Channel Configuration", "All mod channel configuration"};

    private ConfigManager manager;
    private BaseConfig[] configs;

    public MCBConfigsManager(ConfigManager manager) {
        this.manager = manager;
        configs = new BaseConfig[]{new BotConfig(manager, BOTCONFIG_HEADER), new ChatConfig(manager, CHATCONFIG_HEADER),
                new BroadcastConfig(manager, BROADCASTCONFIG_HEADER), new ModChannelConfig(manager, MODCHANNELCONFIG_HEADER)};
    }

    public boolean setupConfigurations() {
        boolean shutdown = true;
        for (BaseConfig bCfg : configs) {
            if (!bCfg.init())
                shutdown = false;
        }
        return shutdown;
    }

    public void reloadAllConfig() {
        for (BaseConfig bCfg : configs) {
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

    public Config getBroadcastConfig() {
        return configs[2].getConfig();
    }

    public Config getModChannelConfig() {
        return configs[3].getConfig();
    }


}
