package us.cyrien.minecordbot.accountSync.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.JSONObject;
import us.cyrien.minecordbot.accountSync.DataKey;
import us.cyrien.minecordbot.accountSync.Database;
import us.cyrien.minecordbot.entity.MCUser;

public class UserConnectionListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        JSONObject data = Database.get(event.getPlayer().getUniqueId().toString());
        MCUser mcUser = new MCUser(event.getPlayer());
        if(data == null) {
            Database.set(mcUser.getPlayer().getUniqueId().toString(), new JSONObject(mcUser.getDataAsMap()));
        } else if (!data.get(DataKey.MC_USERNAME.toString()).equals(event.getPlayer().getName())) {
            data.put(DataKey.MC_USERNAME.toString(), event.getPlayer().getName());
            Database.set(event.getPlayer().getUniqueId().toString(), new JSONObject(data));
        } else if (!data.get(DataKey.DISCORD_USERNAME.toString()).equals("Not Synced yet") && !data.get(DataKey.DISCORD_USERNAME.toString()).equals(mcUser.getMcbUser().getName())) {
            data.put(DataKey.DISCORD_USERNAME.toString(), mcUser.getMcbUser().getName());
            Database.set(event.getPlayer().getUniqueId().toString(), new JSONObject(data));
        }
    }
}
