package us.cyrien.minecordbot.commands.discordCommands;

import io.github.hedgehog1029.frame.annotations.Text;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import us.cyrien.minecordbot.core.annotation.DCmd;
import us.cyrien.minecordbot.core.annotation.DCommand;
import us.cyrien.minecordbot.core.annotation.DMessageReceive;
import us.cyrien.minecordbot.core.annotation.DPermission;
import us.cyrien.minecordbot.core.enums.CommandType;
import us.cyrien.minecordbot.core.enums.PermissionLevel;
import us.cyrien.minecordbot.core.module.DiscordCommand;
import us.cyrien.minecordbot.main.Localization;

import static us.cyrien.minecordbot.core.module.DiscordCommand.HELP_COMMAND_DURATION;

public class SetGameCommand {
    @DCommand(aliases = {"setgame", "sg"}, usage = "mcb.commands.setgame.usage", desc = "mcb.commands.setgame.description", type = CommandType.MISC)
    @DPermission(PermissionLevel.LEVEL_2)
    public void command(@DMessageReceive MessageReceivedEvent e, @DCmd DiscordCommand command, @Text String arg) {
        if (StringUtils.isBlank(arg)) {
            command.sendMessageEmbed(e, command.getInvalidHelpCard(e), HELP_COMMAND_DURATION);
            return;
        }
        String[] args = arg.split("\\s");
        if (args.length == 0) {
            command.sendMessageEmbed(e, command.getInvalidHelpCard(e), HELP_COMMAND_DURATION);
            return;
        }
        e.getJDA().getPresence().setGame(Game.of(arg));
        String r = Localization.getTranslatedMessage("mcb.commands.setgame.changed");
        command.sendMessageEmbed(e, new EmbedBuilder().setColor(e.getGuild().getMember(e.getJDA().getSelfUser()).getColor()).setTitle(String.format(r, arg), null).build(), 30);
    }
}
