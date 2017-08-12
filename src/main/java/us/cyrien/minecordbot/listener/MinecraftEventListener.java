package us.cyrien.minecordbot.listener;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.chat.Messenger;
import us.cyrien.minecordbot.handle.MinecraftMentionHandler;
import us.cyrien.minecordbot.localization.Localization;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.localization.MultiLangMessageParser;
import us.cyrien.minecordbot.prefix.PrefixParser;

import java.awt.*;

public class MinecraftEventListener implements Listener {

    private Minecordbot mcb;
    private Messenger messenger;
    private MultiLangMessageParser langMessageParser;
    private MinecraftMentionHandler mentionHandler;

    public MinecraftEventListener(Minecordbot mcb) {
        this.mcb = mcb;
        messenger = mcb.getMessenger();
        langMessageParser = new MultiLangMessageParser();
        mentionHandler = new MinecraftMentionHandler(mcb);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        TextChannel textChannel = mcb.getJDA().getTextChannelById(MCBConfig.get("mod_channel"));
        if (textChannel != null)
            sendMessageMod(e, textChannel);
        if (!e.isCancelled() && textChannel == null) {
            sendMessage(e);
        }
    }

    private void sendMessage(AsyncPlayerChatEvent e) {
        relay(e, null);
    }

    private void sendMessageMod(AsyncPlayerChatEvent e, TextChannel tc) {
        relay(e, tc);
    }

    private void relay(AsyncPlayerChatEvent e, TextChannel tc) {
        String msg = mentionHandler.handleMention(ChatColor.stripColor(e.getMessage()));
        String prefix = PrefixParser.parseMinecraftPrefix(MCBConfig.get("message_prefix_discord"), e);
        if (tc == null) {
            messenger.sendMessageToAllBoundChannel("**" + prefix + "** " + msg);
        } else {
            if (e.isCancelled())
                messenger.sendMessageToDiscord(tc, "\uD83D\uDD15 **" + prefix + "** " + msg);
            else {
                messenger.sendMessageToDiscord(tc, "**" + prefix + "** " + msg);
                messenger.sendMessageToAllBoundChannel("**" + prefix + "** " + msg);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        TextChannel textChannel = mcb.getJDA().getTextChannelById(MCBConfig.get("mod_channel"));
        String msg = Localization.getTranslatedMessage("mc.message.login");
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

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        TextChannel textChannel = mcb.getJDA().getTextChannelById(MCBConfig.get("mod_channel"));
        String msg = Localization.getTranslatedMessage("mc.message.logout");
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
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent e) {
        TextChannel textChannel = mcb.getJDA().getTextChannelById(MCBConfig.get("mod_channel"));
        String msg = "**" + ChatColor.stripColor(e.getPlayer().getName()) + "**: " + e.getMessage();
        if (textChannel != null) {
            EmbedBuilder eb = new EmbedBuilder().setColor(new Color(60, 92, 243));
            eb.addField("Command Pre-Execution", langMessageParser.parsePlayer(msg, ChatColor.stripColor(e.getPlayer().getName())), false);
            messenger.sendMessageEmbedToDiscord(textChannel, eb.build());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        boolean isDeathBroadCast = MCBConfig.getJSONObject("broadcasts").getBoolean("death_event");
        boolean allowIncog = MCBConfig.getJSONObject("broadcasts").getBoolean("hide_incognito_users");
        if (isDeathBroadCast) {
            if (allowIncog) {
                if (!event.getEntity().hasPermission("minecordbot.incognito")) {
                    sendDeathMessage(event);
                }
            } else
                sendDeathMessage(event);
        }
    }

    private void sendDeathMessage(PlayerDeathEvent event) {
        TextChannel textChannel = mcb.getJDA().getTextChannelById(MCBConfig.get("mod_channel"));
        Player player = event.getEntity();
        EntityDamageEvent.DamageCause deathCause = player.getLastDamageCause().getCause();
        if (player.getKiller() != null) {
            String msg = Localization.getTranslatedMessage("mc.deaths.pvp");
            msg = langMessageParser.parse(msg, player.getDisplayName(), deathCause.name());
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("wolf")) {
            String msg = Localization.getTranslatedMessage("mc.mobs.wolf");
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("ocelot")) {
            String msg = Localization.getTranslatedMessage("mc.mobs.ocelot");
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("pigman")) {
            String msg = Localization.getTranslatedMessage("mc.mobs.pigman");
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("zombie")) {
            String msg = Localization.getTranslatedMessage("mc.mobs.zombie");
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("skeleton")) {
            String msg = Localization.getTranslatedMessage("mc.mobs.skeleton");
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("cave spider")) {
            String msg = Localization.getTranslatedMessage("mc.mobs.cavespider");
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("spider")) {
            String msg = Localization.getTranslatedMessage("mc.mobs.spider");
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("silverfish")) {
            String msg = Localization.getTranslatedMessage("mc.mobs.silverfish");
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("slime")) {
            String msg = Localization.getTranslatedMessage("mc.mobs.slime");
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("blew up")) {
            String msg = Localization.getTranslatedMessage("mc.mobs.creeper");
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("enderman")) {
            String msg = Localization.getTranslatedMessage("mc.mobs.enderman");
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("ghast")) {
            String msg = Localization.getTranslatedMessage("mc.mobs.ghast");
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("blaze")) {
            String msg = Localization.getTranslatedMessage("mc.mobs.blaze");
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("ender dragon")) {
            String msg = Localization.getTranslatedMessage("mc.mobs.enderdragon");
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("wither skeleton")) {
            String msg = Localization.getTranslatedMessage("mc.mobs.witherskeleton");
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("wither")) {
            String msg = Localization.getTranslatedMessage("mc.mobs.wither");
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("anvil")) {
            String msg = Localization.getTranslatedMessage("mc.deaths.anvil");
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        } else if (deathCause == EntityDamageEvent.DamageCause.DROWNING) {
            String msg = Localization.getTranslatedMessage("mc.deaths.drowning");
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        } else if (deathCause == EntityDamageEvent.DamageCause.SUFFOCATION) {
            String msg = Localization.getTranslatedMessage("mc.deaths.suffocation");
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        } else if (deathCause == EntityDamageEvent.DamageCause.SUICIDE) {
            String msg = Localization.getTranslatedMessage("mc.deaths.suicide");
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        } else if (deathCause == EntityDamageEvent.DamageCause.FALL) {
            String msg = Localization.getTranslatedMessage("mc.deaths.fall");
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        } else if (deathCause == EntityDamageEvent.DamageCause.VOID) {
            String msg = Localization.getTranslatedMessage("mc.deaths.vod");
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        } else if (deathCause == EntityDamageEvent.DamageCause.LAVA) {
            String msg = Localization.getTranslatedMessage("mc.deaths.lava");
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        } else if (deathCause == EntityDamageEvent.DamageCause.FIRE) {
            String msg = Localization.getTranslatedMessage("mc.deaths.fire");
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        } else if (deathCause == EntityDamageEvent.DamageCause.CONTACT) {
            String msg = Localization.getTranslatedMessage("mc.deaths.cactus");
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        } else if (deathCause == EntityDamageEvent.DamageCause.WITHER) {
            String msg = Localization.getTranslatedMessage("mc.deaths.withers");
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        } else {
            String msg = ChatColor.stripColor(event.getDeathMessage());
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if(textChannel != null)
                messenger.sendMessageToDiscord(textChannel, "```css" + "\n[" + msg + "]\n```");
        }
    }
}

