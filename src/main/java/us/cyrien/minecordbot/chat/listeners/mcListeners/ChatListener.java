package us.cyrien.minecordbot.chat.listeners.mcListeners;

import com.gmail.nossr50.api.ChatAPI;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.dv8tion.jda.core.entities.TextChannel;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import us.cyrien.minecordbot.HookContainer;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.hooks.GriefPreventionHook;
import us.cyrien.minecordbot.hooks.mcMMOHook;

public class ChatListener extends MCBListener {

    private final GriefPreventionHook griefPreventionHook = HookContainer.getGriefPreventionHook();
    private final mcMMOHook mcMMOHook = HookContainer.getMcMMOHook();

    private TextChannel modTextChannel;

    public ChatListener(Minecordbot mcb) {
        super(mcb);
        String modChannel = configsManager.getModChannelConfig().getString("Mod_TextChannel");
        modTextChannel = modChannel == null || modChannel.isEmpty() ? null : mcb.getBot().getJda().getTextChannelById(modChannel);
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
        } else if (mcMMOHook != null) {
            if (ChatAPI.isUsingPartyChat(e.getPlayer()))
                return ChatType.MCMMO_PARTY;
            else if (ChatAPI.isUsingAdminChat(e.getPlayer()))
                return ChatType.MCMMO_ADMIN;
            else
                return ChatType.DEFAULT;
        } else {
            return ChatType.DEFAULT;
        }
    }

    private void relay(RelayMessage relayMessage) {
        boolean seeChat = configsManager.getModChannelConfig().getBoolean("See_Chat");
        if (modTextChannel == null && relayMessage.getType() == ChatType.DEFAULT) {
            messenger.sendMessageToAllBoundChannel(relayMessage + "");
        } else if (relayMessage.getType() == ChatType.DEFAULT) {
            messenger.sendMessageToAllBoundChannel(relayMessage + "");
            if (seeChat)
                messenger.sendMessageToDiscord(modTextChannel, relayMessage + "");
        } else {
            if (seeChat)
                messenger.sendMessageToDiscord(modTextChannel, relayMessage + "");
        }
    }

    private String formatMessage(ChatType type, AsyncPlayerChatEvent e) {
        String msg = mentionHandler.handleMention(ChatColor.stripColor(e.getMessage()));
        String prefix = configsManager.getChatConfig().getString("Minecraft_Prefix");
        return type.getChatPrefix() + "**" + prefix + "** " + msg;
    }

    public enum ChatType {
        GRIEF_PROTECTION_SOFT_MUTE("GriefProtection-SoftMute| "),
        MCMMO_PARTY("mcMMO-party| "),
        MCMMO_ADMIN("mcMMO-admin| "),
        CANCELLED("\\uD83D\\uDD15| "),
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
