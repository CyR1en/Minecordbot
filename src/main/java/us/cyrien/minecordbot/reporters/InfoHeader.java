package us.cyrien.minecordbot.reporters;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import org.bukkit.Bukkit;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import us.cyrien.mcutils.diagnosis.Diagnostics;
import us.cyrien.mcutils.diagnosis.IReporter;
import us.cyrien.minecordbot.Minecordbot;

import java.util.List;

public class InfoHeader implements IReporter {

    private int priority;

    public InfoHeader() {
        this.priority = 0;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String report() {
        StringBuilder sb = new StringBuilder();
        JDA jda = Minecordbot.getInstance().getBot().getJda();
        DateTime dt = DateTime.now();
        DateTimeFormatter dTF = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        String serverIP = Bukkit.getServer().getIp();
        String serverVersion = Bukkit.getVersion();
        int serverPort = Bukkit.getPort();
        List<Guild> guilds = jda.getGuilds();
        sb.append(dt.toString(dTF)).append(Diagnostics.DOUBLE_LINE_SEPARATOR);
        sb.append("Server IP: ").append(serverIP).append(Diagnostics.LINE_SEPARATOR);
        sb.append("Port: ").append(serverPort).append(Diagnostics.LINE_SEPARATOR);
        sb.append("Version: ").append(serverVersion).append(Diagnostics.DOUBLE_LINE_SEPARATOR);
        sb.append("Guild(s):").append(Diagnostics.LINE_SEPARATOR);
        guilds.forEach(g -> {
            sb.append("- ").append(g.getName());
            sb.append(":(").append(g.getId()).append(")").append(Diagnostics.LINE_SEPARATOR);
        });
        return sb.toString();
    }

    @Override
    public int getPriority() {
        return priority;
    }
}
