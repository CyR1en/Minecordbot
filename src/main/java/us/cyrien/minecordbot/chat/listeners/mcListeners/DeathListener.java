package us.cyrien.minecordbot.chat.listeners.mcListeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.localization.Locale;

public class DeathListener extends MCBListener {

    public DeathListener(Minecordbot mcb) {
        super(mcb);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        boolean isDeathBroadCast = configsManager.getBroadcastConfig().getBoolean("See_Player_Death");
        boolean allowIncog = configsManager.getBroadcastConfig().getBoolean("Hide_Incognito_Player");
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
        boolean bc = configsManager.getModChannelConfig().getBoolean("See_Player_Death");
        Player player = event.getEntity();
        EntityDamageEvent.DamageCause deathCause = player.getLastDamageCause().getCause();
        if (player.getKiller() != null) {
            String msg = Locale.getDeathMessage("deaths.pvp").finish();
            msg = langMessageParser.parse(msg, player.getDisplayName(), deathCause.name());
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel("```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("wolf")) {
            String msg = Locale.getMobMessage("wolf").finish();
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel("```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("ocelot")) {
            String msg = Locale.getMobMessage("ocelot").finish();
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel( "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("pigman")) {
            String msg = Locale.getMobMessage("pigzombie").finish();
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel( "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("zombie")) {
            String msg = Locale.getMobMessage("zombie").finish();
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel( "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("skeleton")) {
            String msg = Locale.getMobMessage("skeleton").finish();
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel( "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("cave spider")) {
            String msg = Locale.getMobMessage("cavespider").finish();
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel( "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("spider")) {
            String msg = Locale.getMobMessage("spider").finish();
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel( "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("silverfish")) {
            String msg = Locale.getMobMessage("silverfish").finish();
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel( "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("slime")) {
            String msg = Locale.getMobMessage("slime").finish();
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel( "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("blew up")) {
            String msg = Locale.getMobMessage("creeper").finish();
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel( "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("enderman")) {
            String msg = Locale.getMobMessage("enderman").finish();
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel( "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("ghast")) {
            String msg = Locale.getMobMessage("ghast").finish();
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel( "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("blaze")) {
            String msg = Locale.getMobMessage("blaze").finish();
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel( "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("ender dragon")) {
            String msg = Locale.getMobMessage("enderdragon").finish();
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel( "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("wither skeleton")) {
            String msg = Locale.getMobMessage("witherskeleton").finish();
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel( "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("wither")) {
            String msg = Locale.getMobMessage("wither").finish();
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel( "```css" + "\n[" + msg + "]\n```");
        } else if (event.getDeathMessage().toLowerCase().contains("anvil")) {
            String msg = Locale.getMobMessage("anvil").finish();
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel( "```css" + "\n[" + msg + "]\n```");
        } else if (deathCause == EntityDamageEvent.DamageCause.DROWNING) {
            String msg = Locale.getDeathMessage("drowning").finish();
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel( "```css" + "\n[" + msg + "]\n```");
        } else if (deathCause == EntityDamageEvent.DamageCause.SUFFOCATION) {
            String msg = Locale.getDeathMessage("suffocation").finish();
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel( "```css" + "\n[" + msg + "]\n```");
        } else if (deathCause == EntityDamageEvent.DamageCause.SUICIDE) {
            String msg = Locale.getDeathMessage("suicide").finish();
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel( "```css" + "\n[" + msg + "]\n```");
        } else if (deathCause == EntityDamageEvent.DamageCause.FALL) {
            String msg = Locale.getDeathMessage("fall").finish();
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel( "```css" + "\n[" + msg + "]\n```");
        } else if (deathCause == EntityDamageEvent.DamageCause.VOID) {
            String msg = Locale.getDeathMessage("void").finish();
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel( "```css" + "\n[" + msg + "]\n```");
        } else if (deathCause == EntityDamageEvent.DamageCause.LAVA) {
            String msg = Locale.getDeathMessage("lava").finish();
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel( "```css" + "\n[" + msg + "]\n```");
        } else if (deathCause == EntityDamageEvent.DamageCause.FIRE) {
            String msg = Locale.getDeathMessage("fire").finish();
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel( "```css" + "\n[" + msg + "]\n```");
        } else if (deathCause == EntityDamageEvent.DamageCause.CONTACT) {
            String msg = Locale.getDeathMessage("cactus").finish();
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel( "```css" + "\n[" + msg + "]\n```");
        } else if (deathCause == EntityDamageEvent.DamageCause.WITHER) {
            String msg = Locale.getDeathMessage("withers").finish();
            msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel( "```css" + "\n[" + msg + "]\n```");
        } else {
            String msg = ChatColor.stripColor(event.getDeathMessage());
            messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            if (bc)
                messenger.sendMessageToAllModChannel( "```css" + "\n[" + msg + "]\n```");
        }
    }
}
