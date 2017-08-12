package us.cyrien.minecordbot.commands.discordCommands;

import io.github.hedgehog1029.frame.annotations.Text;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Icon;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static us.cyrien.minecordbot.core.module.DiscordCommand.HELP_COMMAND_DURATION;

public class SetAvatarCommand {
    private DiscordCommand command;
    private Icon avatar;

    @DCommand(aliases = {"setavatar", "sa"}, usage = "mcb.commands.setavatar.usage", desc = "mcb.commands.setavatar.description", type = CommandType.MISC)
    @DPermission(PermissionLevel.LEVEL_2)
    public void command(@DMessageReceive MessageReceivedEvent e, @DCmd DiscordCommand command, @Text String arg) {
        this.command = command;
        String[] args = arg.split("\\s");
        if (checkArguments(e, args)) {
            e.getJDA().getSelfUser().getManager().setAvatar(avatar).queue();
            String r = Localization.getTranslatedMessage("mcb.commands.setavatar.changed");
            command.sendMessageEmbed(e, new EmbedBuilder().setColor(e.getGuild().getMember(e.getJDA().getSelfUser()).getColor()).setTitle(r, null).build(), 30);
        }
    }

    public boolean checkArguments(MessageReceivedEvent e, String[] args) {
        if (StringUtils.isBlank(StringUtils.join(args, " "))) {
            command.sendMessageEmbed(e, command.getInvalidHelpCard(e), HELP_COMMAND_DURATION);
            return false;
        }
        try {
            URL url = new URL(args[0]);
            URLConnection uc = url.openConnection();
            InputStream in = uc.getInputStream();
            avatar = Icon.from(in);
        } catch (MalformedURLException ex) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Invalid Link(MalformedURLException)", null);
            eb.addField("Exception:", ex.getMessage(), false);
            command.sendMessageEmbed(e, eb.build(), 15);
            return false;
        } catch (IOException ex) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Invalid Link(IOException)", null);
            eb.addField("Exception:", ex.getMessage(), false);
            command.sendMessageEmbed(e, eb.build(), 15);
            return false;
        }
        return true;
    }
}
