package us.cyrien.minecordbot.chat.listeners.mcListeners;

import net.dv8tion.jda.core.EmbedBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import us.cyrien.minecordbot.Minecordbot;

import java.awt.*;

public class CommandListener extends MCBListener {

    public CommandListener(Minecordbot mcb) {
        super(mcb);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        boolean seeCommands = configsManager.getModChannelConfig().getBoolean("See_Commands");
        CommandSender s = e.getPlayer();
        String msg = "**" + ChatColor.stripColor(s.getName()) + "**: " + e.getMessage();
        if (seeCommands) {
            EmbedBuilder eb = new EmbedBuilder().setColor(new Color(60, 92, 243));
            eb.addField("Command-Event", msg, false);
            messenger.sendMessageEmbedToAllModChannel(eb.build());
        }
    }
}