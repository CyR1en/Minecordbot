package us.cyrien.minecordbot.event;

import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.commons.lang.StringUtils;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.Minecordbot;

import java.util.Objects;

public class BotReadyEvent extends ListenerAdapter {

    private Minecordbot mcb;

    public BotReadyEvent(Minecordbot mcb) {
        this.mcb = mcb;
    }

    @Override
    public void onReady(ReadyEvent event) {
        Game game = Game.of("Type " + MCBConfig.get("trigger") + "help");
        if(!Objects.equals(MCBConfig.get("default_game"), MCBConfig.getDefault().get("default_game")) && !StringUtils.isBlank(MCBConfig.get("default_game"))) {
            String sGame = MCBConfig.get("default_game");
            if(sGame != null)
                game = Game.of(sGame);
        }
        event.getJDA().getPresence().setGame(game);
    }
}
