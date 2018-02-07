package us.cyrien.minecordbot.reporters;

import us.cyrien.mcutils.diagnosis.IReporter;

public class JReporter implements IReporter {

    private String name;
    private int priority;

    public JReporter() {
        this.name = "Java Reporter";
        this.priority = 3;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String report() {
        String javaProperty = System.getProperty("java.version");
        javaProperty = javaProperty.replaceAll("_", " ");
        String[] jProps = javaProperty.split(" ");
        String javaVersion = jProps[0];
        String buildNumber = jProps[1];
        return "Java Version: " + javaVersion + "\n" +
                "Build: " + buildNumber;
    }

    @Override
    public int getPriority() {
        return priority;
    }

}
