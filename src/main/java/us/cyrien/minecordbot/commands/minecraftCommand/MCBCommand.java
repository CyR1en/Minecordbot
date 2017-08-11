package us.cyrien.minecordbot.commands.minecraftCommand;

import io.github.hedgehog1029.frame.annotations.Command;
import io.github.hedgehog1029.frame.annotations.Permission;
import io.github.hedgehog1029.frame.annotations.Sender;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import us.cyrien.minecordbot.configuration.MCBConfig;

public class MCBCommand {
    @Command(aliases = {"minecordbot", "mcb"}, usage = "do /minecordbot help", desc = "")
    @Permission("minecordbot.reload")
    public void command(@Sender CommandSender commandSender, String s) {
        if (s.equals("help")) {
            help(commandSender);
        } else if (s.equals("reload")) {
            MCBConfig.reload();
            commandSender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "MineCordBot Reloaded!");
        } else {
            help(commandSender);
        }
    }

    public boolean help(CommandSender cs) {
        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l=== MineCordBot Commands ==="));
        cs.sendMessage("/dme <action>");
        cs.sendMessage("/dcmd <action>");
        cs.sendMessage("/dsync <discord ID>");
        cs.sendMessage("/dconfirm <verification code>");
        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&0&7do \"/<command> help\" for more \n detailed command help"));
        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l==========================="));
        return false;
    }
}
