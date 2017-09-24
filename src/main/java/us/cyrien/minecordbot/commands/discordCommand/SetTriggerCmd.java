package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.localization.Locale;

public class SetTriggerCmd extends MCBCommand{

    public SetTriggerCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "settrigger";
        this.aliases = new String[]{"st"};
        this.arguments = "<new trigger>";
        this.help = Locale.getCommandsMessage("settrigger.description").finish();
        this.ownerCommand = true;
        this.category = minecordbot.OWNER;
        this.type = Type.EMBED;
    }

    @Override
    protected void doCommand(CommandEvent e) {
        String trigger = e.getArgs();
        if(trigger.contains("\\")) {
            respond(e, Locale.getCommandsMessage("settrigger.invalid").finish());
            return;
        }
        MCBConfig.set("trigger", trigger);
        respond(e, Locale.getCommandsMessage("settrigger.changed").finish());
    }
}
