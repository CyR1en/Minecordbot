package us.cyrien.minecordbot.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.main.Minecordbot;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AfkListener implements Listener {

    private HashMap<Player, ScheduledExecutorService> players;
    private Minecordbot mcb;

    public AfkListener(Minecordbot mcb) {
        this.mcb = mcb;
        players = new HashMap<>();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event) {
        int timer = MCBConfig.get("afk_timer") != null ? MCBConfig.get("afk_timer") : MCBConfig.getDefault().getInt("afk_timer");
        Player p = event.getPlayer();
        System.out.println(p.getVelocity().getX() + " : " + p.getVelocity().getY() + " : " + p.getVelocity().getZ());
        if(p.getVelocity().equals(new Vector())) {
            players.put(p, Executors.newSingleThreadScheduledExecutor());
            System.out.println("Starting AFK Count Down for " + p.getDisplayName());
            try {
                players.get(p).scheduleWithFixedDelay(() -> {
                    mcb.getAFKPlayers().add(p);
                    System.out.println(p.getName() + " have been added to the afk list.");
                }, 0, timer, TimeUnit.SECONDS);
            } catch (Exception x) {
                x.printStackTrace();
            }
        } else if (players.containsKey(p) && p.getWalkSpeed() > 0) {
            players.remove(p);
            System.out.println(p.getName() + " have been removed to the afk list.");
            players.get(p).shutdownNow();
        }
    }
}
