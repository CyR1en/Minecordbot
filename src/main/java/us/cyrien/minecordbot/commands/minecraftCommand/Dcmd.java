package us.cyrien.minecordbot.commands.minecraftCommand;

import io.github.hedgehog1029.frame.annotations.Command;
import io.github.hedgehog1029.frame.annotations.Permission;
import io.github.hedgehog1029.frame.annotations.Sender;
import io.github.hedgehog1029.frame.annotations.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import us.cyrien.minecordbot.chat.Messenger;
import us.cyrien.minecordbot.Minecordbot;

public class Dcmd {
    @Command(aliases = "dcmd", usage = "/dcmd <discord command>", desc = "Do discord commands from minecraft")
    @Permission("minecordbot.dcmd")
    public void command(@Sender CommandSender sender, @Text String arg) {
        Messenger msg = new Messenger(Minecordbot.getInstance());
        msg.sendMessageToAllBoundChannel(arg);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lExecuting command."));
    }
}
