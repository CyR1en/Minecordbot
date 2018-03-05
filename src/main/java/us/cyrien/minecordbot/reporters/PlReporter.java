package us.cyrien.minecordbot.reporters;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import us.cyrien.mcutils.diagnosis.Diagnostics;
import us.cyrien.mcutils.diagnosis.IReporter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.ZipFile;

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
        for (int i = 0; i < fList.size(); i++) {
            try {
                if (!isJarFile(fList.get(i))) {
                    fList.remove(i);
                    i--;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sb.append("- Jars in plugins folder(").append(fList.size()).append(") -").append(Diagnostics.LINE_SEPARATOR);
        fList.forEach(f -> sb.append(f.getName()).append(Diagnostics.LINE_SEPARATOR));
        return sb.toString();
    }

    private boolean isJarFile(File file) throws IOException {
        if (!isZipFile(file)) {
            return false;
        }
        ZipFile zip = new ZipFile(file);
        boolean manifest = zip.getEntry("META-INF/MANIFEST.MF") != null;
        zip.close();
        return manifest;
    }

    private boolean isZipFile(File file) throws IOException {
        if(file.isDirectory()) {
            return false;
        }
        if(!file.canRead()) {
            throw new IOException("Cannot read file "+file.getAbsolutePath());
        }
        if(file.length() < 4) {
            return false;
        }
        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
        int test = in.readInt();
        in.close();
        return test == 0x504b0304;
    }

    @Override
    public int getPriority() {
        return priority;
    }

}
