package us.cyrien.minecordbot.accountSync.exceptions;

public class IllegalConfirmSessionIDException extends Exception {

    String msg;

    public IllegalConfirmSessionIDException() {
        msg = "Confirm session id did not match MCBSync's verification code session id";
    }


    public IllegalConfirmSessionIDException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
