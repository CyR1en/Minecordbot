package us.cyrien.minecordbot.entity;

import net.dv8tion.jda.core.entities.User;
import org.bukkit.entity.Player;
import us.cyrien.minecordbot.accountSync.AccountDataFormat;
import us.cyrien.minecordbot.utils.FinderUtil;

import java.util.LinkedHashMap;

public class UnifiedUser {

    private Player p;
    private MCBUser mcbUser;
    private AccountDataFormat accountDataFormat;

    public UnifiedUser(Player p) {
        this.p = p;
        User user = FinderUtil.findUserInDatabase(p);
        this.mcbUser = user == null ? null : new MCBUser(user);
    }

    public UnifiedUser(Player p, MCBUser mcbUser) {
        this.p = p;
        this.mcbUser = mcbUser;
    }

    public void setMcbUser(MCBUser mcbUser) {
        this.mcbUser = mcbUser;
    }

    public boolean isSynced() {
        return mcbUser != null && p != null;
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
