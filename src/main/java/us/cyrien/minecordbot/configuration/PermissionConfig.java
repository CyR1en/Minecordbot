package us.cyrien.minecordbot.configuration;

import us.cyrien.mcutils.config.ConfigManager;

public class PermissionConfig extends BaseConfig {

    public PermissionConfig(ConfigManager configManager, String[] header) {
        super(configManager, header);
    }

    @Override
    public void initialize() {
        String[] commentArr;
        if (config.get("Admin-Role") == null) {
            commentArr = new String[]{"ID of the role that's",
                    "allowed to use Minecordbot Admin commands", "Leave default if you no role."};
            config.set("Admin-Role", new String[]{"123123123", "123123123"}, commentArr);
            config.saveConfig();
        }
        if (config.get("Info-Role") == null) {
            commentArr = new String[]{"ID of the role that's",
                    "allowed to use Minecordbot Info commands", "Leave default if you no role."};
            config.set("Admin-Role", new String[]{"123123123", "123123123"}, commentArr);
            config.saveConfig();
        }
        if (config.get("Misc-Role") == null) {
            commentArr = new String[]{"ID of the role that's",
                    "allowed to use Minecordbot Misc commands", "Leave default if you no role."};
            config.set("Admin-Role", new String[]{"123123123", "123123123"}, commentArr);
            config.saveConfig();
        }
        if (config.get("Fun-Role") == null) {
            commentArr = new String[]{"ID of the role that's",
                    "allowed to use Minecordbot Fun commands", "Leave default if you no role."};
            config.set("Admin-Role", new String[]{"123123123", "123123123"}, commentArr);
            config.saveConfig();
        }
        if (config.get("Help-Role") == null) {
            commentArr = new String[]{"ID of the role that's",
                    "allowed to use Minecordbot Help commands", "Leave default if you no role."};
            config.set("Admin-Role", new String[]{"123123123", "123123123"}, commentArr);
            config.saveConfig();
        }

    }
}
