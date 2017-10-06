package us.cyrien.minecordbot.commands.minecraftCommand;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import us.cyrien.mcutils.annotations.Command;
import us.cyrien.mcutils.annotations.Permission;
import us.cyrien.mcutils.annotations.Sender;
import us.cyrien.minecordbot.Minecordbot;

public class McbCommands {
    @Command(aliases = {"minecordbot", "mcb"}, usage = "do /minecordbot help", desc = "")
    @Permission("minecordbot.reload")
    public void command(@Sender CommandSender commandSender, String s) {
        if (s.equalsIgnoreCase("help")) {
            help(commandSender);
        } else if (s.equalsIgnoreCase("reload")) {
            Minecordbot.getInstance().getMcbConfigsManager().reloadAllConfig();
            Minecordbot.getInstance().getMcbConfigsManager().setupConfigurations();
            commandSender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "MineCordBot Reloaded!");
        } else if(s.equalsIgnoreCase("start")){
            Minecordbot.getInstance().getBot().start();
        } else {
            help(commandSender);
        }
    }

    public boolean help(CommandSender cs) {
        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l=== MineCordBot Commands ==="));
        cs.sendMessage("/dme <action>");
        cs.sendMessage("/dcmd <action>");
        cs.sendMessage("/mcbsync <discord ID>");
        cs.sendMessage("/syncconfirm <verification code>");
        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&0&7do \"/help <command>\" for more \n detailed command help"));
        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l==========================="));
        return false;
    }
}
