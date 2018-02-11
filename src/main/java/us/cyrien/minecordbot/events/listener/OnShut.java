package us.cyrien.minecordbot.events.listener;

import net.dv8tion.jda.core.EmbedBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import us.cyrien.minecordbot.Bot;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.events.ShutdownEvent;
import us.cyrien.minecordbot.localization.Locale;

public class OnShut implements Listener {

    private Minecordbot mcb;

    public OnShut(Minecordbot mcb) {
        this.mcb = mcb;
    }

    @EventHandler
    public void onShut(ShutdownEvent event) {
        if(!event.isCancelled()) {
            EmbedBuilder eb = new EmbedBuilder().setDescription(Locale.getEventMessage("shut").finish()).setColor(Bot.BOT_COLOR);
            mcb.getMessenger().sendMessageEmbedToAllBoundChannel(eb.build());
        }
    }
}
