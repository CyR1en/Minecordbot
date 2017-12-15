package us.cyrien.minecordbot.api;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import us.cyrien.minecordbot.chat.exception.IllegalTextChannelException;

import java.util.function.Consumer;

public interface IMessenger {

    //To Minecraft
    void sendGlobalMessageToMC(String message);

    //To Discord
    void sendMessage(MessageReceivedEvent e, String message, Consumer<Message> consumer) ;

    void sendMessageEmbed(MessageReceivedEvent e, MessageEmbed me, Consumer<Message> consumer);

    void sendMessageEmbed(MessageReceivedEvent e, MessageEmbed me);

    void sendMessage(MessageReceivedEvent e, String message);

    void sendTempMessage(MessageReceivedEvent e, String message, int duration);

    void sendTempMessageEmbed(MessageReceivedEvent e, MessageEmbed me, int duration);

    void sendMessageToAllBoundChannel(String message);

    void sendMessageEmbedToAllBoundChannel(MessageEmbed messageEmbed);

    void sendMessageToAllModChannel(String message);

    void sendMessageEmbedToAllModChannel(MessageEmbed messageEmbed);

    void sendMessageEmbedToDiscord(TextChannel textChannel, MessageEmbed message);

    void sendMessageToDiscord(TextChannel textchannel, String message) ;

    //By ID stuff
    void sendMessageEmbedToDiscordByID(String id, MessageEmbed message) throws IllegalTextChannelException;

    void sendMessageToDiscordByID(String id, String message) throws IllegalTextChannelException ;

    void sendTempMessageToDiscordByID(String id, String message, int duration) throws IllegalTextChannelException;

    void sendTempMessageEmbedToDiscordByID(String id, MessageEmbed message, int duration) throws IllegalTextChannelException;

    void sendMessageToDM(User user, String message);

}
