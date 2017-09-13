package us.cyrien.mcutils.hook;

import us.cyrien.mcutils.annotations.Hook;
import us.cyrien.mcutils.inject.Injector;
import us.cyrien.mcutils.logger.Logger;

import java.lang.reflect.Field;

public class HookInjector implements Injector {
	@Override
	public void inject(Class<?> c, Object instance) {
		for (Field f : c.getDeclaredFields()) {
			if (f.isAnnotationPresent(Hook.class)) {
				IPluginHook hook = HookLoader.get(f.getType());
				f.setAccessible(true);

				if (hook == null) {
					Logger.warn("Couldn't find hook " + f.getType().getName() + ", was it registered?");
					continue;
				}

				try {
					if (hook.available())
						f.set(instance, hook);
					else
						f.set(instance, null);
				} catch (IllegalAccessException e) {
					Logger.err("Couldn't access field " + f.getName() + "!");
				}
			}
		}
	}
}
