package us.cyrien.minecordbot.core.exceptions;

public class IllegalTextChannelException extends Exception {

    String msg;

    public IllegalTextChannelException(String msg) {
        this.msg = msg;
    }
}
