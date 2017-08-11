package us.cyrien.minecordbot.AccountSync;

public enum DataKey {
    MC_USERNAME("Minecraft-Username"),
    DISCORD_ID("Discord-ID"),
    DISCORD_USERNAME("Discord-Username"),
    SYNCED("Synced");

    private String s;

    DataKey(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }
}
    