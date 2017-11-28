package us.cyrien.minecordbot.commands.discordCommand;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import us.cyrien.minecordbot.Bot;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.localization.Locale;
import us.cyrien.minecordbot.utils.HTTPUtils;
import us.cyrien.minecordbot.utils.MessageUtils;
import us.cyrien.minecordbot.utils.UUIDFetcher;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class McSkinCmd extends MCBCommand {

    public McSkinCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "mcskin";
        this.arguments = "<uuid>";
        this.category = Bot.INFO;
        this.help = Locale.getCommandsMessage("mcskin.description").finish();
        this.type = Type.EMBED;
    }

    @Override
    protected void doCommand(CommandEvent e) {
        if (e.getArgs().isEmpty()) {
            respond(ResponseLevel.LEVEL_3, Locale.getCommandMessage("invalid-arguments").finish(), e).queue();
            return;
        }

        String s;
        try {
            s = HTTPUtils.performGetRequest(HTTPUtils.constantURL("https://api.mojang.com/users/profiles/minecraft/" + e.getArgs()));
        } catch (IOException ex) {
            respond(ResponseLevel.LEVEL_3, "Error getting player's UUID: `java.io.IOException`", e).queue();
            return;
        }
        try {
            JsonObject json = (new JsonParser()).parse(s).getAsJsonObject();
            EmbedBuilder eb = new EmbedBuilder();

            byte[] image = MessageUtils.downloadImage("https://crafatar.com/renders/body/" + UUIDFetcher.getUUID(json.get("id").getAsString()).toString() + "?overlay&default=MHF_Steve");
            eb.addField("**" + e.getArgs() + "'s Skin**:", "", false);
            eb.setImage("attachment://image.png");
            respond("querying skin...", e).queue((m) -> scheduler.schedule(() -> {
                m.delete().queue();
                e.getTextChannel().sendFile(image, "image.png", new MessageBuilder().setEmbed(embedMessage(e, eb.build(), ResponseLevel.DEFAULT, "Information")).build()).queue();
            }, 1, TimeUnit.SECONDS));
        } catch (IllegalStateException ex) {
            respond(ResponseLevel.LEVEL_3, Locale.getCommandsMessage("mcskin.cannot-find").finish(), e).queue();
        } catch (Exception ex) {
            ex.printStackTrace();
            respond(ResponseLevel.LEVEL_3, Locale.getCommandsMessage("mcskin.error").finish(), e).queue();
        }
    }
}
