package us.cyrien.minecordbot.AccountSync.Authentication;

import net.dv8tion.jda.core.entities.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.cyrien.minecordbot.AccountSync.AuthManager;
import us.cyrien.minecordbot.AccountSync.exceptions.IllegalConfirmKeyException;
import us.cyrien.minecordbot.AccountSync.exceptions.IllegalConfirmRequesterException;
import us.cyrien.minecordbot.AccountSync.exceptions.IllegalConfirmSessionIDException;
import us.cyrien.minecordbot.main.Minecordbot;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AuthSession {

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

    private final String sessionID = RandomStringUtils.randomNumeric(32);

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
                sender.sendMessage("&6[MCBSync] &c" + illegalConfirmRequester.getMsg());
                System.out.println(illegalConfirmRequester.getMsg());
            } catch (IllegalConfirmKeyException illegalConfirmKey) {
                sender.sendMessage("&6[MCBSync] &c" + illegalConfirmKey.getMsg());
                System.out.println(illegalConfirmKey.getMsg());
            } catch (IllegalConfirmSessionIDException illegalConfirmSessionID) {
                sender.sendMessage("&6[MCBSync] &c" + illegalConfirmSessionID.getMsg());
                System.out.println(illegalConfirmSessionID.getMsg());
            }
        } else {
            try {
                throw new IllegalConfirmRequesterException();
            } catch (IllegalConfirmRequesterException illegalConfirmRequester) {
                sender.sendMessage("&6[MCBSync] &c" + illegalConfirmRequester.getMsg());
            }
        }
        status = authenticated == true ?  Status.APPROVED : Status.DENIED;
        if(status == Status.APPROVED) {
            getMCAcc().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[MCBSync] &rAccount sync &aapproved!"));
            authManager.removeSession(this.getSessionID());
            System.out.println("Authentication Approved");
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
