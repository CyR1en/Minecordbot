package us.cyrien.minecordbot.hooks;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import us.cyrien.mcutils.hook.PluginHook;

public class VaultHook extends PluginHook {

    private RegisteredServiceProvider<Permission> rgP;
    private RegisteredServiceProvider<Chat> rgC;
    private RegisteredServiceProvider<Economy> rgE;

    private static Chat chat;
    private static Permission permission;
    private static Economy economy;

    public VaultHook() {
        this.name = "Vault";
        setupEconomy();
        setupChat();
        setupPermissions();
    }

    private boolean setupEconomy() {
        if (!available()) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    private boolean setupChat() {
        if (!available()) {
            return false;
        }
        RegisteredServiceProvider<Chat> rsp = Bukkit.getServicesManager().getRegistration(Chat.class);
        if (rsp == null) {
            return false;
        }
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
        if (!available()) {
            return false;
        }
        RegisteredServiceProvider<Permission> rsp = Bukkit.getServicesManager().getRegistration(Permission.class);
        if (rsp == null) {
            return false;
        }
        permission = rsp.getProvider();
        return permission != null;
    }

    public Chat getChat() {
        return chat;
    }

    public Permission getPermission() {
        return permission;
    }

    public Economy getEconomy() {
        return economy;
    }
}
