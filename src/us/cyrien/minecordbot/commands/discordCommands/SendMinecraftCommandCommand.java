package us.cyrien.minecordbot.commands.discordCommands;

import io.github.hedgehog1029.frame.annotations.Text;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import us.cyrien.minecordbot.core.annotation.DCmd;
import us.cyrien.minecordbot.core.annotation.DCommand;
import us.cyrien.minecordbot.core.annotation.DMessageReceive;
import us.cyrien.minecordbot.core.annotation.DPermission;
import us.cyrien.minecordbot.core.enums.CommandType;
import us.cyrien.minecordbot.core.enums.PermissionLevel;
import us.cyrien.minecordbot.core.module.DiscordCommand;
import us.cyrien.minecordbot.entity.DiscordCommandSender;
import us.cyrien.minecordbot.entity.DiscordConsoleCommandSender;

import static us.cyrien.minecordbot.core.module.DiscordCommand.HELP_COMMAND_DURATION;

public class SendMinecraftCommandCommand {
    @DCommand(aliases = {"minecraftcommand", "mcmd"}, usage = "mcb.commands.mcmd.usage", desc = "mcb.commands.mcmd.description", type = CommandType.MOD)
    @DPermission(PermissionLevel.LEVEL_3)
    public void command(@DMessageReceive MessageReceivedEvent e, @DCmd DiscordCommand command, @Text String arg) {
        String[] args = arg.split("\\s");
        if (StringUtils.isBlank(arg)) {
            command.sendMessageEmbed(e, command.getInvalidHelpCard(e), HELP_COMMAND_DURATION);
            return;
        }
        if (args[0].equalsIgnoreCase("help")) {
            Bukkit.getServer().dispatchCommand(new DiscordCommandSender(e), arg);
        } else
            Bukkit.getServer().dispatchCommand(new DiscordConsoleCommandSender(e), arg);
    }
}
