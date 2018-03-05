package us.cyrien.minecordbot.chat.listeners.mcListeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import us.cyrien.mcutils.logger.Logger;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.configuration.BroadcastConfig;
import us.cyrien.minecordbot.configuration.ModChannelConfig;

public class DeathListener extends MCBListener {

    public DeathListener(Minecordbot mcb) {
        super(mcb);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        boolean isDeathBroadCast = configsManager.getBroadcastConfig().getBoolean(BroadcastConfig.Nodes.PLAYER_DEATH);
        boolean allowIncog = configsManager.getBroadcastConfig().getBoolean(BroadcastConfig.Nodes.HIDE_INCOGNITO);
        if (isDeathBroadCast) {
            if (allowIncog) {
                if (!event.getEntity().hasPermission("minecordbot.incognito")) {
                    sendDeathMessage(event);
                }
            } else
                sendDeathMessage(event);
        }
    }

    private void sendDeathMessage(PlayerDeathEvent event) {
        if(!safeToSend(event))
            return;
        boolean bc = configsManager.getModChannelConfig().getBoolean(ModChannelConfig.Nodes.SEE_PLAYER_DEATH);
        String msg = ChatColor.stripColor(event.getDeathMessage());
        messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
        if (bc)
            messenger.sendMessageToAllModChannel("```css" + "\n[" + msg + "]\n```");
    }

    private boolean safeToSend(PlayerDeathEvent event) {
        boolean safe = event.getDeathMessage() != null && !event.getDeathMessage().isEmpty();
        if(!safe)
            Logger.warn("Previous PlayerDeathEvent message is null or missing!");
        return safe;
    }
}
