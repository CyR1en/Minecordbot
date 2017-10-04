package us.cyrien.minecordbot.configuration;

import us.cyrien.mcutils.config.ConfigManager;

public class ModChannelConfig extends BaseConfig {
    
    public ModChannelConfig(ConfigManager configManager) {
        super(configManager);
    }

    @Override
    public void initialize() {
        String comment;
        String[] commentArr;
        if (config.get("Mod_TextChannel") == null) {
            commentArr = new String[]{"ID of the text channel you want to make as a mod channel",
                    "Leave it blank if you don't want a mod channel"};
            config.set("Mod_TextChannel", "123123123123123", commentArr);
            config.saveConfig();
        }
        if (config.get("One_Way") == null) {
            commentArr = new String[]{"Do you want to prevent messages that",
                    "are being sent on the mod channel",
                    "to be relayed to Minecraft?"};
            config.set("One_Way", false, commentArr);
            config.saveConfig();
        }
        if (config.get("See_Chat") == null) {
            comment = "Do you want to see in-game chat on a mod channel?";
            config.set("See_Chat", true, comment);
            config.saveConfig();
        }
        if (config.get("See_Player_Join") == null) {
            comment = "Will player join event broadcast be relayed to Mod Channel?";
            config.set("See_Player_Join", true, comment);
            config.saveConfig();
        }
        if (config.get("See_Player_Quit") == null) {
            comment = "Will player quit event broadcast be relayed to Mod Channel?";
            config.set("See_Player_Quit", true, comment);
            config.saveConfig();
        }
        if (config.get("See_Player_Death") == null) {
            comment = "Will player death event broadcast be relayed to Mod Channel?";
            config.set("See_Player_Death", true, comment);
            config.saveConfig();
        }
        if (config.get("See_Commands") == null) {
            commentArr = new String[]{"Do you want to see commands that", "are being executed on a mod channel?"};
            config.set("See_Commands", true, commentArr);
            config.saveConfig();
        }
        if (config.get("See_SV") == null) {
            commentArr = new String[]{"Do you want to see fake join/leave messages",
                    "from SuperVanish on a mod channel?",
                    "Will be marked \"(Vanish)\""};
            config.set("See_SV", true, commentArr);
            config.saveConfig();
        }
        if (config.get("See_Broadcast") == null) {
            comment = "Do you want to see broadcasts on a mod channel?";
            config.set("See_Broadcast", true, comment);
            config.saveConfig();
        }
    }
}
