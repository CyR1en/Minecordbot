package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.entities.Icon;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.DiscordCommand;
import us.cyrien.minecordbot.localization.Localization;
import us.cyrien.minecordbot.utils.OtherUtil;

import java.io.IOException;
import java.io.InputStream;

public class SetAvatar extends DiscordCommand {

    public SetAvatar(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "setavatar";
        this.aliases = new String[]{"sa"};
        this.arguments = "<url>";
        this.help = Localization.getTranslatedMessage("mcb.commands.setavatar.description");
        this.category = minecordbot.OWNER;
        this.ownerCommand = true;
    }

    @Override
    protected void doCommand(CommandEvent event) {
        String url;
        if (event.getArgs().isEmpty())
            if (!event.getMessage().getAttachments().isEmpty() && event.getMessage().getAttachments().get(0).isImage())
                url = event.getMessage().getAttachments().get(0).getUrl();
            else
                url = null;
        else
            url = event.getArgs();
        InputStream s = OtherUtil.imageFromUrl(url);
        if (s == null) {
            event.reply(event.getClient().getError() + " Invalid or missing URL");
        } else {
            try {
                event.getSelfUser().getManager().setAvatar(Icon.from(s)).queue(
                        v -> event.reply(event.getClient().getSuccess() + " Successfully changed avatar."),
                        t -> event.reply(event.getClient().getError() + " Failed to set avatar."));
            } catch (IOException e) {
                event.reply(event.getClient().getError() + " Could not load from provided URL.");
            }
        }
    }
}
