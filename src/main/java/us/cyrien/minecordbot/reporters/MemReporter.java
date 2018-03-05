package us.cyrien.minecordbot.reporters;

import us.cyrien.mcutils.diagnosis.Diagnostics;
import us.cyrien.mcutils.diagnosis.IReporter;

public class MemReporter implements IReporter {

    private String name;
    private int priority;

    public MemReporter() {
        this.name = "Memory Reporter";
        this.priority = 2;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String report() {
        StringBuilder sb = new StringBuilder();
        long allocated = Runtime.getRuntime().totalMemory() / 1024L / 1024L;
        long free = Runtime.getRuntime().freeMemory() / 1024L / 1024L;
        long used = allocated - free;
        sb.append("Allocated Memory: ").append(allocated).append("mb").append(Diagnostics.LINE_SEPARATOR);
        sb.append("Free Memory: ").append(free).append("mb").append(Diagnostics.LINE_SEPARATOR);
        sb.append("Used Memory: ").append(used).append("mb").append(Diagnostics.LINE_SEPARATOR);
        sb.append("Memory Usage: ").append(((double) used / allocated) * 100.0).append("%");
        return sb.toString();
    }

    @Override
    public int getPriority() {
        return priority;
    }

}
