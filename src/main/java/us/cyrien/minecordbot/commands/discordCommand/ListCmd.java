package us.cyrien.minecordbot.commands.discordCommand;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.MetadataValue;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.localization.Locale;

public class ListCmd extends MCBCommand implements Listener{

    private Message message;

    public ListCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "list";
        this.aliases = new String[]{"ls"};
        this.help = Locale.getCommandsMessage("list.description").finish();
        this.category = minecordbot.INFO;
        this.type = Type.EMBED;
    }

    @Override
    protected void doCommand(CommandEvent e) {
        respond(generateList(), e).queue(m -> message = m);
    }

    private MessageEmbed generateList() {
        StringBuilder out;
        EmbedBuilder eb = new EmbedBuilder();
        if (Bukkit.getServer().getOnlinePlayers() == null || Bukkit.getServer().getOnlinePlayers().size() == 0) {
            eb.setTitle("There are no players online...", null);
        } else {
            out = new StringBuilder();
            int counter = 1;
            for (Player p : Bukkit.getOnlinePlayers()) {
                if(!isVanished(p))
                    out.append(counter++).append(". ").append(p.getName()).append("\n");
            }
            eb.addField("Online Players on Minecraft", out.toString(), false);
        }
        return eb.build();
    }

    private void updateList() {
        if(message != null)
            message.editMessage(generateList()).complete();
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
