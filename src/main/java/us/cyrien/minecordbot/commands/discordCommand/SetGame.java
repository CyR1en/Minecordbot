package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.entities.Game;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.DiscordCommand;

public class SetGame extends DiscordCommand{

    public SetGame(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "setgame";
        this.help = "sets the game the bot is playing";
        this.arguments = "[game]";
        this.ownerCommand = true;
        this.category = minecordbot.OWNER;
    }

    @Override
    protected void doCommand(CommandEvent event) {
        try {
            event.getJDA().getPresence().setGame(event.getArgs().isEmpty() ? null : Game.of(event.getArgs()));
            event.reply(event.getClient().getSuccess() + " **" + event.getSelfUser().getName()
                    + "** is " + (event.getArgs().isEmpty() ? "no longer playing anything." : "now playing `" + event.getArgs() + "`"));
        } catch (Exception e) {
            event.reply(event.getClient().getError() + " The game could not be set!");
        }
    }
}
