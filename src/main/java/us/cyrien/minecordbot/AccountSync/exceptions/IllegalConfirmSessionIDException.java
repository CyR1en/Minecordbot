package us.cyrien.minecordbot.AccountSync.exceptions;

public class IllegalConfirmSessionIDException extends Exception {

    String msg;

    public IllegalConfirmSessionIDException() {
        msg = "Confirm requester did not match MCBSync verification code session id";
    }


    public IllegalConfirmSessionIDException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
