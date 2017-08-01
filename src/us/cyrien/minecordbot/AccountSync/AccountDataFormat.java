package us.cyrien.minecordbot.AccountSync;

import org.bukkit.ChatColor;
import org.json.JSONObject;
import us.cyrien.minecordbot.entity.MCUser;

import java.util.LinkedHashMap;

public class AccountDataFormat {

    private String uuid;
    private String discordID;
    private String mcUsername;
    private String discordUsername;
    private boolean authenticated;

    public AccountDataFormat(MCUser mcUser) {
        uuid = mcUser.getPlayer().getUniqueId().toString();
        discordID = mcUser.getMcbUser().getId();
        mcUsername = ChatColor.stripColor(mcUser.getPlayer().getName());
        discordUsername = mcUser.getMcbUser().getName();
        authenticated = mcUser.getMcbUser() != null;
    }

    public AccountDataFormat(String keyUUID, JSONObject accData) {
        uuid = keyUUID;
        discordID = accData.getString(DataKey.DISCORD_ID.toString());
        mcUsername = accData.getString(DataKey.MC_USERNAME.toString());
        discordUsername = accData.getString(DataKey.DISCORD_USERNAME.toString());
        authenticated = accData.getBoolean(DataKey.SYNCED.toString());
    }

    public JSONObject dataAsJSON() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        JSONObject accData = new JSONObject();
        accData.put(DataKey.MC_USERNAME.toString(), mcUsername);
        accData.put(DataKey.DISCORD_ID.toString(), discordID);
        accData.put(DataKey.DISCORD_USERNAME.toString(), discordUsername);
        accData.put(DataKey.SYNCED.toString(), authenticated);
        return new JSONObject().put(uuid.toString(), accData);
    }
}
