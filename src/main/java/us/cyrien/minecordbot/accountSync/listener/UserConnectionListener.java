package us.cyrien.minecordbot.accountSync.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.JSONObject;
import us.cyrien.minecordbot.accountSync.DataKey;
import us.cyrien.minecordbot.accountSync.Database;
import us.cyrien.minecordbot.entity.UnifiedUser;

public class UserConnectionListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

    }
}
