package us.cyrien.minecordbot.accountSync.exceptions;

public class IllegalConfirmKeyException extends Exception {
    String msg;

    public IllegalConfirmKeyException() {
        msg = "Confirm key did not match MCBSync's verification code key";
    }

    public IllegalConfirmKeyException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
