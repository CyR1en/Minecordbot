package us.cyrien.minecordbot.enums;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

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
    },
    NAME{
        @Override
        public String toString() {
            return ChatColor.stripColor(e.getPlayer().getName());
        }
    },
    ENAME {
        @Override
        public String toString() {
            Player p = e.getPlayer();
            return ChatColor.stripColor(p.getDisplayName());
        }
    },
    RANK{
        @Override
        public String toString() {
            PermissionUser user = PermissionsEx.getUser(e.getPlayer());
            String prefix;
            if(Bukkit.getPluginManager().isPluginEnabled("PermissionsEx")) {
                prefix = user.getPrefix();
                if(prefix.equals("")) {
                    prefix = user.getSuffix();
                    if(prefix.equals(""))
                        prefix = "";
                }
            } else {
                prefix = "";
            }
            prefix = ChatColor.translateAlternateColorCodes('&', prefix);
            return ChatColor.stripColor(prefix);
        }
    };

    private static AsyncPlayerChatEvent e;

    public void init(AsyncPlayerChatEvent e) {
        MinecraftPlaceHolder.e = e;
    }
}
