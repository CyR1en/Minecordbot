package us.cyrien.mcutils.loader;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import us.cyrien.mcutils.annotations.Command;
import us.cyrien.mcutils.annotations.HelpTopic;
import us.cyrien.mcutils.annotations.Permission;
import us.cyrien.mcutils.dispatcher.CommandDispatcher;
import us.cyrien.mcutils.dispatcher.exception.IncorrectArgumentsException;
import us.cyrien.mcutils.dispatcher.exception.NoPermissionException;
import us.cyrien.mcutils.loader.exception.InaccessibleMethodException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandMapping extends org.bukkit.command.Command {
    private Command command;
    private Permission permission;
    private HelpTopic helpTopic;
    private Method method;
    private Object container;

    public CommandMapping(Command cmd, Method method, Object object) {
	    super(cmd.aliases()[0], cmd.desc(), cmd.usage(), Arrays.asList(cmd.aliases()));

	    this.command = cmd;
        this.method = method;
        this.container = object;

        if (method.isAnnotationPresent(Permission.class)) {
            this.permission = method.getAnnotation(Permission.class);
        }

        if (method.isAnnotationPresent(HelpTopic.class)) {
            this.helpTopic = method.getAnnotation(HelpTopic.class);
        }
    }

    public Command getCommand() {
        return this.command;
    }

    public Method getMethod() {
        return this.method;
    }

    public String getHelpTopic() {
        if (helpTopic != null)
            return this.helpTopic.value();
        else
            return "Commands";
    }

    public String getPermission() {
        if (permission != null)
            return this.permission.value();
        else
            return "";
    }

    public List<String> getAliases() {
	    ArrayList<String> aliases = new ArrayList<>(Arrays.asList(command.aliases()));
	    aliases.remove(0);

        return aliases;
    }

	public String getUsage() {
		return ChatColor.RED + "Usage: " + this.usageMessage;
	}

    public void invoke(Object... args) throws InaccessibleMethodException {
        try {
            method.invoke(container, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new InaccessibleMethodException(method);
        }
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
	    try {
		    return CommandDispatcher.getDispatcher().dispatch(commandSender, this, strings);
	    } catch (IncorrectArgumentsException e) {
		    commandSender.sendMessage(ChatColor.RED + e.getReason() + "\n" + this.getUsage());
		    return false;
	    } catch (NoPermissionException e) {
		    commandSender.sendMessage(ChatColor.RED + "You don't have permission!");
		    return false;
	    }
    }
}
