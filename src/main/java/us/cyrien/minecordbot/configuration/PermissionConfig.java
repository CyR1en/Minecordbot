package us.cyrien.minecordbot.configuration;

import us.cyrien.mcutils.config.ConfigManager;

public class PermissionConfig extends BaseConfig {

    public PermissionConfig(ConfigManager configManager, String[] header) {
        super(configManager, header);
    }

    @Override
    public void initialize() {
        if (config.get("RoleID.RoleName") == null) {
            config.set("RoleID.RoleName", "sample");
            config.saveConfig();
        }
        if (config.get("RoleID.Permission") == null) {
            config.set("RoleID.Permission", "{-category} {+command}");
            config.saveConfig();
        }
    }
}
