package us.cyrien.mcutils.loader;

import us.cyrien.mcutils.annotations.Command;
import us.cyrien.mcutils.dispatcher.CommandDispatcher;
import us.cyrien.mcutils.inject.Injector;

import java.lang.reflect.Method;

public class CommandInjector implements Injector {
    @Override
    public void inject(Class<?> c, Object instance) {
        final Method[] methods = c.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(Command.class)) {
                Command details = method.getAnnotation(Command.class);

	            CommandDispatcher.getDispatcher().registerCommand(details, method, instance);
            }
        }
    }
}
