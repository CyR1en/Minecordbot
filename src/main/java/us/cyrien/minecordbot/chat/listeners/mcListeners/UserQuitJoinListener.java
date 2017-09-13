package us.cyrien.minecordbot.chat.listeners.mcListeners;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.localization.Locale;

import java.awt.*;

public class UserQuitJoinListener extends MCBListener{

    public UserQuitJoinListener(Minecordbot mcb) {
        super(mcb);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        TextChannel textChannel = mcb.getJDA().getTextChannelById(MCBConfig.get("mod_channel"));
        String msg = Locale.getMcMessage("logout").finish();
        boolean isLeaveBroadCast = MCBConfig.getJSONObject("broadcasts").getBoolean("leave_event");
        if (textChannel != null)
            messenger.sendMessageEmbedToDiscord(textChannel, new EmbedBuilder().setColor(new Color(243, 119, 54))
                    .setTitle(langMessageParser.parsePlayer(msg, ChatColor.stripColor(e.getPlayer().getName())), null).build());
        if (isLeaveBroadCast) {
            boolean allowIncog = MCBConfig.getJSONObject("broadcasts").getBoolean("hide_incognito_users");
            if (allowIncog) {
                if (!e.getPlayer().hasPermission("minecordbot.incognito")) {
                    messenger.sendMessageEmbedToAllBoundChannel(new EmbedBuilder().setColor(new Color(243, 119, 54))
                            .setTitle(langMessageParser.parsePlayer(msg, ChatColor.stripColor(e.getPlayer().getName())), null).build());
                }
            } else {
                messenger.sendMessageEmbedToAllBoundChannel(new EmbedBuilder().setColor(new Color(243, 119, 54))
                        .setTitle(langMessageParser.parsePlayer(msg, ChatColor.stripColor(e.getPlayer().getName())), null).build());
            }
        }

    }



    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        TextChannel textChannel = mcb.getJDA().getTextChannelById(MCBConfig.get("mod_channel"));
        String msg = Locale.getMcMessage("login").finish();
        boolean isJoinBroadCast = MCBConfig.getJSONObject("broadcasts").getBoolean("join_event");
        if (textChannel != null)
            messenger.sendMessageEmbedToDiscord(textChannel, new EmbedBuilder().setColor(new Color(92, 184, 92))
                    .setTitle(langMessageParser.parsePlayer(msg, ChatColor.stripColor(e.getPlayer().getName())), null).build());
        if (isJoinBroadCast) {
            boolean allowIncog = MCBConfig.getJSONObject("broadcasts").getBoolean("hide_incognito_users");
            if (allowIncog) {
                if (!e.getPlayer().hasPermission("minecordbot.incognito")) {
                    messenger.sendMessageEmbedToAllBoundChannel(new EmbedBuilder().setColor(new Color(92, 184, 92))
                            .setTitle(langMessageParser.parsePlayer(msg, ChatColor.stripColor(e.getPlayer().getName())), null).build());
                }
            } else {
                messenger.sendMessageEmbedToAllBoundChannel(new EmbedBuilder().setColor(new Color(92, 184, 92))
                        .setTitle(langMessageParser.parsePlayer(msg, ChatColor.stripColor(e.getPlayer().getName())), null).build());
            }
        }
    }
}
