package us.cyrien.minecordbot.chat.listeners.mcListeners;

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
        String msg = event.getMessage();
        messenger.sendMessageToAllBoundChannel("\uD83D\uDCE2 " + msg);
    }
}
