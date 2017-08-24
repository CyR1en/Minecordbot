package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.chat.DiscordCommandSender;
import us.cyrien.minecordbot.chat.DiscordConsoleCommandSender;
import us.cyrien.minecordbot.commands.DiscordCommand;
import us.cyrien.minecordbot.localization.Localization;
import us.cyrien.minecordbot.utils.FinderUtil;

public class MCCommand extends DiscordCommand {

    public MCCommand(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "minecraftcommand";
        this.aliases = new String[]{"mccommand, mcmd"};
        this.arguments = "<Command> [arguments]";
        this.help = Localization.getTranslatedMessage("mcb.commands.mcmd.description");
        this.category = minecordbot.MISC;
    }

    @Override
    protected void doCommand(CommandEvent e) {
        if(e.getArgs().isEmpty()) {
            respond(e, getHelpCard(e));
            return;
        }
        String arg = e.getArgs();
        String[] args = arg.split("\\s");
        Player p = FinderUtil.findPlayerInDatabase(e.getMember().getUser().getId());
        CommandSender commandSender;
        if(p == null)
            if(args[0].equalsIgnoreCase("help"))
                commandSender = new DiscordCommandSender(e);
            else
                commandSender = new DiscordConsoleCommandSender(e);
        else
            commandSender = p;
        Bukkit.getServer().dispatchCommand(commandSender, arg);
    }
}
