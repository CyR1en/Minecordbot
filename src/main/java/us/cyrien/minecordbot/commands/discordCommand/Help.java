package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.DiscordCommand;
import us.cyrien.minecordbot.localization.Localization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Help extends DiscordCommand {

    public Help(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "help";
        this.help = Localization.getTranslatedMessage("mcb.commands.help.description");
        this.category = minecordbot.HELP;
    }

    @Override
    protected void doCommand(CommandEvent event) {
        if (event.getArgs().isEmpty())
            respond(event, listCommands());
    }

    private MessageEmbed listCommands() {
        EmbedBuilder ebi = new EmbedBuilder();
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
        return ebi.build();
    }

    public List<DiscordCommand> getAllCommandsWithCategoryOf(Command.Category category) {
        List<DiscordCommand> cmds = new ArrayList<>();
        for (Command c : getMinecordbot().getClient().getCommands()) {
            if (c.getCategory().equals(category))
                cmds.add((DiscordCommand)c);
        }
        Collections.sort(cmds);
        return cmds;
    }
}
