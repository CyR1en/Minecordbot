package us.cyrien.minecordbot.hooks;

import org.bukkit.Bukkit;
import us.cyrien.mcutils.hook.IPluginHook;
import us.cyrien.minecordbot.Minecordbot;

public class MCBHook implements IPluginHook {

    @Override
    public boolean available() {
        return Bukkit.getPluginManager().isPluginEnabled("MineCordBot");
    }

    @Override
    public String getPluginName() {
        return "";
    }

    public Minecordbot getPlugin() {
        return (Minecordbot) Bukkit.getPluginManager().getPlugin("MineCordBot");
    }
}
