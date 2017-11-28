package us.cyrien.minecordbot.chat.listeners.mcListeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import us.cyrien.minecordbot.Minecordbot;

public class DeathListener extends MCBListener {

    public DeathListener(Minecordbot mcb) {
        super(mcb);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        boolean isDeathBroadCast = configsManager.getBroadcastConfig().getBoolean("See_Player_Death");
        boolean allowIncog = configsManager.getBroadcastConfig().getBoolean("Hide_Incognito_Player");
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
        boolean bc = configsManager.getModChannelConfig().getBoolean("See_Player_Death");
        Player player = event.getEntity();
        String msg = ChatColor.stripColor(event.getDeathMessage());
        messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
        if (bc)
            messenger.sendMessageToAllModChannel("```css" + "\n[" + msg + "]\n```");
    }
}
