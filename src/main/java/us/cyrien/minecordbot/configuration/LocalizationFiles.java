package us.cyrien.minecordbot.configuration;

import org.apache.commons.collections4.map.HashedMap;
import org.bukkit.configuration.file.YamlConfiguration;
import us.cyrien.minecordbot.main.Minecordbot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

public class LocalizationFiles {

    private HashedMap<String, File> languages;
    private Minecordbot mcb;

    public LocalizationFiles(Minecordbot p, boolean copy) {
        mcb = p;
        languages = new HashedMap<>();
        languages.put("en", new File(p.getDataFolder().toString() + "/localizations/en.yml"));
        languages.put("en_s7", new File(p.getDataFolder().toString() + "/localizations/en_s7.yml"));
        languages.put("es", new File(p.getDataFolder().toString() + "/localizations/es.yml"));
        languages.put("pl", new File(p.getDataFolder().toString() + "/localizations/pl.yml"));
        languages.put("de", new File(p.getDataFolder().toString() + "/localizations/de.yml"));
        languages.put("ro", new File(p.getDataFolder().toString() + "/localizations/ro.yml"));
        languages.put("fr", new File(p.getDataFolder().toString() + "/localizations/fr.yml"));
        for (String key : languages.keySet()) {
            if (copy) {
                saveResource(p, "localizations/" + key + ".yml", true);
            } else {
                try {
                    languages.get(key).createNewFile();
                } catch (IOException ex) {
                }
            }
        }
    }

    public void saveResource(Minecordbot mcb, String resourcePath, boolean replace) {
        if (resourcePath != null && !resourcePath.equals("")) {
            resourcePath = resourcePath.replace('\\', '/');
            InputStream in = mcb.getResource(resourcePath);
            if (in == null) {
                throw new IllegalArgumentException("The embedded resource \'" + resourcePath + "\' cannot be found");
            } else {
                File outFile = new File(mcb.getDataFolder(), resourcePath);
                int lastIndex = resourcePath.lastIndexOf(47);
                File outDir = new File(mcb.getDataFolder(), resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));
                if (!outDir.exists()) {
                    outDir.mkdirs();
                }
                try {
                    if (outFile.exists() && !replace) {
                        mcb.getLogger().log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
                    } else {
                        FileOutputStream ex = new FileOutputStream(outFile);
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            ex.write(buf, 0, len);
                        }
                        ex.close();
                        in.close();
                    }
                } catch (IOException var10) {
                    mcb.getLogger().log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, var10);
                }

            }
        } else {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }
    }

    public YamlConfiguration getLocalization(String lang) {
        File lang1 = new File(mcb.getDataFolder() + "/localizations/" + lang + ".yml");
        return YamlConfiguration.loadConfiguration(lang1);
    }

    public HashedMap<String, File> getLanguages() {
        return languages;
    }

}

