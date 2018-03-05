package us.cyrien.minecordbot.chat.listeners.mcListeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.BroadcastMessageEvent;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.chat.listeners.MChatType;
import us.cyrien.minecordbot.configuration.BroadcastConfig;
import us.cyrien.minecordbot.configuration.ChatConfig;
import us.cyrien.minecordbot.configuration.ModChannelConfig;
import us.cyrien.minecordbot.prefix.PrefixParser;
import us.cyrien.minecordbot.utils.SRegex;

import java.util.List;
import java.util.regex.Pattern;

public class BroadcastListener extends MCBListener {

    public BroadcastListener(Minecordbot mcb) {
        super(mcb);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBroadcastMessage(BroadcastMessageEvent event) {
        String msg = ChatColor.stripColor(event.getMessage());
        boolean seeBc = configsManager.getModChannelConfig().getBoolean(ModChannelConfig.Nodes.SEE_BROADCAST);
        boolean seePlBc = configsManager.getBroadcastConfig().getBoolean(BroadcastConfig.Nodes.PLUGIN_BROADCAST);
        boolean privateBroadcast = isPrivate(event);
        boolean seeCL = configsManager.getBroadcastConfig().getBoolean(BroadcastConfig.Nodes.CLEARLAG);
        if (!privateBroadcast && seePlBc) {
            boolean isClearLag = msg.contains("[ClearLag]") || msg.contains("[Clearlag]") || msg.contains("[clearlag]");
            if (isClearLag && seeCL) {
                messenger.sendMessageToAllBoundChannel("\uD83D\uDCE2 " + msg);
                if (seeBc) {
                    messenger.sendMessageToAllModChannel("\uD83D\uDCE2 " + msg);
                }
            } else if (!isClearLag) {
                messenger.sendMessageToAllBoundChannel("\uD83D\uDCE2 " + msg);
                if (seeBc) {
                    messenger.sendMessageToAllModChannel("\uD83D\uDCE2 " + msg);
                }
            }
        } else if (privateBroadcast) {
            boolean ismcMMOAdmin = mcb.getChatManager().getChatStatus().isIsmcmmoAdminChat();
            boolean seemcMMOAdmin = mcb.getMcbConfigsManager().getModChannelConfig().getBoolean(ModChannelConfig.Nodes.SEE_MMO_ADMIN_CHAT);
            boolean ismcMMOParty = mcb.getChatManager().getChatStatus().ismcmmopartychat();
            boolean seemcMMOParty = mcb.getMcbConfigsManager().getModChannelConfig().getBoolean(ModChannelConfig.Nodes.SEE_MMO_PARTY_CHAT);
            if (ismcMMOAdmin && seemcMMOAdmin) {
                msg = formatMessage(MChatType.MCMMO_ADMIN, msg);
                messenger.sendMessageToAllModChannel(msg);
            } else if (ismcMMOParty && seemcMMOParty) {
                msg = formatMessage(MChatType.MCMMO_PARTY, msg);
                messenger.sendMessageToAllModChannel(msg);
            } else if (mcb.getChatManager().getChatStatus().isCancelled()) {
                msg = formatMessage(MChatType.CANCELLED, msg);
                messenger.sendMessageToAllModChannel(msg);
            }
        } else {
            messenger.sendMessageToAllModChannel("\uD83D\uDCE2 " + msg);
        }
        mcb.getChatManager().getChatStatus().reset();
    }

    private String formatMessage(MChatType type, String str) {
        Player p = findPlayer(str);
        String msg = mentionHandler.handleMention(ChatColor.stripColor(str));
        msg = removePlayer(msg);
        String prefix = PrefixParser.parseMinecraftPrefix(configsManager.getChatConfig().getString(ChatConfig.Nodes.MINECRAFT_PREFIX), p);
        return type.getChatPrefix() + "**" + prefix + "** " + msg;
    }

    private String removePlayer(String message) {
        return message.replaceAll("\\[.*?]", "");
    }

    private Player findPlayer(String msg) {
        SRegex sRegex = new SRegex(msg);
        sRegex.find(Pattern.compile("\\[.*?]"));
        List<String> results = sRegex.getResultsList();
        String playerName = results.size() == 0 ? "" : sRegex.getResultsList().get(0).replaceAll("\\[", "").replaceAll("]", "");
        return playerName.isEmpty() ? null : Bukkit.getPlayer(playerName);
    }

    private boolean isPrivate(BroadcastMessageEvent event) {
        if (event.getRecipients().size() < Bukkit.getServer().getOnlinePlayers().size()) {
            return true;
        } else if (mcb.getChatManager().getChatStatus().isIsmcmmoAdminChat() || mcb.getChatManager().getChatStatus().ismcmmopartychat()) {
            return true;
        } else if (mcb.getChatManager().getChatStatus().isCancelled()) {
            return true;
        }
        return false;
    }

}
