package us.cyrien.minecordbot.hooks;

import com.onarandombox.MultiverseCore.MultiverseCore;
import io.github.hedgehog1029.frame.hook.IPluginHook;
import org.bukkit.Bukkit;

public class MVHook implements IPluginHook {
    @Override
    public boolean available() {
        return Bukkit.getPluginManager().isPluginEnabled("Multiverse-Core");
    }

    public MultiverseCore getMultiverseCore() {
        return (MultiverseCore) Bukkit.getPluginManager().getPlugin("Multiverse-Core");
    }
}
