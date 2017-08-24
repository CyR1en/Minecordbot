package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.DiscordCommand;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.localization.Localization;

public class Reload extends DiscordCommand{

    public Reload(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "reload";
        this.help = Localization.getTranslatedMessage("mcb.commands.reload.description");
        this.category = minecordbot.ADMIN;
    }

    @Override
    protected void doCommand(CommandEvent e) {
        MCBConfig.reload();
        EmbedBuilder eb = new EmbedBuilder().setColor(e.getGuild().getMember(e.getJDA().getSelfUser()).getColor());
        eb.setTitle(Localization.getTranslatedMessage("mcb.commands.reload.reloaded"), null);
        respond(e, eb.build());
    }
}
