package us.cyrien.minecordbot.accountSync.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.JSONException;
import org.json.JSONObject;
import us.cyrien.mcutils.logger.Logger;
import us.cyrien.minecordbot.accountSync.DataKey;
import us.cyrien.minecordbot.accountSync.Database;
import us.cyrien.minecordbot.entity.UnifiedUser;

public class UserConnectionListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        JSONObject data = Database.get(event.getPlayer().getUniqueId().toString());
        UnifiedUser unifiedUser = new UnifiedUser(event.getPlayer());
        if(data == null) {
            Database.set(unifiedUser.getPlayer().getUniqueId().toString(), new JSONObject(unifiedUser.getDataAsMap()));
        } else if (checkDataNodes(unifiedUser, data)){
            if(!data.get(DataKey.MC_USERNAME.toString()).equals(event.getPlayer().getName())) {
                data.put(DataKey.MC_USERNAME.toString(), event.getPlayer().getName());
                Database.set(event.getPlayer().getUniqueId().toString(), new JSONObject(data));
            }
        } else {
            Logger.warn("There was an issue with " + event.getPlayer().getName() + "'s account sync data and have been re-initiated.");
        }
    }

    private boolean checkDataNodes(UnifiedUser unifiedUser, JSONObject data) {
        for(DataKey dataKey : DataKey.values())
            try {
                data.get(dataKey.toString());
            } catch (JSONException ex) {
                Database.set(unifiedUser.getPlayer().getUniqueId().toString(), new JSONObject(unifiedUser.getDataAsMap()));
                return false;
            }
        return true;
    }
}
