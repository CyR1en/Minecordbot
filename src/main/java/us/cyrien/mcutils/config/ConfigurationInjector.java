package us.cyrien.mcutils.config;

import us.cyrien.mcutils.annotations.InjectConfig;
import us.cyrien.mcutils.inject.Injector;
import us.cyrien.mcutils.logger.Logger;

import java.lang.reflect.Field;

public class ConfigurationInjector implements Injector {
	@Override
	public void inject(Class<?> c, Object instance) {
		for (Field f : c.getDeclaredFields()) {
			if (f.isAnnotationPresent(InjectConfig.class)) {
				Object config = ConfigurationBuilder.get(f.getType());

				if (config != null) {
					try {
						f.setAccessible(true);
						f.set(instance, config);
					} catch (IllegalAccessException e) {
						Logger.err("Failed to inject a configuration into field " + f.getName() + " in " + c.getName());
					}
				}
			}
		}
	}
}
