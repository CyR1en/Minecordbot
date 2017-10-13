package us.cyrien.minecordbot.chat.listeners.mcListeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.BroadcastMessageEvent;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.prefix.PrefixParser;
import us.cyrien.minecordbot.utils.SRegex;

public class BroadcastListener extends MCBListener {

    public BroadcastListener(Minecordbot mcb) {
        super(mcb);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBroadcastMessage(BroadcastMessageEvent event) {
        String msg = ChatColor.stripColor(event.getMessage());
        boolean seeBc = configsManager.getModChannelConfig().getBoolean("See_Broadcast");
        boolean seePlBc = configsManager.getBroadcastConfig().getBoolean("See_Plugin_Broadcast");
        boolean privateBroadcast = isPrivate(event);
        boolean seeCL = configsManager.getBroadcastConfig().getBoolean("See_ClearLag");
        if (!privateBroadcast && seePlBc) {
            boolean isClearLag = msg.contains("[ClearLag]");
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
            boolean seemcMMOAdmin = mcb.getMcbConfigsManager().getModChannelConfig().getBoolean("See_mcMMO_Admin_Chat");
            boolean ismcMMOParty = mcb.getChatManager().getChatStatus().ismcmmopartychat();
            boolean seemcMMOParty = mcb.getMcbConfigsManager().getModChannelConfig().getBoolean("See_mcMMO_Admin_Chat");
            if (ismcMMOAdmin && seemcMMOAdmin) {
                msg = formatMessage(ChatListener.ChatType.MCMMO_ADMIN, msg);
                messenger.sendMessageToAllModChannel(msg);
            } else if (ismcMMOParty && seemcMMOParty) {
                msg = formatMessage(ChatListener.ChatType.MCMMO_ADMIN, msg);
                messenger.sendMessageToAllModChannel(msg);
            } else if (mcb.getChatManager().getChatStatus().isCancelled()) {
                msg = formatMessage(ChatListener.ChatType.MCMMO_ADMIN, msg);
                messenger.sendMessageToAllModChannel(msg);
            }
        } else {
            messenger.sendMessageToAllModChannel("\uD83D\uDCE2 " + msg);
        }
        mcb.getChatManager().getChatStatus().reset();
    }

    private String formatMessage(ChatListener.ChatType type, String str) {
        Player p = findPlayer(str);
        String msg = mentionHandler.handleMention(ChatColor.stripColor(str));
        msg = removePlayer(msg);
        String prefix = PrefixParser.parseMinecraftPrefix(configsManager.getChatConfig().getString("Minecraft_Prefix"), p);
        return type.getChatPrefix() + "**" + prefix + "** " + msg;
    }

    private String removePlayer(String message) {
        return message.replaceAll("\\[.*?]", "");
    }

    private Player findPlayer(String msg) {
        SRegex sRegex = new SRegex();
        sRegex.find(msg, "\\[.*?\\]");
        String playerName = sRegex.getResults().get(0).replaceAll("\\[", "").replaceAll("]", "");
        return Bukkit.getPlayer(playerName);
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
