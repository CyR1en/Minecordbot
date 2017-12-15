package us.cyrien.minecordbot.entity;

import org.bukkit.Bukkit;
import us.cyrien.minecordbot.Minecordbot;

import java.util.concurrent.TimeUnit;

public class UpTimer{

    private long startTime;

    public UpTimer(Minecordbot mcb) {
        startTime = System.currentTimeMillis();
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(mcb, new Lag(), 100L, 1L);
        mcb.getScheduler().scheduleWithFixedDelay(() -> mcb.getBot().getUpdatableMap()
                .get("tps").update(), 0, 5, TimeUnit.SECONDS);
    }

    public double getCurrentTps() {
        double tps = Lag.getTPS();
        if(tps > 20.0D)
            tps = 20.0000D;
        return tps;
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
