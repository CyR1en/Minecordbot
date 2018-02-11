package us.cyrien.minecordbot.chat.listeners.discordListeners;

        import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import us.cyrien.minecordbot.Minecordbot;
        import us.cyrien.minecordbot.configuration.ModChannelConfig;

public class ModChannelListener extends TextChannelListener {

    private boolean isOneWay;

    public ModChannelListener(Minecordbot mcb) {
        super(mcb);
        this.channelType = MCBChannelType.MOD_CHANNEL;
        isOneWay = configsManager.getModChannelConfig().getBoolean(ModChannelConfig.Nodes.ONE_WAY);
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        if(!isOneWay)
            relayMessage(event);
    }
}
