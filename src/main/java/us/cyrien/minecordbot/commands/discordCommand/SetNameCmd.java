package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import us.cyrien.minecordbot.Bot;
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
        this.category = Bot.OWNER;
        this.type = Type.EMBED;
    }

    @Override
    protected void doCommand(CommandEvent event) {
        try {
            event.getSelfUser().getManager().setName(event.getArgs()).complete(false);
            String replyMsg = Locale.getCommandsMessage("setusername.changed").finish();
            respond(event, embedMessage(event, replyMsg, ResponseLevel.LEVEL_1));
        } catch (RateLimitedException e) {
            respond(event, embedMessage(event, Locale.getCommandsMessage("setusername.limit").finish(), ResponseLevel.LEVEL_3));
        }
    }
}
