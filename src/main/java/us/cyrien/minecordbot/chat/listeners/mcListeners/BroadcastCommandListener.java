package us.cyrien.minecordbot.chat.listeners.mcListeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.ServerCommandEvent;
import us.cyrien.minecordbot.Minecordbot;

public class BroadcastCommandListener extends MCBListener {

    public BroadcastCommandListener(Minecordbot mcb) {
        super(mcb);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBroadcastCommand(ServerCommandEvent event) {
        String cmd = event.getCommand();
        if (cmd.equals("broadcast ") || cmd.equals("bc ")) {
            String msg = cmd.split(" ", 2)[1];
            messenger.sendMessageToAllBoundChannel("\uD83D\uDCE2 " + msg);
        }
    }
}
