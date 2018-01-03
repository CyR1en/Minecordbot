package us.cyrien.mcutils.diagnosis;

public interface IReporter extends Comparable<IReporter> {

    String getName();

    String report();

    int getPriority();

    @Override
    default int compareTo(IReporter o) {
        return this.getPriority() - o.getPriority();
    }

    default String reportHeader() {
        if(getName() == null)
            return null;
        return "------------------------------------------------------" + Diagnostics.LINE_SEPARATOR +
                getName()  + Diagnostics.LINE_SEPARATOR +
                "------------------------------------------------------";
    }
}
