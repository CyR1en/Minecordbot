package us.cyrien.minecordbot.commands.minecraftCommand;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import us.cyrien.mcutils.annotations.Command;
import us.cyrien.mcutils.annotations.Permission;
import us.cyrien.mcutils.annotations.Sender;
import us.cyrien.mcutils.annotations.Text;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.chat.Messenger;

public class Dcmd {
    @Command(aliases = "dcmd", usage = "/dcmd <discord command>", desc = "Do discord commands from minecraft")
    @Permission("minecordbot.dcmd")
    public void command(@Sender CommandSender sender, @Text String arg) {
        Messenger msg = new Messenger(Minecordbot.getInstance());
        msg.sendMessageToAllBoundChannel(arg);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lExecuting command."));
    }
}
