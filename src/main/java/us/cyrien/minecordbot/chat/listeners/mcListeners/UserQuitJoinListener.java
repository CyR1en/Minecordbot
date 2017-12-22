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
            process(e);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (safeToProceed(e)) {
            process(e);
        }
    }

    private boolean check(PlayerEvent e) {
        boolean allowIncog = configsManager.getBroadcastConfig().getBoolean("Hide_Incognito_Player");
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
        boolean safe = false;
        if (event instanceof PlayerJoinEvent) {
            if (((PlayerJoinEvent) event).getJoinMessage() != null && !((PlayerJoinEvent) event).getJoinMessage().isEmpty()) {
                safe = true;
                Logger.warn("The previous PlayerJoinEvent message was missing!");
            }
        } else if (event instanceof PlayerQuitEvent) {
            if (((PlayerQuitEvent) event).getQuitMessage() != null && !((PlayerQuitEvent) event).getQuitMessage().isEmpty()) {
                safe = true;
                Logger.warn("The previous PlayerQuitEvent message was missing!");
            }
        }
        return safe;
    }

    private void process(PlayerEvent event) {
        SuperVanishHook svHook = HookContainer.getSuperVanishHook();
        String msg;
        boolean isBroadCast;
        boolean seeInModChannel;
        Color color;
        if(event instanceof PlayerJoinEvent) {
            msg = ChatColor.stripColor(((PlayerJoinEvent)event).getJoinMessage());
            isBroadCast = configsManager.getBroadcastConfig().getBoolean("See_Player_Join");
            seeInModChannel = configsManager.getModChannelConfig().getBoolean("See_Player_Join");
            color = JOIN_COLOR;
            if(msg.equals("Fake"))
                ((PlayerJoinEvent)event).setJoinMessage("");
        } else {
            msg = ChatColor.stripColor(((PlayerQuitEvent)event).getQuitMessage());
            isBroadCast = configsManager.getBroadcastConfig().getBoolean("See_Player_Join");
            seeInModChannel = configsManager.getModChannelConfig().getBoolean("See_Player_Join");
            color = LEAVE_COLOR;
            if(msg.equals("Fake"))
                ((PlayerQuitEvent)event).setQuitMessage("");
        }
        if (seeInModChannel) {
            String m = msg;
            if (svHook != null) {
                boolean seeSV = configsManager.getModChannelConfig().getBoolean("See_SV");
                if (VanishAPI.isInvisible(event.getPlayer()) || msg.equals("Fake") && seeSV)
                    m = "(Vanish) " + m;
            }
            messenger.sendMessageEmbedToAllModChannel(new EmbedBuilder().setColor(color)
                    .setTitle(m, null).build());
        }
        if (isBroadCast) {
            if (msg.equals("Fake"))
                messenger.sendMessageEmbedToAllBoundChannel(new EmbedBuilder().setColor(color)
                        .setTitle(msg, null).build());
            else if (check(event))
                messenger.sendMessageEmbedToAllBoundChannel(new EmbedBuilder().setColor(color)
                        .setTitle(msg, null).build());
        }
        mcb.getBot().updateUpdatable("list");
    }
}
