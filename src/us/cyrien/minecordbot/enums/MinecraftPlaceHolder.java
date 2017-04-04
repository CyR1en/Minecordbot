package us.cyrien.minecordbot.enums;

import org.bukkit.ChatColor;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public enum MinecraftPlaceHolder {
    WORLD {
        @Override
        public String toString() {
            return e.getPlayer().getWorld().getName();
        }
    },
    SENDER {
        @Override
        public String toString() {
            return ChatColor.stripColor(e.getPlayer().getName());
        }
    };

    private static AsyncPlayerChatEvent e;

    public void init(AsyncPlayerChatEvent e) {
        MinecraftPlaceHolder.e = e;
    }
}
