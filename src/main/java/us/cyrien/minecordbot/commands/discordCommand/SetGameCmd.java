package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.entities.Game;
import us.cyrien.minecordbot.Bot;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.localization.Locale;

public class SetGameCmd extends MCBCommand {

    public SetGameCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "setgame";
        this.help = Locale.getCommandsMessage("setgame.description").finish();
        this.arguments = "[game]...";
        this.ownerCommand = true;
        this.category = Bot.OWNER;
        this.type = Type.EMBED;
    }

    @Override
    protected void doCommand(CommandEvent event) {
        try {
            if(event.getArgs().isEmpty()) {
                event.getJDA().getPresence().setGame(null);
                String notPlaying = Locale.getCommandsMessage("setgame.notplaying").f(event.getJDA().getSelfUser().getName());
                respond(event, embedMessage(event, notPlaying, ResponseLevel.LEVEL_1));
            } else {
                event.getJDA().getPresence().setGame(Game.playing(event.getArgs()));
                String playing = Locale.getCommandsMessage("setgame.playing").f(event.getJDA().getSelfUser().getName());
                respond(event, embedMessage(event, playing, ResponseLevel.LEVEL_1));
            }
        } catch (Exception e) {
            event.reply(event.getClient().getError() + " The game could not be set!");
        }
    }
}
