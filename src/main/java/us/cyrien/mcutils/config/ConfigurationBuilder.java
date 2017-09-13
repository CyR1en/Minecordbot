package us.cyrien.mcutils.config;

import us.cyrien.mcutils.annotations.Configuration;
import us.cyrien.mcutils.annotations.Setting;
import us.cyrien.mcutils.logger.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public class ConfigurationBuilder {

	private static ArrayList<Class> awaiting = new ArrayList<>();
	private static HashMap<Class, Object> configurations = new HashMap<>();

	public static void add(Class clazz) {
		if (!clazz.isAnnotationPresent(Configuration.class)) {
			Logger.warn("No @Configuration annotation on " + clazz.getName() + "!");
			return;
		}

		awaiting.add(clazz);
	}

	public static void buildAwaiting() {
		for (Class c : awaiting) {
			configurations.put(c, build(c));

			sync(c);
		}
	}

	public static Object get(Class clazz) {
		return configurations.get(clazz);
	}

	/**
	 * Write in-memory changes of config to its file
	 * @param clazz The configuration class to sync.
	 */
	public static void sync(Class clazz) {
		Object config = configurations.get(clazz);

		if (config == null)
			return;

		String name = config.getClass().getDeclaredAnnotation(Configuration.class).value();
		File file = new File(JavaPlugin.getProvidingPlugin(clazz).getDataFolder(), String.format("%s.yml", name));
		FileConfiguration configFile = new YamlConfiguration();

		buildToConfig(config, configFile);

		try {
			configFile.save(file);
		} catch (IOException e) {
			Logger.err("Couldn't sync config " + name + " to its file!");
		}
	}

	public static void syncAll() {
		configurations.keySet().forEach(ConfigurationBuilder::sync);
	}

	/**
	 * Replace the in-memory variant of the configuration with the saved version.
	 * This will silently do nothing if the config class was never loaded (use .add instead)
	 * @param clazz The configuration class to reload.
	 */
	public static void reload(Class clazz) {
		configurations.replace(clazz, build(clazz));
	}

	private static Object build(Class clazz) {
		Object object;

		try {
			object = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			Logger.err("Failed to build a configuration - couldn't access the class!");
			return null;
		}

		String name = object.getClass().getDeclaredAnnotation(Configuration.class).value();
		File file = new File(JavaPlugin.getProvidingPlugin(clazz).getDataFolder(), String.format("%s.yml", name));
		FileConfiguration configFile = YamlConfiguration.loadConfiguration(file);

		buildFromConfig(configFile, clazz, object);

		return object;
	}

	private static void buildToConfig(Object instance, FileConfiguration config) {
		Class clazz = instance.getClass();

		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(Setting.class)) {
				try {
					field.setAccessible(true);
					config.set(field.getName(), field.get(instance));
				} catch (IllegalAccessException | ClassCastException e) {
					Logger.err("Failed to build a configuration - couldn't access a field!");
				}
			}
		}
	}

	private static Object buildFromConfig(FileConfiguration config, Class clazz, Object instance) {
		for (String key : config.getKeys(false)) {
			try {
				Field field = clazz.getField(key);

				field.setAccessible(true);
				fieldSet(field, config, instance, key);
			} catch (NoSuchFieldException ignored) {
				Logger.log(Level.FINER, "A key in YAML was not found in the class it is being rebuilt to.");
			} catch (IllegalAccessException e) {
				Logger.err("Couldn't access a field when rebuilding a config!");
			}
		}

		return instance;
	}

	private static void fieldSet(Field f, FileConfiguration config, Object instance, String key) throws IllegalAccessException {
		if (config.get(key) instanceof Boolean) {
			f.set(instance, config.getBoolean(key));
		} else if (config.get(key) instanceof Integer) {
			f.set(instance, config.getInt(key));
		} else if (config.get(key) instanceof ConfigurationSection) {
			f.set(instance, f.getType().cast(((ConfigurationSection) config.get(key)).getValues(false)));
		} else {
			f.set(instance, f.getType().cast(config.get(key)));
		}
	}
}
