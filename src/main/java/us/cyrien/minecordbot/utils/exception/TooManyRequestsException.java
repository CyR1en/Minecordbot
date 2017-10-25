package us.cyrien.minecordbot.utils.exception;

public class TooManyRequestsException extends Exception {

    private String msg;

    public TooManyRequestsException(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
