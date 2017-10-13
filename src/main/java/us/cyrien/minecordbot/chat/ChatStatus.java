package us.cyrien.minecordbot.chat;

public class ChatStatus {

    private boolean ismcmmoAdminChat;
    private boolean ismcmmoPartyChat;
    private boolean isCancelled;

    public ChatStatus() {
        ismcmmoPartyChat = false;
        ismcmmoPartyChat =false;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public boolean isIsmcmmoAdminChat() {

        return ismcmmoAdminChat;
    }

    public void setIsmcmmoAdminChat(boolean ismcmmoAdminChat) {
        this.ismcmmoAdminChat = ismcmmoAdminChat;
    }

    public boolean ismcmmopartychat() {
        return ismcmmoPartyChat;
    }

    public void setmcmmopartychat(boolean ismcmmopartychat) {
        this.ismcmmoPartyChat = ismcmmopartychat;
    }

    public void reset() {
        ismcmmoAdminChat = false;
        ismcmmoPartyChat = false;
        isCancelled = false;
    }

}
