package us.cyrien.minecordbot.entity;

import org.bukkit.entity.Player;

public class MCUser {

    private Player p;
    private MCBUser mcbUser;

    public MCUser(Player p) {
        this.p = p;
        this.mcbUser = null;
    }

    public Player getPlayer() {
        return p;
    }

    public MCBUser getMcbUser() {
        return mcbUser;
    }
}
