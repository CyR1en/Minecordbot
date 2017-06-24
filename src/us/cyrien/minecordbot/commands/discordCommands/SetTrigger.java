package us.cyrien.minecordbot.commands.discordCommands;

import io.github.hedgehog1029.frame.loader.Text;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.core.annotation.DCmd;
import us.cyrien.minecordbot.core.annotation.DCommand;
import us.cyrien.minecordbot.core.annotation.DMessageReceive;
import us.cyrien.minecordbot.core.annotation.DPermission;
import us.cyrien.minecordbot.core.enums.CommandType;
import us.cyrien.minecordbot.core.enums.PermissionLevel;
import us.cyrien.minecordbot.core.module.DiscordCommand;

public class SetTrigger {
    @DCommand(aliases = {"settrigger", "st"}, usage = "mcb.commands.settrigger.usage", desc = "mcb.commands.settrigger.description", type = CommandType.MISC)
    @DPermission(PermissionLevel.LEVEL_2)
    public void command(@DMessageReceive MessageReceivedEvent event, @DCmd DiscordCommand command, @Text String trigger) {
        if (!StringUtils.isBlank(trigger)) {
            if (trigger.contains("\\")) {
                command.sendMessage(event, "`Trigger can not contain a backward slash.`", 30);
            } else {
                MCBConfig.set("trigger", trigger);
                command.sendMessage(event, "Trigger changed to " + trigger, 30);
            }
        }
    }
}
