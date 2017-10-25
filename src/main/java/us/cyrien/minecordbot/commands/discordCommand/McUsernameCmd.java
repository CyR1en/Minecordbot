package us.cyrien.minecordbot.commands.discordCommand;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import us.cyrien.minecordbot.Bot;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.localization.Locale;
import us.cyrien.minecordbot.utils.HTTPUtils;
import us.cyrien.minecordbot.utils.UUIDFetcher;
import us.cyrien.minecordbot.utils.exception.TooManyRequestsException;

import java.io.IOException;

public class McUsernameCmd extends MCBCommand{

    public McUsernameCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "mcusername";
        this.help = Locale.getCommandsMessage("mcusername.description").finish();
        this.arguments = "<uuid>";
        this.aliases = new String[]{"mcuser"};
        this.category = Bot.INFO;
        this.type = MCBCommand.Type.EMBED;
    }

    @Override
    protected void doCommand(CommandEvent e) {
        if (e.getArgs().isEmpty()) {
            respond(MCBCommand.ResponseLevel.LEVEL_3, Locale.getCommandMessage("invalid-arguments").finish(), e).queue();
            return;
        }

        String s = null;
        try {
            s = HTTPUtils.performGetRequest(HTTPUtils.constantURL
                    ("https://sessionserver.mojang.com/session/minecraft/profile/" + UUIDFetcher.removeDescriminant(e.getArgs())));
        } catch (IOException ex) {
            respond(MCBCommand.ResponseLevel.LEVEL_3, "Error getting player's Username: `java.io.IOException`", e).queue();
            return;
        }
        try {
            JsonObject json = (new JsonParser()).parse(s).getAsJsonObject();
            if(json.has("error"))
                throw new TooManyRequestsException(json.get("errorMessage").getAsString());
            EmbedBuilder eb = new EmbedBuilder();
            eb.addField("**Minecraft Username**:", json.get("name").getAsString(), false);
            respond(embedMessage(e, eb.build(), null), e).queue();
        } catch (IllegalStateException ex) {
            respond(MCBCommand.ResponseLevel.LEVEL_3, Locale.getCommandsMessage("mcusername.cannot-find").finish(), e).queue();
        } catch (TooManyRequestsException ex) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.addField("**Error**: " + ex.getClass().getSimpleName(), ex.getMessage(), false);
            respond(embedMessage(e, eb.build(), ResponseLevel.LEVEL_3), e).queue();
        } catch (Exception ex) {
            ex.printStackTrace();
            respond(MCBCommand.ResponseLevel.LEVEL_3, Locale.getCommandsMessage("mcusername.error").finish(), e).queue();
        }
    }

}
