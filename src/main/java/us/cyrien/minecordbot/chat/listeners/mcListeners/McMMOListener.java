package us.cyrien.minecordbot.chat.listeners.mcListeners;

import com.gmail.nossr50.events.chat.McMMOAdminChatEvent;
import com.gmail.nossr50.events.chat.McMMOPartyChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import us.cyrien.minecordbot.Minecordbot;

public class McMMOListener extends MCBListener{

    public McMMOListener(Minecordbot mcb) {
        super(mcb);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onmcMMOAdminChat(McMMOAdminChatEvent e) {
        mcb.getChatManager().getChatStatus().setIsmcmmoAdminChat(true);
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onmcMMOPartyChat(McMMOPartyChatEvent e) {
        mcb.getChatManager().getChatStatus().setmcmmopartychat(true);
    }
}
