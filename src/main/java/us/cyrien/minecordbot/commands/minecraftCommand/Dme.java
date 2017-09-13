package us.cyrien.minecordbot.commands.minecraftCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.cyrien.mcutils.annotations.Command;
import us.cyrien.mcutils.annotations.Permission;
import us.cyrien.mcutils.annotations.Sender;
import us.cyrien.mcutils.annotations.Text;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.chat.Messenger;

public class Dme {
    @Command(aliases = "dme", usage = "/dme <action>", desc = "/me command but for discord.")
    @Permission("minecordbot.dme")
    public void command(@Sender CommandSender sender, @Text String arg) {
        if(sender instanceof  Player) {
            Player p = (Player) sender;
            for (Player pl : Bukkit.getServer().getOnlinePlayers())
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5* " + "&r" + p.getDisplayName() + " &5" + arg));
            new Messenger(Minecordbot.getInstance()).sendMessageToAllBoundChannel("**" + p.getName() + "** " + "_" + arg + "_");
        } else {
            sender.sendMessage("Only players can do that execute /dme command");
        }
    }
}
