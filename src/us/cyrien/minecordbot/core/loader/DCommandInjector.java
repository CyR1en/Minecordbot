package us.cyrien.minecordbot.core.loader;


import io.github.hedgehog1029.frame.inject.Injector;
import us.cyrien.minecordbot.core.annotation.DCommand;
import us.cyrien.minecordbot.core.dispatcher.DCommandDispatcher;

import java.lang.reflect.Method;

public class DCommandInjector implements Injector {
    public DCommandInjector() {
    }

    public void inject(Class<?> c, Object instance) {
        Method[] methods = c.getDeclaredMethods();
        Method[] var4 = methods;
        int var5 = methods.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Method method = var4[var6];
            if(method.isAnnotationPresent(DCommand.class)) {
                DCommand details = method.getAnnotation(DCommand.class);
                DCommandDispatcher.getDispatcher().registerCommand(details, method, instance);
            }
        }

    }
}
