package us.cyrien.minecordbot.hooks;

import io.github.hedgehog1029.frame.hook.IPluginHook;
import org.bukkit.Bukkit;

public class VaultHook implements IPluginHook {
    @Override
    public boolean available() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled("Vault");
    }

}
