package us.cyrien.mcutils.hook;

import us.cyrien.mcutils.logger.Logger;

import java.util.HashMap;

public class HookLoader {

	private static HashMap<Class<? extends IPluginHook>, IPluginHook> hooks = new HashMap<>();

	public static void addHook(Class<? extends IPluginHook> clazz) {
		try {
			hooks.put(clazz, clazz.newInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			Logger.err("Error instantiating hook class; is it private? Offender: " + clazz.getName());
		}
	}

	public static IPluginHook get(Class clazz) {
		return hooks.get(clazz);
	}
}
