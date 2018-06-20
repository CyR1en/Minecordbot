package us.cyrien.minecordbot.chat.listeners.mcListeners;

import de.myzelyam.api.vanish.VanishAPI;
import net.dv8tion.jda.core.EmbedBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.MetadataValue;
import us.cyrien.mcutils.logger.Logger;
import us.cyrien.minecordbot.HookContainer;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.configuration.BroadcastConfig;
import us.cyrien.minecordbot.configuration.ModChannelConfig;
import us.cyrien.minecordbot.hooks.SuperVanishHook;

import java.awt.*;

public class UserQuitJoinListener extends MCBListener {

    private static final Color JOIN_COLOR = new Color(92, 184, 92);
    private static final Color LEAVE_COLOR = new Color(243, 119, 54);

    public UserQuitJoinListener(Minecordbot mcb) {
        super(mcb);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (safeToProceed(e)) {
            processEvent(e);
        } else {
            e.setQuitMessage(e.getPlayer().getName() + " left the game");
            processEvent(e);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (safeToProceed(e)) {
            processEvent(e);
        } else {
            e.setJoinMessage(e.getPlayer().getName() + " joined the game");
            processEvent(e);
        }
    }

    private void processEvent(PlayerEvent event) {
        boolean j = event instanceof PlayerJoinEvent;
        Color c = j ? JOIN_COLOR : LEAVE_COLOR;
        mcb.getBot().getUpdatableMap().get("list").update();
        SuperVanishHook svHook = HookContainer.getSuperVanishHook();
        String msg = j ? ChatColor.stripColor(((PlayerJoinEvent) event).getJoinMessage()) :
                ChatColor.stripColor(((PlayerQuitEvent) event).getQuitMessage());
        boolean isBroadCast = j ? configsManager.getBroadcastConfig().getBoolean(BroadcastConfig.Nodes.PLAYER_JOIN) :
                configsManager.getBroadcastConfig().getBoolean(BroadcastConfig.Nodes.PLAYER_QUIT);
        boolean see = j ? configsManager.getModChannelConfig().getBoolean(ModChannelConfig.Nodes.SEE_PLAYER_JOIN) :
                configsManager.getModChannelConfig().getBoolean(ModChannelConfig.Nodes.SEE_PLAYER_QUIT);
        if (see) {
            String m = msg;
            if (svHook != null) {
                boolean seeSV = configsManager.getModChannelConfig().getBoolean(ModChannelConfig.Nodes.SEE_SV);
                String message = j ? ((PlayerJoinEvent) event).getJoinMessage() : ((PlayerQuitEvent) event).getQuitMessage();
                if (VanishAPI.isInvisible(event.getPlayer()) || message.equals("Fake") && seeSV)
                    m = "(Vanish) " + m;
            }
            messenger.sendMessageEmbedToAllModChannel(new EmbedBuilder().setColor(c)
                    .setTitle(m, null).build());
        }
        if (isBroadCast) {
            String message = j ? ((PlayerJoinEvent) event).getJoinMessage() : ((PlayerQuitEvent) event).getQuitMessage();
            if (message.equals("Fake")) {
                messenger.sendMessageEmbedToAllBoundChannel(new EmbedBuilder().setColor(c)
                        .setTitle(msg, null).build());
                if (j)
                    ((PlayerJoinEvent) event).setJoinMessage("");
                else
                    ((PlayerQuitEvent) event).setQuitMessage("");
            } else if (check(event)) {
                messenger.sendMessageEmbedToAllBoundChannel(new EmbedBuilder().setColor(c)
                        .setTitle(msg, null).build());
            }
        }
    }

    private boolean check(PlayerEvent e) {
        boolean allowIncog = configsManager.getBroadcastConfig().getBoolean(BroadcastConfig.Nodes.HIDE_INCOGNITO);
        if (isVanished(e.getPlayer())) {
            return false;
        } else if (allowIncog) {
            return !e.getPlayer().hasPermission("minecordbot.incognito");
        }
        return true;
    }

    private boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }

    private boolean safeToProceed(PlayerEvent event) {
        if (event instanceof PlayerJoinEvent) {
            if (((PlayerJoinEvent) event).getJoinMessage() == null) {
                Logger.warn("The previous PlayerJoinEvent message was null! Changing message to default.");
                return false;
            } else if (((PlayerJoinEvent) event).getJoinMessage().isEmpty()) {
                Logger.warn("The previous PlayerJoinEvent message was missing! Changing message to default.");
                return false;
            } else {
                return true;
            }
        } else {
            if (((PlayerQuitEvent) event).getQuitMessage() == null) {
                Logger.warn("The previous PlayerQuitEvent message was null! Changing message to default.");
                return false;
            } else if (((PlayerQuitEvent) event).getQuitMessage().isEmpty()) {
                Logger.warn("The previous PlayerQuitEvent message was missing! Changing message to default.");
                return false;
            } else {
                return true;
            }
        }
    }
}
