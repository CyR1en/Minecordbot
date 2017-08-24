package us.cyrien.minecordbot.commands;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.requests.RestAction;
import org.json.JSONArray;
import org.json.JSONObject;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.localization.Localization;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public abstract class DiscordCommand extends Command implements Comparable {

    public static final long RESPONSE_DURATION = 5;

    private Minecordbot minecordbot;

    protected TextChannel[] boundChannels;

    public DiscordCommand(Minecordbot minecordbot) {
        this(minecordbot, null);
    }

    public DiscordCommand(Minecordbot minecordbot, Command... children) {
        this.minecordbot = minecordbot;
        this.guildOnly = true;
        this.helpBiConsumer = (ce, c) -> getHelpCard(ce);
        Long[] perms = new Long[]{Permission.ALL_TEXT_PERMISSIONS,
                Permission.ALL_GUILD_PERMISSIONS, Permission.ALL_CHANNEL_PERMISSIONS};
        List<Permission> commandPerms = new ArrayList<>();
        for(Long l : perms)
            for(Permission p : Permission.getPermissions(l))
                commandPerms.add(p);
        this.botPermissions = (Permission[])commandPerms.toArray();
        this.children = children;
        boundChannels = initTCs();
    }

    public Minecordbot getMinecordbot() {
        return minecordbot;
    }

    public void respond(CommandEvent event, String message) {
        respond(event, message, RESPONSE_DURATION, TimeUnit.MINUTES);
    }

    public void respond(CommandEvent event, MessageEmbed messageEmbed) {
        respond(event, messageEmbed, RESPONSE_DURATION, TimeUnit.MINUTES);
    }

    public RestAction<Message> respond(MessageEmbed messageEmbed, CommandEvent event) {
        return event.getTextChannel().sendMessage(messageEmbed);
    }

    public RestAction<Message> respond(String message, CommandEvent event) {
        return event.getTextChannel().sendMessage(message);
    }

    public void respond(CommandEvent event, String message, long duration, TimeUnit timeUnit) {
        event.reply(message, (msg) -> {
            minecordbot.getEventWaiter().waitForEvent(MessageReceivedEvent.class, (c) -> {
                return true;
            }, (a) -> {
                msg.delete().queue();;
            }, duration, timeUnit, () -> System.out.println("Could not auto delete message"));
        });
    }

    public void respond(CommandEvent event, MessageEmbed messageEmbed, long duration, TimeUnit timeUnit) {
        event.reply(messageEmbed, (msg) -> {
            minecordbot.getEventWaiter().waitForEvent(MessageReceivedEvent.class, (c) -> {
                return true;
            }, (a) -> {
                msg.delete().queue();
            }, duration, timeUnit, () -> System.out.println("Could not auto delete message"));
        });
    }

    @Override
    protected void execute(CommandEvent event) {
        boolean bound = false;
        for (TextChannel tc : boundChannels)
            if (tc.getId().equals(event.getTextChannel().getId()))
                bound = true;
        if (!bound) {
            respond(event, invalidTcMessageEmbed());
            return;
        }
        if (!this.arguments.isEmpty() && !event.getArgs().isEmpty()) {
            respond(event, getHelpCard(event));
            return;
        }
        doCommand(event);
    }

    protected abstract void doCommand(CommandEvent e);

    public MessageEmbed getHelpCard(CommandEvent e) {
        EmbedBuilder eb = new EmbedBuilder().setColor(e.getGuild().getMember(e.getJDA().getSelfUser()).getColor());
        eb.setTitle(this.getName().substring(0, 1).toUpperCase() + this.getName().substring(1) + " Command Help Card:", null);
        eb.addField("Usage", e.getClient().getPrefix() + this.getName() + " " + this.getArguments(), false);
        eb.addField("Description", this.help, false);
        String r;
        if (this.aliases.length == 0 || this.aliases == null)
            r = Localization.getTranslatedMessage("mcb.command.no-alias");
        else
            r = this.getAliases().toString().replaceAll("\\[", "").replaceAll("]", "");
        eb.addField("Alias", r, false);
        String permission = this.userPermissions.length < 1 ? "None" : this.userPermissions.toString();
        if(isOwnerCommand())
            permission = "OWNER";
        if(category.equals(minecordbot.ADMIN))
            permission = "ADMIN";
        eb.addField("Permission", "Required Permission: " + permission, false);
        return eb.build();
    }

    @Override
    public int compareTo(Object o) {
        Command temp = (Command) o;
        return this.getName().compareTo(temp.getName());
    }

    private TextChannel[] initTCs() {
        JSONObject ctc = MCBConfig.getJSONObject("command_text_channel");
        String key = this.category == null ? minecordbot.MISC.getName().toLowerCase() : this.category.getName().toLowerCase();
        List<TextChannel> tcs = getValidTextChannelAsList(ctc.getJSONArray(key));
        return (TextChannel[])tcs.toArray();
    }

    private List<TextChannel> getValidTextChannelAsList(JSONArray arr) {
        List<TextChannel> textChannels = new ArrayList<>();
        for (Object o : arr) {
            TextChannel tc = minecordbot.getJDA().getTextChannelById(String.valueOf(o));
            if (tc != null)
                textChannels.add(tc);
        }
        if (textChannels.size() == 0) {
            return null;
        } else {
            return textChannels;
        }
    }

    public String noPermissionMessage() {
        return "`" + Localization.getTranslatedMessage("mcb.command.no-perm-message") + "`";
    }

    public MessageEmbed noPermissionMessageEmbed() {
        String s = Localization.getTranslatedMessage("mcb.command.no-perm-message");
        EmbedBuilder eb = new EmbedBuilder().setTitle(s, null);
        return eb.build();
    }

    public String invalidArgumentsMessage() {
        return "`" + Localization.getTranslatedMessage("mcb.command.invalid-arguments") + "`";
    }

    public MessageEmbed invalidArgumentsMessageEmbed() {
        String s = Localization.getTranslatedMessage("mcb.command.no-perm-message");
        EmbedBuilder eb = new EmbedBuilder().setTitle(s, null);
        return eb.build();
    }

    public String invalidTcMessage() {
        return "`" + Localization.getTranslatedMessage("mcb.command.invalid-tc") + "`";
    }

    public MessageEmbed invalidTcMessageEmbed() {
        String s =Localization.getTranslatedMessage("mcb.command.invalid-tc") ;
        EmbedBuilder eb = new EmbedBuilder().setTitle(s, null);
        return eb.build();
    }
}
