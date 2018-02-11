package us.cyrien.mcutils.diagnosis.impl;

import us.cyrien.mcutils.diagnosis.Diagnostics;
import us.cyrien.mcutils.diagnosis.IReporter;
import us.cyrien.mcutils.diagnosis.TypeDiagnosis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

public class DiagnosticsImpl implements Diagnostics {

    private LinkedList<IReporter> reporters;
    private TypeDiagnosis typeDiagnosis;

    public DiagnosticsImpl setReporters(LinkedList<IReporter> reporters) {
        this.reporters = reporters;
        return this;
    }

    public DiagnosticsImpl setTypeDiagnosis(TypeDiagnosis typeDiagnosis) {
        this.typeDiagnosis = typeDiagnosis;
        return this;
    }

    @Override
    public LinkedList<IReporter> getReporters() {
        return null;
    }

    @Override
    public TypeDiagnosis getTypeDiagnosis() {
        return typeDiagnosis;
    }

    @Override
    public File fullReport() {
        try {
            File file = new File("Diagnostics.txt");
            FileWriter fW = new FileWriter(file);
            PrintWriter pWriter = new PrintWriter(fW);
            pWriter.println(buildReport());
            pWriter.flush();
            pWriter.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    private String buildReport() {
        final StringBuilder sb = new StringBuilder();
        reporters.forEach(r ->  {
            if(r.reportHeader() != null) {
                sb.append(r.reportHeader());
            }
            sb.append(Diagnostics.LINE_SEPARATOR).append(r.report())
                    .append(Diagnostics.DOUBLE_LINE_SEPARATOR);
        });
        return sb.toString();
    }
}
