package us.cyrien.minecordbot.event;

import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.main.Minecordbot;

public class BotReadyEvent extends ListenerAdapter {

    private Minecordbot mcb;

    public BotReadyEvent(Minecordbot mcb) {
        this.mcb = mcb;
    }

    @Override
    public void onReady(ReadyEvent event) {
        event.getJDA().getPresence().setGame(Game.of("Type " + MCBConfig.get("trigger") + "help"));
    }
}
