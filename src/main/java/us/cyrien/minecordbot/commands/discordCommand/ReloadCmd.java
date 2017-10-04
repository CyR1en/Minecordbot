package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import us.cyrien.minecordbot.Bot;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.localization.Locale;

public class ReloadCmd extends MCBCommand {

    public ReloadCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "reload";
        this.help = Locale.getCommandsMessage("reload.description").finish();
        this.category = Bot.ADMIN;
        this.type = Type.EMBED;
    }

    @Override
    protected void doCommand(CommandEvent e) {
        configsManager.reloadAllConfig();
        EmbedBuilder eb = new EmbedBuilder().setColor(e.getGuild().getMember(e.getJDA().getSelfUser()).getColor());
        eb.setTitle(Locale.getCommandsMessage("reload.reloaded").finish(), null);
        respond(e, eb.build());
    }
}
