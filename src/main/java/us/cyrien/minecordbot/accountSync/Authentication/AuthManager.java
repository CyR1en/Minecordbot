package us.cyrien.minecordbot.accountSync.Authentication;

import java.util.HashMap;

public class AuthManager {

    private HashMap<String, AuthSession> authSessions;

    public AuthManager() {
        authSessions = new HashMap<>();
    }

    public AuthSession getSession(String authToken) {
        return authSessions.get(authToken);
    }

    public void addSession(AuthSession authSession) {
        authSessions.put(authSession.getAuthToken().toString(), authSession);
    }

    public void removeSession(String authToken) {
        authSessions.remove(authToken);
    }

    public void clearSessions() {
        authSessions.clear();
    }

    public HashMap<String, AuthSession> getAuthSessions() {
        return authSessions;
    }
}
