package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.MetadataValue;
import us.cyrien.minecordbot.Bot;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.localization.Locale;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ListCmd extends MCBCommand implements Listener {

    private Message message;

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
        TextChannel tc = e.getTextChannel();
        tc.sendMessage("Listing....").queue(m -> {
            message = m;
            m.delete().queue();
        });
        respond(generateList(message), e).queue(m -> mcb.getChatManager().addSavedMessage(m));
    }

    private MessageEmbed generateList(@Nullable Message message) {
        StringBuilder out;
        EmbedBuilder eb = new EmbedBuilder();
        Guild guild = message != null ? message.getGuild() : null;
        if (guild != null)
            eb.setColor(guild.getMember(mcb.getBot().getJda().getSelfUser()).getColor());
        List<Player> playerList = (List<Player>) Bukkit.getServer().getOnlinePlayers();
        if (playerList == null || playerList.size() == 0) {
            eb.setTitle("There are no players online...", null);
        } else if (playerList.size() == 1 && isVanished(playerList.get(0))) {
            eb.setTitle("There are no players online...", null);
        } else if (isModChannel(message.getTextChannel())) {
            out = new StringBuilder();
            int counter = 1;
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (isVanished(p))
                    out.append(counter++).append(". \uD83D\uDEB7").append(p.getName()).append("\n");
                else
                    out.append(counter++).append(". ").append(p.getName()).append("\n");
            }
            eb.addField("Online Players on Minecraft", out.toString(), false);
        } else {
            out = new StringBuilder();
            int counter = 1;
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!isVanished(p))
                    out.append(counter++).append(". ").append(p.getName()).append("\n");
            }
            eb.addField("Online Players on Minecraft", out.toString(), false);
        }
        return eb.build();
    }

    private void updateList(Message message) {
        message.editMessage(generateList(message)).queue();
    }

    private void updateList() {
        scheduler.schedule(() -> {
            for (Message msg : mcb.getChatManager().getSavedMessage())
                updateList(msg);
        }, 1, TimeUnit.SECONDS);

    }

    private boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        updateList();
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        updateList();
    }
}
