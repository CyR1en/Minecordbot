package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.localization.Locale;

public class PollCmd extends MCBCommand {

    private final int REGIONAL_A = "\uD83C\uDDE6".codePointAt(0);

    public PollCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "poll";
        this.arguments = "<question> or <question>|<option1>|<option2>...";
        this.help = Locale.getCommandsMessage("poll.description").finish();
        this.category = minecordbot.ADMIN;
    }

    @Override
    protected void doCommand(CommandEvent event) {
        String[] parts = event.getArgs().split("\\|");
        if (parts.length == 1) {
            respond(event, formatQuestion(event.getArgs()), m -> {
                m.addReaction("\uD83D\uDC4D").queue();
                m.addReaction("\uD83D\uDC4E").queue();
            });
        } else {
            StringBuilder builder = new StringBuilder(formatQuestion(parts[0]));
            for (int i = 1; i < parts.length; i++) {
                String r = String.copyValueOf(Character.toChars(REGIONAL_A + i - 1));
                builder.append("\n").append(r).append(" ").append(parts[i].trim());
            }
            respond(event, builder.toString(), m ->
            {
                for (int i = 1; i < parts.length; i++)
                    m.addReaction(String.copyValueOf(Character.toChars(REGIONAL_A + i - 1))).queue();
            });
        }
        event.getMessage().delete().queue();
    }

    private static String formatQuestion(String str) {
        return "\uD83D\uDDF3 **" + str + "**";
    }
}
