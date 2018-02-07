package us.cyrien.minecordbot.configuration;

import us.cyrien.mcutils.config.ConfigManager;

public class BroadcastConfig extends BaseConfig {

    public BroadcastConfig(ConfigManager configManager, String[] header) {
        super(configManager, header);
    }

    @Override
    public void initialize() {
        for (Node node : Node.values()) {
            initNode(node);
        }
    }

    private void initNode(Node node) {
        String[] comment = node.getComment();
        if (config.get(node.key()) == null) {
            config.set(node.key(), node.getDefaultValue(), comment);
            config.saveConfig();
        }
    }

    public enum Node {
        PLUGIN_BROADCAST("See_Plugin_Broadcast", new String[]{"Will broadcast from other plugins be relayed to Discord?"}, true),
        PLAYER_JOIN("See_Player_Join", new String[]{"Will player join event broadcast be relayed to Discord?"}, true),
        PLAYER_QUIT("See_Player_Quit", new String[]{"Will player quit event broadcast be relayed to Discord?"}, true),
        PLAYER_DEATH("See_Player_Death", new String[]{"Will player death event broadcast be relayed to Discord?"}, true),
        CLEARLAG("See_ClearLag", new String[]{"Will ClearLag broadcasts be relayed to Discord?"}, true),
        SERVER_START("See_Server_Start", new String[]{
                "Do you want a broadcast",
                "when server starts?"}, true),
        SERVER_SHUT("See_Server_Shut", new String[]{
                "Do you want a broadcast",
                "when server starts?"}, true),
        HIDE_INCOGNITO("Hide_Incognito_Player", new String[]{"Do players with minecordbot.incognito",
                "permission be hidden from",
                "Join/Quit broadcast that are being relayed to Discord."}, false);

        private String key;
        private String[] comment;
        private Object defaultValue;

        Node(String key, String[] comment, Object defaultValue) {
            this.key = key;
            this.comment = comment;
            this.defaultValue = defaultValue;
        }

        public Object getDefaultValue() {
            return defaultValue;
        }

        public String[] getComment() {
            return comment;
        }

        public String key() {
            return toString();
        }

        @Override
        public String toString() {
            return key;
        }
    }
}
