package us.cyrien.minecordbot.configuration;

import us.cyrien.mcutils.config.ConfigManager;

public class ModChannelConfig extends BaseConfig {
    
    public ModChannelConfig(ConfigManager configManager, String[] header) {
        super(configManager, header);
    }

    @Override
    public void initialize() {
        String comment;
        String[] commentArr;
        if (config.get("Mod_Channels") == null) {
            commentArr = new String[]{"ID of the text channel you want to make as a mod channel",
                    "Leave it blank if you don't want a mod channel"};
            config.set("Mod_Channels", new String[]{"123123123", "123123123"}, commentArr);
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
            comment = "Will player join event broadcast be relayed to Mod MCBChannel?";
            config.set("See_Player_Join", true, comment);
            config.saveConfig();
        }
        if (config.get("See_Player_Quit") == null) {
            comment = "Will player quit event broadcast be relayed to Mod MCBChannel?";
            config.set("See_Player_Quit", true, comment);
            config.saveConfig();
        }
        if (config.get("See_Player_Death") == null) {
            comment = "Will player death event broadcast be relayed to Mod MCBChannel?";
            config.set("See_Player_Death", true, comment);
            config.saveConfig();
        }
        if (config.get("See_Commands") == null) {
            commentArr = new String[]{"Do you want to see commands that", "are being executed on a mod channel?"};
            config.set("See_Commands", true, commentArr);
            config.saveConfig();
        }
        if (config.get("See_Cancelled_Chats") == null) {
            commentArr = new String[]{"Do you want to see messages that",
                    "have been cancelled by other plugin?",
                    "Cancelled messages are considered", "a private message"};
            config.set("See_Cancelled_Chats", true, commentArr);
            config.saveConfig();
        }
        if (config.get("See_GriefPrevention_SoftMute") == null) {
            commentArr = new String[]{"Do you want to see player chat that",
                    "have been GriefPrevention soft muted."};
            config.set("See_GriefPrevention_SoftMute", true, commentArr);
            config.saveConfig();
        }
        if(config.get("See_mcMMO_Admin_Chat") == null) {
            comment = "Do you want to see mcMMO Admin chat?";
            config.set("See_mcMMO_Admin_Chat", true, comment);
            config.saveConfig();
        }
        if(config.get("See_mcMMO_Party_Chat") == null) {
            comment = "Do you want to see mcMMO Party chat?";
            config.set("See_mcMMO_Party_Chat", true, comment);
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
        if(config.get("See_VentureChat_Channel_Chats") == null) {
            commentArr = new String[]{"Do you want to see VentureChat channel",
                    "chats on Minecordbot Discord mod channel?"};
            config.set("See_VentureChat_Channel_Chats", true, commentArr);
            config.saveConfig();
        }
        if(config.get("See_VentureChat_Quick_Chats") == null) {
            commentArr = new String[]{"Do you want to see VentureChat quick",
                    "chats on Minecordbot Discord mod channel?"};
            config.set("See_VentureChat_Quick_Chats", true, commentArr);
            config.saveConfig();
        }
        if(config.get("See_VentureChat_Private_Mention") == null) {
            commentArr = new String[]{"Do you want to see VentureChat private",
                    "mention on Minecordbot Discord mod channel?",
                    "These are messages that starts with @ + name"};
            config.set("See_VentureChat_Private_Mention", true, commentArr);
            config.saveConfig();
        }
        if(config.get("See_VentureChat_Channel_Party_Chat") == null) {
            commentArr = new String[]{"Do you want to see VentureChat party",
                    "chats on Minecordbot Discord mod channel?"};
            config.set("See_VentureChat_Channel_Party_Chat", true, commentArr);
            config.saveConfig();
        }
        if(config.get("See_VentureChat_Channel_Private_Chat") == null) {
            commentArr = new String[]{"Do you want to see VentureChat private",
                    "conversation on Minecordbot Discord mod channel?"};
            config.set("See_VentureChat_Channel_Private_Chat", true, commentArr);
            config.saveConfig();
        }
    }
}
