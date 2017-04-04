package us.cyrien.minecordbot.core.loader;


import io.github.hedgehog1029.frame.inject.Injector;
import us.cyrien.minecordbot.core.annotation.DCommand;
import us.cyrien.minecordbot.core.dispatcher.DCommandDiscpatcher;
import us.cyrien.minecordbot.main.Minecordbot;

import java.lang.reflect.Method;
import java.util.Arrays;

public class DCommandInjector implements Injector {
    public DCommandInjector() {
    }

    public void inject(Class<?> c, Object instance) {
        Minecordbot.DEBUG_LOGGER.info(c.getSimpleName().toUpperCase() + " COMMAND INJECTION ---"); // FIXME: 3/25/2017
        Method[] methods = c.getDeclaredMethods();
        Method[] var4 = methods;
        int var5 = methods.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Method method = var4[var6];
            if(method.isAnnotationPresent(DCommand.class)) {
                DCommand details = method.getAnnotation(DCommand.class);
                // FIXME: 3/25/2017 
                Minecordbot.DEBUG_LOGGER.info("ALIASES : " + Arrays.toString(details.aliases()));
                Minecordbot.DEBUG_LOGGER.info("USAGE : " + details.usage());
                Minecordbot.DEBUG_LOGGER.info("DESCRIPTION : " + details.desc());
                // FIXME: 3/25/2017
                DCommandDiscpatcher.getDispatcher().registerCommand(details, method, instance);
                Minecordbot.DEBUG_LOGGER.info(c.getSimpleName().toUpperCase() + " COMMAND  INJECTED ---");
            }
        }

    }
}
