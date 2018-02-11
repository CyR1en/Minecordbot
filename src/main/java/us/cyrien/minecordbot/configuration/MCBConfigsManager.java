package us.cyrien.minecordbot.configuration;

import us.cyrien.mcutils.config.ConfigManager;


public class MCBConfigsManager {

    private final String[] BOTCONFIG_HEADER = new String[]{"Bot Configuration", "All bot configuration"};
    private final String[] CHATCONFIG_HEADER = new String[]{"Chat Configuration", "All chat configurations"};
    private final String[] BROADCASTCONFIG_HEADER = new String[]{"Broadcast Configuration", "All broadcast configuration",
            "Some nodes below will also be", "effective to Mod Channels"};
    private final String[] MODCHANNELCONFIG_HEADER = new String[]{"Mod Channel Configuration", "All mod channel configuration"};
    private final String[] PERMISSIONCONFIG_HEADER = new String[]{"Permission Configuration", " ", "can be configured using perm command"};

    private ConfigManager manager;
    private BaseConfig[] configs;

    public MCBConfigsManager(ConfigManager manager) {
        this.manager = manager;
        configs = new BaseConfig[]{new BotConfig(manager, BOTCONFIG_HEADER), new ChatConfig(manager, CHATCONFIG_HEADER),
                new BroadcastConfig(manager, BROADCASTCONFIG_HEADER), new ModChannelConfig(manager, MODCHANNELCONFIG_HEADER),
                new PermissionConfig(manager, PERMISSIONCONFIG_HEADER)};
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

    public BotConfig getBotConfig() {
        return (BotConfig) configs[0];
    }

    public ChatConfig getChatConfig() {
        return (ChatConfig) configs[1];
    }

    public BroadcastConfig getBroadcastConfig() {
        return (BroadcastConfig) configs[2];
    }

    public ModChannelConfig getModChannelConfig() {
        return (ModChannelConfig) configs[3];
    }

    public PermissionConfig getPermConfig() {
        return (PermissionConfig) configs[4];
    }

    public BaseConfig[] getConfigs() {
        return configs;
    }
}
