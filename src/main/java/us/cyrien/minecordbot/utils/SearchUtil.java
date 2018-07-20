package us.cyrien.minecordbot.utils;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.accountSync.SimplifiedDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SearchUtil {

    public static List<TextChannel> findValidTextChannels(List<String> tcID) {
        List<TextChannel> out = new ArrayList<>();
        tcID.forEach((s) -> {
            JDA jda = Minecordbot.getInstance().getBot().getJda();
            if (!s.isEmpty() && jda != null) {
                if(StringUtils.isNumeric(s)) {
                    TextChannel tc = jda.getTextChannelById(s);
                    if (tc != null)
                        out.add(tc);
                } else {
                    List<Guild> guilds = jda.getGuilds();
                    guilds.forEach(g -> g.getTextChannels().forEach(tc -> {
                        if(tc.getName().equals(s))
                            out.add(tc);
                    }));
                }
            }
        });
        return out;
    }

    public static Member findMember(String query) {
        List<Guild> guilds = Minecordbot.getInstance().getBot().getJda().getGuilds();
        List<Member> members;
        for (Guild guild : guilds) {
            members = com.jagrosh.jdautilities.utils.FinderUtil.findMembers(query, guild);
            if (members.size() > 0)
                return members.get(0);
        }
        return null;
    }

    public static Player findPlayerInDatabase(String discordID) {
        Map<Object, String> inverseData = SimplifiedDatabase.getInvertedData();
        for (Map.Entry<Object, String> map : inverseData.entrySet()) {
            if(discordID.equals(map.getKey())) {
                UUID uuid = UUID.fromString(map.getValue());
                Player p = Bukkit.getPlayer(uuid) == null ? Bukkit.getOfflinePlayer(uuid).getPlayer() : Bukkit.getPlayer(uuid);
                if(p != null)
                    return  p;
            }
        }
        return null;
    }

    public static User findUserInDatabase(Player p) {
        Map<String, Object> config = SimplifiedDatabase.getData().toMap();
        for (Map.Entry<String, Object> map : config.entrySet()) {
            if (p != null && map.getValue().equals(p.getUniqueId())) {
                String userID = SimplifiedDatabase.get(p.getUniqueId().toString());
                if (userID != null && !userID.equals("Not Synced yet"))
                    return Minecordbot.getInstance().getBot().getJda().getUserById(userID);
            }
        }
        return null;
    }

}