package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
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
        TextChannel tc = isModChannel(e.getTextChannel()) ? e.getTextChannel() : null;
        respond(generateList(tc), e).queue(m -> message = m);
    }

    private MessageEmbed generateList(@Nullable TextChannel textChannel) {
        StringBuilder out;
        EmbedBuilder eb = new EmbedBuilder();
        List<Player> playerList = (List<Player>) Bukkit.getServer().getOnlinePlayers();
        if (playerList == null || playerList.size() == 0) {
            eb.setTitle("There are no players online...", null);
        } else if (playerList.size() == 1 && isVanished(playerList.get(0))) {
            eb.setTitle("There are no players online...", null);
        } else if (textChannel != null) {
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

    private boolean isModChannel(TextChannel textChannel) {
        String modChannel = configsManager.getModChannelConfig().getString("Mod_TextChannel");
        TextChannel tc = getMcb().getBot().getJda().getTextChannelById(modChannel);
        if (tc != null)
            return tc.equals(textChannel);
        return false;
    }

    private void updateList(TextChannel textChannel) {
        if (message != null)
            message.editMessage(generateList(textChannel)).complete();
    }

    private void updateList() {
        updateList(null);
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
