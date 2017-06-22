package us.cyrien.minecordbot.main;

import io.github.hedgehog1029.frame.Frame;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.utils.SimpleLog;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import us.cyrien.minecordbot.commands.discordCommands.*;
import us.cyrien.minecordbot.commands.minecraftCommand.Dcmd;
import us.cyrien.minecordbot.commands.minecraftCommand.Dme;
import us.cyrien.minecordbot.commands.minecraftCommand.Reload;
import us.cyrien.minecordbot.configuration.LocalizationFiles;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.core.DrocsidFrame;
import us.cyrien.minecordbot.core.module.DiscordCommand;
import us.cyrien.minecordbot.entity.Messenger;
import us.cyrien.minecordbot.entity.UpTimer;
import us.cyrien.minecordbot.event.BotReadyEvent;
import us.cyrien.minecordbot.handle.Metrics;
import us.cyrien.minecordbot.handle.Updater;
import us.cyrien.minecordbot.listener.AfkListener;
import us.cyrien.minecordbot.listener.DiscordMessageListener;
import us.cyrien.minecordbot.listener.MinecraftEventListener;
import us.cyrien.minecordbot.listener.TabCompleteV2;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Minecordbot extends JavaPlugin {

    public static final SimpleLog LOGGER = SimpleLog.getLog("MCB");

    private static List<DiscordCommand> discordCommands;
    private static Minecordbot instance;
    private static Messenger messenger;
    private static UpTimer upTimer;

    private JDA jda;
    private ArrayList<Player> AFKPlayers;
    private Updater updater;
    private Metrics metrics;

    @Override
    public void onEnable() {
        discordCommands = new ArrayList<>();
        Bukkit.getScheduler().runTaskLater(this, Frame::main, 1L);
        Bukkit.getScheduler().runTaskLater(this, DrocsidFrame::main, 1L);
        if (initConfig()) {
            initJDA();
            initInstances();
            initDCmds();
            initMCmds();
            initDListener();
            initMListener();
        }
    }

    @Override
    public void onDisable() {
        shutdown();
    }

    public void shutdown() {
        if (jda != null)
            jda.shutdown();
    }

    //Framework stuff
    public void registerMinecraftCommandModule(Class module) {
        Frame.addModule(module);
    }

    public void registerMinecraftEventModule(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public void registerDiscordEventModule(ListenerAdapter la) {
        jda.addEventListener(la);
    }

    public void registerDiscordCommandModule(Class module) {
        DrocsidFrame.addModule(module);
    }

    //private stuff/ initiation
    private boolean initConfig() {
        boolean initialized = MCBConfig.load();
        if (!initialized)
            SimpleLog.getLog("Minecordbot").warn("MCB config generated, please populate all fields before restarting");
        return initialized;
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
        //registerMinecraftEventModule(new AfkListener(this));
    }

    private void initDListener() {
        registerDiscordEventModule(new DiscordMessageListener(this));
        registerDiscordEventModule(new BotReadyEvent(this));
    }

    private void initMCmds() {
        registerMinecraftCommandModule(Dcmd.class);
        registerMinecraftCommandModule(Dme.class);
        registerMinecraftCommandModule(Reload.class);
    }

    private void initDCmds() {
        registerDiscordCommandModule(PingCommand.class);
        registerDiscordCommandModule(ReloadCommand.class);
        registerDiscordCommandModule(HelpCommand.class);
        registerDiscordCommandModule(TextChannelCommand.class);
        registerDiscordCommandModule(InfoCommand.class);
        registerDiscordCommandModule(ListCommand.class);
        registerDiscordCommandModule(EvalCommand.class);
        registerDiscordCommandModule(PermissionCommand.class);
        registerDiscordCommandModule(SendMinecraftCommandCommand.class);
        registerDiscordCommandModule(SetNicknameCommand.class);
        registerDiscordCommandModule(SetUsernameCommand.class);
        registerDiscordCommandModule(SetGameCommand.class);
        registerDiscordCommandModule(SetAvatarCommand.class);
        registerDiscordCommandModule(ShutDownCommand.class);
        registerDiscordCommandModule(SetTrigger.class);
        //registerDiscordCommandModule(ImageSearchCommand.class);
    }

    private void initInstances() {
        messenger = new Messenger(this);
        LocalizationFiles localizationFiles = new LocalizationFiles(this, true);
        instance = this;
        upTimer = new UpTimer();
        if (MCBConfig.get("auto_update"))
            updater = new Updater(this, 101682, this.getFile(), Updater.UpdateType.DEFAULT, false);
        else
            updater = new Updater(this, 101682, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, true);
        metrics = new Metrics(this);
        AFKPlayers = new ArrayList<>();
    }

    //accessors and modifiers
    public static List<DiscordCommand> getDiscordCommands() {
        return discordCommands;
    }

    public JDA getJDA() {
        return jda;
    }

    public ArrayList<Player> getAFKPlayers() {
        return AFKPlayers;
    }

    public static Messenger getMessenger() {
        return messenger;
    }

    public static Minecordbot getInstance() {
        return instance;
    }

    public static String getUpTime() {
        return upTimer.getCurrentUptime();
    }

    public void handleCommand(MessageReceivedEvent mRE) {
        Iterator cmdIterator = discordCommands.iterator();

        while (cmdIterator.hasNext()) {
            DiscordCommand dc = (DiscordCommand) cmdIterator.next();
            String raw = mRE.getMessage().getContent();
            String noTrigger = raw.replaceAll(MCBConfig.get("trigger"), "");
            String head = noTrigger.split(" ")[0];
            String[] strings = raw.replaceAll(MCBConfig.get("trigger") + head, "").trim().split("\\s");
            if (dc.getAliases().contains(head) || dc.getName().equalsIgnoreCase(head)) {
                dc.execute(mRE, strings);
            }
        }
    }
}
