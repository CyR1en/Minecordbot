package us.cyrien.minecordbot;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandClient;
import com.jagrosh.jdautilities.commandclient.CommandClientBuilder;
import com.jagrosh.jdautilities.waiter.EventWaiter;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.utils.PermissionUtil;
import us.cyrien.mcutils.logger.Logger;
import us.cyrien.minecordbot.chat.listeners.discordListeners.DiscordRelayListener;
import us.cyrien.minecordbot.chat.listeners.discordListeners.ModChannelListener;
import us.cyrien.minecordbot.commands.discordCommand.*;

import javax.security.auth.login.LoginException;

public class Bot {

    private CommandClientBuilder cb;
    private EventWaiter eventWaiter;
    private CommandClient client;
    private JDA jda;
    private Minecordbot mcb;

    public static Command.Category ADMIN = new Command.Category("Admin", (e) -> {
        if (e.getAuthor().getId().equals(e.getClient().getOwnerId())) {
            return true;
        }
        if (e.getGuild() == null) {
            return true;
        }
        return PermissionUtil.checkPermission(e.getMember(), Permission.ADMINISTRATOR);
    });

    public static Command.Category OWNER = new Command.Category("Owner");

    public static Command.Category INFO = new Command.Category("Info");

    public static Command.Category MISC = new Command.Category("Misc");

    public static Command.Category FUN = new Command.Category("Fun");

    public static Command.Category HELP = new Command.Category("Help");

    public Bot(Minecordbot minecordbot, EventWaiter waiter) {
        this.mcb = minecordbot;
        eventWaiter = waiter;
        cb = new CommandClientBuilder();
        if (start()) {
            initCommandClient();
            initListeners();
        }
    }

    public boolean start() {
        try {
            String token = mcb.getMcbConfigsManager().getBotConfig().getString("Bot_Token");
            String trigger = mcb.getMcbConfigsManager().getBotConfig().getString("Command_Trigger");
            String gameStr = mcb.getMcbConfigsManager().getBotConfig().getString("Default_Game");
            if (token == null || token.isEmpty()) {
                Logger.err("No token was provided. Shutting down...");
                mcb.getServer().shutdown();
                return false;
            } else if (token.equals("sample.token")) {
                Logger.err("Token was left at default. Please provide a different token. Shutting down...");
                mcb.getServer().shutdown();
                return false;
            }
            JDABuilder builder = new JDABuilder(AccountType.BOT).setToken(token);
            Game game = Game.of("Type " + trigger + "help");
            if (gameStr != null && !gameStr.equals("default") && !gameStr.isEmpty()) {
                game = Game.of(gameStr);
            }
            builder.setGame(game);
            jda = builder.buildAsync();
        } catch (LoginException | RateLimitedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void shutdown() {
        if (jda != null) jda.shutdown();
    }

    public void registerDiscordCommandModule(Command... commands) {
        for (Command c : commands)
            cb.addCommand(c);
    }

    private void initListeners() {
        jda.addEventListener(new DiscordRelayListener(mcb));
        jda.addEventListener(new ModChannelListener(mcb));
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
                new HelpCmd(mcb),
                new ListCmd(mcb),
                new InfoCmd(mcb),
                new PingCmd(mcb),
                new EvalCmd(mcb),
                new PollCmd(mcb),
                new PurgeCmd(mcb),
                new ReloadCmd(mcb),
                new SpoilerCmd(mcb),
                new SetGameCmd(mcb),
                new SetNameCmd(mcb),
                new ShutdownCmd(mcb),
                new MCCommandCmd(mcb),
                new SetAvatarCmd(mcb),
                new SetTriggerCmd(mcb),
                new TextChannelCmd(mcb));
        client = cb.build();
        jda.addEventListener(client);
    }
}
