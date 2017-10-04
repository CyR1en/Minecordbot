package us.cyrien.minecordbot.entity;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.bukkit.entity.Player;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.utils.FinderUtil;

import java.util.HashMap;
import java.util.UUID;

public class MCBUser {

    private String name;
    private HashMap<Guild, String> nickNames;
    private String ID;
    private UUID mcUUID;
    private User user;

    public MCBUser(MessageReceivedEvent e) {
        nickNames = new HashMap<>();
        name = e.getAuthor().getName();
        nickNames.put(e.getGuild(), e.getMember().getNickname());
        ID = e.getAuthor().getId();
        mcUUID = null;
        user = e.getAuthor();
    }

    public MCBUser(CommandEvent e) {
        this(e.getEvent());
    }

    public MCBUser(User user, Guild guild) {
        nickNames = new HashMap<>();
        name = user.getName();
        nickNames.put(guild, guild.getMember(user).getNickname());
        ID = user.getId();
    }

    public MCBUser(User user) {
        nickNames = new HashMap<>();
        name = user.getName();
        ID = user.getId();
        Minecordbot.getInstance().getBot().getJda().getMutualGuilds(user).forEach(guild -> nickNames.put(guild, guild.getMember(user).getNickname()));
    }

    public Player parseAsPlayer() {
        return FinderUtil.findPlayerInDatabase(this.ID);
    }

    public User getUser() {
        return user;
    }

    public UUID getMcUUID() {
        return mcUUID;
    }

    public void setMcUUID(UUID uuid) {
        mcUUID = uuid;
    }

    public String getName() {
        return name;
    }

    public HashMap<Guild, String> getNickNames() {
        return nickNames;
    }

    public String getID() {
        return ID;
    }

    @Override
    public String toString() {
        return getName() + "|" + getNickNames().toString() + "|" + getID();
    }
}
