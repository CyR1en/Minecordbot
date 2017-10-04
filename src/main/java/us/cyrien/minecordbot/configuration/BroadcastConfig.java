package us.cyrien.minecordbot.configuration;

import us.cyrien.mcutils.config.ConfigManager;

public class BroadcastConfig extends BaseConfig {

    public BroadcastConfig(ConfigManager configManager) {
        super(configManager);
    }

    @Override
    public void initialize() {
        String comment;
        String[] commentArr;
        if (config.get("See_Plugin_Broadcast") == null) {
            comment = "Will broadcast from other plugins be relayed to Discord?";
            config.set("See_Plugin_Broadcast", true, comment);
            config.saveConfig();
        }
        if (config.get("See_Player_Join") == null) {
            comment = "Will player join event broadcast be relayed to Discord??";
            config.set("See_Player_Join", true, comment);
            config.saveConfig();
        }
        if (config.get("See_Player_Quit") == null) {
            comment = "Will player quit event broadcast be relayed to Discord?";
            config.set("See_Player_Quit", true, comment);
            config.saveConfig();
        }
        if (config.get("See_Player_Death") == null) {
            comment = "Will player death event broadcast be relayed to Discord?";
            config.set("See_Player_Death", true, comment);
            config.saveConfig();
        }
        if (config.get("Hide_Incognito_Player") == null) {
            commentArr = new String[]{"Do players with minecordbot.incognito",
                    "permission be hidden from",
                    "Join/Quit broadcast that are being relayed to Discord."};
            config.set("Hide_Incognito_Player", true, commentArr);
            config.saveConfig();
        }
    }
}
