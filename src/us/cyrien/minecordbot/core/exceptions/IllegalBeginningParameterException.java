package us.cyrien.minecordbot.core.exceptions;


public class IllegalBeginningParameterException extends Exception{

    String msg;

    public IllegalBeginningParameterException(String s) {
        msg = s;
    }
}
