package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import org.bukkit.Bukkit;
import us.cyrien.minecordbot.Bot;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.configuration.BotConfig;
import us.cyrien.minecordbot.localization.Locale;


public class InfoCmd extends MCBCommand {

    public InfoCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "info";
        this.aliases = new String[]{"inf", "in"};
        this.help = Locale.getCommandsMessage("info.description").finish();
        this.category = Bot.INFO;
        this.type = Type.EMBED;
    }

    @Override
    protected void doCommand(CommandEvent e) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        String path = "info.minfo.";
        int textChannelCount = e.getGuild().getTextChannels().size();
        int voiceChannelCount = e.getGuild().getVoiceChannels().size();
        String clientID = configsManager.getBotConfig().getString(BotConfig.Nodes.BOT_ID);
        String botName = e.getJDA().getSelfUser().getName();
        String nickName = (Locale.getCommandsMessage(path + "nonick").finish());
        if (e.getGuild().getMember(e.getJDA().getSelfUser()).getNickname() != null)
            nickName = e.getGuild().getMember(e.getJDA().getSelfUser()).getNickname();
        String mcbInfo = "\n" + Locale.getCommandsMessage(path + "version").finish() + ": " + Bukkit.getPluginManager().getPlugin("MineCordBot").getDescription().getVersion() +
                "\n" + Locale.getCommandsMessage(path + "textchannel").finish() + ": " + textChannelCount +
                "\n" + Locale.getCommandsMessage(path + "voicechannel").finish() + ": " + voiceChannelCount +
                "\n" + Locale.getCommandsMessage(path + "uptime").finish() + ": " + getMcb().getUpTime();
        String botInfo = "\n" + Locale.getCommandsMessage(path + "clientid").finish() + ": " + clientID +
                "\n" + Locale.getCommandsMessage(path + "botname").finish() + ": " + botName +
                "\n" + Locale.getCommandsMessage(path + "botnick").finish() + ": " + nickName;
        embedBuilder.setColor(Bot.BOT_COLOR);
        embedBuilder.setDescription("A Powerful Way to Bridge Minecraft and Discord");
        embedBuilder.setThumbnail("https://vectr.com/cyrien/k3vhJlcOMS.png?width=168&height=168&select=k3vhJlcOMSpage0");
        embedBuilder.setAuthor("Minecordbot", "https://www.spigotmc.org/resources/minecordbot.30725/", "https://vectr.com/cyrien/k3vhJlcOMS.png?width=168&height=168&select=k3vhJlcOMSpage0" );
        //embedBuilder.addBlankField(false);
        embedBuilder.addField(Locale.getCommandsMessage(path + "generalinfo_header").finish(), mcbInfo, false);
        embedBuilder.addField(Locale.getCommandsMessage(path + "botinfo_header").finish(), botInfo, false);
        //embedBuilder.addBlankField(false);
        User user = mcb.getBot().getJda().getUserById("193970511615623168");
        if (user != null) {
            embedBuilder.setFooter("Questions? contact " + user.getName() + "#" + user.getDiscriminator() + " or join https://discord.cyrien.us", user.getAvatarUrl());
        } else {
            embedBuilder.setFooter("- C Y R I \u039E N -", "https://yt3.ggpht.com/-uuXItiIhgcU/AAAAAAAAAAI/AAAAAAAAAAA/3xzbfTTz9oU/s88-c-k-no-mo-rj-c0xffffff/photo.jpg");
        }
        respond(e, embedBuilder.build());
    }
}
