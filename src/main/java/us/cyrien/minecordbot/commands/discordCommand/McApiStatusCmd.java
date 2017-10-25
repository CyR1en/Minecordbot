package us.cyrien.minecordbot.commands.discordCommand;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import us.cyrien.minecordbot.Bot;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.localization.Locale;
import us.cyrien.minecordbot.utils.HTTPUtils;

import java.io.IOException;
import java.util.Map;

public class McApiStatusCmd extends MCBCommand {

    public McApiStatusCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "mcstat";
        this.help = Locale.getCommandsMessage("mcstat.description").finish();
        this.category = Bot.INFO;
        this.type = Type.EMBED;
    }

    @Override
    protected void doCommand(CommandEvent event) {
        String s;
        try {
            s = HTTPUtils.performGetRequest(HTTPUtils.constantURL("https://mcapi.ca/mcstatus"));
        } catch (IOException ex) {
            respond(ResponseLevel.LEVEL_3, "Error getting server info: `java.io.IOException`", event).queue();
            return;
        }

        try {
            JsonObject json = (new JsonParser()).parse(s).getAsJsonObject();
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("**Mojang status:**", "https://mojang.com");
            boolean isAllOnline = true;
            int onlineCount = 0;
            for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                if (entry.getValue().getAsJsonObject().get("status").getAsString().equals("Online")) {
                    onlineCount++;
                    builder.addField(entry.getKey(), "\u2B06 "+ "*Online*", true);
                } else {
                    isAllOnline = false;
                    builder.addField(entry.getKey(), "\u2B07 " + "*Offline*", true);
                }
            }
            if (isAllOnline) {
                respond(event, embedMessage(event, builder.build(), ResponseLevel.LEVEL_1));
            } else {
                if (onlineCount > 0) {
                    respond(event, embedMessage(event, builder.build(), ResponseLevel.LEVEL_2));
                } else {
                    respond(event, embedMessage(event, builder.build(), ResponseLevel.LEVEL_3));
                }
            }
        } catch (IllegalStateException e) {
            respond(ResponseLevel.LEVEL_3, "Unable to parse minecraft status!", event).queue();
        }
    }
}
