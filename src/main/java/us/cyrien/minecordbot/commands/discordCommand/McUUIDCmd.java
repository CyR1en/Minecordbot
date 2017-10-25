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

import java.io.IOException;

public class McUUIDCmd extends MCBCommand{

    public McUUIDCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "mcuuid";
        this.help = Locale.getCommandsMessage("mcuuid.description").finish();
        this.arguments = "<username>";
        this.category = Bot.INFO;
        this.type = Type.EMBED;
    }

    @Override
    protected void doCommand(CommandEvent e) {
        if (e.getArgs().isEmpty()) {
            respond(ResponseLevel.LEVEL_3, Locale.getCommandMessage("invalid-arguments").finish(), e).queue();
            return;
        }

        String s = null;
        try {
            s = HTTPUtils.performGetRequest(HTTPUtils.constantURL("https://api.mojang.com/users/profiles/minecraft/" + e.getArgs()));
        } catch (IOException ex) {
            respond(ResponseLevel.LEVEL_3, "Error getting player's UUID: `java.io.IOException`", e).queue();
            return;
        }
        try {
            JsonObject json = (new JsonParser()).parse(s).getAsJsonObject();
            EmbedBuilder eb = new EmbedBuilder();
            eb.addField("**" + e.getArgs() + "'s UUID**:", "`" + UUIDFetcher.getUUID(json.get("id").getAsString()).toString() + "`", false);
            respond(embedMessage(e, eb.build(), null), e).queue();
        } catch (IllegalStateException ex) {
            respond(ResponseLevel.LEVEL_3, Locale.getCommandsMessage("mcuuid.cannot-find").finish(), e).queue();
        } catch (Exception ex) {
            ex.printStackTrace();
            respond(ResponseLevel.LEVEL_3, Locale.getCommandsMessage("mcuuid.error").finish(), e).queue();
        }
    }
}
