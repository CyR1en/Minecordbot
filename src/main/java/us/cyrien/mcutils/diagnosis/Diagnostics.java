package us.cyrien.mcutils.diagnosis;

import java.io.File;
import java.util.LinkedList;

/**
 * <p>Gathers reporters and have a full read back through a file or Output </p>
 */
public interface Diagnostics {

    String LINE_SEPARATOR = "\r\n";
    String DOUBLE_LINE_SEPARATOR = "\r\n\r\n";

    LinkedList<IReporter> getReporters();

    File fullReport();

    TypeDiagnosis getTypeDiagnosis();

}
