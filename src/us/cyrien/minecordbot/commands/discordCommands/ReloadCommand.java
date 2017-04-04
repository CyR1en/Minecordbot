package us.cyrien.minecordbot.commands.discordCommands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.core.annotation.DCmd;
import us.cyrien.minecordbot.core.annotation.DCommand;
import us.cyrien.minecordbot.core.annotation.DMessageReceive;
import us.cyrien.minecordbot.core.annotation.DPermission;
import us.cyrien.minecordbot.core.enums.CommandType;
import us.cyrien.minecordbot.core.enums.PermissionLevel;
import us.cyrien.minecordbot.core.module.DiscordCommand;
import us.cyrien.minecordbot.main.Localization;

public class ReloadCommand {
    @DCommand(aliases = {"reload", "rl"}, usage = "mcb.commands.reload.usage", desc = "mcb.commands.reload.description", type = CommandType.MOD)
    @DPermission(PermissionLevel.LEVEL_3)
    public void command(@DMessageReceive MessageReceivedEvent e, @DCmd DiscordCommand command) {
        MCBConfig.reload();
        EmbedBuilder eb = new EmbedBuilder().setColor(e.getGuild().getMember(e.getJDA().getSelfUser()).getColor());
        eb.setTitle(Localization.getTranslatedMessage("mcb.commands.reload.reloaded"), null);
        command.sendMessageEmbed(e, eb.build(), DiscordCommand.HELP_COMMAND_DURATION);
    }
}
