package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import org.bukkit.ChatColor;
import sun.misc.BASE64Decoder;
import us.cyrien.minecordbot.Bot;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.localization.Locale;
import us.cyrien.minecordbot.utils.mcpinger.MCPing;

import java.awt.*;
import java.io.IOException;

public class McServerCmd extends MCBCommand {

    public McServerCmd(Minecordbot mcb) {
        super(mcb);
        this.name = "mcserver";
        this.arguments = "<server ip>";
        this.help = Locale.getCommandsMessage("mcserver.description").finish();
        this.category = Bot.INFO;
        this.type = Type.EMBED;
    }

    @Override
    protected void doCommand(CommandEvent e) {
        MCPing.McPing ping = ping(e.getArgs(), e);
        EmbedBuilder eb = new EmbedBuilder();
        if (ping != null)
            respond("pinging...", e).queue((m) -> {
                try {
                    if (ping.status) {
                        eb.setColor(Color.GREEN);

                        String[] parts = ping.favicon.split("\\,");
                        String imageString = parts[1];
                        byte[] imageByte;
                        BASE64Decoder decoder = new BASE64Decoder();
                        imageByte = decoder.decodeBuffer(imageString);

                        eb.setThumbnail("attachment://image.png");

                        eb.addField("**" + e.getArgs() + "**", "Status: ***ONLINE***", false);
                        eb.addField("Ping:", ping.ping, true);
                        eb.addField("Version:", ping.version, true);
                        eb.addField("Players:", ping.players, true);
                        eb.addField("MOTD:", ChatColor.stripColor(ping.motd), false);

                        m.delete().queue();
                        e.getTextChannel().sendFile(imageByte, "image.png", new MessageBuilder()
                                .setEmbed(embedMessage(e, eb.build(), ResponseLevel.LEVEL_1, "Information")).build())
                                .queue();
                    } else {
                        eb.setColor(Color.RED);
                        eb.addField("**" + e.getArgs() + "**", "Status: ***OFFLINE***", false);
                        m.delete().queue();
                        respond(embedMessage(e, eb.build(), ResponseLevel.LEVEL_3), e).queue();
                    }
                } catch (IOException ignore) {
                }
            });
    }
}