package us.cyrien.mcutils.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation tells the dispatcher that this parameter should be the sender object (the person who sent the command).
 * <p>
 * Can be any subclass of CommandSender.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Sender {
}
