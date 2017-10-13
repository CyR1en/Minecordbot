package us.cyrien.mcutils.loader.exception;

public class InaccessibleClassException extends Exception {

    private Class offender;

    public InaccessibleClassException(Class clazz) {
        this.offender = clazz;
    }

    public Class getOffender() {
        return this.offender;
    }
}
