package us.cyrien.mcutils.module;

import us.cyrien.mcutils.logger.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ModuleLoader {
	private static HashSet<Class> annotated = new HashSet<>();
	private static HashMap<Class, Object> instances = new HashMap<>();

	public static void add(Class clazz) {
		annotated.add(clazz);
	}

	public static Set<Class> getModuleClasses() {
		return annotated;
	}

	public static Object getInstance(Class clazz) {
		if (!instances.containsKey(clazz)) {
			try {
				instances.put(clazz, clazz.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				Logger.err("Failed to instantiate a module!");
				return null;
			}
		}

		return instances.get(clazz);
	}
}
