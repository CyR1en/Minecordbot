package us.cyrien.minecordbot.hook;

import io.github.hedgehog1029.frame.hook.IPluginHook;
import org.bukkit.Bukkit;
import us.cyrien.minecordbot.main.Minecordbot;


public class MCBHook implements IPluginHook {

    @Override
    public boolean available() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled("MineCordBot");
    }

    public Minecordbot getMinecordbot() {
        if(available())
            return (Minecordbot) Bukkit.getPluginManager().getPlugin("MineCordBot");
        return null;
    }
}
