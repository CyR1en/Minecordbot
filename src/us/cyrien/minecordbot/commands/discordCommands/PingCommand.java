package us.cyrien.minecordbot.commands.discordCommands;


import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import us.cyrien.minecordbot.core.annotation.DCmd;
import us.cyrien.minecordbot.core.annotation.DCommand;
import us.cyrien.minecordbot.core.annotation.DMessageReceive;
import us.cyrien.minecordbot.core.enums.CommandType;
import us.cyrien.minecordbot.core.module.DiscordCommand;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class PingCommand {
    @DCommand(aliases = {"ping", "pang", "pong"}, usage = "mcb.commands.ping.usage", desc = "mcb.commands.ping.description", type = CommandType.INFO)
    public void command(@DMessageReceive MessageReceivedEvent mre, @DCmd DiscordCommand c) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        sendLatency(mre, msg -> scheduler.schedule(() -> msg.delete().queue(), 20, TimeUnit.SECONDS), c);
    }

    private void sendLatency(MessageReceivedEvent mre, Consumer<Message> consumer, DiscordCommand c) {
        mre.getTextChannel().sendMessage("Pong...").queue(msg ->
                msg.editMessage("Pong: `" + mre.getMessage().getCreationTime()
                        .until(msg.getCreationTime(), ChronoUnit.MILLIS) + " ms`").queue());
    }
}
