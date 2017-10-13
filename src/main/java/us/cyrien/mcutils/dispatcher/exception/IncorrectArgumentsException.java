package us.cyrien.mcutils.dispatcher.exception;

public class IncorrectArgumentsException extends Exception {

    private String message;

    public IncorrectArgumentsException(String msg) {
        this.message = msg;
    }

    public String getReason() {
        return this.message;
    }
}
