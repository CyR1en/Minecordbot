package us.cyrien.minecordbot.chat.listeners.mcListeners;

import com.gmail.nossr50.datatypes.chat.ChatMode;
import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.util.Misc;
import com.gmail.nossr50.util.player.UserManager;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import us.cyrien.minecordbot.HookContainer;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.hooks.GriefPreventionHook;
import us.cyrien.minecordbot.prefix.PrefixParser;

public class ChatListener extends MCBListener {

    private final GriefPreventionHook griefPreventionHook = HookContainer.getGriefPreventionHook();

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
        DataStore dataStore = griefPrevention == null ? null : griefPrevention.dataStore;
        if (e.isCancelled()) {
            return ChatType.CANCELLED;
        } else if (griefPrevention != null && dataStore.isSoftMuted(e.getPlayer().getUniqueId())) {
            return ChatType.GRIEF_PROTECTION_SOFT_MUTE;
        } else {
            return ChatType.DEFAULT;
        }
    }

    private ChatType handleMCMMO(Player player) {
        if (!Misc.isNPCEntity(player) && UserManager.hasPlayerDataKey(player)) {
            McMMOPlayer mcMMOPlayer = UserManager.getOfflinePlayer(player);
            if (mcMMOPlayer != null) {
                if (mcMMOPlayer.isChatEnabled(ChatMode.PARTY))
                    return ChatType.MCMMO_PARTY;
                else if (mcMMOPlayer.isChatEnabled(ChatMode.ADMIN))
                    return ChatType.MCMMO_ADMIN;
            }
        }
        return ChatType.DEFAULT;
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
        GRIEF_PROTECTION_SOFT_MUTE("GriefProtection-SoftMute| "),
        MCMMO_PARTY("mcMMO-party | "),
        MCMMO_ADMIN("mcMMO-admin | "),
        CANCELLED("\uD83D\uDD15 | "),
        DEFAULT("");

        public String chatPrefix;

        ChatType(String prefix) {
            chatPrefix = prefix;
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
