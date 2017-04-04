package us.cyrien.minecordbot.parse;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import us.cyrien.minecordbot.enums.DiscordPlaceHolders;
import us.cyrien.minecordbot.enums.MinecraftPlaceHolder;

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

    public static String parseMinecraftPrefix(String prefix, AsyncPlayerChatEvent e) {
        for(MinecraftPlaceHolder s : MinecraftPlaceHolder.values()) {
            s.init(e);
            String pH = "{" + s.name().toLowerCase() + "}";
            String pH0 ="\\{" + s.name().toLowerCase() + "}";
            System.out.println(prefix);
            System.out.println(pH);
            if (prefix.contains(pH)) {
                prefix = prefix.replaceAll(pH0, s.toString());
            }
        }
        return prefix;
    }
}
