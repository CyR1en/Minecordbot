package us.cyrien.minecordbot.prefix;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.onarandombox.MultiverseCore.utils.WorldManager;
import io.github.hedgehog1029.frame.annotations.Hook;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import us.cyrien.minecordbot.hooks.MVHook;
import us.cyrien.minecordbot.hooks.VaultHook;

public enum MinecraftPlaceHolder {
    WORLD {
        @Override
        public String toString() {
            String world;
            if (mvHook != null) {
                MultiverseCore mv = mvHook.getMultiverseCore();
                WorldManager wm = new WorldManager(mv);
                if (wm.isMVWorld(e.getPlayer().getWorld())) {
                    MultiverseWorld mvw = wm.getMVWorld(e.getPlayer().getWorld());
                    world = mvw.getAlias() == null ? mvw.getName() : mvw.getAlias();
                } else {
                    world = e.getPlayer().getWorld().getName();
                }
            } else if(vaultHook != null) {
                Chat c = rgC.getProvider();
                world = c.getPlayerPrefix(e.getPlayer());
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
            if (vaultHook != null) {
                Permission vaultPerm = rgP.getProvider();
                prefix = vaultPerm.getPrimaryGroup(e.getPlayer());
            } else {
                prefix = "";
            }
            prefix = ChatColor.translateAlternateColorCodes('&', prefix);
            return ChatColor.stripColor(prefix);
        }
    };

    private static AsyncPlayerChatEvent e;

    @Hook
    private static MVHook mvHook;
    @Hook
    private static VaultHook vaultHook;

    private static RegisteredServiceProvider<Permission> rgP;
    private static RegisteredServiceProvider<Chat> rgC;

    public void init(AsyncPlayerChatEvent e) {
        MinecraftPlaceHolder.e = e;
        if(vaultHook != null) {
            MinecraftPlaceHolder.rgP = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
            MinecraftPlaceHolder.rgC = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        }
    }
}
