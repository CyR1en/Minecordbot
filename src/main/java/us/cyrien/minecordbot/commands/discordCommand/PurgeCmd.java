package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.entities.Message;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.localization.Locale;

import java.util.ArrayList;
import java.util.List;

public class PurgeCmd extends MCBCommand {

    public PurgeCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "purge";
        this.help = Locale.getCommandsMessage("purge.description").finish();
        this.arguments = "<number>";
        this.category = minecordbot.OWNER;
        this.type = Type.EMBED;
    }

    @Override
    protected void doCommand(CommandEvent event) {
        String args = event.getArgs();
        int num = 100;
        try {
            if (!args.isEmpty())
                num = Integer.parseInt(args);
        } catch (NumberFormatException e) {
            respond(event, Locale.getCommandsMessage("purge.invalidNum").finish());
            return;
        }
        if (num < 1 || num > 100) {
            respond(event, Locale.getCommandsMessage("purge.invalidNum").finish());
            return;
        }
        try {
            event.getChannel().getHistory().retrievePast(num == 100 ? 100 : num + 1).queue(success -> {
                List<Message> list = new ArrayList<>(success);
                list.forEach(m -> m.delete().queue());
                respond(event, Locale.getCommandsMessage("purge.deleting").finish(), (m) -> m.delete().queue());
            }, failure -> respond(event, Locale.getCommandsMessage("purge.failedToRetrieve").finish()));
        } catch (Exception e) {
            respond(event, Locale.getCommandsMessage("purge.couldNotRetrieve").finish());
        }
    }

}
