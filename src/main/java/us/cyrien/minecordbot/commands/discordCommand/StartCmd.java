package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import us.cyrien.minecordbot.Bot;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.localization.Locale;

public class StartCmd extends MCBCommand {

    public StartCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "start";
        this.help = Locale.getCommandsMessage("start.description").finish();
        this.ownerCommand = true;
        this.category = Bot.OWNER;
        this.type = MCBCommand.Type.EMBED;
    }

    @Override
    protected void doCommand(CommandEvent e) {
        respond(e, Locale.getCommandsMessage("start.starting").finish());
        getMcb().getBot().shutdown();
    }
}
