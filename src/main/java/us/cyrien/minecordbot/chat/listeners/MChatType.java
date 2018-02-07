package us.cyrien.minecordbot.chat.listeners;

import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import mineverse.Aust1n46.chat.MineverseChat;
import mineverse.Aust1n46.chat.api.MineverseChatAPI;
import mineverse.Aust1n46.chat.api.MineverseChatPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import us.cyrien.minecordbot.HookContainer;

public enum MChatType {
    GRIEF_PROTECTION_SOFT_MUTE("GriefPrevention-SoftMute | "),
    VENTURECHAT_PRIVATE("VentureChat-PrivateMessage | "),
    VENTURECHAT_PARTY("VentureChat-Party [{party}] | "),
    VENTURECHAT_PRIVATE_MENTION("VentureChat-PrivateMention | "),
    VENTURECHAT_QUICKCHAT("VentureChat-QuickChat [{channel}] | "),
    VENTURECHAT_CHANNEL("VentureChat-Channel [{channel}] | "),
    MCMMO_PARTY("mcMMO-party | "),
    MCMMO_ADMIN("mcMMO-admin | "),
    CANCELLED("\uD83D\uDD15 | "),
    IGNORE(""),
    DEFAULT("");

    public String chatPrefix;

    MChatType(String prefix) {
        chatPrefix = prefix;
    }

    public void setChatPrefix(String s) {
        chatPrefix = s;
    }

    public String getChatPrefix() {
        return chatPrefix;
    }

    public static MChatType classify(AsyncPlayerChatEvent e) {
        GriefPrevention griefPrevention = HookContainer.getGriefPreventionHook() == null ?
                null : HookContainer.getGriefPreventionHook().getPlugin();
        MineverseChat ventureChat = HookContainer.getVentureChatHook() == null ?
                null : HookContainer.getVentureChatHook().getPlugin();
        DataStore dataStore = griefPrevention == null ? null : griefPrevention.dataStore;
        MineverseChatPlayer mCP = ventureChat == null ? null : MineverseChatAPI.getMineverseChatPlayer(e.getPlayer());
        if (e.isCancelled()) {
            return MChatType.CANCELLED;
        } else if (griefPrevention != null && dataStore.isSoftMuted(e.getPlayer().getUniqueId())) {
            return MChatType.GRIEF_PROTECTION_SOFT_MUTE;
        } else if (ventureChat != null && mCP.hasConversation() && !mCP.isQuickChat()) {
            return MChatType.VENTURECHAT_PRIVATE;
        } else if (ventureChat != null && mCP.isPartyChat() && !mCP.isQuickChat())  {
            if (e.isCancelled())
                return MChatType.IGNORE;
            else
                return MChatType.VENTURECHAT_PARTY;
        } else if (ventureChat != null && mCP.isQuickChat()) {
            MChatType ct = MChatType.VENTURECHAT_QUICKCHAT;
            ct.setChatPrefix(ct.getChatPrefix().replaceAll("\\{channel}", mCP.getQuickChannel().getName()));
            return ct;
        }  else if (ventureChat != null && !mCP.getCurrentChannel().equals(MineverseChat.ccInfo.getDefaultChannel())) {
            MChatType ct = MChatType.VENTURECHAT_CHANNEL;
            ct.setChatPrefix(ct.getChatPrefix().replaceAll("\\{channel}", mCP.getCurrentChannel().getName()));
            return ct;
        }  else if (ventureChat != null && e.getMessage().startsWith("@")) {
            if(e.isCancelled())
                return MChatType.IGNORE;
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                MineverseChatPlayer p = MineverseChatAPI.getMineverseChatPlayer(player);
                if (p.isOnline() && e.getMessage().startsWith("@" + p.getPlayer().getDisplayName().replace("§r", ""))) {
                    MChatType ct = MChatType.VENTURECHAT_PRIVATE_MENTION;
                    ct.setChatPrefix(ct.getChatPrefix().replaceAll("\\{player}", ChatColor.stripColor(player.getDisplayName())));
                    return ct;
                }
                if (p.isOnline() && e.getMessage().startsWith("@" + p.getName())) {
                    MChatType ct = MChatType.VENTURECHAT_PRIVATE_MENTION;
                    ct.setChatPrefix(ct.getChatPrefix().replaceAll("\\{player}", ChatColor.stripColor(player.getDisplayName())));
                    return ct;
                }
            }
            return MChatType.DEFAULT;
        } else {
            return MChatType.DEFAULT;
        }
    }
}
