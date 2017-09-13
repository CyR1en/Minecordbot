package us.cyrien.minecordbot.chat.listeners.discordListeners;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import us.cyrien.minecordbot.Minecordbot;

public class DiscordRelayListener extends TextChannelListener {

    public DiscordRelayListener(Minecordbot mcb) {
        super(mcb);
        this.channelType = MCBChannelType.BOUND_CHANNEL;
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        relayMessage(event);
    }
}
