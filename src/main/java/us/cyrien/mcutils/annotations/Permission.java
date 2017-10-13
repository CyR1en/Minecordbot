package us.cyrien.mcutils.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Optional annotation.
 * <p>
 * It let you specify a required permission node for this command.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {
    String value();
}
