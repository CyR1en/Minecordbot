package us.cyrien.minecordbot.prefix;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.bukkit.entity.Player;

public class PrefixParser {

    public static String parseDiscordPrefixes(String prefix, MessageReceivedEvent e) {
        for(DiscordPlaceHolders s : DiscordPlaceHolders.values() ) {
            s.init(e);
            String pH = "{" + s.name().toLowerCase() + "}";
            String pH0 ="\\{" + s.name().toLowerCase() + "}";
            if (prefix.contains(pH)) {
                prefix = prefix.replaceAll(pH0, s.toString());
            }
        }
        return prefix;
    }

    public static String parseMinecraftPrefix(String prefix, Player p) {
        for(MinecraftPlaceHolder s : MinecraftPlaceHolder.values()) {
            s.init(p);
            String pH = "{" + s.name().toLowerCase() + "}";
            String pH0 ="\\{" + s.name().toLowerCase() + "}";
            if (prefix.contains(pH)) {
                prefix = prefix.replaceAll(pH0, s.toString());
            }
        }
        return prefix;
    }
}
