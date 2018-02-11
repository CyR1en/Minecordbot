package us.cyrien.minecordbot.configuration;

import us.cyrien.mcutils.config.ConfigManager;

public class ChatConfig extends BaseConfig {

    public ChatConfig(ConfigManager configManager, String[] header) {
        super(configManager, header);
    }

    @Override
    public void initialize() {
        for (Nodes node : Nodes.values())
            initNode(node);
    }

    public enum Nodes implements Node {
        RELAY_CHANNELS("Relay_Channels", new String[]{"MCBChannel ID's of text channels you", "want to bind Minecraft chat with"},
                new String[]{"12345", "12345"}),
        MESSAGE_FORMAT("Message_Format", new String[]{"Formatting codes for messages coming from Discord"}, "&7"),
        DISCORD_PREFIX("Discord_Prefix", new String[]{"Prefix of messages that came from Discord.", "This can be formatted."}, "&6&l[Discord] &r{role} {ename}"),
        MINECRAFT_PREFIX("Minecraft_Prefix", new String[]{"Prefix of messages that came from Minecraft."}, "[Minecraft] {erank} {ename}"),
        BLOCKED_PREFIX("Blocked_Prefix", new String[]{"If a message from Discord is a command with any of the prefix",
                "below. That message won't be relayed to Minecraft.",
                "You can block your own bot's prefix", "by placing {this} place holder."}, new String[]{"~", "~~", "{this}"}),
        BLOCKED_BOTS("Blocked_Bots", new String[]{"If a message from Discord came from any of the Bots below.",
                "That message won't be relayed to Minecraft."}, new String[]{"123123123", "123123123"});


        private String key;
        private String[] comment;
        private Object defaultValue;

        Nodes(String key, String[] comment, Object defaultValue) {
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
