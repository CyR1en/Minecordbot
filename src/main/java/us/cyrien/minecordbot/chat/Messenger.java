package us.cyrien.minecordbot.chat;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.cyrien.mcutils.logger.Logger;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.chat.exception.IllegalTextChannelException;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Messenger {

    private Minecordbot mcb;

    private ScheduledExecutorService scheduler;

    public Messenger(Minecordbot mcb) {
        scheduler = Executors.newScheduledThreadPool(1);
        this.mcb = mcb;
    }

    //To Minecraft
    public void sendGlobalMessageToMC(String message) {
        for (Player p : Bukkit.getServer().getOnlinePlayers())
            p.sendMessage(message);
    }

    //To Discord
    public void sendMessage(MessageReceivedEvent e, String message, Consumer<Message> consumer) {
        e.getTextChannel().sendMessage(message).queue(consumer);
    }

    public void sendMessageEmbed(MessageReceivedEvent e, MessageEmbed me, Consumer<Message> consumer) {
        e.getTextChannel().sendMessage(me).queue(consumer);
    }

    public void sendMessageEmbed(MessageReceivedEvent e, MessageEmbed me) {
        sendMessageEmbed(e, me, null);
    }

    public void sendMessage(MessageReceivedEvent e, String message) {
        sendMessage(e, message, null);
    }

    public void sendTempMessage(MessageReceivedEvent e, String message, int duration) {
        e.getTextChannel().sendMessage(message).queue(msg -> scheduler.schedule(() -> {
            msg.delete().queue();
            e.getMessage().delete().queue();
        }, duration, TimeUnit.SECONDS));
    }

    public void sendTempMessageEmbed(MessageReceivedEvent e, MessageEmbed me, int duration) {
        e.getTextChannel().sendMessage(me).queue(msg -> scheduler.schedule(() -> {
            msg.delete().queue();
            e.getMessage().delete().queue();
        }, duration, TimeUnit.SECONDS));
    }

    public void sendMessageToAllBoundChannel(String message) {
        List<TextChannel> tcArray = mcb.getRelayChannels();
        if (tcArray.size() == 0) {
            Logger.warn("There are no bound relay channels");
            return;
        }
        tcArray.forEach((tc) -> tc.sendMessage(message).queue());
    }

    public void sendMessageEmbedToAllBoundChannel(MessageEmbed messageEmbed) {
        List<TextChannel> tcArray = mcb.getRelayChannels();
        if (tcArray.size() == 0) {
            Logger.warn("There are no bound relay channels");
            return;
        }
        tcArray.forEach((tc) -> tc.sendMessage(messageEmbed).queue());
    }

    public void sendMessageToAllModChannel(String message) {
        List<TextChannel> tcArray = mcb.getModChannels();
        if (tcArray.size() == 0) {
            Logger.warn("There are no bound Mod channels");
            return;
        }
        tcArray.forEach((tc) -> tc.sendMessage(message).queue());
    }

    public void sendMessageEmbedToAllModChannel(MessageEmbed messageEmbed) {
        List<TextChannel> tcArray = mcb.getModChannels();
        if (tcArray.size() == 0) {
            Logger.warn("There are no bound Mod channels");
            return;
        }
        tcArray.forEach((tc) -> tc.sendMessage(messageEmbed).queue());
    }

    public void sendMessageEmbedToDiscord(TextChannel textChannel, MessageEmbed message) {
        textChannel.sendMessage(message).queue();
    }

    public void sendMessageToDiscord(TextChannel textchannel, String message) {
        textchannel.sendMessage(message).queue();
    }

    //By ID stuff
    public void sendMessageEmbedToDiscordByID(String id, MessageEmbed message) throws IllegalTextChannelException {
        TextChannel tc = mcb.getBot().getJda().getTextChannelById(id);
        if (tc == null)
            throw new IllegalTextChannelException("Text channel " + id + " cannot be found");
        tc.sendMessage(message).queue();
    }

    public void sendMessageToDiscordByID(String id, String message) throws IllegalTextChannelException {
        TextChannel tc = mcb.getBot().getJda().getTextChannelById(id);
        if (tc == null)
            throw new IllegalTextChannelException("Text channel " + id + " cannot be found");
        tc.sendMessage(message).queue();
    }

    public void sendTempMessageToDiscordByID(String id, String message, int duration) throws IllegalTextChannelException {
        TextChannel tc = mcb.getBot().getJda().getTextChannelById(id);
        if (tc == null)
            throw new IllegalTextChannelException("Text channel " + id + " cannot be found");
        tc.sendMessage(message).queue(msg -> scheduler.schedule(() -> {
            msg.delete().queue();
        }, duration, TimeUnit.SECONDS));
    }

    public void sendTempMessageEmbedToDiscordByID(String id, MessageEmbed message, int duration) throws IllegalTextChannelException {
        TextChannel tc = mcb.getBot().getJda().getTextChannelById(id);
        if (tc == null)
            throw new IllegalTextChannelException("Text channel " + id + " cannot be found");
        tc.sendMessage(message).queue(msg -> scheduler.schedule(() -> {
            msg.delete().queue();
        }, duration, TimeUnit.SECONDS));
    }

    public void sendMessageToDM(User user, String message) {
        user.openPrivateChannel().queue(pc -> pc.sendMessage(message).queue(null, t -> {
            Logger.warn(ChatColor.stripColor(cannotSendCode()));
        }), t -> {
            Logger.warn(ChatColor.stripColor(cannotSendCode()));
        });
    }


    private String cannotSendCode() {
        return "&6[MCBMessenger] &rVerification code cannot be sent because you are blocking Direct Messages. &aEnable Direct Messages and try again.";
    }

}