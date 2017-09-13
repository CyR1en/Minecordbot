package us.cyrien.minecordbot;

import us.cyrien.jdautilities.commandclient.Command;
import us.cyrien.jdautilities.commandclient.CommandClient;
import us.cyrien.jdautilities.commandclient.CommandClientBuilder;
import us.cyrien.jdautilities.waiter.EventWaiter;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.utils.PermissionUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import us.cyrien.mcutils.Frame;
import us.cyrien.mcutils.annotations.Hook;
import us.cyrien.mcutils.logger.Logger;
import us.cyrien.minecordbot.accountSync.Authentication.AuthManager;
import us.cyrien.minecordbot.accountSync.Database;
import us.cyrien.minecordbot.accountSync.listener.UserConnectionListener;
import us.cyrien.minecordbot.chat.Messenger;
import us.cyrien.minecordbot.chat.listeners.discordListeners.DiscordRelayListener;
import us.cyrien.minecordbot.chat.listeners.mcListeners.*;
import us.cyrien.minecordbot.commands.discordCommand.*;
import us.cyrien.minecordbot.commands.minecraftCommand.*;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.entity.UpTimer;
import us.cyrien.minecordbot.event.BotReadyEvent;
import us.cyrien.minecordbot.handle.Metrics;
import us.cyrien.minecordbot.hooks.*;
import us.cyrien.minecordbot.localization.LocalizationFiles;

import javax.security.auth.login.LoginException;

public class Minecordbot extends JavaPlugin {

    private static Minecordbot instance;

    private CommandClientBuilder cb;
    private Messenger messenger;
    private AuthManager authManager;
    private UpTimer upTimer;
    private EventWaiter eventWaiter;
    private CommandClient client;
    private JDA jda;
    private Metrics metrics;

    @Hook
    private  EssentialsHook essentialsHook;

    public Command.Category ADMIN = new Command.Category("Admin", (e) -> {
        if (e.getAuthor().getId().equals(e.getClient().getOwnerId())) {
            return true;
        }
        if (e.getGuild() == null) {
            return true;
        }
        return PermissionUtil.checkPermission(e.getMember(), Permission.ADMINISTRATOR);
    });

    public Command.Category OWNER = new Command.Category("Owner");

    public Command.Category INFO = new Command.Category("Info");

    public Command.Category MISC = new Command.Category("Misc");

    public Command.Category FUN = new Command.Category("Fun");

    public Command.Category HELP = new Command.Category("Help");

    @Override
    public void onEnable() {
        eventWaiter = new EventWaiter();
        cb = new CommandClientBuilder();
        authManager = new AuthManager();
        Bukkit.getScheduler().runTaskLater(this, Frame::main, 1L);
        initDatabase();
        if (initConfig()) {
            initJDA();
            initInstances();
            initMCmds();
            initPluginHooks();
            initCommandClient();
            initDListener();
            initMListener();
        }
    }

    @Override
    public void onDisable() {
        shutdown();
    }

    public void shutdown() {
        if (jda != null) jda.shutdown();
    }

    //Framework stuff
    public void registerMinecraftCommandModule(Class module) {
        Frame.addModule(module);
    }

    public void registerMinecraftEventModule(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public void registerMCPluginHook(Class... hooks) {
        for (Class p : hooks) {
            Frame.addHook(p);
        }
    }

    public void registerDiscordEventModule(Object... listener) {
        for (Object o : listener)
            jda.addEventListener(o);
    }


    public void registerDiscordCommandModule(Command... commands) {
        for (Command c : commands)
            cb.addCommand(c);
    }

    //private stuff/ initiation
    private boolean initConfig() {
        boolean initialized = MCBConfig.load();
        if (!initialized) {
            Logger.warn("Minecordbot configuration have been generated or new fields have been added on the config. " +
                    "\n Please make sure to fill in all config fields correctly. Server will be stopped for safety.");
            Bukkit.shutdown();
        }
        return initialized;
    }

    private void initCommandClient() {
        String ownerID = MCBConfig.get("owner_id");
        cb.setOwnerId(ownerID);
        cb.setCoOwnerIds("193970511615623168");
        cb.useHelpBuilder(false);
        cb.setEmojis("\uD83D\uDE03", "\uD83D\uDE2E", "\uD83D\uDE26");
        cb.setPrefix(MCBConfig.get("trigger"));
        registerDiscordCommandModule(
                new HelpCmd(this),
                new ListCmd(this),
                new InfoCmd(this),
                new PingCmd(this),
                new EvalCmd(this),
                new PollCmd(this),
                new PurgeCmd(this),
                new ReloadCmd(this),
                new SpoilerCmd(this),
                new SetGameCmd(this),
                new SetNameCmd(this),
                new MCCommandCmd(this),
                new SetAvatarCmd(this),
                new SetTriggerCmd(this),
                new TextChannelCmd(this));
        client = cb.build();
        registerDiscordEventModule(client);
    }

    private void initDatabase() {
        Database.load();
    }

    private void initJDA() {
        try {
            jda = new JDABuilder(AccountType.BOT).setToken(MCBConfig.get("bot_token")).buildAsync();
        } catch (LoginException | RateLimitedException e) {
            e.printStackTrace();
        }
    }

    private void initMListener() {
        registerMinecraftEventModule(new BroadcastCommandListener(this));
        registerMinecraftEventModule(new ChatListener(this));
        registerMinecraftEventModule(new CommandListener(this));
        registerMinecraftEventModule(new DeathListener(this));
        registerMinecraftEventModule(new MentionListener(this));
        registerMinecraftEventModule(new UserConnectionListener());
        registerMinecraftEventModule(new UserQuitJoinListener(this));
        registerMinecraftEventModule(new ListCmd(this));
        if(essentialsHook != null)
            registerMinecraftEventModule(new HelpOpListener(this));
        if(isV1_12())
            registerMinecraftEventModule(new BroadcastListener(this));
        else
            Logger.info("Broadcast Listener is unsupported with " + Bukkit.getBukkitVersion());
    }

    private void initDListener() {
        registerDiscordEventModule(new DiscordRelayListener(this));
        registerDiscordEventModule(new BotReadyEvent(this));
        registerDiscordEventModule(eventWaiter);
    }

    private void initMCmds() {
        registerMinecraftCommandModule(Dcmd.class);
        registerMinecraftCommandModule(Dme.class);
        registerMinecraftCommandModule(ExeDCommand.class);
        registerMinecraftCommandModule(DSync.class);
        registerMinecraftCommandModule(DConfirm.class);
    }

    private void initPluginHooks() {
        registerMCPluginHook(MCBHook.class);
        registerMCPluginHook(GriefPreventionHook.class);
        registerMCPluginHook(VaultHook.class);
        registerMCPluginHook(MVHook.class);
        registerMCPluginHook(PermissionsExHook.class);
        registerMCPluginHook(EssentialsHook.class);
    }

    private void initInstances() {
        messenger = new Messenger(this);
        LocalizationFiles localizationFiles = new LocalizationFiles(this, true);
        instance = this;
        upTimer = new UpTimer();
        metrics = new Metrics(this);
    }

    private boolean isV1_12() {
        String version = Bukkit.getServer().getClass().getPackage().getName();
        System.out.println(version);
        String formattedVersion = version.substring(version.lastIndexOf(46) + 1);
        System.out.println("Formatted: " + formattedVersion);
        return formattedVersion.equals("v1_12_R1");
    }

    public CommandClient getClient() {
        return client;
    }

    public Messenger getMessenger() {
        return messenger;
    }

    public EventWaiter getEventWaiter() {
        return eventWaiter;
    }

    public JDA getJDA() {
        return jda;
    }

    public AuthManager getAuthManager() {
        return authManager;
    }

    public static Minecordbot getInstance() {
        return instance;
    }

    public String getUpTime() {
        return upTimer.getCurrentUptime();
    }
}
