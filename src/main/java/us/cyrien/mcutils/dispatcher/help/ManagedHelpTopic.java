package us.cyrien.mcutils.dispatcher.help;

import us.cyrien.mcutils.annotations.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpTopic;

public class ManagedHelpTopic extends HelpTopic {

    public ManagedHelpTopic(String name, Command command, String permission) {
        this.name = "/" + name;
        this.shortText = command.desc();
        this.fullText = ChatColor.GOLD + "Description: " + ChatColor.RESET + command.desc() +
                ChatColor.GOLD + "\nUsage: " + ChatColor.RESET + command.usage();
        this.amendedPermission = permission;
    }

    @Override
    public boolean canSee(CommandSender commandSender) {
        return commandSender.hasPermission(this.amendedPermission);
    }
}
