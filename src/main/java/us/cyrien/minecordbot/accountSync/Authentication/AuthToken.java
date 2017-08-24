package us.cyrien.minecordbot.accountSync.Authentication;

import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.entity.Player;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.accountSync.exceptions.IllegalConfirmKeyException;
import us.cyrien.minecordbot.accountSync.exceptions.IllegalConfirmRequesterException;
import us.cyrien.minecordbot.accountSync.exceptions.IllegalConfirmSessionIDException;

public class AuthToken implements Comparable {

    private String token;
    private Player mcAcc;
    private String sessionID;

    public AuthToken(String sessionID, Player mcAcc) {
        this.sessionID = sessionID;
        this.mcAcc = mcAcc;
        token = generateToken();
    }

    public AuthToken(Player mcAcc, AuthSession authSession) {
        this.sessionID = authSession.getSessionID();
        this.mcAcc = mcAcc;
        token = generateToken();
    }

    public AuthToken(Player mcAcc, String token){
        AuthManager authManager = Minecordbot.getInstance().getAuthManager();
        AuthSession authSession = authManager.getSession(token);
        this.mcAcc = mcAcc;
        this.token = token;
        this.sessionID = authSession.getSessionID();
    }

    private String generateToken() {
        token = RandomStringUtils.randomAlphanumeric(6);
        return token;
    }

    protected boolean authenticateToken(AuthToken token) throws IllegalConfirmRequesterException, IllegalConfirmKeyException, IllegalConfirmSessionIDException {
        if (!compareToken(token))
            return false;
        if(!this.toString().equals(token.toString())) {
            throw new IllegalConfirmKeyException();
        } else if (!this.getMcAcc().equals(token.getMcAcc())) {
            throw new IllegalConfirmRequesterException();
        } else if (!this.getSessionID().equals(token.getSessionID())) {
            throw new IllegalConfirmSessionIDException();
        }
        return true;
    }

    private void setToken(String s) {
        token = s;
    }

    public AuthToken getToken() {
        return this;
    }

    public boolean compareToken(AuthToken token) {
        return this.compareTo(token) > 0;
    }

    public String toString() {
        return token;
    }

    public Player getMcAcc() {
        return mcAcc;
    }

    public String getSessionID() {
        return sessionID;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof AuthToken || ((AuthToken) o).getToken() == this.getToken())
            return 1;
        return -1;
    }
}
