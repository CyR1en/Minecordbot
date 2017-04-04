package us.cyrien.minecordbot.core;


import io.github.hedgehog1029.frame.module.ModuleLoader;
import us.cyrien.minecordbot.core.inject.DFrameInjector;
import us.cyrien.minecordbot.core.loader.DCommandInjector;
import us.cyrien.minecordbot.main.Minecordbot;

public class DrocsidFrame {

    public static void main() {
        Minecordbot.DEBUG_LOGGER.info("========= FRAME INJECTIONS ========="); // FIXME: 3/25/2017
        new DFrameInjector().injector(new DCommandInjector()).injectAll();
        Minecordbot.DEBUG_LOGGER.info("====== FRAME INJECTION SUCCESS ====="); // FIXME: 3/25/2017
    }

    public static void addModule(Class clazz) {
        Minecordbot.DEBUG_LOGGER.info("ADDING " + clazz.getSimpleName().toUpperCase() + " MODULE"); // FIXME: 3/25/2017
        ModuleLoader.add(clazz);
    }

}
