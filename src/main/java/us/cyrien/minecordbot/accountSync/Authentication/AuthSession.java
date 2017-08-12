package us.cyrien.minecordbot.accountSync.Authentication;

import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.utils.SimpleLog;
import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.JSONObject;
import us.cyrien.minecordbot.accountSync.Database;
import us.cyrien.minecordbot.accountSync.exceptions.IllegalConfirmKeyException;
import us.cyrien.minecordbot.accountSync.exceptions.IllegalConfirmRequesterException;
import us.cyrien.minecordbot.accountSync.exceptions.IllegalConfirmSessionIDException;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.entity.MCBUser;
import us.cyrien.minecordbot.entity.MCUser;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AuthSession {

    public static final SimpleLog SYNC_LOGGER = SimpleLog.getLog("MCBSync");

    private enum Status {
        PENDING,
        APPROVED,
        DENIED,
        CANCELLED
    }

    private Player MCAcc;
    private User DiscordAcc;
    private AuthToken authToken;
    private Status status;
    private AuthManager authManager;

    private final String sessionID = RandomStringUtils.randomNumeric(6);

    public AuthSession(Player mcAcc, User discordAcc, AuthManager authManager) {
        this.MCAcc = mcAcc;
        this.DiscordAcc = discordAcc;
        this.authManager = authManager;
        authToken = new AuthToken(mcAcc, sessionID);
        status = Status.PENDING;
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            if(status == Status.PENDING) {
                status = Status.CANCELLED;
                mcAcc.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[MCBSync] &cAccount sync cancelled!"));
            }
            scheduler.shutdownNow();
        }, 2, TimeUnit.MINUTES);
        authManager.addSession(this);
    }

    public AuthSession(Player mcAcc, User discordAcc) {
        this(mcAcc, discordAcc, Minecordbot.getAuthManager());
    }

    public Status getStatus() {
        return status;
    }

    public Player getMCAcc() {
        return MCAcc;
    }

    public User getDiscordAcc() {
        return DiscordAcc;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void authorize(Player sender, AuthToken token) {
        boolean authenticated = false;
        if(token.getMcAcc().getUniqueId().equals(sender.getUniqueId())) {
            try {
                authenticated = authToken.authenticateToken(token);
            } catch (IllegalConfirmRequesterException illegalConfirmRequester) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6[MCBSync] &c" + illegalConfirmRequester.getMsg()));
                System.out.println(illegalConfirmRequester.getMsg());
            } catch (IllegalConfirmKeyException illegalConfirmKey) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6[MCBSync] &c" + illegalConfirmKey.getMsg()));
                System.out.println(illegalConfirmKey.getMsg());
            } catch (IllegalConfirmSessionIDException illegalConfirmSessionID) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6[MCBSync] &c" + illegalConfirmSessionID.getMsg()));
                System.out.println(illegalConfirmSessionID.getMsg());
            }
        } else {
            try {
                throw new IllegalConfirmRequesterException();
            } catch (IllegalConfirmRequesterException illegalConfirmRequester) {
                sender.sendMessage("&6[MCBSync] &c" + illegalConfirmRequester.getMsg());
            }
        }
        status = authenticated ?  Status.APPROVED : Status.DENIED;
        if(status == Status.APPROVED) {
            getMCAcc().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[MCBSync] &rAccount sync &aapproved!"));
            System.out.println("Authentication Approved");
            MCUser mcUser = new MCUser(sender);
            MCBUser mcbUser = new MCBUser(authManager.getSession(this.getSessionID()).getDiscordAcc());
            mcUser.setMcbUser(mcbUser);
            Database.set(mcUser.getPlayer().getUniqueId().toString(), new JSONObject(mcUser.getDataAsMap()));
            authManager.removeSession(this.getSessionID());
        } else {
            getMCAcc().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[MCBSync] &rAccount sync &cdenied!"));
            authManager.removeSession(this.getSessionID());
            System.out.println("Authentication Denied");
        }
    }

    @Override
    public String toString() {
        return "Session by: " + getMCAcc().getDisplayName() +
                "\n Session ID: " + getSessionID() +
                "\n Sync request to " + getDiscordAcc() +
                "\n Status: " + getStatus();
    }
}
