package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.utils.PermissionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.cyrien.minecordbot.Bot;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.chat.entity.*;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.localization.Locale;
import us.cyrien.minecordbot.utils.SearchUtil;

import java.io.PrintWriter;
import java.io.StringWriter;

public class MCCommandCmd extends MCBCommand {

    public MCCommandCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "minecraftcommand";
        this.aliases = new String[]{"mcmd"};
        this.arguments = "<Command> [argument]...";
        this.help = Locale.getCommandsMessage("mcmd.description").finish();
        this.category = Bot.MISC;
    }

    @Override
    protected void doCommand(CommandEvent e) {
        if (e.getArgs().isEmpty()) {
            respond(e, getHelpCard(e, this));
            return;
        }
        String arg = e.getArgs();
        String[] args = arg.split("\\s");
        Player p = SearchUtil.findPlayerInDatabase(e.getMember().getUser().getId());
        CommandSender commandSender;
        boolean allowed = false;
        if (checkPermission(e.getMember())) {
            allowed = true;
        } else if ((PermissionUtil.checkPermission(e.getTextChannel(), e.getMember(), Permission.ADMINISTRATOR))) {
            allowed = true;
        } else if (e.getAuthor().getId().equals(e.getClient().getOwnerId())) {
            allowed = true;
        }
        if (p == null && allowed) {
            commandSender = new DiscordConsoleCommandSender(e);
            Bukkit.getScheduler().runTaskLater(mcb, () -> Bukkit.getServer().dispatchCommand(commandSender, arg), 1L);
        } else if (p != null) {
            commandSender = new DiscordPlayerCommandSender(p, e);
            Bukkit.getScheduler().runTaskLater(mcb, () -> {
                try {
                    Bukkit.getServer().dispatchCommand(commandSender, arg);
                } catch (CommandException ex) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    ex.printStackTrace(pw);
                    String ex1 = sw.toString();
                    if (ex1.contains("Caused by: java.lang.ClassCastException:")) {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setDescription(Locale.getCommandsMessage("mcmd.cannot-cast").finish());
                        respond(e, embedMessage(e, eb.build(), ResponseLevel.LEVEL_3));
                    } else {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setDescription(Locale.getCommandsMessage("mcmd.error").finish());
                        respond(e, embedMessage(e, eb.build(), ResponseLevel.LEVEL_3));
                    }
                } catch (Exception ex) {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setDescription(Locale.getCommandsMessage("mcmd.error").finish());
                    respond(e, embedMessage(e, eb.build(), ResponseLevel.LEVEL_3));
                }
            }, 1L);
        } else {
            respond(e, noPermissionMessageEmbed());
        }
    }
}
