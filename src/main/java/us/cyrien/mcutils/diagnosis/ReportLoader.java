package us.cyrien.mcutils.diagnosis;

import us.cyrien.mcutils.logger.Logger;

import java.util.HashMap;

public class ReportLoader {

    private static HashMap<Class<? extends IReporter>, IReporter> reporters = new HashMap<>();

    public static void addReporter(Class<? extends IReporter> clazz) {
        try {
            reporters.put(clazz, clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            Logger.err("Error instantiating reporter class; is it private? Offender: " + clazz.getName());
        }
    }

    public static  HashMap<Class<? extends IReporter>, IReporter> getReporters() {
        return reporters;
    }

    public static IReporter get(Class clazz) {
        return reporters.get(clazz);
    }
}
