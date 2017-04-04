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
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import us.cyrien.minecordbot.commands.discordCommands.*;
import us.cyrien.minecordbot.configuration.LocalizationFiles;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.core.DrocsidFrame;
import us.cyrien.minecordbot.core.module.DiscordCommand;
import us.cyrien.minecordbot.entity.Messenger;
import us.cyrien.minecordbot.entity.UpTimer;
import us.cyrien.minecordbot.listener.DiscordMessageListener;
import us.cyrien.minecordbot.listener.MinecraftEventListener;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Minecordbot extends JavaPlugin {

    public static final SimpleLog DEBUG_LOGGER = SimpleLog.getLog("MCB-DEBUG");
    public static final SimpleLog LOGGER = SimpleLog.getLog("MCB");

    private static List<DiscordCommand> discordCommands;
    private static Minecordbot instance;
    private static Messenger messenger;
    private static UpTimer upTimer;

    private JDA jda;

    @Override
    public void onEnable() {
        discordCommands = new ArrayList<>();
        Bukkit.getScheduler().runTaskLater(this, Frame::main, 1L);
        Bukkit.getScheduler().runTaskLater(this, DrocsidFrame::main, 1L);
        if(initConfig()) {
            initJDA();
            initInstances();
            initDCmds();
            initMCmds();
            initDListener();
            initMListener();
        }
        upTimer = new UpTimer();
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

    private void initMCmds() {

    }

    private void initMListener() {
        registerMinecraftEventModule(new MinecraftEventListener(this));
    }

    private void initDListener() {
        registerDiscordEventModule(new DiscordMessageListener(this));
    }

    private void initDCmds() {
        registerDiscordCommandModule(PingCommand.class);
        registerDiscordCommandModule(ReloadCommand.class);
        registerDiscordCommandModule(HelpCommand.class);
        registerDiscordCommandModule(TextChannelCommand.class);
        registerDiscordCommandModule(InfoCommand.class);
        registerDiscordCommandModule(ListCommand.class);
    }

    private void initInstances() {
        messenger = new Messenger(this);
         LocalizationFiles localizationFiles = new LocalizationFiles(this, true);
        instance = this;
    }

    //accessors and modifiers
    public static List<DiscordCommand> getDiscordCommands() {
        return discordCommands;
    }

    public JDA getJDA() {
        return jda;
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
        Iterator var1 = discordCommands.iterator();

        while (var1.hasNext()) {
            DiscordCommand dc = (DiscordCommand) var1.next();
            String raw = mRE.getMessage().getContent();
            String noTrigger = raw.replaceAll(MCBConfig.get("trigger"), "");
            String head = noTrigger.split(" ")[0];
            String[] strings = raw.replaceAll(MCBConfig.get("trigger") + head, "").trim().split(" ");
            Minecordbot.DEBUG_LOGGER.info("COMMAND NAME : " + dc.getName()); // FIXME: 3/25/2017
            Minecordbot.DEBUG_LOGGER.info("COMMAND ALIAS : " + dc.getAliases() + " --> " + head); // FIXME: 3/25/2017
            if (dc.getAliases().contains(head) || dc.getName().equalsIgnoreCase(head)) {
                Minecordbot.DEBUG_LOGGER.info("EXECUTING"); // FIXME: 3/25/2017
                dc.execute(mRE, strings);
            }
        }
    }
}
