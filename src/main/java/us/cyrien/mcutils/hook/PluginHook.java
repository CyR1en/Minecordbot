package us.cyrien.mcutils.hook;

import org.bukkit.Bukkit;

/**
 * Easily hook other plugins by extending this class to a subclass.
 * Interfaces {@link us.cyrien.mcutils.hook.IPluginHook}
 *
 * <p> PluginHook implementation example.
 * <pre>
 *         {@code
 *              public MCBHook extends PluginHook<Minecordbot> {
 *                  public MCBHook() {
 *                      this.name = "MineCordBot";
 *                  }
 *              }
 *         }
 *     </pre>
 * </p>
 *
 * @param <T> Type of Plugin that's going to get hooked.
 */
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
