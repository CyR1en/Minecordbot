package us.cyrien.minecordbot.chat.listeners.mcListeners;

import de.myzelyam.api.vanish.PlayerHideEvent;
import de.myzelyam.api.vanish.PlayerShowEvent;
import de.myzelyam.api.vanish.VanishAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.localization.Locale;

public class SuperVanishListener extends MCBListener {

    public SuperVanishListener(Minecordbot mcb) {
        super(mcb);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerHide(PlayerHideEvent event) {
        if(VanishAPI.getConfiguration().getBoolean("Configuration.Messages.VanishReappearMessages.BroadcastMessageOnVanish")) {
            PlayerQuitEvent playerQuitEvent = new PlayerQuitEvent(event.getPlayer(), Locale.getMcMessage("logout").finish());
            playerQuitEvent.setQuitMessage("Fake");
            Bukkit.getServer().getPluginManager().callEvent(playerQuitEvent);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerShow(PlayerShowEvent event) {
        if(VanishAPI.getConfiguration().getBoolean("Configuration.Messages.VanishReappearMessages.BroadcastMessageOnReappear")) {
            PlayerJoinEvent playerJoinEvent = new PlayerJoinEvent(event.getPlayer(), Locale.getMcMessage("login").finish());
            playerJoinEvent.setJoinMessage("Fake");
            Bukkit.getServer().getPluginManager().callEvent(playerJoinEvent);
        }
    }

}
