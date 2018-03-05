package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import us.cyrien.minecordbot.Bot;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.commands.Updatable;
import us.cyrien.minecordbot.localization.Locale;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ListCmd extends MCBCommand implements Updatable {

    public ListCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "list";
        this.aliases = new String[]{"ls"};
        this.help = Locale.getCommandsMessage("list.description").finish();
        this.category = Bot.INFO;
        this.type = Type.EMBED;
    }

    @Override
    protected void doCommand(CommandEvent e) {
        respond(Locale.getCommandsMessage("list.listing").finish(), e).queue(m -> {
            mcb.getChatManager().addSavedList(m, e);
            update();
        });
    }

    private MessageEmbed generateList(Message message) {
        StringBuilder out;
        EmbedBuilder eb = new EmbedBuilder();
        Guild guild = message != null ? message.getGuild() : null;
        if (guild != null)
            eb.setColor(guild.getMember(mcb.getBot().getJda().getSelfUser()).getColor());
        List<Player> playerList = (List<Player>) Bukkit.getServer().getOnlinePlayers();
        if (playerList == null || playerList.size() == 0) {
            eb.setTitle(Locale.getCommandsMessage("list.no-players").finish(), null);
        } else if (playerList.size() == 1 && isVanished(playerList.get(0))) {
            eb.setTitle(Locale.getCommandsMessage("list.no-players").finish(), null);
        } else if (isModChannel(message.getTextChannel())) {
            out = new StringBuilder();
            int counter = 1;
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (isVanished(p))
                    out.append(counter++).append(". \uD83D\uDEB7").append(p.getName()).append("\n");
                else
                    out.append(counter++).append(". ").append(p.getName()).append("\n");
            }
            eb.addField(Locale.getCommandsMessage("list.header").finish(), out.toString(), false);
        } else {
            out = new StringBuilder();
            int counter = 1;
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!isVanished(p))
                    out.append(counter++).append(". ").append(p.getName()).append("\n");
            }
            eb.addField(Locale.getCommandsMessage("list.header").finish(), out.toString(), false);
        }
        return eb.build();
    }

    public void update() {
        scheduler.schedule(() -> mcb.getChatManager().getSavedLists().forEach((msg, event) ->
                msg.editMessage(embedMessage(event, generateList(msg), ResponseLevel.DEFAULT)).queue()), 1, TimeUnit.SECONDS);
    }

    private boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }
}
