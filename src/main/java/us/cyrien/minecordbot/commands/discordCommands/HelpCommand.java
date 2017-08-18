package us.cyrien.minecordbot.commands.discordCommands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.core.annotation.DCmd;
import us.cyrien.minecordbot.core.annotation.DCommand;
import us.cyrien.minecordbot.core.annotation.DMessageReceive;
import us.cyrien.minecordbot.core.enums.CommandType;
import us.cyrien.minecordbot.core.module.DiscordCommand;
import us.cyrien.minecordbot.entity.MCBUser;
import us.cyrien.minecordbot.localization.Localization;
import us.cyrien.minecordbot.Minecordbot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HelpCommand {

    @DCommand(aliases = {"help", "h"}, usage = "mcb.commands.help.usage", desc = "mcb.commands.help.description", type = CommandType.HELP)
    public void execute(@DMessageReceive MessageReceivedEvent e, @DCmd DiscordCommand command, DiscordCommand arg) {
        EmbedBuilder eb = new EmbedBuilder().setColor(e.getGuild().getMember(e.getJDA().getSelfUser()).getColor());
        if (arg == null) {
            command.sendMessageEmbed(e, listCommands(eb), 60);
        } else if (arg.isNullified()) {
            String noSuchCommandResponse = Localization.getTranslatedMessage("mcb.commands.help.no-such-command");
            command.sendMessageEmbed(e, new EmbedBuilder().setTitle(String.format(noSuchCommandResponse, arg.getName()), null).build(), 15);
        } else {
            arg.setSender(new MCBUser(e));
            command.sendMessageEmbed(e, arg.getHelpCard(e), 60);
        }
    }

    private MessageEmbed listCommands(EmbedBuilder ebi) {
        ebi.setThumbnail("https://media-elerium.cursecdn.com/attachments/thumbnails/124/611/310/172/minecord.png");
        ebi.setAuthor(Localization.getTranslatedMessage("mcb.commands.help.list.header"), null,
                "https://media-elerium.cursecdn.com/attachments/thumbnails/124/611/310/172/minecord.png");
        String trigger = MCBConfig.get("trigger");
        for (int i = 0; i <= CommandType.values().length - 1; i++) {
            StringBuilder str = new StringBuilder();
            if (getAllCommandsOf(CommandType.values()[i]).size() != 0) {
                for (DiscordCommand c : getAllCommandsOf(CommandType.values()[i])) {
                    str.append(trigger)
                            .append(c.getName())
                            .append(" - ").append(c.getDescription()).append("\n");
                }
                ebi.addField(" - " + CommandType.values()[i].toString(), str.toString(), false);
            }
        }
        return ebi.build();
    }

    private static List<DiscordCommand> getAllCommandsOf(CommandType commandType) {
        List<DiscordCommand> commands = new ArrayList<>();
        for (DiscordCommand c : Minecordbot.getDiscordCommands())
            if (c.getCommandType() == commandType)
                commands.add(c);
        Collections.sort(commands);
        return commands;
    }
}
