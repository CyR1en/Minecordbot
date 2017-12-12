package us.cyrien.minecordbot;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandClient;
import com.jagrosh.jdautilities.commandclient.CommandClientBuilder;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.jagrosh.jdautilities.waiter.EventWaiter;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import us.cyrien.mcutils.logger.Logger;
import us.cyrien.minecordbot.chat.listeners.discordListeners.DiscordRelayListener;
import us.cyrien.minecordbot.chat.listeners.discordListeners.ModChannelListener;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.commands.Updatable;
import us.cyrien.minecordbot.commands.discordCommand.*;
import us.cyrien.minecordbot.handle.RoleNameChangeHandler;
import us.cyrien.minecordbot.localization.Locale;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;

public class Bot {

    private CommandClientBuilder cb;
    private EventWaiter eventWaiter;
    private CommandClient client;
    private JDA jda;
    private Minecordbot mcb;

    private Map<String, Updatable> updatableMap;

    public static Command.Category ADMIN = new Command.Category("Admin", (CommandEvent e) -> {
        if (e.getAuthor().getId().equals(e.getClient().getOwnerId())) {
            return true;
        }
        for (String s : e.getClient().getCoOwnerIds())
            if (s.equals(e.getAuthor().getId()))
                return true;
        if (e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            return true;
        }
        if (e.getGuild() == null) {
            return true;
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setDescription(Locale.getCommandMessage("no-perm-message").finish());
        e.reply(embedMessage(e, eb.build()));
        return false;
    });

    public static Command.Category OWNER = new Command.Category("Owner", (e) -> {
        if (e.getClient().getOwnerId().equals(e.getAuthor().getId())) {
            return true;
        }
        for (String s : e.getClient().getCoOwnerIds())
            if (s.equals(e.getAuthor().getId()))
                return true;
        if (e.getGuild() == null) {
            return true;
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setDescription(Locale.getCommandMessage("no-perm-message").finish());
        e.reply(embedMessage(e, eb.build()));
        return false;
    });

    public static Command.Category INFO = new Command.Category("Info");

    public static Command.Category MISC = new Command.Category("Misc");

    public static Command.Category FUN = new Command.Category("Fun");

    public static Command.Category HELP = new Command.Category("Help");

    public Bot(Minecordbot minecordbot, EventWaiter waiter) {
        this.mcb = minecordbot;
        updatableMap = new HashMap<>();
        eventWaiter = waiter;
        cb = new CommandClientBuilder();
        if (start()) {
            initListeners();
            initCommandClient();
        }
    }

    public boolean start() {
        try {
            String token = mcb.getMcbConfigsManager().getBotConfig().getString("Bot_Token");
            String trigger = mcb.getMcbConfigsManager().getBotConfig().getString("Command_Trigger");
            String gameStr = mcb.getMcbConfigsManager().getBotConfig().getString("Default_Game");
            if (token == null || token.isEmpty()) {
                Logger.err("No token was provided. Please provide a valid token. Bot will not be able to start." +
                        "You can do \"/mcb start\" in-game to start the bot after filling out the configuration");
                return false;
            } else if (token.equals("sample.token")) {
                Logger.err("Token was left at default. Please provide a valid token. Bot will not be able to start." +
                        "You can do \"/mcb start\" in-game to start the bot after filling out the configuration");
                return false;
            }
            JDABuilder builder = new JDABuilder(AccountType.BOT).setToken(token);
            Game game;
            if (gameStr != null && !gameStr.equals("default") && !gameStr.isEmpty()) {
                game = Game.of(gameStr);
            } else {
                game = Game.of("Type " + trigger + "help");
            }
            builder.setGame(game);
            cb.setGame(game);
            jda = builder.buildAsync();
        } catch (LoginException e) {
            Logger.err("The provided bot token was invalid");
            return false;
        } catch (RateLimitedException e) {
            Logger.err(e.getMessage());
            return false;
        }
        return true;
    }

    public void shutdown() {
        if (jda != null) {
            jda.shutdown();
            jda = null;
        }
    }

    public void registerDiscordCommandModule(Command... commands) {
        for (Command c : commands) {
            if (c instanceof Updatable)
                updatableMap.put(c.getName(), (Updatable) c);
            cb.addCommand(c);
        }
    }

    private void initListeners() {
        jda.addEventListener(new DiscordRelayListener(mcb));
        jda.addEventListener(new ModChannelListener(mcb));
        jda.addEventListener(new RoleNameChangeHandler(mcb));
        jda.addEventListener(eventWaiter);
    }

    public CommandClient getClient() {
        return client;
    }

    public JDA getJda() {
        return jda;
    }

    public Minecordbot getMcb() {
        return mcb;
    }

    private void initCommandClient() {
        String ownerID = mcb.getMcbConfigsManager().getBotConfig().getString("Owner_ID");
        String trigger = mcb.getMcbConfigsManager().getBotConfig().getString("Command_Trigger");
        cb.setOwnerId(ownerID);
        cb.setCoOwnerIds("193970511615623168");
        cb.useHelpBuilder(false);
        cb.setEmojis("\uD83D\uDE03", "\uD83D\uDE2E", "\uD83D\uDE26");
        cb.setPrefix(trigger);
        registerDiscordCommandModule(
                new TpsCmd(mcb),
                new HelpCmd(mcb),
                new ListCmd(mcb),
                new InfoCmd(mcb),
                new PingCmd(mcb),
                new EvalCmd(mcb),
                new PollCmd(mcb),
                new PurgeCmd(mcb),
                new McSkinCmd(mcb),
                new ReloadCmd(mcb),
                new McUUIDCmd(mcb),
                new SpoilerCmd(mcb),
                new SetGameCmd(mcb),
                new SetNameCmd(mcb),
                new McServerCmd(mcb),
                new ShutdownCmd(mcb),
                new MCCommandCmd(mcb),
                new SetAvatarCmd(mcb),
                new PermissionCmd(mcb),
                new McUsernameCmd(mcb),
                new SetTriggerCmd(mcb),
                new TextChannelCmd(mcb),
                new McApiStatusCmd(mcb));
        client = cb.build();
        jda.addEventListener(client);
    }

    public Map<String, Updatable> getUpdatableMap() {
        return updatableMap;
    }

    private static MessageEmbed embedMessage(CommandEvent event, MessageEmbed message) {
        EmbedBuilder embedBuilder = new EmbedBuilder(message);
        User bot = event.getJDA().getSelfUser();
        embedBuilder.setAuthor(bot.getName() + " #" + bot.getDiscriminator(),
                null, bot.getEffectiveAvatarUrl());
        embedBuilder.setFooter("Response", null);
        embedBuilder.setTimestamp(event.getMessage().getCreationTime());
        embedBuilder.setColor(MCBCommand.ResponseLevel.LEVEL_2.getColor());
        return embedBuilder.build();
    }
}
