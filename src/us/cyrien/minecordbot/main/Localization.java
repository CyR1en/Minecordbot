package us.cyrien.minecordbot.main;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import us.cyrien.minecordbot.configuration.MCBConfig;

import java.io.File;


public class Localization {

	private static YamlConfiguration getLocalization() {
	    String loc = MCBConfig.get("localization");
	    File f = new File(Bukkit.getPluginManager().getPlugin("MineCordBot").getDataFolder() + "/localizations/" + loc + ".yml");
	    if(!f.exists())
            f = new File(Bukkit.getPluginManager().getPlugin("MineCordBot").getDataFolder()+ "localizations/en.yml");
        return YamlConfiguration.loadConfiguration(f);
    }

    public static String getTranslatedMessage(String messagePath) {
	    String s = getLocalization().getString(messagePath);
	    if(s == null) {
            Minecordbot.LOGGER.warn("Can not get localization for " + messagePath + ". Returned path");
	        return messagePath;
        }
	    return getLocalization().getString(messagePath);
    }
}