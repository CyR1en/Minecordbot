package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import org.bukkit.Bukkit;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.DiscordCommand;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.localization.Localization;

public class Info extends DiscordCommand{

    public Info(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "info";
        this.aliases = new String[]{"inf", "in"};
        this.category = minecordbot.INFO;
    }

    @Override
    protected void doCommand(CommandEvent e) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        String path = "mcb.commands.info.minfo.";
        int textChannelCount = e.getGuild().getTextChannels().size();
        int voiceChannelCount = e.getGuild().getVoiceChannels().size();
        String clientID = MCBConfig.get("client_id");
        String botName = e.getJDA().getSelfUser().getName();
        Guild guild = e.getGuild();
        Member member = guild.getMember(e.getJDA().getSelfUser());
        String nickName = (Localization.getTranslatedMessage(path + "nonick"));
        if (e.getGuild().getMember(e.getJDA().getSelfUser()).getNickname() != null)
            nickName = e.getGuild().getMember(e.getJDA().getSelfUser()).getNickname();
        String mcbInfo = "\n" + Localization.getTranslatedMessage(path + "version") + ": " + Bukkit.getPluginManager().getPlugin("MineCordBot").getDescription().getVersion() +
                "\n" + Localization.getTranslatedMessage(path + "textchannel") + ": " + textChannelCount +
                "\n" + Localization.getTranslatedMessage(path + "voicechannel") + ": " + voiceChannelCount +
                "\n" + Localization.getTranslatedMessage(path + "uptime") + ": " + getMinecordbot().getUpTime();
        String botInfo = "\n" + Localization.getTranslatedMessage(path + "clientid") + ": " + clientID +
                "\n" + Localization.getTranslatedMessage(path + "botname") + ": " + botName +
                "\n" + Localization.getTranslatedMessage(path + "botnick") + ": " + nickName;
        embedBuilder.setColor(member.getColor());
        embedBuilder.setThumbnail("https://media-elerium.cursecdn.com/attachments/thumbnails/124/611/310/172/minecord.png");
        embedBuilder.setAuthor("MineCordBot", "https://dev.bukkit.org/projects/minecordbot-bukkit", "https://media-elerium.cursecdn.com/attachments/thumbnails/124/611/310/172/minecord.png" );
        //embedBuilder.addBlankField(false);
        embedBuilder.addField(Localization.getTranslatedMessage(path + "generalinfo_header"), mcbInfo, false);
        embedBuilder.addField(Localization.getTranslatedMessage(path + "botinfo_header"), botInfo, false);
        //embedBuilder.addBlankField(false);
        embedBuilder.setFooter("- C Y R I \u039E N  | A L V A R I \u039E N -", "https://yt3.ggpht.com/-uuXItiIhgcU/AAAAAAAAAAI/AAAAAAAAAAA/3xzbfTTz9oU/s88-c-k-no-mo-rj-c0xffffff/photo.jpg");
        respond(e, embedBuilder.build());
    }
}
