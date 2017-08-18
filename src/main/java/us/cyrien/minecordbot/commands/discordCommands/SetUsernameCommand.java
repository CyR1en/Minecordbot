package us.cyrien.minecordbot.commands.discordCommands;

import io.github.hedgehog1029.frame.annotations.Text;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import us.cyrien.minecordbot.core.annotation.DCmd;
import us.cyrien.minecordbot.core.annotation.DCommand;
import us.cyrien.minecordbot.core.annotation.DMessageReceive;
import us.cyrien.minecordbot.core.annotation.DPermission;
import us.cyrien.minecordbot.core.enums.CommandType;
import us.cyrien.minecordbot.core.enums.PermissionLevel;
import us.cyrien.minecordbot.core.module.DiscordCommand;
import us.cyrien.minecordbot.localization.Localization;

import static us.cyrien.minecordbot.core.module.DiscordCommand.HELP_COMMAND_DURATION;

public class SetUsernameCommand {
    @DCommand(aliases = {"setusername", "su"}, usage = "mcb.commands.setusername.usage", desc = "mcb.commands.setusername.description", type = CommandType.MISC)
    @DPermission(PermissionLevel.LEVEL_2)
    public void command(@DMessageReceive MessageReceivedEvent e, @DCmd DiscordCommand command, @Text String arg) {
        if (StringUtils.isBlank(arg)) {
            command.sendMessageEmbed(e, command.getInvalidHelpCard(e), HELP_COMMAND_DURATION);
            return;
        }
        e.getJDA().getSelfUser().getManager().setName(arg).queue();
        String r = Localization.getTranslatedMessage("mcb.commands.setusername.changed");
        command.sendMessageEmbed(e, new EmbedBuilder().setColor(e.getGuild().getMember(e.getJDA().getSelfUser()).getColor()).setTitle(r, null).build(), 30);
    }
}
