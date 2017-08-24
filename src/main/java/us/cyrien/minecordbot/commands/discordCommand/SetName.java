package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.DiscordCommand;
import us.cyrien.minecordbot.localization.Localization;

public class SetName extends DiscordCommand {

    public SetName(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "setname";
        this.aliases = new String[]{"setname", "sn"};
        this.arguments = "<username>";
        this.help = Localization.getTranslatedMessage("mcb.commands.setusername.description");
        this.ownerCommand = true;
        this.category = minecordbot.OWNER;
    }

    @Override
    protected void doCommand(CommandEvent event) {
        try {
            String oldName = event.getSelfUser().getName();
            event.getSelfUser().getManager().setName(event.getArgs()).complete(false);
            event.reply(event.getClient().getSuccess() + " Name changed from `" + oldName + "` to `" + event.getArgs() + "`");
        } catch (RateLimitedException e) {
            event.reply(event.getClient().getError() + " Name can only be changed twice per hour!");
        } catch (Exception e) {
            event.reply(event.getClient().getError() + " That name is not valid!");
        }
    }
}
