package us.cyrien.minecordbot.commands;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.requests.RestAction;
import us.cyrien.mcutils.logger.Logger;
import us.cyrien.minecordbot.Bot;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.configuration.MCBConfigsManager;
import us.cyrien.minecordbot.localization.Locale;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;


public abstract class MCBCommand extends Command implements Comparable<Command> {

    protected static final long RESPONSE_DURATION = 5;

    protected final ScheduledExecutorService scheduler;
    protected Minecordbot mcb;
    protected MCBConfigsManager configsManager;

    protected boolean auto;
    protected Type type;

    public MCBCommand(Minecordbot minecordbot) {
        this.mcb = minecordbot;
        this.guildOnly = true;
        this.helpBiConsumer = (ce, c) -> respond(ce,getHelpCard(ce, c));
        this.botPermissions = setupPerms();
        configsManager = minecordbot.getMcbConfigsManager();
        type = Type.DEFAULT;
        auto = configsManager.getBotConfig().getBoolean("Delete_Response");
        scheduler = Executors.newSingleThreadScheduledExecutor();
        Locale.init(mcb.getMcbConfigsManager());
    }

    @Override
    protected void execute(CommandEvent event) {
        int countedReqArgs = countRequiredArgs();
        int countedArgsProvided = event.getArgs().isEmpty() ? 0 : event.getArgs().split(" ").length;
        if((countedReqArgs > countedArgsProvided || countedReqArgs < countedArgsProvided) && countRequiredArgs() != -1) {
            respond(event, invalidArgumentsMessageEmbed());
            respond(event, getHelpCard(event, this));
            return;
        }
        if(category != null && !category.test(event))
            respond(event, noPermissionMessageEmbed());
        doCommand(event);
        Logger.info(event.getAuthor().getName() + " executed " + this.getName() + " command.");
    }

    public void respond(CommandEvent event, String message) {
        if(type == Type.EMBED) {
            respond(event, embedMessage(event,message), RESPONSE_DURATION, TimeUnit.MINUTES);
        } else
            respond(event, message, RESPONSE_DURATION, TimeUnit.MINUTES);
    }

    public void respond(CommandEvent event, MessageEmbed messageEmbed) {
        respond(event, messageEmbed, RESPONSE_DURATION, TimeUnit.MINUTES);
    }

    public RestAction<Message> respond(MessageEmbed messageEmbed, CommandEvent event) {
        return event.getTextChannel().sendMessage(messageEmbed);
    }

    public RestAction<Message> respond(String message, CommandEvent event) {
        if(type == Type.EMBED)
            return  event.getTextChannel().sendMessage(embedMessage(event, message));
        else
            return event.getTextChannel().sendMessage(message);
    }

    public void respond(CommandEvent event, String message, Consumer<Message> consumer) {
        respond(message, event).queue(consumer);
    }

    public void respond(CommandEvent event, MessageEmbed messageEmbed, Consumer<Message> consumer) {
        respond(messageEmbed, event).queue(consumer);
    }

    public void respond(CommandEvent event, String message, long duration, TimeUnit timeUnit) {
        Consumer<Message> consumer = (msg) -> scheduler.schedule(() -> {
            msg.delete().queue();
            event.getMessage().delete().queue();
        }, duration, timeUnit);
        if(type == Type.EMBED)
            event.reply(embedMessage(event, message), consumer);
        else
            event.reply(message, consumer);
    }

    public void respond(CommandEvent event, MessageEmbed messageEmbed, long duration, TimeUnit timeUnit) {
        Consumer<Message> consumer = (msg) -> scheduler.schedule(() -> {
            msg.delete().queue();
            event.getMessage().delete().queue();
        }, duration, timeUnit);
        event.reply(messageEmbed, consumer);
    }

    protected abstract void doCommand(CommandEvent e);

    protected static MessageEmbed getHelpCard(CommandEvent e, Command c) {
        EmbedBuilder eb = new EmbedBuilder().setColor(e.getGuild().getMember(e.getJDA().getSelfUser()).getColor());
        eb.setTitle(c.getName().substring(0, 1).toUpperCase() + c.getName().substring(1) + " Command Help Card:", null);
        String argument = c.getArguments() == null ? "" : c.getArguments();
        eb.addField("Usage", e.getClient().getPrefix() + c.getName() + " " + argument, false);
        eb.addField("Description", c.getHelp(), false);
        String r;
        if (c.getAliases().length == 0)
            r = Locale.getCommandMessage("no-alias").finish();
        else
            r = Arrays.toString(c.getAliases()).replaceAll("\\[", "").replaceAll("]", "");
        eb.addField("Alias", r, false);
        String permission = c.getUserPermissions().length < 1 ? "None" : Arrays.toString(c.getUserPermissions());
        if(c.isOwnerCommand())
            permission = "OWNER";
        if(c.getCategory().equals(Bot.ADMIN))
            permission = "ADMIN";
        eb.addField("Permission", "Required Permission: " + permission, false);
        return eb.build();
    }

    @Override
    public int compareTo(Command o) {
        return this.getName().compareTo(o.getName());
    }

    public Type getType() {
        return type;
    }

    public Minecordbot getMcb() {
        return mcb;
    }

    private String noPermissionMessage() {
        return Locale.getCommandMessage("no-perm-message").finish();
    }

    protected MessageEmbed noPermissionMessageEmbed() {
        String s = noPermissionMessage();
        EmbedBuilder eb = new EmbedBuilder().setTitle(s, null);
        eb.setColor(Color.RED);
        return eb.build();
    }

    private String invalidArgumentsMessage() {
        return Locale.getCommandMessage("invalid-arguments").finish();
    }

    private MessageEmbed invalidArgumentsMessageEmbed() {
        String s = invalidArgumentsMessage();
        EmbedBuilder eb = new EmbedBuilder().setTitle(s, null);
        eb.setColor(Color.RED);
        return eb.build();
    }

    private int countRequiredArgs() {
        if(this.getArguments() == null)
            return 0;
        String raw = this.getArguments();
        if(raw.endsWith("...") || raw.endsWith("...>"))
            return -1;
        String noOptionals = raw.replaceAll("[.*?]", "").trim();
        String[] requiredArgs = noOptionals.split(" ");
        return requiredArgs.length;
    }

    private Permission[] setupPerms() {
        return new Permission[] {Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_READ, Permission.MESSAGE_WRITE,
        Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_EXT_EMOJI, Permission.MESSAGE_MENTION_EVERYONE, Permission.MESSAGE_HISTORY};
    }

    private MessageEmbed embedMessage(CommandEvent event, String message) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        User bot = event.getJDA().getSelfUser();
        Guild g = event.getGuild();
        embedBuilder.setColor(g.getMember(bot).getColor());
        embedBuilder.setAuthor(bot.getName() + " #" + bot.getDiscriminator(),
                null, bot.getEffectiveAvatarUrl());
        embedBuilder.setDescription(message);
        embedBuilder.setFooter("Response", null);
        embedBuilder.setTimestamp(event.getMessage().getCreationTime());
        return embedBuilder.build();
    }

    protected boolean isModChannel(TextChannel c) {
        List<TextChannel> tcs = mcb.getModChannels();
        return tcs.contains(c);
    }

    public enum Type {
        EMBED, DEFAULT
    }
}
