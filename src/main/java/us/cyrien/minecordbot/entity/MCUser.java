package us.cyrien.minecordbot.entity;

import org.bukkit.entity.Player;
import us.cyrien.minecordbot.accountSync.AccountDataFormat;

import java.util.LinkedHashMap;

public class MCUser {

    private Player p;
    private MCBUser mcbUser;
    private AccountDataFormat accountDataFormat;

    public MCUser(Player p) {
        this.p = p;
        this.mcbUser = null;
    }

    public MCUser(Player p, MCBUser mcbUser) {
        this.p = p;
        this.mcbUser = mcbUser;
    }



    public void setMcbUser(MCBUser mcbUser) {
        this.mcbUser = mcbUser;
    }

    public AccountDataFormat getAccountDataFormat() {
        accountDataFormat = new AccountDataFormat(this);
        return accountDataFormat;
    }

    public Player getPlayer() {
        return p;
    }

    public MCBUser getMcbUser() {
        return mcbUser;
    }

    public LinkedHashMap<String, Object> getDataAsMap() {
        accountDataFormat = new AccountDataFormat(this);
        return accountDataFormat.dataAsMap();
    }
}
