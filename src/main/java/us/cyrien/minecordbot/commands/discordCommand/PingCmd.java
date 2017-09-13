package us.cyrien.minecordbot.commands.discordCommand;

import us.cyrien.jdautilities.commandclient.CommandEvent;
import us.cyrien.jdautilities.waiter.EventWaiter;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import us.cyrien.mcutils.logger.Logger;
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
        this.category = minecordbot.MISC;
        this.waiter = minecordbot.getEventWaiter();
    }

    @Override
    protected void doCommand(CommandEvent e) {
        e.getTextChannel().sendMessage("ping...").queue(msg -> msg.editMessage("ping: `" + e.getMessage().getCreationTime()
                .until(msg.getCreationTime(), ChronoUnit.MILLIS) + " ms`").queue((m) -> {
            waiter.waitForEvent(MessageReceivedEvent.class, (c) -> msg.getId().equals(c.getMessageId()), (a) -> {
                if(auto) {
                    msg.delete().queue();
                }
            }, 5, TimeUnit.MINUTES, () -> Logger.warn("could not auto delete command reply"));
        }));
    }
}
