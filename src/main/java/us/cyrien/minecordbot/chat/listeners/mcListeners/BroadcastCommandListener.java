package us.cyrien.minecordbot.chat.listeners.mcListeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.ServerCommandEvent;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.configuration.ModChannelConfig;

public class BroadcastCommandListener extends MCBListener {

    public BroadcastCommandListener(Minecordbot mcb) {
        super(mcb);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBroadcastCommand(ServerCommandEvent event) {
        String cmd = ChatColor.stripColor(event.getCommand());
        if (cmd.equals("broadcast ") || cmd.equals("bc ")) {
            String msg = cmd.split(" ", 2)[1];
            messenger.sendMessageToAllBoundChannel("\uD83D\uDCE2 " + msg);
            boolean seeBc = configsManager.getModChannelConfig().getBoolean(ModChannelConfig.Nodes.SEE_BROADCAST);
            if(seeBc) {
                messenger.sendMessageToAllModChannel("\uD83D\uDCE2 " + msg );
            }
        }
    }
}
