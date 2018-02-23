package us.cyrien.minecordbot.chat.listeners.mcListeners;

import net.dv8tion.jda.core.EmbedBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import us.cyrien.minecordbot.Bot;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.configuration.ModChannelConfig;

import java.awt.*;

public class CommandListener extends MCBListener {

    public CommandListener(Minecordbot mcb) {
        super(mcb);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        boolean seeCommands = configsManager.getModChannelConfig().getBoolean(ModChannelConfig.Nodes.SEE_COMMADS);
        CommandSender s = e.getPlayer();
        String msg = "**" + ChatColor.stripColor(s.getName()) + "**: " + e.getMessage();
        if (seeCommands) {
            Color color = Bot.BOT_COLOR;
            EmbedBuilder eb = new EmbedBuilder().setColor(color);
            eb.addField("Command-Event", msg, false);
            messenger.sendMessageEmbedToAllModChannel(eb.build());
        }
    }
}