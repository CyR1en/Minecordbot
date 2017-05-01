package us.cyrien.minecordbot.entity;

public class UpTimer {

    private long startTime;

    public UpTimer() {
        startTime = System.currentTimeMillis();
    }

    public String getCurrentUptime() {
        long currTime = System.currentTimeMillis();
        long diff = currTime - startTime;
        return (int) (diff / 86400000L) + "d " + (int)
                (diff / 3600000L % 24L) + "h " + (int)
                (diff / 60000L % 60L) + "m " + (int)
                (diff / 1000L % 60L) + "s";
    }
}
