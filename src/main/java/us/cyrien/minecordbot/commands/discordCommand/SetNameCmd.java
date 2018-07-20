package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
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
        event.getSelfUser().getManager().setName(event.getArgs()).queue();
        String replyMsg = Locale.getCommandsMessage("setusername.changed").finish();
        respond(event, embedMessage(event, replyMsg, ResponseLevel.LEVEL_1));
    }
}
