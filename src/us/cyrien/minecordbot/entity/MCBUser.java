package us.cyrien.minecordbot.entity;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONObject;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.core.enums.PermissionLevel;

public class MCBUser {

    private String name;
    private String nick;
    private String Id;
    private PermissionLevel permissionLevel;

    public MCBUser(MessageReceivedEvent e) {
        name = e.getAuthor().getName();
        nick = e.getMember().getNickname();
        Id = e.getAuthor().getId();
        setPermLevel();
    }

    public MCBUser(User user, Guild guild) {
        name = user.getName();
        nick = guild.getMember(user).getNickname();
        Id = user.getId();
        setPermLevel();
    }

    public boolean hasPermission(PermissionLevel level) {
        if (this.getPermissionLevel() == PermissionLevel.CYRIEN || this.getPermissionLevel() == PermissionLevel.OWNER)
            return true;
        else
            return this.getPermissionLevel().ordinal() >= level.ordinal();
    }

    private void setPermLevel() {
        JSONObject perms = MCBConfig.getJSONObject("permissions");
        if (this.getId().equals("193970511615623168")) {
            permissionLevel = PermissionLevel.CYRIEN;
            return;
        } else if (this.getId().equals(MCBConfig.get("owner_id"))) {
            permissionLevel = PermissionLevel.OWNER;
            return;
        }
        for (int i = 1; i <= 3; i++) {
            for (Object s : perms.getJSONArray("level_" + i)) {
                if (s.toString().equalsIgnoreCase(getId()))
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

    public String getNick() {
        return nick;
    }

    public String getId() {
        return Id;
    }

    public PermissionLevel getPermissionLevel() {
        return permissionLevel;
    }

    @Override
    public String toString() {
        return getName() + "|" + getNick() + "|" + getId();
    }
}
