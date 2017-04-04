package us.cyrien.minecordbot.core.module;

import java.util.HashSet;
import java.util.Set;

public class MCBModuleLoader {
    private static HashSet<Class> annotated = new HashSet();

    public MCBModuleLoader() {
    }

    public static void add(Class clazz) {
        annotated.add(clazz);
    }

    public static Set<Class> getModuleClasses() {
        return annotated;
    }

}
