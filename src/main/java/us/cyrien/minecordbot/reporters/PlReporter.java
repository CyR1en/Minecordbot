package us.cyrien.minecordbot.reporters;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import us.cyrien.mcutils.diagnosis.Diagnostics;
import us.cyrien.mcutils.diagnosis.IReporter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class PlReporter implements IReporter {

    private String name;
    private int priority;

    public PlReporter() {
        this.name = "Plugin Reporter";
        this.priority = 4;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String report() {
        StringBuilder sb = new StringBuilder();
        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
        sb.append("- Loaded Plugins(").append(plugins.length).append(") -").append(Diagnostics.LINE_SEPARATOR);
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            sb.append(plugin.getName()).append(":").append(plugin.getDescription().getVersion()).append(Diagnostics.LINE_SEPARATOR);
        }
        sb.append(Diagnostics.LINE_SEPARATOR);
        File pluginsFolder = new File("plugins");
        File[] files = pluginsFolder.listFiles() == null ? new File[]{} : pluginsFolder.listFiles();
        ArrayList<File> fList = new ArrayList<>(Arrays.asList(files));
        for (int i = 0; i < fList.size(); i++)
            if (fList.get(i).getPath().endsWith(".jar")) {
                fList.remove(i);
                i--;
            }
        sb.append("- Jars in plugins folder(").append(fList.size()).append(") -").append(Diagnostics.LINE_SEPARATOR);
        fList.forEach(f -> sb.append(f.getName()).append(Diagnostics.LINE_SEPARATOR));
        return sb.toString();
    }

    @Override
    public int getPriority() {
        return priority;
    }

}
