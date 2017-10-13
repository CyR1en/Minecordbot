package us.cyrien.minecordbot.configuration;

import us.cyrien.mcutils.config.ConfigManager;

public class ChatConfig extends BaseConfig {

    public ChatConfig(ConfigManager configManager, String[] header) {
        super(configManager, header);
    }

    @Override
    public void initialize() {
        String comment;
        String[] commentArr;
        if (config.get("Relay_Channels") == null) {
            commentArr = new String[]{"MCBChannel ID's of text channels you", "want to bind Minecraft chat with"};
            config.set("Relay_Channels", new String[]{"123123123", "123123123"}, commentArr);
            config.saveConfig();
        }
        if (config.get("Message_Format") == null) {
            comment = "Formatting codes for messages coming from Discord";
            config.set("Message_Format", "&7", comment);
            config.saveConfig();
        }
        if (config.get("Discord_Prefix") == null) {
            commentArr = new String[]{"Prefix of messages that came from Discord.", "This can be formatted."};
            config.set("Discord_Prefix", "&6&l[Discord] &r{role} {ename}", commentArr);
            config.saveConfig();
        }
        if (config.get("Minecraft_Prefix") == null) {
            commentArr = new String[]{"Prefix of messages that came from Minecraft."};
            config.set("Minecraft_Prefix", "[Minecraft] {erank} {ename}", commentArr);
            config.saveConfig();
        }
        if (config.get("Blocked_Prefix") == null) {
            commentArr = new String[]{"If a message from Discord is a command with any of the prefix",
                    "below. That message won't be relayed to Minecraft.",
                    "You can block your own bot's prefix", "by placing {this} place holder."};
            config.set("Blocked_Prefix", new String[]{"~", "~~", "{this}"}, commentArr);
            config.saveConfig();
        }
        if (config.get("Blocked_Bots") == null) {
            commentArr = new String[]{"If a message from Discord came from any of the Bots below.",
                    "That message won't be relayed to Minecraft."};
            config.set("Blocked_Bots", new String[]{"123123123", "123123123"}, commentArr);
            config.saveConfig();
        }
    }

    /*
    public enum ChatNodes {
        RELAY_CHANNELS("Relay_Channels"),
        MESSAGE_FORMAT("")
        private String node;
        Nodes(String node) {
            this.node = node;
        }

        @Override
        public String toString() {
            return node;
        }
    }
        */
}
