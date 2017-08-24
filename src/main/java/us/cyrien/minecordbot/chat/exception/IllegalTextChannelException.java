package us.cyrien.minecordbot.chat.exception;

public class IllegalTextChannelException extends Exception {

    String msg;

    public IllegalTextChannelException(String msg) {
        this.msg = msg;
    }
}
