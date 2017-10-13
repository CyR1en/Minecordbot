package us.cyrien.mcutils.dispatcher.exception;

public class CommandExistsException extends Exception {
    String command;

    public CommandExistsException(String command) {
        this.command = command;
    }
}
