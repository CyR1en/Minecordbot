package us.cyrien.minecordbot.entity;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONObject;
import us.cyrien.minecordbot.core.enums.PermissionLevel;
import us.cyrien.minecordbot.configuration.MCBConfig;

public class User {

    private String name;
    private String nick;
    private String Id;
    private PermissionLevel permissionLevel;

    public User() {
    }

    public User setUser(MessageReceivedEvent e) {
        name = e.getAuthor().getName();
        nick = e.getMember().getNickname();
        Id = e.getAuthor().getId();
        permissionLevel = getPermLevel();
        return this;
    }

    public boolean hasPermission(PermissionLevel level) {
        return this.getId().equalsIgnoreCase(MCBConfig.get("owner_id")) || this.getPermissionLevel().ordinal() >= level.ordinal();
    }

    public PermissionLevel getPermLevel() {
        JSONObject perms = MCBConfig.getJSONObject("permissions");
        for(int i = 1; i <= 3; i++) {
            for(Object s : perms.getJSONArray("level_" + i)) {
                if(s.toString().equalsIgnoreCase(getId()))
                    switch (i) {
                        case 1:
                            return PermissionLevel.LEVEL_1;
                        case 2:
                            return PermissionLevel.LEVEL_2;
                        case 3:
                            return PermissionLevel.LEVEL_3;
                    }
            }
        }
        return PermissionLevel.LEVEL_0;
    }

    public String getName(){
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

}
