package us.cyrien.minecordbot.configuration;

import us.cyrien.mcutils.config.ConfigManager;

public class BotConfig extends BaseConfig {

    public BotConfig(ConfigManager configManager, String[] header) {
        super(configManager, header);
    }

    @Override
    public void initialize() {
        String comment;
        String[] commentArr;
        if (config.get("Bot_Token") == null) {
            commentArr = new String[]{"Bot token can be found on your", "Discord application page."};
            config.set("Bot_Token", "sample.token", commentArr);
            config.saveConfig();
        }
        if (config.get("Bot_ID") == null) {
            commentArr = new String[]{"Bot ID, this is also known as Client ID", "on your Discord application page."};
            config.set("Bot_ID", "123123123123", commentArr);
            config.saveConfig();
        }
        if (config.get("Owner_ID") == null) {
            commentArr = new String[]{"Owner ID of the owner of the bot, server, or guild.",
                    "You can easily get your id by right", "clicking your self and copy id.",
                    "This only works when developer mode is enabled.",
                    "Go to settings>appearance and enable developer mode"};
            config.set("Owner_ID", "12312312312313", commentArr);
            config.saveConfig();
        }
        if (config.get("Command_Trigger") == null) {
            comment = "Set of character that distinguish if it's a command.";
            config.set("Command_Trigger", "!!", comment);
            config.saveConfig();
        }
        if (config.get("Delete_Response") == null) {
            comment = "Should bot's command responses be deleted after 5 minutes?";
            config.set("Delete_Response", false, comment);
            config.saveConfig();
        }
        if (config.get("Localization") == null) {
            commentArr = new String[]{"What language should the bot respond with"};
            config.set("Localization", "en", commentArr);
            config.saveConfig();
        }
        if (config.get("Default_Game") == null) {
            commentArr = new String[]{"This will be the game of the bot", "and will be shown under the name",
                    "of the bot as \"Playing <Default_Game>\""};
            config.set("Default_Game", "default", commentArr);
            config.saveConfig();
        }
    }
}
