package us.cyrien.minecordbot.prefix;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.bukkit.entity.Player;
import us.cyrien.minecordbot.utils.SRegex;

import java.util.List;
import java.util.regex.Pattern;

public class PrefixParser {

    public static String parseDiscordPrefixes(String prefix, MessageReceivedEvent e) {
        return parseDiscord(prefix, e);
    }


    public static String parseMinecraftPrefix(String prefix, Player p) {
        if(p == null)
            return "";
        for(MinecraftPlaceHolder s : MinecraftPlaceHolder.values()) {
            s.init(p);
            String pH = "{" + s.name().toLowerCase() + "}";
            String pH0 ="\\{" + s.name().toLowerCase() + "}";
            if (prefix.contains(pH)) {
                prefix = prefix.replaceAll(pH0, s.toString());
            }
        }
        return prefix.replaceAll("\\s", " ");
    }

    private static String parseDiscord(String prefix, MessageReceivedEvent e) {
        for(DiscordPlaceHolders s : DiscordPlaceHolders.values() ) {
            s.init(e);
            String pH = "{" + s.name().toLowerCase() + "}";
            String pH0 ="\\{" + s.name().toLowerCase() + "}";
            if (prefix.contains(pH)) {
                prefix = prefix.replaceAll(pH0, s.toString());
            }
        }
        SRegex sRegex = new SRegex(prefix , Pattern.compile("&\\w\\s"));
        List<String> results = sRegex.getResultsList();
        for (String s : results ) {
            prefix = prefix.replaceAll(s, s.trim());
        }
        return prefix;
    }


}
