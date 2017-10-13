package us.cyrien.minecordbot.accountSync;

public enum DataKey {
    MC_USERNAME("Minecraft-Username"),
    DISCORD_ID("Discord-ID"),
    DISCORD_USERNAME("Discord-Username");

    private String s;

    DataKey(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }
}
    