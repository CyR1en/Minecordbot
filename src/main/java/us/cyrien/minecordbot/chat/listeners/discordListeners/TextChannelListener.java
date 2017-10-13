package us.cyrien.minecordbot.chat.listeners.discordListeners;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.ChatColor;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.chat.Messenger;
import us.cyrien.minecordbot.configuration.MCBConfigsManager;
import us.cyrien.minecordbot.prefix.PrefixParser;

import java.util.List;

public abstract class TextChannelListener extends ListenerAdapter {

    private Minecordbot mcb;
    private Messenger messenger;
    protected MCBChannelType channelType;
    protected MCBConfigsManager configsManager;

    public TextChannelListener(Minecordbot mcb) {
        this.mcb = mcb;
        messenger = mcb.getMessenger();
        this.channelType = MCBChannelType.DEFAULT_CHANNEL;
        configsManager = mcb.getMcbConfigsManager();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        boolean self = event.getAuthor().equals(mcb.getBot().getJda().getSelfUser());
        boolean blocked = prefixIsBlocked(event.getMessage().getContent()) || botIsBlocked(event.getAuthor().getId());
        if (self || blocked)
            return;
        switch (channelType) {
            case MOD_CHANNEL:
                if (isModChannel(event.getTextChannel()))
                    execute(event);
                break;
            case BOUND_CHANNEL:
                if (isRelayChannel(event.getTextChannel()))
                    execute(event);
                break;
            case DEFAULT_CHANNEL:
                execute(event);
                break;
            case PRIVATE_CHANNEL:
                if (event.getChannelType() == ChannelType.PRIVATE)
                    execute(event);
                break;
        }
    }

    public abstract void execute(MessageReceivedEvent event);

    protected boolean botIsBlocked(String id) {
        List<String> blockedB = (List<String>) configsManager.getChatConfig().getList("Blocked_Bots");
        if (mcb.getBot().getJda().getUserById(id) != null) {
            User user = mcb.getBot().getJda().getUserById(id);
            if (user.isBot()) {
                for (Object s : blockedB) {
                    if (user.getId().equals(s.toString().trim()))
                        return true;
                }
            }
        }
        return false;
    }

    protected boolean prefixIsBlocked(String message) {
        List<String> blockedP = (List<String>) configsManager.getChatConfig().getList("Blocked_Prefix");
        for (String p : blockedP) {
            if (p.equals("{this}"))
                p = mcb.getBot().getClient().getPrefix();
            if (message.startsWith(p))
                return true;
        }
        return false;
    }

    private boolean isModChannel(TextChannel c) {
        List<TextChannel> tcs = mcb.getModChannels();
        return tcs.contains(c);
    }
    private boolean isRelayChannel(TextChannel c) {
        List<TextChannel> tcs = mcb.getRelayChannels();
        return tcs.contains(c);
    }

    protected void relayMessage(MessageReceivedEvent event) {
        String msg = event.getMessage().getContent();
        String prefix = PrefixParser.parseDiscordPrefixes(configsManager.getChatConfig().getString("Discord_Prefix"), event);
        String format = configsManager.getChatConfig().getString("Message_Format");
        getMessenger().sendGlobalMessageToMC(ChatColor.translateAlternateColorCodes('&', prefix + " " + (format + msg)).trim());
    }

    public Messenger getMessenger() {
        return messenger;
    }

    public Minecordbot getMcb() {
        return mcb;
    }

    public enum MCBChannelType {
        PRIVATE_CHANNEL,
        BOUND_CHANNEL,
        MOD_CHANNEL,
        DEFAULT_CHANNEL
    }

}