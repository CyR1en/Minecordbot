package us.cyrien.minecordbot.configuration;

import us.cyrien.mcutils.config.ConfigManager;

public class BotConfig extends BaseConfig {

    public BotConfig(ConfigManager configManager, String[] header) {
        super(configManager, header);
    }

    @Override
    public void initialize() {
        for (Nodes node : Nodes.values())
            initNode(node);
    }

    public enum Nodes implements Node {
        BOT_TOKEN("Bot_Token", new String[]{"Bot token can be found on your", "Discord application page."}, "sample.token"),
        BOT_ID("Bot_ID", new String[]{"Bot ID, this is also known as Client ID", "on your Discord application page."}, "12345"),
        OWNER_ID("Owner_ID", new String[]{"Owner ID of the owner of the bot, server, or guild.",
                "You can easily get your id by right", "clicking your self and copy id.",
                "This only works when developer mode is enabled.",
                "Go to settings>appearance and enable developer mode"}, "12345"),
        COMMAND_TRIGGER("Command_Trigger", new String[]{"Set of character that distinguish if it's a command."}, "!!"),
        DELETE_RESPONSE("Delete_Response", new String[]{"Should bot's command responses be deleted after 5 minutes?"}, false),
        LOCALICAZATION("Localization", new String[]{"What language should the bot respond with"}, "en"),
        DEFAULT_GAME("Default_Game", new String[]{"This will be the game of the bot", "and will be shown under the name",
                "of the bot as \"Playing <Default_Game>\""}, "default");

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
