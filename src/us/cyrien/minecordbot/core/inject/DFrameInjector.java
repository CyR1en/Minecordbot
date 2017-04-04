package us.cyrien.minecordbot.core.inject;

import io.github.hedgehog1029.frame.inject.Injector;
import io.github.hedgehog1029.frame.module.ModuleLoader;

import java.util.ArrayList;
import java.util.Iterator;

public class DFrameInjector {
    private ArrayList<Injector> injectors = new ArrayList();

    public DFrameInjector() {
    }

    public DFrameInjector injector(Injector injector) {
        this.injectors.add(injector);
        return this;
    }

    public void injectAll() {
        Iterator var1 = ModuleLoader.getModuleClasses().iterator();

        while(var1.hasNext()) {
            Class<?> clazz = (Class)var1.next();
            Object instance = ModuleLoader.getInstance(clazz);
            this.injectors.forEach((i) -> i.inject(clazz, instance));
        }

    }
}
