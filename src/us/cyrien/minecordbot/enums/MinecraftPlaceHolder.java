package us.cyrien.minecordbot.enums;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.onarandombox.MultiverseCore.utils.WorldManager;
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
            String world;
            if (Bukkit.getPluginManager().isPluginEnabled("Multiverse-Core")) {
                MultiverseCore mv = (MultiverseCore) Bukkit.getPluginManager().getPlugin("Multiverse-Core");
                WorldManager wm = new WorldManager(mv);
                if (wm.isMVWorld(e.getPlayer().getWorld())) {
                    MultiverseWorld mvw = wm.getMVWorld(e.getPlayer().getWorld());
                    world = mvw.getAlias() == null ? mvw.getName() : mvw.getAlias();
                } else {
                    world = e.getPlayer().getWorld().getName();
                }
            } else {
                world = e.getPlayer().getWorld().getName();
            }
            return world;
        }
    },
    SENDER {
        @Override
        public String toString() {
            return ChatColor.stripColor(e.getPlayer().getName());
        }
    },
    NAME {
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
    RANK {
        @Override
        public String toString() {
            String prefix;
            if (Bukkit.getPluginManager().isPluginEnabled("PermissionsEx")) {
                PermissionUser user = PermissionsEx.getUser(e.getPlayer());
                prefix = user.getPrefix();
                if (prefix.equals("")) {
                    prefix = user.getSuffix();
                    if (prefix.equals(""))
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
