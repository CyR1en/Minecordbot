package us.cyrien.mcutils.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Tells Frame to register the method as a command.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    /**
     * Names for the command (e.g. an alias of hello results in the command "/hello" in Minecraft).
     */
    String[] aliases();

    /**
     * Used for the /help usage.
     */
    String usage() default "";

    /**
     * Used for the /help description.
     */
    String desc();
}
