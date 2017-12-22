package us.cyrien.minecordbot.chat.listeners.mcListeners;

import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import mineverse.Aust1n46.chat.MineverseChat;
import mineverse.Aust1n46.chat.api.MineverseChatAPI;
import mineverse.Aust1n46.chat.api.MineverseChatPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import us.cyrien.minecordbot.HookContainer;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.hooks.GriefPreventionHook;
import us.cyrien.minecordbot.hooks.VentureChatHook;
import us.cyrien.minecordbot.prefix.PrefixParser;

public class ChatListener extends MCBListener {

    private final GriefPreventionHook griefPreventionHook = HookContainer.getGriefPreventionHook();
    private final VentureChatHook ventureChatHook = HookContainer.getVentureChatHook();

    public ChatListener(Minecordbot mcb) {
        super(mcb);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        ChatType type = getChatType(e);
        String message = formatMessage(type, e);
        RelayMessage relayMessage = new RelayMessage(message, type);
        relay(relayMessage);
    }

    private ChatType getChatType(AsyncPlayerChatEvent e) {
        GriefPrevention griefPrevention = griefPreventionHook == null ? null : griefPreventionHook.getPlugin();
        MineverseChat ventureChat = ventureChatHook == null ? null : ventureChatHook.getPlugin();
        DataStore dataStore = griefPrevention == null ? null : griefPrevention.dataStore;
        MineverseChatPlayer mCP = ventureChat == null ? null : MineverseChatAPI.getMineverseChatPlayer(e.getPlayer());
        if (e.isCancelled()) {
            return ChatType.CANCELLED;
        } else if (griefPrevention != null && dataStore.isSoftMuted(e.getPlayer().getUniqueId())) {
            return ChatType.GRIEF_PROTECTION_SOFT_MUTE;
        } else if (ventureChat != null && mCP.hasConversation() && !mCP.isQuickChat()) {
            return ChatType.VENTURECHAT_PRIVATE;
        } else if (ventureChat != null && mCP.isPartyChat() && !mCP.isQuickChat())  {
            if (e.isCancelled())
                return ChatType.IGNORE;
            else
                return ChatType.VENTURECHAT_PARTY;
        } else if (ventureChat != null && mCP.isQuickChat()) {
            ChatType ct = ChatType.VENTURECHAT_QUICKCHAT;
            ct.setChatPrefix(ct.getChatPrefix().replaceAll("\\{channel}", mCP.getQuickChannel().getName()));
            return ct;
        }  else if (ventureChat != null && !mCP.getCurrentChannel().equals(MineverseChat.ccInfo.getDefaultChannel())) {
            ChatType ct = ChatType.VENTURECHAT_CHANNEL;
            ct.setChatPrefix(ct.getChatPrefix().replaceAll("\\{channel}", mCP.getCurrentChannel().getName()));
            return ct;
        }  else if (ventureChat != null && e.getMessage().startsWith("@")) {
            if(e.isCancelled())
                return ChatType.IGNORE;
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                MineverseChatPlayer p = MineverseChatAPI.getMineverseChatPlayer(player);
                if (p.isOnline() && e.getMessage().startsWith("@" + p.getPlayer().getDisplayName().replace("§r", ""))) {
                    ChatType ct = ChatType.VENTURECHAT_PRIVATE_MENTION;
                    ct.setChatPrefix(ct.getChatPrefix().replaceAll("\\{player}", ChatColor.stripColor(player.getDisplayName())));
                    return ct;
                }
                if (p.isOnline() && e.getMessage().startsWith("@" + p.getName())) {
                    ChatType ct = ChatType.VENTURECHAT_PRIVATE_MENTION;
                    ct.setChatPrefix(ct.getChatPrefix().replaceAll("\\{player}", ChatColor.stripColor(player.getDisplayName())));
                    return ct;
                }
            }
            return ChatType.DEFAULT;
        } else {
            return ChatType.DEFAULT;
        }
    }

    private void relay(RelayMessage relayMessage) {
        boolean seeChatOnMod = configsManager.getModChannelConfig().getBoolean("See_Chat");
        if (relayMessage.getType() == ChatType.DEFAULT) {
            messenger.sendMessageToAllBoundChannel(relayMessage + "");
        }
        if (seeChatOnMod) {
            switch (relayMessage.getType()) {
                case CANCELLED:
                    boolean seeCancelled = configsManager.getModChannelConfig().getBoolean("See_Cancelled_Chat");
                    if (seeCancelled)
                        messenger.sendMessageToAllModChannel(relayMessage + "");
                    break;
                case GRIEF_PROTECTION_SOFT_MUTE:
                    boolean seeGPSM = configsManager.getModChannelConfig().getBoolean("See_GriefPrevention_SoftMute");
                    if (seeGPSM)
                        messenger.sendMessageToAllModChannel(relayMessage + "");
                    break;
                case MCMMO_PARTY:
                    boolean seeParty = configsManager.getModChannelConfig().getBoolean("See_mcMMO_Party_Chat");
                    if (seeParty)
                        messenger.sendMessageToAllModChannel(relayMessage + "");
                    break;
                case MCMMO_ADMIN:
                    boolean seeAdmin = configsManager.getModChannelConfig().getBoolean("See_mcMMO_Admin_Chat");
                    if (seeAdmin)
                        messenger.sendMessageToAllModChannel(relayMessage + "");
                    break;
                case DEFAULT:
                    messenger.sendMessageToAllModChannel(relayMessage + "");
                    break;
            }
        }
    }

    private String formatMessage(ChatType type, AsyncPlayerChatEvent e) {
        String msg = mentionHandler.handleMention(ChatColor.stripColor(e.getMessage()));
        String prefix = PrefixParser.parseMinecraftPrefix(configsManager.getChatConfig().getString("Minecraft_Prefix"), e.getPlayer());
        return type.getChatPrefix() + "**" + prefix + "** " + msg;
    }

    public enum ChatType {
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

        ChatType(String prefix) {
            chatPrefix = prefix;
        }

        public void setChatPrefix(String s) {
            chatPrefix = s;
        }

        public String getChatPrefix() {
            return chatPrefix;
        }
    }

    private class RelayMessage {

        private String message;
        private ChatType type;

        public RelayMessage(String message, ChatType type) {
            this.message = message;
            this.type = type;
        }

        public String getMessage() {
            return message;
        }

        public ChatType getType() {
            return type;
        }

        @Override
        public String toString() {
            return message;
        }
    }
}
