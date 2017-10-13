package us.cyrien.minecordbot.chat.listeners.discordListeners;

        import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import us.cyrien.minecordbot.Minecordbot;

public class ModChannelListener extends TextChannelListener {

    private boolean isOneWay;

    public ModChannelListener(Minecordbot mcb) {
        super(mcb);
        this.channelType = MCBChannelType.MOD_CHANNEL;
        isOneWay = configsManager.getModChannelConfig().getBoolean("One_Way");
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        if(!isOneWay)
            relayMessage(event);
    }
}
