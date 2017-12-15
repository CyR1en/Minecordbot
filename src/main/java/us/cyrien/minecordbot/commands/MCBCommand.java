package us.cyrien.minecordbot.commands;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.requests.RestAction;
import us.cyrien.mcutils.logger.Logger;
import us.cyrien.minecordbot.Bot;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.configuration.MCBConfigsManager;
import us.cyrien.minecordbot.localization.Locale;
import us.cyrien.minecordbot.utils.SRegex;
import us.cyrien.minecordbot.utils.mcpinger.MCPing;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.regex.Pattern;


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
        this.helpBiConsumer = (ce, c) -> respond(ce, getHelpCard(ce, c));
        this.botPermissions = setupPerms();
        configsManager = minecordbot.getMcbConfigsManager();
        type = Type.DEFAULT;
        auto = configsManager.getBotConfig().getBoolean("Delete_Response");
        scheduler = mcb.getScheduler();
        Locale.init(mcb.getMcbConfigsManager());
    }

    @Override
    protected void execute(CommandEvent event) {
        try {
            int countedReqArgs = countRequiredArgs();
            int countedArgsProvided = event.getArgs().isEmpty() ? 0 : event.getArgs().split(" ").length;
            if ((countedReqArgs > countedArgsProvided || countedReqArgs < countedArgsProvided) && countRequiredArgs() != -1) {
                respond(event, invalidArgumentsMessageEmbed());
                respond(event, getHelpCard(event, this));
                return;
            }
            if (!checkRoleBasedPerm(event.getMember())) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setDescription(Locale.getCommandMessage("no-perm-message").finish());
                event.reply(embedMessage(event, eb.build(), ResponseLevel.LEVEL_3));
                return;
            }
            doCommand(event);
            Logger.info(event.getAuthor().getName() + " executed " + this.getName() + " command.");
        } catch (Exception ex) {
            ex.printStackTrace();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ex.printStackTrace(new PrintStream(baos));
            byte[] out = baos.toByteArray();
                /*
                File file = new File("StackTrace.txt");
                PrintStream ps = new PrintStream(file);
                ex.printStackTrace(ps);
                */
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(Locale.getCommandMessage("fatal-error").f(this.getClass().getSimpleName()));
            eb.setDescription(ex.getClass().getSimpleName());
            eb.addField("StackTrace", "Send a report to the Dev! Click the [here](attachment://stacktrace.txt) to download the stacktrace.", false);
            MessageEmbed formatted = embedMessage(event, eb.build(), ResponseLevel.LEVEL_3);
            respond(event, "Generating stacktrace...", (msg) -> scheduler.schedule(() -> {
                msg.delete().queue();
                event.getTextChannel().sendFile(out, "stacktrace.txt", new MessageBuilder().setEmbed(formatted).build()).queue();
            }, 1, TimeUnit.SECONDS));
        }
    }

    public void respond(CommandEvent event, String message) {
        respond(null, event, message);
    }

    public void respond(ResponseLevel level, CommandEvent event, String message) {
        if (type == Type.EMBED) {
            level = level == null ? ResponseLevel.DEFAULT : level;
            respond(event, embedMessage(event, message, level), RESPONSE_DURATION, TimeUnit.MINUTES);
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
        return respond(null, message, event);
    }

    public RestAction<Message> respond(ResponseLevel level, String message, CommandEvent event) {
        if (type == Type.EMBED) {
            level = level == null ? ResponseLevel.DEFAULT : level;
            return event.getTextChannel().sendMessage(embedMessage(event, message, level));
        } else
            return event.getTextChannel().sendMessage(message);
    }

    public void respond(CommandEvent event, String message, Consumer<Message> consumer) {
        respond(null, message, event).queue(consumer);
    }

    public void respond(CommandEvent event, MessageEmbed messageEmbed, Consumer<Message> consumer) {
        respond(messageEmbed, event).queue(consumer);
    }

    public void respond(CommandEvent event, String message, long duration, TimeUnit timeUnit) {
        respond(event, message, duration, timeUnit, null);
    }

    public void respond(CommandEvent event, String message, ResponseLevel level) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setDescription(message);
        respond(event, embedMessage(event, eb.build(), level));
    }

    public void respond(CommandEvent event, MessageEmbed message, ResponseLevel level) {
        respond(event, embedMessage(event, message, level));
    }


    //new RespondAPI below. Omit above methods after
    public void respond(CommandEvent event, String message, long duration, TimeUnit timeUnit, ResponseLevel level) {
        Consumer<Message> consumer = auto ? (msg) -> scheduler.schedule(() -> {
            msg.delete().queue();
            event.getMessage().delete().queue();
        }, duration, timeUnit) : msg -> {
        };
        if (type == Type.EMBED) {
            level = level == null ? ResponseLevel.DEFAULT : level;
            event.reply(embedMessage(event, message, level), consumer);
        } else
            event.reply(message, consumer);
    }

    public void respond(CommandEvent event, MessageEmbed messageEmbed, long duration, TimeUnit timeUnit) {
        Consumer<Message> consumer = auto ? (msg) -> scheduler.schedule(() -> {
            msg.delete().queue();
            event.getMessage().delete().queue();
        }, duration, timeUnit) : msg -> {
        };
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
        if (c.isOwnerCommand())
            permission = "OWNER";
        if (c.getCategory().equals(Bot.ADMIN))
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
        eb.setColor(ResponseLevel.LEVEL_3.getColor());
        return eb.build();
    }

    private int countRequiredArgs() {
        if (this.getArguments() == null)
            return 0;
        String raw = this.getArguments();
        if (raw.endsWith("...") || raw.endsWith("...>"))
            return -1;
        String noOptionals = raw.replaceAll("\\[.*?]", "").trim();
        List<String> requiredArgs = new SRegex(noOptionals).find(Pattern.compile("<.*?>")).getResultsList();
        return requiredArgs.size();
    }

    private Permission[] setupPerms() {
        return new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_READ, Permission.MESSAGE_WRITE,
                Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_EXT_EMOJI, Permission.MESSAGE_MENTION_EVERYONE, Permission.MESSAGE_HISTORY};
    }

    protected MessageEmbed embedMessage(CommandEvent event, String message, ResponseLevel level, String footer) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        User bot = event.getJDA().getSelfUser();
        embedBuilder.setAuthor(bot.getName() + " #" + bot.getDiscriminator(),
                null, bot.getEffectiveAvatarUrl());
        embedBuilder.setDescription(message);
        footer = footer == null ? (level == null ? ResponseLevel.DEFAULT.getFooter() : level.getFooter()) : footer;
        embedBuilder.setFooter(footer, null);
        embedBuilder.setTimestamp(event.getMessage().getCreationTime());
        embedBuilder.setColor(getResponseColor(level, event.getGuild(), bot));
        return embedBuilder.build();
    }

    protected MessageEmbed embedMessage(CommandEvent event, String message, ResponseLevel level) {
        return embedMessage(event, message, level, null);
    }

    protected MessageEmbed embedMessage(CommandEvent event, MessageEmbed message, ResponseLevel level) {
        return embedMessage(event, message, level, null);
    }

    protected MessageEmbed embedMessage(CommandEvent event, MessageEmbed message, ResponseLevel level, String footer) {
        EmbedBuilder embedBuilder = new EmbedBuilder(message);
        User bot = event.getJDA().getSelfUser();
        embedBuilder.setAuthor(bot.getName() + "#" + bot.getDiscriminator(),
                null, bot.getEffectiveAvatarUrl());
        footer = footer == null ? (level == null ? ResponseLevel.DEFAULT.getFooter() : level.getFooter()) : footer;
        embedBuilder.setFooter(footer, null);
        embedBuilder.setTimestamp(event.getMessage().getCreationTime());
        embedBuilder.setColor(getResponseColor(level, event.getGuild(), bot));
        return embedBuilder.build();
    }

    private Color getResponseColor(ResponseLevel level, Guild g, User bot) {
        if (level == null) {
            return g.getMember(bot).getColor();
        }
        switch (level) {
            case LEVEL_1:
                return ResponseLevel.LEVEL_1.getColor();
            case LEVEL_2:
                return ResponseLevel.LEVEL_2.getColor();
            case LEVEL_3:
                return ResponseLevel.LEVEL_3.getColor();
            default:
                return g.getMember(bot).getColor();
        }
    }

    protected MCPing.McPing ping(String serverIP, CommandEvent e) {
        EmbedBuilder eb = new EmbedBuilder();
        String[] ipPort = serverIP.split(":");
        if (ipPort.length == 1) {
            return MCPing.pingPc(ipPort[0], 25565, true);
        } else if (ipPort.length == 2) {
            try {
                return MCPing.pingPc(ipPort[0], Integer.parseInt(ipPort[1]), true);
            } catch (NumberFormatException ex) {
                eb.setDescription("Error getting server info: `java.lang.NumberFormatException`");
                respond(e, embedMessage(e, eb.build(), ResponseLevel.LEVEL_3));
                return null;
            }
        } else {
            eb.setDescription(Locale.getCommandsMessage("mcserver.error").finish());
            respond(e, embedMessage(e, eb.build(), ResponseLevel.LEVEL_3));
            return null;
        }
    }

    protected boolean isModChannel(TextChannel c) {
        List<TextChannel> tcs = mcb.getModChannels();
        return tcs.contains(c);
    }

    protected boolean checkRoleBasedPerm(Member m) {
        if (m.getRoles().size() == 0) {
            System.out.println("Member's role size is 0");
            return allowed(mcb.getMcbConfigsManager().getPermConfig().getString("Default"));
        }
        Role r = null;
        boolean roleInDb = false;
        for (Role r1 : m.getRoles()) {
            if (exists(r1)) {
                roleInDb = true;
                r = r1;
                break;
            }
        }
        if(roleInDb) {
            return allowed(mcb.getMcbConfigsManager().getPermConfig().getString(r.getId() + ".Permission"));
        } else {
            return allowed(mcb.getMcbConfigsManager().getPermConfig().getString("Default"));
        }
    }

    private boolean exists(Role role) {
        Set<String> keys = mcb.getMcbConfigsManager().getPermConfig().getKeys();
        return keys.contains(role.getId());
    }

    private boolean allowed(String rolePerm) {
        String perm = rolePerm;
        if (perm == null || perm.isEmpty())
            return true;
        perm = perm.toLowerCase();
        //check command targets
        String lowerName = name.toLowerCase();
        if (perm.contains("{+" + lowerName + "}"))
            return true;
        if (perm.contains("{-" + lowerName + "}"))
            return false;

        //check alias targets
        if(aliases.length > 0) {
            for (String alias : aliases) {
                String lowerAlias = alias.toLowerCase();
                if (perm.contains("{+" + lowerAlias + "}"))
                    return true;
                if (perm.contains("{-" + lowerAlias + "}"))
                    return false;
            }
        }

        //check category target
        String lowerCat = category == null ? null : category.getName().toLowerCase();
        if (lowerCat != null) {
            if (perm.contains("{+" + lowerCat + "}"))
                return true;
            if (perm.contains("{-" + lowerCat + "}"))
                return false;
        }
        return !perm.contains("{-all}");
    }

    public enum Type {
        EMBED, DEFAULT
    }

    public enum ResponseLevel {
        LEVEL_1 {
            public Color getColor() {
                return new Color(92, 184, 92);
            }

            public String getFooter() {
                return "Success";
            }
        }, LEVEL_2 {
            public Color getColor() {
                return new Color(243, 119, 54);
            }

            public String getFooter() {
                return "Warning";
            }
        }, LEVEL_3 {
            public Color getColor() {
                return new Color(188, 75, 79);
            }

            public String getFooter() {
                return "Error";
            }
        }, DEFAULT {
            public Color getColor() {
                return null;
            }

            public String getFooter() {
                return "Response";
            }
        };

        public abstract Color getColor();

        public abstract String getFooter();
    }
}
