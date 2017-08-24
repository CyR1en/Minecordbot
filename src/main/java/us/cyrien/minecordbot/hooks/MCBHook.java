package us.cyrien.minecordbot.hooks;

import io.github.hedgehog1029.frame.hook.IPluginHook;
import org.bukkit.Bukkit;
import us.cyrien.minecordbot.Minecordbot;


public class MCBHook implements IPluginHook {

    @Override
    public boolean available() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled("MineCordBot");
    }

    public Minecordbot getMinecordbot() {
        return (Minecordbot) Bukkit.getPluginManager().getPlugin("MineCordBot");
    }
}
