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
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import us.cyrien.mcutils.Frame;
import us.cyrien.mcutils.hook.PluginHook;
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
import us.cyrien.minecordbot.handle.Metrics;
import us.cyrien.minecordbot.hooks.*;
import us.cyrien.minecordbot.localization.LocalizationFiles;

import javax.annotation.Nullable;
import javax.security.auth.login.LoginException;
import java.util.Objects;

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
        Bukkit.getScheduler().runTaskLater(this, () -> {
            Frame.main();
            if (HookContainer.getEssentialsHook() != null)
                registerMinecraftEventModule(new HelpOpListener(this));
        }, 1L);
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
    public void registerModule(Class module) {
        Frame.addModule(module);
    }

    public void registerMinecraftEventModule(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }


    /**
     * Register a {@link us.cyrien.mcutils.hook.PluginHook PluginHook} that allow the hook to be
     * used without being instantiated.
     *
     * <p> Once the plugin hook have been registered. The hook can be called to a class like wise.
     *     <pre>
     *          {@code
     *              @Hook
     *              private MCBHook hook;
     *              public void hook() {
     *                  if(hook != null)
     *                      Minecordbot mcb = hook.getPlugin();
     *              }
     *          }
     *     </pre>
     * </p>
     *  @param hook
     *        Hook class that implements {@link PluginHook PluginHook}.
     * @param classes
     *        Classes that uses the hook.
     */
    public void registerMCPluginHook(Class hook, @Nullable Class... classes) {
        Frame.addHook(hook);
        for(Class c : classes)
            registerModule(c);
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
            JDABuilder builder = new JDABuilder(AccountType.BOT).setToken(MCBConfig.get("bot_token"));
            Game game = Game.of("Type " + MCBConfig.get("trigger") + "help");
            if(!Objects.equals(MCBConfig.get("default_game"), MCBConfig.getDefault().get("default_game")) && !StringUtils.isBlank(MCBConfig.get("default_game"))) {
                String sGame = MCBConfig.get("default_game");
                if(sGame != null)
                    game = Game.of(sGame);
            }
            builder.setGame(game);
            jda = builder.buildAsync();
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
        if (isV1_12())
            registerMinecraftEventModule(new BroadcastListener(this));
        else
            Logger.info("Broadcast Listener is unsupported with " + Bukkit.getBukkitVersion());
    }

    private void initDListener() {
        registerDiscordEventModule(new DiscordRelayListener(this));
        registerDiscordEventModule(eventWaiter);
    }

    private void initMCmds() {
        registerModule(Dcmd.class);
        registerModule(Dme.class);
        registerModule(ExeDCommand.class);
        registerModule(DSync.class);
        registerModule(DConfirm.class);
    }

    private void registerMCPluginHook(Class clazz) {
        registerMCPluginHook(clazz, HookContainer.class);
    }

    private void initPluginHooks() {
        registerMCPluginHook(MCBHook.class);
        registerMCPluginHook(GriefPreventionHook.class);
        registerMCPluginHook(VaultHook.class);
        registerMCPluginHook(MVHook.class);
        registerMCPluginHook(PermissionsExHook.class);
        registerMCPluginHook(EssentialsHook.class);
        registerMCPluginHook(mcMMOHook.class);
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
        String formattedVersion = version.substring(version.lastIndexOf(46) + 1);
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
