package us.cyrien.minecordbot.localization;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import us.cyrien.mcutils.logger.Logger;
import us.cyrien.minecordbot.configuration.MCBConfig;

import java.io.File;


public class Locale {

	private static YamlConfiguration getLocalization() {
	    String loc = MCBConfig.get("localization");
	    File f = new File(Bukkit.getPluginManager().getPlugin("MineCordBot").getDataFolder() + "/localizations/" + loc + ".yml");
	    if(!f.exists())
            f = new File(Bukkit.getPluginManager().getPlugin("MineCordBot").getDataFolder()+ "localizations/en.yml");
        return YamlConfiguration.loadConfiguration(f);
    }

    private static String getTranslatedMessage(String messagePath) {
	    String s = getLocalization().getString(messagePath);
	    if(s == null) {
            Logger.warn("Can not get localization for " + messagePath + ". Returned path");
	        return messagePath;
        }
	    return getLocalization().getString(messagePath);
    }

    public static Formatter getCommandMessage(String path) {
	    return new Formatter(getTranslatedMessage("mcb.command." + path));
    }

    public static Formatter getCommandsMessage(String path) {
	    return new Formatter(getTranslatedMessage("mcb.commands." + path));
    }

    public static Formatter getDeathMessage(String path) {
	    return new Formatter(getTranslatedMessage("mc.death." + path));
    }

    public static Formatter getMcMessage(String path) {
	    return new Formatter(getTranslatedMessage("mc.message." + path));
    }

    public static Formatter getMobMessage(String path) {
	    return new Formatter(getTranslatedMessage("mc.mobs." + path));
    }

    public static class Formatter {
        private String message;

        public Formatter(String message) {
            this.message = message;
        }

        public String format(Object... objects) {
            return String.format(message, objects);
        }

        public String finish() {
            return message;
        }
    }
}