package us.cyrien.minecordbot.utils;

import net.dv8tion.jda.core.entities.*;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.accountSync.SimplifiedDatabase;

import java.util.*;

public class FinderUtil {

    public static List<TextChannel> findTextChannel(String query, Guild guild) {
        String id;
        if (query.matches("<#\\d+>")) {
            id = query.replaceAll("<#(\\d+)>", "$1");
            TextChannel tc = guild.getJDA().getTextChannelById(id);
            if (tc != null && tc.getGuild().equals(guild))
                return Collections.singletonList(tc);
        } else if (StringUtils.isNumeric(query)) {
            TextChannel tc = guild.getJDA().getTextChannelById(query);
            if (tc != null && tc.getGuild().equals(guild))
                return Collections.singletonList(tc);
        }
        ArrayList<TextChannel> exact = new ArrayList<>();
        ArrayList<TextChannel> wrongcase = new ArrayList<>();
        ArrayList<TextChannel> startswith = new ArrayList<>();
        ArrayList<TextChannel> contains = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        guild.getTextChannels().stream().forEach(tc -> {
            if (tc.getName().equals(lowerQuery))
                exact.add(tc);
            else if (tc.getName().equalsIgnoreCase(lowerQuery) && exact.isEmpty())
                wrongcase.add(tc);
            else if (tc.getName().toLowerCase().startsWith(lowerQuery) && wrongcase.isEmpty())
                startswith.add(tc);
            else if (tc.getName().toLowerCase().contains(lowerQuery) && startswith.isEmpty())
                contains.add(tc);
        });
        if (!exact.isEmpty())
            return exact;
        if (!wrongcase.isEmpty())
            return wrongcase;
        if (!startswith.isEmpty())
            return startswith;
        return contains;
    }

    public static List<Member> findMember(String query, Guild guild) {
        String id;
        if (query.matches("<#\\d+>")) {
            id = query.replaceAll("<#(\\d+)", "$1");
            Member member = guild.getMemberById(id);
            if (member != null && member.getGuild().equals(guild))
                return Collections.singletonList(member);
        } else if (StringUtils.isNumeric(query)) {
            Member member = guild.getMemberById(query);
            if (member != null && member.getGuild().equals(guild))
                return Collections.singletonList(member);
        }
        ArrayList<Member> exact = new ArrayList<>();
        ArrayList<Member> wrongcase = new ArrayList<>();
        ArrayList<Member> startswith = new ArrayList<>();
        ArrayList<Member> contains = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        guild.getMembers().stream().forEach(m -> {
            if (m.getEffectiveName().equals(lowerQuery))
                exact.add(m);
            else if (m.getEffectiveName().equalsIgnoreCase(lowerQuery) && exact.isEmpty())
                wrongcase.add(m);
            else if (m.getEffectiveName().toLowerCase().startsWith(lowerQuery) && wrongcase.isEmpty())
                startswith.add(m);
            else if (m.getEffectiveName().toLowerCase().contains(lowerQuery) && startswith.isEmpty())
                contains.add(m);
        });
        if (!exact.isEmpty())
            return exact;
        if (!wrongcase.isEmpty())
            return wrongcase;
        if (!startswith.isEmpty())
            return startswith;
        return contains;
    }

    public static Member findMember(String query) {
        List<Guild> guilds = Minecordbot.getInstance().getBot().getJda().getGuilds();
        List<Member> members;
        for (Guild guild : guilds) {
            members = findMember(query, guild);
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
                if (userID != null && !userID.equals("Not Synced yet")) {
                    return Minecordbot.getInstance().getBot().getJda().getUserById(userID);
                }
            }
        }
        return null;
    }

    public static List<VoiceChannel> findVoiceChannel(String query, Guild guild) {
        String id;
        if (query.matches("<#\\d+>")) {
            id = query.replaceAll("<#(\\d+)>", "$1");
            VoiceChannel vc = guild.getJDA().getVoiceChannelById(id);
            if (vc != null && vc.getGuild().equals(guild))
                return Collections.singletonList(vc);
        }
        ArrayList<VoiceChannel> exact = new ArrayList<>();
        ArrayList<VoiceChannel> wrongcase = new ArrayList<>();
        ArrayList<VoiceChannel> startswith = new ArrayList<>();
        ArrayList<VoiceChannel> contains = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        guild.getVoiceChannels().stream().forEach(vc -> {
            if (vc.getName().equals(lowerQuery))
                exact.add(vc);
            else if (vc.getName().equalsIgnoreCase(lowerQuery) && exact.isEmpty())
                wrongcase.add(vc);
            else if (vc.getName().toLowerCase().startsWith(lowerQuery) && wrongcase.isEmpty())
                startswith.add(vc);
            else if (vc.getName().toLowerCase().contains(lowerQuery) && startswith.isEmpty())
                contains.add(vc);
        });
        if (!exact.isEmpty())
            return exact;
        if (!wrongcase.isEmpty())
            return wrongcase;
        if (!startswith.isEmpty())
            return startswith;
        return contains;
    }

    public static List<Role> findRole(String query, Guild guild) {
        String id;
        if (query.matches("<@&\\d+>")) {
            id = query.replaceAll("<@&(\\d+)>", "$1");
            Role role = guild.getRoleById(id);
            if (role != null)
                return Collections.singletonList(role);
        }
        if (query.matches("[Ii][Dd]\\s*:\\s*\\d+")) {
            id = query.replaceAll("[Ii][Dd]\\s*:\\s*(\\d+)", "$1");
            for (Role role : guild.getRoles())
                if (role.getId().equals(id))
                    return Collections.singletonList(role);
        }
        ArrayList<Role> exact = new ArrayList<>();
        ArrayList<Role> wrongcase = new ArrayList<>();
        ArrayList<Role> startswith = new ArrayList<>();
        ArrayList<Role> contains = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        guild.getRoles().stream().forEach(role -> {
            if (role.getName().equals(query))
                exact.add(role);
            else if (role.getName().equalsIgnoreCase(query) && exact.isEmpty())
                wrongcase.add(role);
            else if (role.getName().toLowerCase().startsWith(lowerQuery) && wrongcase.isEmpty())
                startswith.add(role);
            else if (role.getName().toLowerCase().contains(lowerQuery) && startswith.isEmpty())
                contains.add(role);
        });
        if (!exact.isEmpty())
            return exact;
        if (!wrongcase.isEmpty())
            return wrongcase;
        if (!startswith.isEmpty())
            return startswith;
        return contains;
    }
}