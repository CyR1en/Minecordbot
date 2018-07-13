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
        processEvent(e);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent e) {
        processEvent(e);
    }

    private void processEvent(PlayerEvent event) {
        boolean j = event instanceof PlayerJoinEvent;
        Color c = j ? JOIN_COLOR : LEAVE_COLOR;
        mcb.getBot().getUpdatableMap().get("list").update();
        SuperVanishHook svHook = HookContainer.getSuperVanishHook();
        String msg = getMessage(j, event);
        boolean isBroadcast = j ? configsManager.getBroadcastConfig().getBoolean(BroadcastConfig.Nodes.PLAYER_JOIN) :
                configsManager.getBroadcastConfig().getBoolean(BroadcastConfig.Nodes.PLAYER_QUIT);
        boolean seeInModChannel = j ? configsManager.getModChannelConfig().getBoolean(ModChannelConfig.Nodes.SEE_PLAYER_JOIN) :
                configsManager.getModChannelConfig().getBoolean(ModChannelConfig.Nodes.SEE_PLAYER_QUIT);
        if (seeInModChannel && !event.getPlayer().hasPermission("minecordbot.incognito")) {
            String m = msg;
            if (svHook != null) {
                boolean seeSV = configsManager.getModChannelConfig().getBoolean(ModChannelConfig.Nodes.SEE_SV);
                if (VanishAPI.isInvisible(event.getPlayer()) || m.equals("Fake") && seeSV)
                    m = "(Vanish) " + m;
            }
            messenger.sendMessageEmbedToAllModChannel(new EmbedBuilder().setColor(c)
                    .setTitle(m, null).build());
        }
        if (isBroadcast) {
            if (msg.equals("Fake")) {
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

    public String getMessage(boolean isJoinEvent, PlayerEvent event) {
        if(safeToProceed(event))
            return isJoinEvent ? ChatColor.stripColor(((PlayerJoinEvent) event).getJoinMessage()) :
                    ChatColor.stripColor(((PlayerQuitEvent) event).getQuitMessage());
        else
            return isJoinEvent ? event.getPlayer().getName() + " joined the game" :
                    event.getPlayer().getName() + " left the game";
    }

    private boolean check(PlayerEvent e) {
        boolean hideIncog = configsManager.getBroadcastConfig().getBoolean(BroadcastConfig.Nodes.HIDE_INCOGNITO);
        if (isVanished(e.getPlayer())) {
            return false;
        } else if (hideIncog) {
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
        String msg = "The previous %s message was %s. Relaying default message.";
        if (event instanceof PlayerJoinEvent) {
            if (((PlayerJoinEvent) event).getJoinMessage() == null) {
                Logger.info(String.format(msg, event.getClass().getSimpleName(), "null"));
                return false;
            } else if (((PlayerJoinEvent) event).getJoinMessage().isEmpty()) {
                Logger.info(String.format(msg, event.getClass().getSimpleName(), "empty"));
                return false;
            } else {
                return true;
            }
        } else {
            if (((PlayerQuitEvent) event).getQuitMessage() == null) {
                Logger.info(String.format(msg, event.getClass().getSimpleName(), "null"));
                return false;
            } else if (((PlayerQuitEvent) event).getQuitMessage().isEmpty()) {
                Logger.info(String.format(msg, event.getClass().getSimpleName(), "empty"));
                return false;
            } else {
                return true;
            }
        }
    }
}
