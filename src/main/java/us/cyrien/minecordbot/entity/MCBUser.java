package us.cyrien.minecordbot.entity;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONObject;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.core.enums.PermissionLevel;
import us.cyrien.minecordbot.Minecordbot;

import java.util.HashMap;
import java.util.UUID;

public class MCBUser {

    private String name;
    private HashMap<Guild , String> nickNames;
    private String ID;
    private PermissionLevel permissionLevel;
    private UUID mcUUID;

    public MCBUser(MessageReceivedEvent e) {
        nickNames = new HashMap<>();
        name = e.getAuthor().getName();
        nickNames.put(e.getGuild(), e.getMember().getNickname());
        ID = e.getAuthor().getId();
        mcUUID = null;
        setPermLevel();
    }

    public MCBUser(User user, Guild guild) {
        nickNames = new HashMap<>();
        name = user.getName();
        nickNames.put(guild, guild.getMember(user).getNickname());
        ID = user.getId();
        setPermLevel();
    }

    public MCBUser(User user) {
        nickNames = new HashMap<>();
        name = user.getName();
        ID = user.getId();
        Minecordbot.getInstance().getJDA().getMutualGuilds(user).forEach(guild -> nickNames.put(guild, guild.getMember(user).getNickname()));
    }

    public Player parseAsPlayer() {
        return Bukkit.getPlayer()
    }

    public boolean hasPermission(PermissionLevel level) {
        if (this.getPermissionLevel() == PermissionLevel.CYRIEN || this.getPermissionLevel() == PermissionLevel.OWNER)
            return true;
        else
            return this.getPermissionLevel().ordinal() >= level.ordinal();
    }

    public UUID getMcUUID() {
        return mcUUID;
    }

    public void setMcUUID(UUID uuid) {
        mcUUID = uuid;
    }

    private void setPermLevel() {
        JSONObject perms = MCBConfig.getJSONObject("permissions");
        if (this.getID().equals("193970511615623168")) {
            permissionLevel = PermissionLevel.CYRIEN;
            return;
        } else if (this.getID().equals(MCBConfig.get("owner_id"))) {
            permissionLevel = PermissionLevel.OWNER;
            return;
        }
        for (int i = 1; i <= 3; i++) {
            for (Object s : perms.getJSONArray("level_" + i)) {
                if (s.toString().equalsIgnoreCase(getID()))
                    switch (i) {
                        case 1:
                            permissionLevel = PermissionLevel.LEVEL_1;
                            return;
                        case 2:
                            permissionLevel = PermissionLevel.LEVEL_2;
                            return;
                        case 3:
                            permissionLevel = PermissionLevel.LEVEL_3;
                            return;
                    }
            }
        }
        permissionLevel = PermissionLevel.LEVEL_0;
    }

    public String getName() {
        return name;
    }

    public HashMap<Guild, String> getNickNames() {
        return nickNames;
    }

    public String getID() {
        return ID;
    }

    public PermissionLevel getPermissionLevel() {
        return permissionLevel;
    }

    @Override
    public String toString() {
        return getName() + "|" + getNickNames().toString() + "|" + getID();
    }
}
