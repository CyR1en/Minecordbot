package us.cyrien.minecordbot;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandClient;
import com.jagrosh.jdautilities.commandclient.CommandClientBuilder;
import com.jagrosh.jdautilities.waiter.EventWaiter;
import io.github.hedgehog1029.frame.Frame;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.utils.PermissionUtil;
import net.dv8tion.jda.core.utils.SimpleLog;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import us.cyrien.minecordbot.accountSync.Authentication.AuthManager;
import us.cyrien.minecordbot.accountSync.Database;
import us.cyrien.minecordbot.accountSync.listener.UserConnectionListener;
import us.cyrien.minecordbot.chat.Messenger;
import us.cyrien.minecordbot.chat.listeners.DiscordMessageListener;
import us.cyrien.minecordbot.chat.listeners.MinecraftEventListener;
import us.cyrien.minecordbot.chat.listeners.TabCompleteV2;
import us.cyrien.minecordbot.commands.discordCommand.*;
import us.cyrien.minecordbot.commands.minecraftCommand.*;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.entity.UpTimer;
import us.cyrien.minecordbot.event.BotReadyEvent;
import us.cyrien.minecordbot.handle.Metrics;
import us.cyrien.minecordbot.instrumentation.Instrumentator;
import us.cyrien.minecordbot.localization.LocalizationFiles;

import javax.security.auth.login.LoginException;
import java.io.File;

public class Minecordbot extends JavaPlugin {

    public static final SimpleLog LOGGER = SimpleLog.getLog("Minecordbot");

    private static Minecordbot instance;
    private static Instrumentator instrumentator;

    private CommandClientBuilder cb;
    private Messenger messenger;
    private AuthManager authManager;
    private UpTimer upTimer;
    private EventWaiter eventWaiter;
    private CommandClient client;
    private JDA jda;
    private Metrics metrics;

    public Command.Category ADMIN = new Command.Category("Admin", (e) -> {
        if (e.getAuthor().getId().equals(e.getClient().getOwnerId()))
            return true;
        if (e.getGuild() == null)
            return true;
        return PermissionUtil.checkPermission(e.getMember(), Permission.ADMINISTRATOR);
    });

    public Command.Category OWNER = new Command.Category("Owner", (e) -> e.getAuthor().getId().equals(e.getClient().getOwnerId()));

    public Command.Category INFO = new Command.Category("Info");

    public Command.Category MISC = new Command.Category("Miscellaneous");

    public Command.Category FUN = new Command.Category("Fun");

    public Command.Category HELP = new Command.Category("Help");

    @Override
    public void onEnable() {
        saveResource("libraries/natives/32/linux/libattach.so", true);
        saveResource("libraries/natives/32/solaris/libattach.so", true);
        saveResource("libraries/natives/32/windows/attach.dll", true);
        saveResource("libraries/natives/64/linux/libattach.so", true);
        saveResource("libraries/natives/64/mac/libattach.dylib", true);
        saveResource("libraries/natives/64/solaris/libattach.so", true);
        saveResource("libraries/natives/64/windows/attach.dll", true);

        eventWaiter = new EventWaiter();
        cb = new CommandClientBuilder();
        authManager = new AuthManager();
        Bukkit.getScheduler().runTaskLater(this, Frame::main, 1L);
        initDatabase();
        if (initConfig()) {
            initJDA();
            initInstances();
            initMCmds();
            initCommandClient();
            initDListener();
            initMListener();
        }
        try {
            instrumentator.instrumentate();
            instrumentator = new Instrumentator(this, new File(getDataFolder(), "libraries/natives/").getPath());
        } catch (Throwable e){
            this.getLogger().warning("[Minecordbot] CraftServer.broadcast() instrumentation failed! server cannot start properly, shutting down...");
            Bukkit.shutdown();
            return;
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

    public void registerDiscordEventModule(Object... listener) {
        for(Object o : listener)
            jda.addEventListener(o);
    }


    public void registerDiscordCommandModule(Command... commands) {
        for(Command c : commands)
            client.addCommand(c);
    }

    //private stuff/ initiation
    private boolean initConfig() {
        boolean initialized = MCBConfig.load();
        if (!initialized)
            LOGGER.warn("MCB data generated, please populate all fields before restarting");
        return initialized;
    }

    private void initCommandClient() {
        cb.setOwnerId(MCBConfig.get("owner_id"));
        cb.setEmojis("\uD83D\uDE03", "\uD83D\uDE2E", "\uD83D\uDE26");
        cb.setPrefix(MCBConfig.get("trigger"));
        registerDiscordCommandModule(
                new List(this),
                new Info(this),
                new Ping(this),
                new Help(this),
                new Eval(this),
                new SetGame(this),
                new SetName(this),
                new SetAvatar(this),
                new Reload(this),
                new MCCommand(this),
                new TextChannel(this));
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
        registerMinecraftEventModule(new MinecraftEventListener(this));
        registerMinecraftEventModule(new TabCompleteV2(this));
        registerMinecraftEventModule(new UserConnectionListener());
        registerMinecraftEventModule(new List(this));
        //registerMinecraftEventModule(new AfkListener(this));
    }

    private void initDListener() {
        registerDiscordEventModule(new DiscordMessageListener(this));
        registerDiscordEventModule(new BotReadyEvent(this));
        registerDiscordEventModule(eventWaiter);
    }

    private void initMCmds() {
        registerMinecraftCommandModule(Dcmd.class);
        registerMinecraftCommandModule(Dme.class);
        registerMinecraftCommandModule(MCBCommand.class);
        registerMinecraftCommandModule(DSync.class);
        registerMinecraftCommandModule(DConfirm.class);
    }

    private void initInstances() {
        messenger = new Messenger(this);
        LocalizationFiles localizationFiles = new LocalizationFiles(this, true);
        instance = this;
        upTimer = new UpTimer();
        metrics = new Metrics(this);
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

    public AuthManager getAuthManager() { return authManager; }

    public static Minecordbot getInstance() {
        return instance;
    }

    public String getUpTime() {
        return upTimer.getCurrentUptime();
    }
}
