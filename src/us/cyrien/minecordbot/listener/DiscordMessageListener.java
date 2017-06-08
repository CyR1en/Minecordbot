package us.cyrien.minecordbot.listener;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.ChatColor;
import org.json.JSONArray;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.entity.Messenger;
import us.cyrien.minecordbot.main.Minecordbot;
import us.cyrien.minecordbot.parse.PrefixParser;

public class DiscordMessageListener extends ListenerAdapter {

    private Minecordbot mcb;
    private Messenger messenger;

    public DiscordMessageListener(Minecordbot mcb) {
        this.mcb = mcb;
        messenger = mcb.getMessenger();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        boolean isCommand = event.getMessage().getContent().startsWith(MCBConfig.get("trigger"));
        boolean notSelf = !event.getMember().getUser().getId().equalsIgnoreCase(event.getJDA().getSelfUser().getId());
        TextChannel tc = event.getJDA().getTextChannelById(MCBConfig.get("mod_channel"));
        boolean modChannel = tc != null && tc.equals(event.getTextChannel());
        boolean bound = containsChannel(event.getTextChannel().getId()) || modChannel;
        boolean blocked = prefixIsBlocked(event.getMessage().getContent()) || botIsBlocked(event.getAuthor().getId());
        if (isCommand && notSelf) {
            mcb.handleCommand(event);
        } else {
            if (bound && notSelf && !blocked) {
                String msg = event.getMessage().getContent();
                String prefix = PrefixParser.parseDiscordPrefixes(MCBConfig.get("message_prefix_minecraft"), event);
                messenger.sendGlobalMessageToMC(ChatColor.translateAlternateColorCodes('&', prefix + (MCBConfig.get("message_format") + msg)).trim());
            }
        }
    }

    private boolean botIsBlocked(String id) {
        JSONArray blockedB = MCBConfig.get("blocked_bots");
        if(mcb.getJDA().getUserById(id) != null) {
            User user = mcb.getJDA().getUserById(id);
            if(user.isBot()) {
                for (Object s : blockedB) {
                    if (user.getId().equals(s.toString().trim()))
                        return true;
                }
            }
        }
        return false;
    }

    private boolean prefixIsBlocked(String message) {
        JSONArray blockedP = MCBConfig.get("blocked_command_prefix");
        for(Object p : blockedP) {
            if(message.startsWith(p.toString()))
                return true;
        }
        return false;
    }

    private boolean containsChannel(String id) {
        JSONArray tcArray = MCBConfig.get("text_channels");
        if (tcArray != null) {
            for(Object s : tcArray)
                if(s.toString().equalsIgnoreCase(id))
                    return true;
        }
        return false;
    }
}
