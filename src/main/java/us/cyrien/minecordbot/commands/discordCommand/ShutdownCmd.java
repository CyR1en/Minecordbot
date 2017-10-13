package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import us.cyrien.minecordbot.Bot;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.localization.Locale;

public class ShutdownCmd extends MCBCommand {

    public ShutdownCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "shutdown";
        this.help = Locale.getCommandsMessage("shutdown.description").finish();
        this.ownerCommand = true;
        this.category = Bot.OWNER;
        this.type = Type.EMBED;
    }

    @Override
    protected void doCommand(CommandEvent e) {
        respond(e, Locale.getCommandMessage("shutdown.shutting").finish());
        getMcb().getBot().shutdown();
    }
}
