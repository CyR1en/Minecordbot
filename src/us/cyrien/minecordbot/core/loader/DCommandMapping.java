package us.cyrien.minecordbot.core.loader;

import io.github.hedgehog1029.frame.dispatcher.exception.IncorrectArgumentsException;
import io.github.hedgehog1029.frame.dispatcher.exception.NoPermissionException;
import io.github.hedgehog1029.frame.loader.exception.InaccessibleMethodException;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import us.cyrien.minecordbot.core.annotation.DCommand;
import us.cyrien.minecordbot.core.annotation.DPermission;
import us.cyrien.minecordbot.core.dispatcher.DCommandDiscpatcher;
import us.cyrien.minecordbot.core.enums.PermissionLevel;
import us.cyrien.minecordbot.core.exceptions.IllegalBeginningParameterException;
import us.cyrien.minecordbot.core.module.DiscordCommand;
import us.cyrien.minecordbot.main.Localization;
import us.cyrien.minecordbot.main.Minecordbot;

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

        setUsage(Localization.getTranslatedMessage(usage));
        setDescription(Localization.getTranslatedMessage(description));

        if (method.isAnnotationPresent(DPermission.class)) {
            Minecordbot.DEBUG_LOGGER.info("PERMISSION ANNOTATION FOUND");
            this.permission = method.getAnnotation(DPermission.class).value();
            Minecordbot.DEBUG_LOGGER.info("COMMAND PERMISSION SET TO " + permission);
        } else if (method.isAnnotationPresent(DCommand.class)) {
            this.permission = PermissionLevel.LEVEL_0;
        }
    }

    public DCommand getCommand() {
        return this.command;
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

    public Object getContainer() {
        return container;
    }

    @Override
    public boolean execute(MessageReceivedEvent e, String[] strings) {
        try {
            Minecordbot.DEBUG_LOGGER.info("DISCPATCHING"); // FIXME: 3/25/2017
            return DCommandDiscpatcher.getDispatcher().dispatch(e, this, strings);
        } catch (IncorrectArgumentsException var5) {
            sendMessage(e, "`incorrect arguments`", 20);
            return false;
        } catch (NoPermissionException var6) {
            sendMessage(e, "`You have no permission`", 20);
            return false;
        } catch (IllegalBeginningParameterException var7) {
            var7.printStackTrace();
            return false;
        }
    }
}
