package us.cyrien.minecordbot.enums;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.utils.PermissionUtil;
import us.cyrien.minecordbot.localization.Localization;

import java.util.function.Predicate;

public enum CommandType {
    MISC("Miscellaneous") {
        @Override
        public String toString() {
            return Localization.getTranslatedMessage("mcb.commands.help.List.misc");
        }
    },
    HELP("Help") {
        @Override
        public String toString() {
            return Localization.getTranslatedMessage("mcb.commands.help.List.help");
        }
    },
    FUN ("Fun"){
        @Override
        public String toString() {
            return Localization.getTranslatedMessage("mcb.commands.help.List.fun");
        }
    },
    INFO ("Info"){
        @Override
        public String toString() {
            return Localization.getTranslatedMessage("mcb.commands.help.List.info");
        }
    },
    ADMIN ("Admin", (e) -> {
        if (e.getAuthor().getId().equals(e.getClient().getOwnerId()))
            return true;
        if (e.getGuild() == null)
            return true;
        return PermissionUtil.checkPermission(e.getMember(), Permission.ADMINISTRATOR);
    }) {
        @Override
        public String toString() {
            return Localization.getTranslatedMessage("mcb.commands.help.List.mod");
        }
    },
    SPECIAL ("Special"){
        @Override
        public String toString() {
            return Localization.getTranslatedMessage("mcb.commands.help.List.special");
        }
    };

    public Command.Category category;

    CommandType(String name) {
        category = new Command.Category(name);
    }

    CommandType(String name, Predicate<CommandEvent> predicate) {
        category = new Command.Category(name, predicate);
    }

    public static Command.Category getCategoryFromType(CommandType commandType) {
        for(CommandType c : values()) {
            if(c.equals(commandType))
                return c.category;
        }
        return null;
    }
}
