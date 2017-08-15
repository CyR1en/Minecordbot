package us.cyrien.minecordbot.prefix;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.onarandombox.MultiverseCore.utils.WorldManager;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import us.cyrien.minecordbot.Minecordbot;

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
            } else if(Bukkit.getPluginManager().isPluginEnabled("Vault")) {
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
            if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
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

    private static RegisteredServiceProvider<Permission> rgP;
    private static RegisteredServiceProvider<Chat> rgC;

    public void init(AsyncPlayerChatEvent e) {
        MinecraftPlaceHolder.e = e;
        if(Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            MinecraftPlaceHolder.rgP = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
            MinecraftPlaceHolder.rgC = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        }
    }
}
