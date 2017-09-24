package us.cyrien.minecordbot.chat.listeners.mcListeners;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.configuration.MCBConfig;

import java.awt.*;

public class CommandListener extends MCBListener {

    public CommandListener(Minecordbot mcb) {
        super(mcb);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        CommandSender s = e.getPlayer();
        TextChannel textChannel = mcb.getJDA().getTextChannelById(MCBConfig.get("mod_channel"));
        String msg = "**" + ChatColor.stripColor(s.getName()) + "**: " + e.getMessage();
        if (textChannel != null) {
            EmbedBuilder eb = new EmbedBuilder().setColor(new Color(60, 92, 243));
            eb.addField("Command-Event", langMessageParser.parsePlayer(msg, ChatColor.stripColor(s.getName())), false);
            messenger.sendMessageEmbedToDiscord(textChannel, eb.build());
        }
    }
}
