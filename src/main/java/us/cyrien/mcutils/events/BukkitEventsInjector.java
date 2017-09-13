package us.cyrien.mcutils.events;

import us.cyrien.mcutils.inject.Injector;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitEventsInjector implements Injector {
	@Override
	public void inject(Class<?> c, Object instance) {
		if (instance instanceof Listener)
			Bukkit.getPluginManager().registerEvents((Listener) instance, JavaPlugin.getProvidingPlugin(c));
	}
}
