package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import us.cyrien.minecordbot.Bot;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.commands.Updatable;
import us.cyrien.minecordbot.localization.Locale;

import java.text.DecimalFormat;

public class TpsCmd extends MCBCommand implements Updatable {

    public TpsCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "tps";
        this.help = Locale.getCommandsMessage("tps.description").finish();
        this.category = Bot.INFO;
        this.type = Type.EMBED;
    }

    @Override
    protected void doCommand(CommandEvent e) {
        respond("Generating info...", e).queue(message -> {
            mcb.getChatManager().addSavedTPS(message, e);
            update();
        });
    }

    private MessageEmbed generate(double tps) {
        EmbedBuilder eb = new EmbedBuilder();
        double lagPercentage = Math.round((1.0D - tps / 20.0D) * 100.0D);
        eb.addField("TPS:", "`" + new DecimalFormat("#.####").format(tps) + "`", true);
        eb.addField("Lag Percentage: ", lagPercentage + "%", true);
        eb.addField("Free RAM: ", Runtime.getRuntime().freeMemory() / 1024L / 1024L + "mb", true);
        eb.addField("Total Memory: ", Runtime.getRuntime().totalMemory() / 1024L / 1024L + "mb", true);
        eb.addField("Allocated Memory: ", Runtime.getRuntime().totalMemory() / 1024L / 1024L + "mb", true);
        eb.addBlankField(true);
        return eb.build();
    }

    public void update() {
        double tps = mcb.getUpTimer().getCurrentTps();

        ResponseLevel level;
        if (tps >= 18.0D) {
            level = ResponseLevel.LEVEL_1;
        } else if (tps >= 15.0D) {
            level = ResponseLevel.LEVEL_2;
        } else {
            level = ResponseLevel.LEVEL_3;
        }

        mcb.getChatManager().getSavedTPS().forEach((msg, event) ->
                msg.editMessage(embedMessage(event, generate(tps), level, "Information")).queue());
    }
}
