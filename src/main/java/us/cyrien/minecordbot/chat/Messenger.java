package us.cyrien.minecordbot.chat;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.core.exceptions.IllegalTextChannelException;
import us.cyrien.minecordbot.Minecordbot;

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
        JSONArray tcArray = MCBConfig.get("text_channels");
        assert tcArray != null;
        for (Object s : tcArray) {
            if (mcb.getJDA().getTextChannelById(s.toString()) != null)
                mcb.getJDA().getTextChannelById(s.toString()).sendMessage(message).queue();
        }
    }

    public void sendMessageEmbedToAllBoundChannel(MessageEmbed messageEmbed) {
        JSONArray tcArray = MCBConfig.get("text_channels");
        assert tcArray != null;
        for (Object s : tcArray) {
            if (mcb.getJDA().getTextChannelById(s.toString()) != null)
                mcb.getJDA().getTextChannelById(s.toString()).sendMessage(messageEmbed).queue();
        }
    }

    public void sendMessageEmbedToDiscord(TextChannel textChannel, MessageEmbed message) {
        textChannel.sendMessage(message).queue();
    }

    public void sendMessageToDiscord(TextChannel textchannel, String message) {
        textchannel.sendMessage(message).queue();
    }

    //By ID stuff
    public void sendMessageEmbedToDiscordByID(String id, MessageEmbed message) throws IllegalTextChannelException{
        TextChannel tc = mcb.getJDA().getTextChannelById(id);
        if(tc == null)
            throw new IllegalTextChannelException("Text channel " + id + " cannot be found");
        tc.sendMessage(message).queue();
    }

    public void sendMessageToDiscordByID(String id, String message) throws IllegalTextChannelException {
        TextChannel tc = mcb.getJDA().getTextChannelById(id);
        if(tc == null)
            throw new IllegalTextChannelException("Text channel " + id + " cannot be found");
        tc.sendMessage(message).queue();
    }

    public void sendTempMessageToDiscordByID(String id, String message, int duration) throws IllegalTextChannelException {
        TextChannel tc = mcb.getJDA().getTextChannelById(id);
        if(tc == null)
            throw new IllegalTextChannelException("Text channel " + id + " cannot be found");
        tc.sendMessage(message).queue(msg -> scheduler.schedule(() -> {
            msg.delete().queue();
        }, duration, TimeUnit.SECONDS));
    }

    public void sendTempMessageEmbedToDiscordByID(String id, MessageEmbed message, int duration) throws IllegalTextChannelException {
        TextChannel tc = mcb.getJDA().getTextChannelById(id);
        if(tc == null)
            throw new IllegalTextChannelException("Text channel " + id + " cannot be found");
        tc.sendMessage(message).queue(msg -> scheduler.schedule(() -> {
            msg.delete().queue();
        }, duration, TimeUnit.SECONDS));
    }

    //Command Response
    public void sendCommandEmbedResponse(MessageReceivedEvent e, MessageEmbed me, int duration)  {
        boolean tempResponse = MCBConfig.get("auto_delete_command_response");
        if (tempResponse)
            sendTempMessageEmbed(e, me, duration);
        else
            sendMessageEmbed(e, me);
    }

    public void sendCommandResponse(MessageReceivedEvent e, String message, int duration) {
        boolean tempResponse = MCBConfig.get("auto_delete_command_response");
        if (tempResponse)
            sendTempMessage(e, message, duration);
        else
            sendMessage(e, message);
    }

}