package us.cyrien.minecordbot;

import com.jagrosh.jdautilities.waiter.EventWaiter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import us.cyrien.mcutils.Frame;
import us.cyrien.mcutils.config.ConfigManager;
import us.cyrien.mcutils.hook.PluginHook;
import us.cyrien.mcutils.logger.Logger;
import us.cyrien.minecordbot.accountSync.Authentication.AuthManager;
import us.cyrien.minecordbot.accountSync.Database;
import us.cyrien.minecordbot.accountSync.listener.UserConnectionListener;
import us.cyrien.minecordbot.chat.ChatManager;
import us.cyrien.minecordbot.chat.Messenger;
import us.cyrien.minecordbot.chat.listeners.mcListeners.*;
import us.cyrien.minecordbot.commands.minecraftCommand.*;
import us.cyrien.minecordbot.configuration.*;
import us.cyrien.minecordbot.entity.UpTimer;
import us.cyrien.minecordbot.events.StartEvent;
import us.cyrien.minecordbot.events.listener.OnShut;
import us.cyrien.minecordbot.events.listener.OnStart;
import us.cyrien.minecordbot.handle.Metrics;
import us.cyrien.minecordbot.hooks.*;
import us.cyrien.minecordbot.localization.Locale;
import us.cyrien.minecordbot.localization.LocalizationFiles;
import us.cyrien.minecordbot.reporters.*;
import us.cyrien.minecordbot.utils.UUIDFetcher;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Minecordbot extends JavaPlugin {

    private static Minecordbot instance;

    private ScheduledExecutorService scheduler;
    private Messenger messenger;
    private AuthManager authManager;
    private UpTimer upTimer;
    private Bot bot;
    private MCBConfigsManager mcbConfigsManager;
    private EventWaiter eventWaiter;
    private LocalizationFiles localizationFiles;
    private ConfigManager cfgManager;
    private ChatManager chatManager;
    private Metrics metrics;

    @Override
    public void onEnable() {
        try {
            start();
        } catch (Exception e) {
            sendErr(e);
        }
    }

    @Override
    public void onDisable() {
        boolean broadcast = getMcbConfigsManager().getBroadcastConfig().getBoolean(BroadcastConfig.Nodes.SERVER_SHUT);
        if(broadcast) {
            EmbedBuilder eb = new EmbedBuilder().setDescription(Locale.getEventMessage("shut").finish()).setColor(Bot.BOT_COLOR);
            messenger.sendMessageEmbedToAllBoundChannel(eb.build());
            messenger.sendMessageEmbedToAllModChannel(eb.build());
        }
        if (bot != null) {
            chatManager.clearCache();
            bot.shutdown();
        }
    }

    private void start() {
        initInstances();
        Bukkit.getScheduler().runTaskLater(this, () -> {
            if (mcbConfigsManager.setupConfigurations()) {
                Locale.init(mcbConfigsManager);
                bot = new Bot(this, eventWaiter);
                initDatabase();
                initMCmds();
                initPluginHooks();
                initReporters();
                Frame.main();
                UUIDFetcher.init();
                initMListener();
                postInit();
            } else {
                this.getServer().shutdown();
            }
        }, 1L);
        Bukkit.getScheduler().runTaskLater(this, () ->
                Bukkit.getPluginManager().callEvent(new StartEvent(this)), 20L);

    }

    public ChatManager getChatManager() {
        return chatManager;
    }

    private void initInstances() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        localizationFiles = new LocalizationFiles(this, true);
        cfgManager = new ConfigManager(this);
        chatManager = new ChatManager(this);
        authManager = new AuthManager();
        eventWaiter = new EventWaiter();
        mcbConfigsManager = new MCBConfigsManager(cfgManager);
        messenger = new Messenger(this);
        metrics = new Metrics(this);
        instance = this;
    }

    //Framework stuff
    public void registerModule(Class module) {
        Frame.addModule(module);
    }

    public void registerMinecraftEventModule(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public void registerReporter(Class reporter) {
        Frame.addReporter(reporter);
    }


    /**
     * Register a {@link us.cyrien.mcutils.hook.PluginHook PluginHook} that allow the hook to be
     * used without being instantiated.
     * <p>
     * <p> Once the plugin hook have been registered. The hook can be called to a class like wise.
     * <pre>
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
     *
     * @param hook    Hook class that implements {@link PluginHook PluginHook}.
     * @param classes Classes that uses the hook.
     */
    public void registerMCPluginHook(Class hook, @Nullable Class... classes) {
        Frame.addHook(hook);
        for (Class c : classes)
            registerModule(c);
    }

    //private stuff/ initiation
    private void initDatabase() {
        Database.load();
    }

    private void initReporters() {
        registerReporter(InfoHeader.class);
        registerReporter(CfgReporter.class);
        registerReporter(JReporter.class);
        registerReporter(MemReporter.class);
        registerReporter(OSReporter.class);
        registerReporter(PlReporter.class);
        Logger.info("- Reporters registered.");
    }

    private void initMListener() {
        registerMinecraftEventModule(new BroadcastCommandListener(this));
        registerMinecraftEventModule(new ChatListener(this));
        registerMinecraftEventModule(new CommandListener(this));
        registerMinecraftEventModule(new DeathListener(this));
        registerMinecraftEventModule(new UserConnectionListener());
        registerMinecraftEventModule(new UserQuitJoinListener(this));
        registerMinecraftEventModule(new OnShut(this));
        registerMinecraftEventModule(new OnStart(this));
        if (supportNewFeat()) {
            registerMinecraftEventModule(new BroadcastListener(this));
        } else
            Logger.bukkitWarn("- Broadcast Listener is unsupported with " + Bukkit.getBukkitVersion());

        if (supportNewFeat()) {
            registerMinecraftEventModule(new MentionListener(this));
        } else
            Logger.bukkitWarn("- Mention Listener is unsupported with " + Bukkit.getBukkitVersion());

        Logger.info("- Initialized Minecraft listeners.");
    }

    private void initMCmds() {
        registerModule(Dcmd.class);
        registerModule(Dme.class);
        registerModule(McbCommands.class);
        registerModule(DSync.class);
        registerModule(DConfirm.class);
        Logger.info("- Bukkit commands registered.");
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
        registerMCPluginHook(SuperVanishHook.class);
        registerMCPluginHook(VentureChatHook.class);
        Logger.info("- Plugin hooks registered.");
    }

    private void postInit() {
        if (HookContainer.getEssentialsHook() != null) {
            registerMinecraftEventModule(new HelpOpListener(this));
            Logger.info("- Successfully Hooked Essentials and now listening for events.");
        }
        if (HookContainer.getSuperVanishHook() != null) {
            registerMinecraftEventModule(new SuperVanishListener(this));
            Logger.info("- Successfully Hooked SuperVanish and now listening for events.");
        }
        if (HookContainer.getMcMMOHook() != null) {
            registerMinecraftEventModule(new McMMOListener(this));
            Logger.info("- Successfully Hooked mcMMO and now listening for events.");
        }
        upTimer = new UpTimer(this);
    }

    public boolean supportNewFeat() {
        String version = Bukkit.getServer().getClass().getPackage().getName();
        String formattedVersion = version.substring(version.lastIndexOf(46) + 1)
                .replaceAll("(_)([A-Z])\\w+", "").replaceAll("v", "");
        double parsed = Double.valueOf(formattedVersion.replaceAll("_", "."));
        return parsed >= 1.12;
    }

    public List<TextChannel> getModChannels() {
        List<String> tcID = (List<String>) getMcbConfigsManager().getModChannelConfig().getList(ModChannelConfig.Nodes.MOD_CHANNELS);
        if(tcID == null)
            return new ArrayList<>();
        return findValidTextChannels(tcID);
    }

    public List<TextChannel> getRelayChannels() {
        List<String> tcID = (List<String>) getMcbConfigsManager().getChatConfig().getList(ChatConfig.Nodes.RELAY_CHANNELS);
        if(tcID == null)
            return new ArrayList<>();
        return findValidTextChannels(tcID);
    }

    private List<TextChannel> findValidTextChannels(List<String> tcID) {
        List<TextChannel> out = new ArrayList<>();
        tcID.forEach((s) -> {
            if (!s.isEmpty() && bot.getJda() != null) {
                TextChannel tc = bot.getJda().getTextChannelById(s);
                if (tc != null)
                    out.add(tc);
            }
        });
        return out;
    }

    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    public EventWaiter getEventWaiter() {
        return eventWaiter;
    }

    public Bot getBot() {
        return bot;
    }

    public MCBConfigsManager getMcbConfigsManager() {
        return mcbConfigsManager;
    }

    public Messenger getMessenger() {
        return messenger;
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

    public UpTimer getUpTimer() {
        return upTimer;
    }

    public LocalizationFiles getLocalizationFiles() {
        return localizationFiles;
    }

    public ConfigManager getCfgManager() {
        return cfgManager;
    }

    private void sendErr(Exception ex) {
        if (getMcbConfigsManager() != null) {
            User user = null;
            String ownerID = getMcbConfigsManager().getBotConfig().getString(BotConfig.Nodes.OWNER_ID);
            if (StringUtils.isNumeric(ownerID)) {
                if (getBot() != null && getBot().getJda() != null)
                    user = getBot().getJda().getUserById(Long.valueOf(ownerID));
            }
            if (user != null)
                user.openPrivateChannel().queue(pc -> pc.sendMessage("An error occurred: " + ex.getClass().getSimpleName()).queue(),
                        t -> Logger.warn("Cannot send warning"));
        }
    }
}
