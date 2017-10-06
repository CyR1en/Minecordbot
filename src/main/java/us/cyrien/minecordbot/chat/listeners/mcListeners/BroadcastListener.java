package us.cyrien.minecordbot.chat.listeners.mcListeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.BroadcastMessageEvent;
import us.cyrien.minecordbot.Minecordbot;

public class BroadcastListener extends MCBListener {

    public BroadcastListener(Minecordbot mcb) {
        super(mcb);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBroadcastMessage(BroadcastMessageEvent event) {
        String msg = ChatColor.stripColor(event.getMessage());
        boolean seeBc = configsManager.getModChannelConfig().getBoolean("See_Broadcast");
        boolean privateBroadcast = event.getRecipients().size() < Bukkit.getServer().getOnlinePlayers().size();
        boolean enableClearLag = configsManager.getBroadcastConfig().getBoolean("See_ClearLag");
        if(!privateBroadcast) {
            if(msg.trim().startsWith("[ClearLag]") && enableClearLag) {
                messenger.sendMessageToAllBoundChannel("\uD83D\uDCE2 " + msg);
                if(seeBc) {
                    messenger.sendMessageToAllModChannel("\uD83D\uDCE2 " + msg );
                }
                return;
            }
            messenger.sendMessageToAllBoundChannel("\uD83D\uDCE2 " + msg);
            if(seeBc) {
                messenger.sendMessageToAllModChannel("\uD83D\uDCE2 " + msg );
            }
        }
    }
}
