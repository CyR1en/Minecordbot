package us.cyrien.minecordbot.commands.discordCommand;

import us.cyrien.jdautilities.commandclient.Command;
import us.cyrien.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.User;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.localization.Locale;

import java.util.ArrayList;
import java.util.Collections;

public class HelpCmd extends MCBCommand {

    public HelpCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "help";
        this.help = Locale.getCommandsMessage("help.description").finish();
        this.category = minecordbot.HELP;
    }

    @Override
    protected void doCommand(CommandEvent e) {
        EmbedBuilder eb = new EmbedBuilder();
        JDA jda = getMinecordbot().getJDA();
        eb.setColor(e.getGuild().getMember(jda.getSelfUser()).getColor());
        eb.setDescription(Locale.getCommandsMessage("help.more").format(e.getClient().getPrefix()));
        if(e.getArgs().isEmpty()) {
            eb.setAuthor("MineCordBot Commands", "https://dev.bukkit.org/projects/minecordbot-bukkit", "https://media-elerium.cursecdn.com/attachments/thumbnails/124/611/310/172/minecord.png" );
            eb = listCommands(eb);
            User user = jda.getUserById("193970511615623168");
            if (user != null) {
                eb.setFooter("For more help, contact CyRien#9503 or join https://discord.gg/rEK5XmV", user.getAvatarUrl());
                respond(e, eb.build());
            }
        }
    }

    private EmbedBuilder listCommands(EmbedBuilder ebi) {
        Command.Category[] categories = {getMinecordbot().ADMIN, getMinecordbot().FUN, getMinecordbot().HELP,
                getMinecordbot().INFO, getMinecordbot().MISC, getMinecordbot().OWNER};
        for (int i = 0; i <= categories.length - 1; i++) {
            StringBuilder str = new StringBuilder();
            if (getAllCommandsWithCategoryOf(categories[i]).size() != 0) {
                for (Command c : getAllCommandsWithCategoryOf(categories[i])) {
                    str.append(getMinecordbot().getClient().getPrefix()).append(c.getName())
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
        for (Command c : getMinecordbot().getClient().getCommands()) {
            if (c.getCategory().equals(category))
                commands.add((MCBCommand)c);
        }
        Collections.sort(commands);
        return commands;
    }
}
