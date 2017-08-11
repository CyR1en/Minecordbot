package us.cyrien.minecordbot.core.enums;

import us.cyrien.minecordbot.main.Localization;

public enum CommandType {
    MISC {
        @Override
        public String toString() {
            return Localization.getTranslatedMessage("mcb.commands.help.list.misc");
        }
    },
    HELP {
        @Override
        public String toString() {
            return Localization.getTranslatedMessage("mcb.commands.help.list.help");
        }
    },
    FUN {
        @Override
        public String toString() {
            return Localization.getTranslatedMessage("mcb.commands.help.list.fun");
        }
    },
    INFO {
        @Override
        public String toString() {
            return Localization.getTranslatedMessage("mcb.commands.help.list.info");
        }
    },
    MOD {
        @Override
        public String toString() {
            return Localization.getTranslatedMessage("mcb.commands.help.list.mod");
        }
    },
    SPECIAL {
        @Override
        public String toString() {
            return Localization.getTranslatedMessage("mcb.commands.help.list.special");
        }
    }
}
