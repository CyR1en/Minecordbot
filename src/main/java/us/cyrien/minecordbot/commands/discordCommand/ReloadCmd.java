package us.cyrien.minecordbot.commands.discordCommand;

import us.cyrien.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import org.bukkit.Bukkit;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.chat.DiscordConsoleCommandSender;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.localization.Locale;

public class ReloadCmd extends MCBCommand {

    public ReloadCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "reload";
        this.help = Locale.getCommandsMessage("reload.description").finish();
        this.category = minecordbot.ADMIN;
        this.type = Type.EMBED;
    }

    @Override
    protected void doCommand(CommandEvent e) {
        MCBConfig.reload();
        EmbedBuilder eb = new EmbedBuilder().setColor(e.getGuild().getMember(e.getJDA().getSelfUser()).getColor());
        eb.setTitle(Locale.getCommandsMessage("reload.reloaded").finish(), null);
        respond(e, eb.build());
        Bukkit.getServer().dispatchCommand(new DiscordConsoleCommandSender(e), "reload MineCordBot");
    }
}
