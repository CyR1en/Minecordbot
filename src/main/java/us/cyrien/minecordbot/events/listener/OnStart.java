package us.cyrien.minecordbot.events.listener;

import net.dv8tion.jda.core.EmbedBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import us.cyrien.minecordbot.Bot;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.events.StartEvent;
import us.cyrien.minecordbot.localization.Locale;

public class OnStart implements Listener {

    private Minecordbot mcb;

    public OnStart(Minecordbot mcb) {
        this.mcb = mcb;
    }

    @EventHandler
    public void onStart(StartEvent event) {
        if (!event.isCancelled()) {
            EmbedBuilder eb = new EmbedBuilder().setDescription(Locale.getEventMessage("start").finish()).setColor(Bot.BOT_COLOR);
            mcb.getMessenger().sendMessageEmbedToAllBoundChannel(eb.build());
            mcb.getMessenger().sendMessageEmbedToAllModChannel(eb.build());
        }
    }
}
