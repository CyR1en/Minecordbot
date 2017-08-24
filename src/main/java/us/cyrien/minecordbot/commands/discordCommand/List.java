package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.DiscordCommand;
import us.cyrien.minecordbot.localization.Localization;

public class List extends DiscordCommand implements Listener{

    private Message message;

    public List(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "List";
        this.aliases = new String[]{"ls"};
        this.help = Localization.getTranslatedMessage("mcb.commands.List.description");
        this.category = minecordbot.INFO;
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
            for (Player p : Bukkit.getOnlinePlayers())
                out.append(counter++).append(". ").append(p.getName()).append("\n");
            eb.addField("Online Players on Minecraft", out.toString(), false);
        }
        return eb.build();
    }

    public void updateList() {
        message.editMessage(generateList()).complete();
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
