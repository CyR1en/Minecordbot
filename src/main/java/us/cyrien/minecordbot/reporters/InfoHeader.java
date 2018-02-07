package us.cyrien.minecordbot.reporters;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import org.bukkit.Bukkit;
import org.joda.time.DateTime;
import us.cyrien.mcutils.diagnosis.IReporter;
import us.cyrien.minecordbot.Minecordbot;

import java.util.List;

public class infoHeader implements IReporter {

    private final int PRIORITY = 0;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String report() {
        StringBuilder sb = new StringBuilder();
        JDA jda =  Minecordbot.getInstance().getBot().getJda();
        DateTime dt = DateTime.now();
        String serverIP = Bukkit.getServer().getIp();
        int serverPort = Bukkit.getPort();
        List<Guild> guilds = jda.getGuilds();
        return null;
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }
}
