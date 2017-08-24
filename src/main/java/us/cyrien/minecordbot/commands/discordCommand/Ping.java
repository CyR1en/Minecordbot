package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.jagrosh.jdautilities.waiter.EventWaiter;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.DiscordCommand;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.localization.Localization;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class Ping extends DiscordCommand {

    private EventWaiter waiter;

    public Ping(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "ping";
        this.aliases = new String[]{"pong, p"};
        this.help = Localization.getTranslatedMessage("mcb.commands.ping.description");
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_READ, Permission.MESSAGE_MANAGE};
        this.category = minecordbot.MISC;
        this.waiter = minecordbot.getEventWaiter();
    }

    @Override
    protected void doCommand(CommandEvent e) {
        e.getTextChannel().sendMessage("Ping...").queue(msg -> {
            msg.editMessage("Ping: `" + e.getMessage().getCreationTime()
                    .until(msg.getCreationTime(), ChronoUnit.MILLIS) + " ms`").queue((m) -> {
                waiter.waitForEvent(MessageReceivedEvent.class, (c) -> {
                    return msg.getId().equals(c.getMessageId());
                }, (a) -> {
                    boolean b = MCBConfig.get("auto_delete_command_response");
                    if(b) {
                        msg.delete().queue();
                    }
                }, 5, TimeUnit.MINUTES, () -> System.out.println("could not auto delete command reply"));
            });
        });
    }
}
