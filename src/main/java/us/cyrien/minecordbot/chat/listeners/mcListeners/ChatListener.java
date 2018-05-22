package us.cyrien.minecordbot.chat.listeners.mcListeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.chat.listeners.MChatType;
import us.cyrien.minecordbot.configuration.ChatConfig;
import us.cyrien.minecordbot.configuration.ModChannelConfig;
import us.cyrien.minecordbot.prefix.PrefixParser;

public class ChatListener extends MCBListener {

    public ChatListener(Minecordbot mcb) {
        super(mcb);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        MChatType type = MChatType.classify(e);
        String message = formatMessage(type, e);
        RelayMessage relayMessage = new RelayMessage(message, type);
        relay(relayMessage);
    }

    private void relay(RelayMessage relayMessage) {
        boolean seeChatOnMod = configsManager.getModChannelConfig().getBoolean(ModChannelConfig.Nodes.SEE_CHAT);
        if (relayMessage.getType() == MChatType.DEFAULT) {
            messenger.sendMessageToAllBoundChannel(relayMessage + "");
        }
        if (seeChatOnMod) {
            switch (relayMessage.getType()) {
                case CANCELLED:
                    boolean seeCancelled = configsManager.getModChannelConfig().getBoolean(ModChannelConfig.Nodes.SEE_CANCELLED_CHAT);
                    if (seeCancelled)
                        messenger.sendMessageToAllModChannel(relayMessage + "");
                    break;
                case GRIEF_PROTECTION_SOFT_MUTE:
                    boolean seeGPSM = configsManager.getModChannelConfig().getBoolean(ModChannelConfig.Nodes.SEE_GP_SOFTMUTE);
                    if (seeGPSM)
                        messenger.sendMessageToAllModChannel(relayMessage + "");
                    break;
                case MCMMO_PARTY:
                    boolean seeParty = configsManager.getModChannelConfig().getBoolean(ModChannelConfig.Nodes.SEE_MMO_PARTY_CHAT);
                    if (seeParty)
                        messenger.sendMessageToAllModChannel(relayMessage + "");
                    break;
                case MCMMO_ADMIN:
                    boolean seeAdmin = configsManager.getModChannelConfig().getBoolean(ModChannelConfig.Nodes.SEE_MMO_ADMIN_CHAT);
                    if (seeAdmin)
                        messenger.sendMessageToAllModChannel(relayMessage + "");
                    break;
                case VENTURECHAT_PARTY:
                    boolean seeVParty = configsManager.getModChannelConfig().getBoolean(ModChannelConfig.Nodes.SEE_VC_CHANNEL_PARTY_CHAT);
                    if (seeVParty)
                        messenger.sendMessageToAllModChannel(relayMessage + "");
                    break;
                case VENTURECHAT_QUICKCHAT:
                    boolean seeVQC = configsManager.getModChannelConfig().getBoolean(ModChannelConfig.Nodes.SEE_VC_QUICK_CHAT);
                    if (seeVQC)
                        messenger.sendMessageToAllModChannel(relayMessage + "");
                    break;
                case VENTURECHAT_PRIVATE:
                    boolean seeVPrivate = configsManager.getModChannelConfig().getBoolean(ModChannelConfig.Nodes.SEE_VC_CHANNEL_PRIVATE_CHATS);
                    if (seeVPrivate)
                        messenger.sendMessageToAllModChannel(relayMessage + "");
                    break;
                case VENTURECHAT_CHANNEL:
                    boolean seeVChannel = configsManager.getModChannelConfig().getBoolean(ModChannelConfig.Nodes.SEE_VC_CHANNEL_CHAT);
                    if (seeVChannel)
                        messenger.sendMessageToAllModChannel(relayMessage + "");
                    break;
                case VENTURECHAT_PRIVATE_MENTION:
                    boolean seeVPM = configsManager.getModChannelConfig().getBoolean(ModChannelConfig.Nodes.SEE_VC_PRIVATE_MENTION);
                    if (seeVPM)
                        messenger.sendMessageToAllModChannel(relayMessage + "");
                    break;
                case DEFAULT:
                    messenger.sendMessageToAllModChannel(relayMessage + "");
                    break;
            }
        }
    }

    private String formatMessage(MChatType type, AsyncPlayerChatEvent e) {
        String msg = mentionHandler.handleMention(ChatColor.stripColor(e.getMessage()));
        String prefix = PrefixParser.parseMinecraftPrefix(configsManager.getChatConfig().getString(ChatConfig.Nodes.MINECRAFT_PREFIX), e.getPlayer());
        return type.getChatPrefix() + prefix + msg;
    }

    private class RelayMessage {

        private String message;
        private MChatType type;

        public RelayMessage(String message, MChatType type) {
            this.message = message;
            this.type = type;
        }

        public String getMessage() {
            return message;
        }

        public MChatType getType() {
            return type;
        }

        @Override
        public String toString() {
            return message;
        }
    }
}
