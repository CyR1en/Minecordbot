package us.cyrien.mcutils.hook;

import org.bukkit.Bukkit;

public class PluginHook<T> implements IPluginHook {

    protected String name = "";

    @Override
    public boolean available() {
        return Bukkit.getPluginManager().isPluginEnabled(getPluginName());
    }

    @Override
    public String getPluginName() {
        return name;
    }

    public T getPlugin() {
        return (T) Bukkit.getPluginManager().getPlugin(getPluginName());
    }

}
