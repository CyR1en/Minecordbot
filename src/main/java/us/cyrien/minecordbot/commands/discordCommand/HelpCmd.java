package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.User;
import us.cyrien.minecordbot.Bot;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.localization.Locale;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class HelpCmd extends MCBCommand {

    public HelpCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "help";
        this.help = Locale.getCommandsMessage("help.description").finish();
        this.category = Bot.HELP;
    }

    @Override
    protected void doCommand(CommandEvent e) {
        EmbedBuilder eb = new EmbedBuilder();
        JDA jda = getMcb().getBot().getJda();
        Color color = Bot.BOT_COLOR; //e.getGuild().getMember(jda.getSelfUser()).getColor()
        eb.setColor(color);
        eb.setDescription(Locale.getCommandsMessage("help.more").f(e.getClient().getPrefix()));
        if (e.getArgs().isEmpty()) {
            eb.setAuthor("Minecordbot Commands", null, null);
            eb.setThumbnail("https://vectr.com/cyrien/k3vhJlcOMS.png?width=168&height=168&select=k3vhJlcOMSpage0");
            eb = listCommands(eb);
            User user = jda.getUserById("193970511615623168");
            if (user != null) {
                eb.setFooter("Questions? contact " + user.getName() + "#" + user.getDiscriminator() + " or join https://discord.cyrien.us", user.getAvatarUrl());
                respond(e, eb.build());
            } else {
                eb.setFooter("- C Y R I \u039E N -", "https://yt3.ggpht.com/-uuXItiIhgcU/AAAAAAAAAAI/AAAAAAAAAAA/3xzbfTTz9oU/s88-c-k-no-mo-rj-c0xffffff/photo.jpg");
            }
        }
    }

    private EmbedBuilder listCommands(EmbedBuilder ebi) {
        Command.Category[] categories = {Bot.ADMIN, Bot.FUN, Bot.HELP,
                Bot.INFO, Bot.MISC, Bot.OWNER};
        for (int i = 0; i <= categories.length - 1; i++) {
            StringBuilder str = new StringBuilder();
            if (getAllCommandsWithCategoryOf(categories[i]).size() != 0) {
                for (Command c : getAllCommandsWithCategoryOf(categories[i])) {
                    str.append(getMcb().getBot().getClient().getPrefix()).append(c.getName())
                            .append(c.getArguments() == null ? "" : " " + c.getArguments())
                            .append(" - ").append(c.getHelp()).append("\n");
                }
                ebi.addField(" - " + categories[i].getName(), str.toString(), false);
            }
        }
        return ebi;
    }

    private java.util.List<MCBCommand> getAllCommandsWithCategoryOf(Command.Category category) {
        ArrayList<MCBCommand> commands = new ArrayList<>();
        for (Command c : getMcb().getBot().getClient().getCommands()) {
            if (c.getCategory().equals(category))
                commands.add((MCBCommand) c);
        }
        Collections.sort(commands);
        return commands;
    }
}
