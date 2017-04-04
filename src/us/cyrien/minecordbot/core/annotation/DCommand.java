package us.cyrien.minecordbot.core.annotation;

import us.cyrien.minecordbot.core.enums.CommandType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DCommand {
    String[] aliases();

    String usage() default "";

    String desc() default "";

    CommandType type() default CommandType.MISC;
}
