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

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (safeToProceed(e)) {
            mcb.getBot().getUpdatableMap().get("list").update();
            SuperVanishHook svHook = HookContainer.getSuperVanishHook();
            String msg = ChatColor.stripColor(e.getQuitMessage());
            boolean isLeaveBroadcast = configsManager.getBroadcastConfig().getBoolean(BroadcastConfig.Nodes.PLAYER_QUIT);
            boolean seeQuit = configsManager.getModChannelConfig().getBoolean(ModChannelConfig.Nodes.SEE_PLAYER_QUIT);
            if (seeQuit) {
                String m = msg;
                if (svHook != null) {
                    boolean seeSV = configsManager.getModChannelConfig().getBoolean(ModChannelConfig.Nodes.SEE_SV);
                    if (VanishAPI.isInvisible(e.getPlayer()) || e.getQuitMessage().equals("Fake") && seeSV)
                        m = "(Vanish) " + m;
                }
                messenger.sendMessageEmbedToAllModChannel(new EmbedBuilder().setColor(LEAVE_COLOR)
                        .setTitle(m, null).build());
            }
            if (isLeaveBroadcast) {
                if (e.getQuitMessage().equals("Fake")) {
                    messenger.sendMessageEmbedToAllBoundChannel(new EmbedBuilder().setColor(LEAVE_COLOR)
                            .setTitle(msg, null).build());
                    e.setQuitMessage("");
                } else if (check(e)) {
                    messenger.sendMessageEmbedToAllBoundChannel(new EmbedBuilder().setColor(LEAVE_COLOR)
                            .setTitle(msg, null).build());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (safeToProceed(e)) {
            mcb.getBot().getUpdatableMap().get("list").update();
            SuperVanishHook svHook = HookContainer.getSuperVanishHook();
            String msg = ChatColor.stripColor(e.getJoinMessage());
            boolean isJoinBroadCast = configsManager.getBroadcastConfig().getBoolean(BroadcastConfig.Nodes.PLAYER_JOIN);
            boolean seeJoin = configsManager.getModChannelConfig().getBoolean(ModChannelConfig.Nodes.SEE_PLAYER_JOIN);
            if (seeJoin) {
                String m = msg;
                if (svHook != null) {
                    boolean seeSV = configsManager.getModChannelConfig().getBoolean(ModChannelConfig.Nodes.SEE_SV);
                    if (VanishAPI.isInvisible(e.getPlayer()) || e.getJoinMessage().equals("Fake") && seeSV)
                        m = "(Vanish) " + m;
                }
                messenger.sendMessageEmbedToAllModChannel(new EmbedBuilder().setColor(JOIN_COLOR)
                        .setTitle(m, null).build());
            }
            if (isJoinBroadCast) {
                if (e.getJoinMessage().equals("Fake")) {
                    messenger.sendMessageEmbedToAllBoundChannel(new EmbedBuilder().setColor(JOIN_COLOR)
                            .setTitle(msg, null).build());
                    e.setJoinMessage("");
                } else if (check(e)) {
                    messenger.sendMessageEmbedToAllBoundChannel(new EmbedBuilder().setColor(JOIN_COLOR)
                            .setTitle(msg, null).build());
                }
            }
        }
    }

    private boolean check(PlayerEvent e) {
        boolean allowIncog = configsManager.getBroadcastConfig().getBoolean(BroadcastConfig.Nodes.HIDE_INCOGNITO);
        if (isVanished(e.getPlayer())) {
            return false;
        } else if (allowIncog) {
            if (e.getPlayer().hasPermission("minecordbot.incognito")) {
                return false;
            }
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
        boolean safe = true;
        if (event instanceof PlayerJoinEvent) {
            if (((PlayerJoinEvent) event).getJoinMessage() == null) {
                safe = false;
            } else if (((PlayerJoinEvent) event).getJoinMessage().isEmpty()) {
                safe = false;
            }
            if(!safe)
                Logger.warn("The previous PlayerJoinEvent message was missing!");
        } else if (event instanceof PlayerQuitEvent) {
            if (((PlayerQuitEvent) event).getQuitMessage() == null) {
                safe = false;
            } else if (((PlayerQuitEvent) event).getQuitMessage().isEmpty()) {
                safe = false;
            }
            if(!safe)
                Logger.warn("The previous PlayerQuitEvent message was missing!");
        }
        return safe;
    }
}
