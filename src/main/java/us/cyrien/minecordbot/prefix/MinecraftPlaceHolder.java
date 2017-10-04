package us.cyrien.minecordbot.prefix;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.onarandombox.MultiverseCore.utils.WorldManager;
import net.milkbowl.vault.chat.Chat;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import us.cyrien.mcutils.annotations.Hook;
import us.cyrien.minecordbot.HookContainer;
import us.cyrien.minecordbot.hooks.MVHook;
import us.cyrien.minecordbot.hooks.PermissionsExHook;
import us.cyrien.minecordbot.hooks.VaultHook;

public enum MinecraftPlaceHolder {
    WORLD {
        @Override
        public String toString() {
            String world = e.getPlayer().getWorld().getName();
            if (mvHook != null) {
                MultiverseCore mv = mvHook.getPlugin();
                WorldManager wm = new WorldManager(mv);
                if (wm.isMVWorld(e.getPlayer().getWorld())) {
                    MultiverseWorld mvw = wm.getMVWorld(e.getPlayer().getWorld());
                    world = mvw.getAlias() == null ? mvw.getName() : mvw.getAlias();
                } else {
                    world = e.getPlayer().getWorld().getName();
                }
            } else if (vaultHook != null) {
                Chat c = vaultHook.getChat();
                if (c != null)
                    world = c.getPlayerPrefix(e.getPlayer());
            }
            return world == null ? "" : world;
        }
    },
    ENAME {
        @Override
        public String toString() {
            Player p = e.getPlayer();
            return ChatColor.stripColor(p.getDisplayName());
        }
    },
    ERANK {
        @Override
        public String toString() {
            String prefix = "";
            if (vaultHook != null) {
                Chat c = vaultHook.getChat();
                if (c != null) {
                    String group = c.getPrimaryGroup(e.getPlayer());
                    prefix = c.getGroupPrefix(e.getPlayer().getWorld(), group);
                    prefix = prefix == null || StringUtils.isEmpty(prefix) ? group : prefix;
                }
            } else if (pexHook != null) {
                PermissionsEx pex = pexHook.getPlugin();
                PermissionUser pexUser = pex.getPermissionsManager().getUser(e.getPlayer().getUniqueId());
                prefix = pexUser.getPrefix(e.getPlayer().getWorld().getName());
            }
            prefix = ChatColor.translateAlternateColorCodes('&', prefix);
            return ChatColor.stripColor(prefix);
        }
    },
    RANK_GROUP {
        @Override
        public String toString() {
            String prefix = "";
            if (vaultHook != null) {
                Chat c = vaultHook.getChat();
                if (c != null) {
                    prefix = c.getPrimaryGroup(e.getPlayer());
                }
            } else if (pexHook != null) {
                PermissionsEx pex = pexHook.getPlugin();
                PermissionUser pexUser = pex.getPermissionsManager().getUser(e.getPlayer().getUniqueId());
                prefix = pexUser.getPrefix(e.getPlayer().getWorld().getName());
            }
            prefix = ChatColor.translateAlternateColorCodes('&', prefix);
            return ChatColor.stripColor(prefix);
        }
    },
    RANK_PREFIX {
        @Override
        public String toString() {
            String prefix = "";
            if (vaultHook != null) {
                Chat c = vaultHook.getChat();
                if (c != null) {
                    String group = c.getPrimaryGroup(e.getPlayer());
                    prefix = c.getGroupPrefix(e.getPlayer().getWorld(), group);
                    prefix = prefix == null || StringUtils.isEmpty(prefix) ? "" : prefix;
                }
            } else if (pexHook != null) {
                PermissionsEx pex = pexHook.getPlugin();
                PermissionUser pexUser = pex.getPermissionsManager().getUser(e.getPlayer().getUniqueId());
                prefix = pexUser.getPrefix(e.getPlayer().getWorld().getName());
            }
            prefix = ChatColor.translateAlternateColorCodes('&', prefix);
            return ChatColor.stripColor(prefix);
        }
    },
    SENDER,
    NAME;

    private static AsyncPlayerChatEvent e;

    @Hook
    private final static MVHook mvHook = HookContainer.getMvHook();
    @Hook
    private final static VaultHook vaultHook = HookContainer.getVaultHook();
    @Hook
    private final static PermissionsExHook pexHook = HookContainer.getPermissionsExHook();

    public void init(AsyncPlayerChatEvent e) {
        MinecraftPlaceHolder.e = e;
    }

    @Override
    public String toString() {
        return ChatColor.stripColor(e.getPlayer().getName());
    }
}
