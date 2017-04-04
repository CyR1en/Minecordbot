package us.cyrien.minecordbot.core.dispatcher;

import io.github.hedgehog1029.frame.annotations.Optional;
import io.github.hedgehog1029.frame.annotations.Text;
import io.github.hedgehog1029.frame.dispatcher.exception.IncorrectArgumentsException;
import io.github.hedgehog1029.frame.dispatcher.exception.NoPermissionException;
import io.github.hedgehog1029.frame.loader.exception.InaccessibleMethodException;
import io.github.hedgehog1029.frame.logger.Logger;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import us.cyrien.minecordbot.core.annotation.DCmd;
import us.cyrien.minecordbot.core.annotation.DMessageReceive;
import us.cyrien.minecordbot.core.enums.PermissionLevel;
import us.cyrien.minecordbot.core.exceptions.IllegalBeginningParameterException;
import us.cyrien.minecordbot.core.loader.DCommandMapping;
import us.cyrien.minecordbot.core.module.DiscordCommand;
import us.cyrien.minecordbot.entity.User;
import us.cyrien.minecordbot.main.Minecordbot;
import us.cyrien.minecordbot.utils.ArrayListUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class DCommandDiscpatcher {
    private static DCommandDiscpatcher dispatcher;
    private static Collection<DiscordCommand> map;

    public DCommandDiscpatcher() {
    }

    public static DCommandDiscpatcher getDispatcher() {
        if (dispatcher == null) {
            dispatcher = new DCommandDiscpatcher();
        }

        return dispatcher;
    }

    public void registerCommand(us.cyrien.minecordbot.core.annotation.DCommand command, Method callback, Object instance) {
        DCommandMapping mcbCommandMapping = new DCommandMapping(command, callback, instance);
        getCommandMap().add(mcbCommandMapping);
    }

    public final boolean dispatch(MessageReceivedEvent me, DCommandMapping command, String... oargs) throws IncorrectArgumentsException, NoPermissionException, IllegalBeginningParameterException {
        ArrayList<Object> params = new ArrayList();
        Parameter[] methodArgs = command.getMethod().getParameters();
        ArrayDeque<Parameter> parameters = new ArrayDeque(Arrays.asList(methodArgs));
        ArrayDeque args = new ArrayDeque(Arrays.asList(oargs));
        if(methodArgs.length > 1) {
            if (!isDCommand(methodArgs[0]) && !isMessageReceive(methodArgs[0])) {
                throw new IllegalBeginningParameterException
                        ("First parameter of " + command.getMethod().getName() +
                                " needs to be \"@DMessageReceiveEvent MessageReceiveEvent e\" or \"@DCmd DiscordCommand command\"");
            }
            if (!isDCommand(methodArgs[1]) && !isMessageReceive(methodArgs[1])) {
                throw new IllegalBeginningParameterException
                        ("Second parameter of " + command.getMethod().getName() +
                                " needs to be \"@DMessageReceiveEvent MessageReceiveEvent e\" and \"@DiscordCommand DiscordCommand command\"");
            }
        }
        while (!parameters.isEmpty()) {
            Parameter current = (Parameter) parameters.pop();
            Minecordbot.DEBUG_LOGGER.info("CURRENT PARAMETER : " + current);
            if (args.peek() == null && isOptional(current)) {
                params.add((Object) null);
            } else {
                if (args.peek() == null && !isOptional(current) && !isMessageReceive(current)) {
                    throw new IncorrectArgumentsException(String.format("No argument provided for parameter %s!", new Object[]{current.getName()}));
                }
                String spl;
                if (subclassOf(Event.class, current)) {
                    if (isMessageReceive(current))
                        params.add(me);
                } else if (subclassOf(DiscordCommand.class, current)) {
                    if (isDCommand(current)) {
                        params.add(command);
                    } else if (args.peek() != null) {
                        spl = (String)args.pop();
                        List<DiscordCommand> mcbCmds = Minecordbot.getDiscordCommands();
                        System.out.println(spl + "asjdioajsdijadasdasd");
                        if(spl.equals("")) {
                            params.add(null);
                        } else if(ArrayListUtils.getIndexOf(spl, mcbCmds) == -1 && !spl.equals("")) {
                            DiscordCommand tempCmd = new DCommandMapping(command.getCommand(), command.getMethod(), command.getContainer());
                            params.add(tempCmd.nullify(spl));
                        } else {
                            DiscordCommand cmd = Minecordbot.getDiscordCommands().get(ArrayListUtils.getIndexOf(spl, mcbCmds ));
                            params.add(cmd);
                        }
                    } else {
                        params.add(null);
                    }
                } else if (subclassOf(String.class, current)) {
                    if (current.isAnnotationPresent(Text.class)) {
                        StringBuilder builder = new StringBuilder();
                        args.forEach((a) -> {
                            builder.append(a).append(" ");
                        });
                        params.add(builder.toString());
                        break;
                    }
                    params.add(args.pop());
                } else if (subclassOf(Integer.TYPE, current)) {
                    int arg;
                    try {
                        arg = Integer.valueOf((String) args.pop()).intValue();
                    } catch (NumberFormatException var12) {
                        throw new IncorrectArgumentsException(String.format("Parameter %s requires an INTEGER.", new Object[]{current.getName()}));
                    }
                    params.add(Integer.valueOf(arg));
                } else if (subclassOf(Boolean.TYPE, current)) {
                    spl = (String) args.pop();
                    if (spl.equalsIgnoreCase("true")) {
                        params.add(Boolean.valueOf(true));
                    } else {
                        if (!spl.equalsIgnoreCase("false")) {
                            throw new IncorrectArgumentsException(String.format("Parameter %s requires a BOOLEAN.", new Object[]{current.getName()}));
                        }
                        params.add(Boolean.valueOf(false));
                    }
                }
            }
        }
        User sender = new User().setUser(me);
        PermissionLevel plevel = command.getPermission();
        Minecordbot.DEBUG_LOGGER.info("SENDER PERMISSION LEVEL: " + sender.getPermissionLevel()); // FIXME: 3/25/2017
        Minecordbot.DEBUG_LOGGER.info("COMMAND PERMISSION LEVEL: " + plevel); // FIXME: 3/25/2017
        if (command.getPermission() != null && !new User().setUser(me).hasPermission(plevel)) {
            Minecordbot.DEBUG_LOGGER.info("USER HAS NO PERMISSION"); // FIXME: 3/25/2017
            throw new NoPermissionException();
        } else {
            command.setPermission(PermissionLevel.LEVEL_0);
            Minecordbot.DEBUG_LOGGER.info("INVOKING COMMAND"); // FIXME: 3/25/2017
            try {
                command.invoke(params.toArray());
                Minecordbot.DEBUG_LOGGER.info("SUCCESSFULLY INVOKED COMMAND"); // FIXME: 3/26/2017 
                return true;
            } catch (InaccessibleMethodException var11) {
                Logger.err("Could not invoke method for command " + command.getName() + "!");
                var11.printStackTrace();
                return false;
            }
        }
    }

    private static boolean subclassOf(Class<?> c, Parameter p) {
        return c.isAssignableFrom(p.getType());
    }

    private static boolean isOptional(Parameter p) {
        return p.isAnnotationPresent(Optional.class);
    }

    private static boolean isMessageReceive(Parameter p) {
        return p.isAnnotationPresent(DMessageReceive.class) && subclassOf(Event.class, p);
    }

    private static boolean isDCommand(Parameter p) {
        return p.isAnnotationPresent(DCmd.class) && subclassOf(DiscordCommand.class, p);
    }

    private static Collection<DiscordCommand> getCommandMap() {
        if (map == null) {
            map = Minecordbot.getDiscordCommands();
        }
        return map;
    }
}
