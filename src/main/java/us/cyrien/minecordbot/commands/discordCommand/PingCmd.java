package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.jagrosh.jdautilities.waiter.EventWaiter;
import us.cyrien.minecordbot.Bot;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.localization.Locale;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class PingCmd extends MCBCommand {

    private EventWaiter waiter;

    public PingCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "ping";
        this.aliases = new String[]{"pong, p"};
        this.help = Locale.getCommandsMessage("ping.description").finish();
        this.category = Bot.MISC;
        waiter = minecordbot.getEventWaiter();
    }

    @Override
    protected void doCommand(CommandEvent e) {
        e.getTextChannel().sendMessage("ping...").queue(msg -> msg.editMessage("ping: `" + e.getMessage().getCreationTime()
                .until(msg.getCreationTime(), ChronoUnit.MILLIS) + " ms`").queue((m) -> {
            scheduler.schedule(() -> {
                if (auto)
                    m.delete().queue();
            }, RESPONSE_DURATION, TimeUnit.MINUTES);
        }));
    }
}
