package us.cyrien.minecordbot.core.loader;

import io.github.hedgehog1029.frame.dispatcher.exception.IncorrectArgumentsException;
import io.github.hedgehog1029.frame.dispatcher.exception.NoPermissionException;
import io.github.hedgehog1029.frame.loader.exception.InaccessibleMethodException;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import us.cyrien.minecordbot.core.annotation.DCommand;
import us.cyrien.minecordbot.core.annotation.DPermission;
import us.cyrien.minecordbot.core.dispatcher.DCommandDispatcher;
import us.cyrien.minecordbot.core.enums.PermissionLevel;
import us.cyrien.minecordbot.core.exceptions.IllegalBeginningParameterException;
import us.cyrien.minecordbot.core.exceptions.IllegalTextChannelException;
import us.cyrien.minecordbot.core.module.DiscordCommand;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DCommandMapping extends DiscordCommand {
    private DCommand command;
    private Method method;
    private Object container;

    public DCommandMapping(DCommand cmd, Method method, Object object) {
        super(cmd.aliases()[0], cmd.desc(), cmd.usage(), Arrays.asList(cmd.aliases()), cmd.type());

        this.command = cmd;
        this.method = method;
        this.container = object;

        if (method.isAnnotationPresent(DPermission.class)) {
            this.permission = method.getAnnotation(DPermission.class).value();
        } else if (method.isAnnotationPresent(DCommand.class)) {
            this.permission = PermissionLevel.LEVEL_0;
        }
    }

    public DCommand getCommand() {
        return this.command;
    }

    public Object getContainer() {
        return container;
    }

    public Method getMethod() {
        return this.method;
    }

    public List<String> getAliases() {
        ArrayList<String> aliases = new ArrayList<>(Arrays.asList(command.aliases()));
        aliases.remove(0);

        return aliases;
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
    public boolean execute(MessageReceivedEvent e, String[] strings) {
        try {
            return DCommandDispatcher.getDispatcher().dispatch(e, this, strings);
        } catch (IncorrectArgumentsException var5) {
            sendMessageEmbed(e, invalidArgumentsMessageEmbed(), HELP_COMMAND_DURATION);
            return false;
        } catch (NoPermissionException var6) {
            sendMessageEmbed(e, noPermissionMessageEmbed(), 40);
            return false;
        } catch (IllegalTextChannelException var7) {
            sendMessageEmbed(e, invalidTcMessageEmbed(), 40);
            return false;
        } catch (IllegalBeginningParameterException var8) {
            var8.printStackTrace();
            return false;
        }
    }
}
