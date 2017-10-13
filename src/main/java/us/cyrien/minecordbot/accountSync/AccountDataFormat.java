package us.cyrien.minecordbot.accountSync;

import org.bukkit.ChatColor;
import org.json.JSONObject;
import us.cyrien.minecordbot.entity.UnifiedUser;

import java.util.LinkedHashMap;

public class AccountDataFormat {

    private String discordID;
    private String mcUsername;
    private String discordUsername;

    public AccountDataFormat(UnifiedUser mcUser) {
        discordID = mcUser.getMcbUser() == null ? "Not Synced yet" : mcUser.getMcbUser().getID();
        mcUsername = ChatColor.stripColor(mcUser.getPlayer().getName());
        discordUsername = mcUser.getMcbUser() == null ? "Not Synced yet" : mcUser.getMcbUser().getName() ;
    }

    public AccountDataFormat(String keyUUID, JSONObject accData) {
        discordID = accData.getString(DataKey.DISCORD_ID.toString());
        mcUsername = accData.getString(DataKey.MC_USERNAME.toString());
        discordUsername = accData.getString(DataKey.DISCORD_USERNAME.toString());
    }

    public LinkedHashMap<String, Object> dataAsMap() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put(DataKey.MC_USERNAME.toString(), mcUsername);
        map.put(DataKey.DISCORD_ID.toString(), discordID);
        map.put(DataKey.DISCORD_USERNAME.toString(), discordUsername);
        return map;
    }
}
