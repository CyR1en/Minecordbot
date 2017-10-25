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
    private List<TextChannel> tcArray;

    public MentionListener(Minecordbot mcb) {
        super(mcb);
        jda = mcb.getBot().getJda();
        tcArray= mcb.getRelayChannels();
    }

    @EventHandler
    public void onTabComplete(TabCompleteEvent e) {
        String[] buffers = e.getBuffer().split(" ");
        List<String> all = new ArrayList<>();
        if (e.getBuffer().endsWith("@")) {
            tcArray.forEach((tc) -> {
                for (Member m : tc.getMembers())
                    all.add("@" + m.getUser().getName().replaceAll("_", "-").replaceAll(" ", "_"));
            });
            e.setCompletions(all);
        } else if (buffers[buffers.length - 1].startsWith("@")) {
            tcArray.forEach((tc) -> {
                for (Member m : tc.getMembers())
                    if(m.getUser().getName().startsWith(buffers[buffers.length - 1].replaceAll("@", "")))
                        all.add("@" + m.getUser().getName().replaceAll(" ", "_"));
            });
            e.setCompletions(all);
        }
    }
}
