package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.entities.Game;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.localization.Locale;

public class SetGameCmd extends MCBCommand {

    public SetGameCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "setgame";
        this.help = Locale.getCommandsMessage("setgame.description").finish();
        this.arguments = "[game]";
        this.ownerCommand = true;
        this.category = minecordbot.OWNER;
        this.type = Type.EMBED;
    }

    @Override
    protected void doCommand(CommandEvent event) {
        try {
            event.getJDA().getPresence().setGame(event.getArgs().isEmpty() ? null : Game.of(event.getArgs()));
            String notPlaying = Locale.getCommandsMessage("setgame.notplaying").format(event.getJDA().getSelfUser().getName());
            String playing = Locale.getCommandsMessage("setgame.playing").format(event.getJDA().getSelfUser().getName());
            respond(event, event.getClient().getSuccess() + (event.getArgs().isEmpty() ? notPlaying : playing + "`" + event.getArgs() + "`"));
        } catch (Exception e) {
            event.reply(event.getClient().getError() + " The game could not be set!");
        }
    }
}
