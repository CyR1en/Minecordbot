package us.cyrien.minecordbot.core.annotation;

import us.cyrien.minecordbot.core.enums.PermissionLevel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DPermission {
    PermissionLevel value() default PermissionLevel.LEVEL_0;
}
