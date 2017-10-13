package us.cyrien.minecordbot.accountSync.exceptions;

public class IllegalConfirmRequesterException extends Exception{
    String msg;

    public IllegalConfirmRequesterException() {
        msg = "Confirm requester did not match MCBSync's verification code requester";
    }


    public IllegalConfirmRequesterException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
