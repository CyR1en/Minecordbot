package us.cyrien.minecordbot.AccountSync.Authentication;

import net.dv8tion.jda.core.entities.Member;
import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.entity.Player;

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
    private Member DiscordAcc;
    private AuthToken authToken;
    private Status status;

    private final String id = RandomStringUtils.randomNumeric(6);

    public AuthSession(Player MCAcc, Member DiscordAcc) {
        this.MCAcc = MCAcc;
        this.DiscordAcc = DiscordAcc;
        authToken = new AuthToken(MCAcc, id);
        status = Status.PENDING;
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            status = Status.CANCELLED;
            scheduler.shutdownNow();
        }, 15, TimeUnit.MINUTES);
    }

    public Status getStatus() {
        return status;
    }

    public Player getMCAcc() {
        return MCAcc;
    }

    public Member getDiscordAcc() {
        return DiscordAcc;
    }

    public String getId() {
        return id;
    }

    public void authorize(String token) {
        AuthToken authToken1 = new AuthToken(token);
        boolean authenticated = authToken.authenticateToken(authToken1);
        status = authenticated ? Status.APPROVED : Status.DENIED;
    }
}
