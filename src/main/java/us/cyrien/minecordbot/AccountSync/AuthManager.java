package us.cyrien.minecordbot.AccountSync;

import us.cyrien.minecordbot.AccountSync.Authentication.AuthSession;

import java.util.HashMap;

public class AuthManager {

    private HashMap<String, AuthSession> authSessions;

    public AuthManager() {
        authSessions = new HashMap<>();
    }

    public AuthSession getSession(String authSessionID) {
        return authSessions.get(authSessionID);
    }

    public void addSession(AuthSession authSession) {
        authSessions.put(authSession.getSessionID(), authSession);
    }

    public void removeSession(AuthSession authSession) {
        authSessions.remove(authSession);
    }

    public void removeSession(String sessionID) {
        authSessions.forEach((authSessionID, authToken) -> {
            if(authSessionID.equals(sessionID)) {
                authSessions.remove(authSessionID);
            }
        });
    }

    public void clearSessions() {
        authSessions.clear();
    }

    public HashMap<String, AuthSession> getAuthSessions() {
        return authSessions;
    }
}
