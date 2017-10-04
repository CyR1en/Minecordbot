package us.cyrien.minecordbot.chat.listeners.mcListeners;

import net.dv8tion.jda.core.entities.TextChannel;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
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
        String cmd = ChatColor.stripColor(event.getCommand());
        if (cmd.equals("broadcast ") || cmd.equals("bc ")) {
            String msg = cmd.split(" ", 2)[1];
            String modChannel = configsManager.getModChannelConfig().getString("Mod_TextChannel");
            TextChannel textChannel = StringUtils.isEmpty(modChannel) ? null : mcb.getBot().getJda().getTextChannelById(modChannel);
            messenger.sendMessageToAllBoundChannel("\uD83D\uDCE2 " + msg);
            boolean seeBc = configsManager.getModChannelConfig().getBoolean("See_Broadcast");
            if(seeBc) {
                messenger.sendMessageToDiscord(textChannel,"\uD83D\uDCE2 " + msg );
            }
        }
    }
}
