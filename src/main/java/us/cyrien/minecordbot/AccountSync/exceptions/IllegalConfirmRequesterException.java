package us.cyrien.minecordbot.AccountSync.exceptions;

public class IllegalConfirmRequesterException extends Exception{
    String msg;

    public IllegalConfirmRequesterException() {
        msg = "Confirm requester did not match MCBSync verification code requester";
    }


    public IllegalConfirmRequesterException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
