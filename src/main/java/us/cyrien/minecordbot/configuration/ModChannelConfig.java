package us.cyrien.minecordbot.configuration;

import us.cyrien.mcutils.config.ConfigManager;

public class ModChannelConfig extends BaseConfig {

    public ModChannelConfig(ConfigManager configManager, String[] header) {
        super(configManager, header);
    }

    @Override
    public void initialize() {
        for (Nodes node : Nodes.values())
            initNode(node);
    }

    public enum Nodes implements Node {
        MOD_CHANNEL("Mod_Channels", new String[]{"ID of the text channel you want to make as a mod channel",
                "Leave it blank if you don't want a mod channel"}, new String[]{"123123123", "123123123"}),
        ONE_WAY("One_Way", new String[]{"Do you want to prevent messages that",
                "are being sent on the mod channel",
                "to be relayed to Minecraft?"}, false),
        SEE_CHAT("See_Chat", new String[]{"Do you want to see in-game chat on a mod channel?"}, true),
        SEE_PLAYER_JOIN("See_Player_Join", new String[]{"Will player join event broadcast be relayed to Mod MCBChannel?"}, true),
        SEE_PLAYER_QUIT("See_Player_Quit", new String[]{"Will player quit event broadcast be relayed to Mod MCBChannel?"}, true),
        SEE_PLAYER_DEATH("See_Player_Death", new String[]{"Will player death event broadcast be relayed to Mod MCBChannel?"}, true),
        SEE_COMMADS("See_Commands", new String[]{"Do you want to see commands that", "are being executed on a mod channel?"}, true),
        SEE_CANCELLED_CHAT("See_Cancelled_Chats", new String[]{"Do you want to see messages that",
                "have been cancelled by other plugin?",
                "Cancelled messages are considered", "a private message"}, true),
        SEE_GP_SOFTMUTE("See_GriefPrevention_SoftMute", new String[]{"Do you want to see player chat that",
                "have been GriefPrevention soft muted."}, true),
        SEE_MMO_ADMIN_CHAT("See_mcMMO_Admin_Chat", new String[]{"Do you want to see mcMMO Admin chat?"}, true),
        SEE_MMO_PARTY_CHAT("See_mcMMO_Party_Chat", new String[]{"Do you want to see mcMMO Party chat?"}, true),
        SEE_SV("See_SV", new String[]{"Do you want to see fake join/leave messages",
                "from SuperVanish on a mod channel?",
                "Will be marked \"(Vanish)\""}, true),
        SEE_BROADCAST("See_Broadcast", new String[]{"Do you want to see broadcasts on a mod channel?"}, true),
        SEE_VC_CHANNEL_CHAT("See_VentureChat_Channel_Chats", new String[]{"Do you want to see VentureChat channel",
                "chats on Minecordbot Discord mod channel?"}, true),
        SEE_VC_QUICK_CHAT("See_VentureChat_Quick_Chats", new String[]{"Do you want to see VentureChat quick",
                "chats on Minecordbot Discord mod channel?"}, true),
        SEE_VC_PRIVATE_MENTION("See_VentureChat_Private_Mention", new String[]{"Do you want to see VentureChat private",
                "mention on Minecordbot Discord mod channel?",
                "These are messages that starts with @ + name"}, true),
        SEE_VC_CHANNEL_PARTY_CHAT("See_VentureChat_Channel_Party_Chat", new String[]{"Do you want to see VentureChat party",
                "chats on Minecordbot Discord mod channel?"}, true),
        SEE_VC_CHANNEL_PRIVATE_CHATS("See_VentureChat_Channel_Private_Chat",new String[]{"Do you want to see VentureChat private",
                "conversation on Minecordbot Discord mod channel?"}, true);

        private String key;
        private String[] comments;
        private Object defaultValue;

        Nodes(String key, String[] comments, Object defaultValue) {
            this.key = key;
            this.comments = comments;
            this.defaultValue = defaultValue;
        }

        @Override
        public Object getDefaultValue() {
            return defaultValue;
        }

        @Override
        public String[] getComment() {
            return comments;
        }

        @Override
        public String key() {
            return key;
        }

        @Override
        public String toString() {
            return key();
        }
    }
}
