package us.cyrien.minecordbot.commands.minecraftCommand;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import us.cyrien.mcutils.annotations.Command;
import us.cyrien.mcutils.annotations.Permission;
import us.cyrien.mcutils.annotations.Sender;
import us.cyrien.minecordbot.configuration.MCBConfig;

public class ExeDCommand {
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
        cs.sendMessage("/mcbsync <discord ID>");
        cs.sendMessage("/synconfirm <verification code>");
        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&0&7do \"/help <command>\" for more \n detailed command help"));
        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l==========================="));
        return false;
    }
}
