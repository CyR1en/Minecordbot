package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import us.cyrien.minecordbot.Bot;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.localization.Locale;

public class PermissionCmd extends MCBCommand {

    public PermissionCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "permission";
        this.aliases = new String[]{"perm"};
        this.help = Locale.getCommandsMessage("perm.description").finish();
        this.arguments = "<set | remove> [sub command arguments]...";
    }

    @Override
    protected void doCommand(CommandEvent e) {
        if(!(e.getArgs().equalsIgnoreCase("set") && e.getArgs().equalsIgnoreCase("remove")))
            respond(e, Locale.getCommandsMessage("perm.invalid-sub").finish());
    }

    private class Set extends MCBCommand {

        public Set(Minecordbot minecordbot) {
            super(minecordbot);
            this.name = "set";
            this.help = Locale.getCommandsMessage("perm.sub.set.description").finish();
            this.arguments = "<category> <role>";
            this.type = Type.EMBED;
            this.category = Bot.MISC;
        }

        @Override
        protected void doCommand(CommandEvent e) {

        }
    }

    private class Remove extends MCBCommand {

        public Remove(Minecordbot minecordbot) {
            super(minecordbot);
            this.name = "remove";
            this.help = Locale.getCommandsMessage("perm.sub.remove.description").finish();
            this.arguments = "<category>";
            this.type = Type.EMBED;
            this.category = Bot.MISC;
        }

        @Override
        protected void doCommand(CommandEvent e) {

        }
    }
}
