package us.cyrien.mcutils.diagnosis;

import us.cyrien.mcutils.diagnosis.impl.DiagnosticsImpl;
import us.cyrien.mcutils.logger.Logger;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;

public class DiagnosticsBuilder {

    private LinkedList<IReporter> reporters;
    private TypeDiagnosis typeDiagnosis;

    public DiagnosticsBuilder() {
        this(null);
    }

    public DiagnosticsBuilder(Diagnostics diagnosis) {
        if (diagnosis != null)
            reporters = diagnosis.getReporters();
        else
            reporters = new LinkedList<>();
        typeDiagnosis = TypeDiagnosis.MANUAL;
    }

    @SafeVarargs
    public final DiagnosticsBuilder setTypeDiagnosis(TypeDiagnosis typeDiagnosis, Class<? extends IReporter>... reporters) {
        if(typeDiagnosis == null)
            return this;
        this.typeDiagnosis = typeDiagnosis;
        switch (typeDiagnosis) {
            case ALL:
                if(reporters.length != 0)
                    throw new IllegalArgumentException("TypeDiagnosis.ALL can't have any explicit reporters.");
                ReportLoader.getReporters().forEach((cK, cV) -> this.reporters.add(cV));
                break;
            case PARTIAL:
                if(reporters == null)
                    throw new IllegalArgumentException("No explicit reporters have been provided for TypeDiagnosis.PARTIAL.");
                for (Class<? extends IReporter> iReporter : reporters) {
                    Map<Class<? extends IReporter>, IReporter> rList = ReportLoader.getReporters();
                    if(rList.containsKey(iReporter))
                        this.reporters.add(rList.get(iReporter));
                    else
                        Logger.err("Could not include " + iReporter.getSimpleName() + " to TypeDiagnosis.Partial; Was it registered?");
                }
                break;
        }
        return this;
    }

    public DiagnosticsBuilder appendReporter(IReporter reporter) {
        if (!reporters.contains(reporter) && typeDiagnosis == TypeDiagnosis.MANUAL)
            reporters.add(reporter);
        return this;
    }


    public Diagnostics build() {
        if (isValidDiagnosis()) {
            Collections.sort(reporters);
            return new DiagnosticsImpl().setTypeDiagnosis(typeDiagnosis).setReporters(reporters);
        } else
            throw new IllegalStateException("Could not build Diagnostic because of improper use of " + this.getClass().getSimpleName());
    }

    private boolean isValidDiagnosis() {
        switch (typeDiagnosis) {
            case ALL:
                return true;
            case PARTIAL:
                return reporters.size() != 0;
            case MANUAL:
                return reporters.size() != 0;
            default:
                return false;
        }
    }
}
