package us.cyrien.minecordbot.core;


import io.github.hedgehog1029.frame.module.ModuleLoader;
import us.cyrien.minecordbot.core.inject.DFrameInjector;
import us.cyrien.minecordbot.core.loader.DCommandInjector;

public class DrocsidFrame {

    public static void main() {
        new DFrameInjector().injector(new DCommandInjector()).injectAll();
    }

    public static void addModule(Class clazz) {
        ModuleLoader.add(clazz);
    }

}
