package us.cyrien.minecordbot.reporters;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import us.cyrien.mcutils.diagnosis.Diagnostics;
import us.cyrien.mcutils.diagnosis.IReporter;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.configuration.BotConfig;

import java.io.File;

public class CfgReporter implements IReporter {

    private String name;
    private int priority;

    public CfgReporter() {
        this.name = "Configuration Reporter";
        this.priority = 5;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String report() {
        StringBuilder sb = new StringBuilder();
        File dataFolder = Minecordbot.getInstance().getDataFolder();
        File[] files = dataFolder.listFiles() == null ? new File[]{} : dataFolder.listFiles();
        for (File f : files)
            if (!f.isDirectory() && f.getName().contains("Config") && f.getPath().endsWith(".yml")) {
                FileConfiguration config = YamlConfiguration.loadConfiguration(f);
                if(config.getKeys(false).contains(BotConfig.Nodes.BOT_TOKEN.key()))
                    config.set(BotConfig.Nodes.BOT_TOKEN.key(), "-- Token omitted for security --");
                String s = config.saveToString().replaceAll("\\R", Diagnostics.LINE_SEPARATOR);
                sb.append("-").append(f.getName()).append("-").append(Diagnostics.LINE_SEPARATOR);
                sb.append(s).append(Diagnostics.LINE_SEPARATOR);
            }
        return sb.toString();
    }
}
