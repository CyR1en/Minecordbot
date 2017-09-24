package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.localization.Locale;

public class SetNameCmd extends MCBCommand {

    public SetNameCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "setname";
        this.aliases = new String[]{"setname", "sn"};
        this.arguments = "<username>";
        this.help = Locale.getCommandsMessage("setusername.description").finish();
        this.ownerCommand = true;
        this.category = minecordbot.OWNER;
        this.type = Type.EMBED;
    }

    @Override
    protected void doCommand(CommandEvent event) {
        try {
            String oldName = event.getSelfUser().getName();
            event.getSelfUser().getManager().setName(event.getArgs()).complete(false);
            String replyMsg = Locale.getCommandsMessage("setusername.changed").finish();
            respond(event,event.getClient().getSuccess() + " " + String.format(replyMsg, oldName,  event.getArgs()));
        } catch (RateLimitedException e) {
            String replyMsg = Locale.getCommandsMessage("setusername.limit").finish();
            respond(event, event.getClient().getError() + " " + replyMsg);
        } catch (Exception e) {
            String replyMessage = Locale.getCommandsMessage("setusername.invalid").finish();
            respond(event,event.getClient().getError() + " " + replyMessage);
        }
    }
}
