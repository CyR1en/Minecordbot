package us.cyrien.minecordbot.chat.listeners.discordListeners;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.configuration.MCBConfig;

public class ModChannelListener extends TextChannelListener {

    private boolean isOneway;

    public ModChannelListener(Minecordbot mcb) {
        super(mcb);
        this.channelType = MCBChannelType.MOD_CHANNEL;
        Object b = MCBConfig.get("oneway_mod_channel");
        isOneway = Boolean.parseBoolean(String.valueOf(b));
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        if(!isOneway)
            relayMessage(event);
    }
}
