package us.cyrien.minecordbot.commands.discordCommands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import us.cyrien.minecordbot.core.annotation.DCmd;
import us.cyrien.minecordbot.core.annotation.DCommand;
import us.cyrien.minecordbot.core.annotation.DMessageReceive;
import us.cyrien.minecordbot.core.annotation.DPermission;
import us.cyrien.minecordbot.core.enums.CommandType;
import us.cyrien.minecordbot.core.enums.PermissionLevel;
import us.cyrien.minecordbot.core.module.DiscordCommand;
import us.cyrien.minecordbot.main.Minecordbot;

public class ShutDownCommand {
    @DCommand(aliases = "halt", usage = "mcb.commands.shutdown.usage", desc = "mcb.commands.shutdown.description", type = CommandType.MOD)
    @DPermission(PermissionLevel.LEVEL_3)
    public void command(@DMessageReceive MessageReceivedEvent event, @DCmd DiscordCommand command) {
        Minecordbot mcb = command.getMCB();
        mcb.shutdown();
    }
}
