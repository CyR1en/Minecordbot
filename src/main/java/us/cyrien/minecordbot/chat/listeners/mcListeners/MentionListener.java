package us.cyrien.minecordbot.chat.listeners.mcListeners;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.TabCompleteEvent;
import us.cyrien.minecordbot.Minecordbot;

import java.util.ArrayList;
import java.util.List;

public class MentionListener extends MCBListener {

    private JDA jda;
    private List<String> tcArray;

    public MentionListener(Minecordbot mcb) {
        super(mcb);
        jda = mcb.getBot().getJda();
        tcArray= (List<String>) configsManager.getChatConfig().getList("Relay_Channels");
    }

    @EventHandler
    public void onTabComplete(TabCompleteEvent e) {
        String[] buffers = e.getBuffer().split(" ");
        List<String> all = new ArrayList<>();
        if (e.getBuffer().endsWith("@")) {
            for (Object s : tcArray) {
                TextChannel tc = jda.getTextChannelById(s.toString());
                if (tc != null)
                    for (Member m : tc.getMembers())
                        all.add("@" + m.getUser().getName().replaceAll(" ", "_"));
            }
            e.setCompletions(all);
        } else if (buffers[buffers.length - 1].startsWith("@")) {
            for (Object s : tcArray) {
                TextChannel tc = jda.getTextChannelById(s.toString());
                if (tc != null)
                    for (Member m : tc.getMembers())
                        if(m.getUser().getName().startsWith(buffers[buffers.length - 1].replaceAll("@", "")))
                            all.add("@" + m.getUser().getName().replaceAll(" ", "_"));
            }
            e.setCompletions(all);
        }
    }
}
