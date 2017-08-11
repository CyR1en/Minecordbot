package us.cyrien.minecordbot.AccountSync.exceptions;

public class IIllegalAuthTokenFormatException extends Exception {

    String msg;

    public IIllegalAuthTokenFormatException() {
        msg = "AuthToken not in valid format";
    }

    public IIllegalAuthTokenFormatException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

}
