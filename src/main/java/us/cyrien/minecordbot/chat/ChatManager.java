package us.cyrien.minecordbot.chat;

import net.dv8tion.jda.core.entities.Message;
import us.cyrien.minecordbot.Minecordbot;

import java.util.ArrayList;
import java.util.List;

public class ChatManager {

    private List<Message> savedMessage;

    private Minecordbot mcb;
    private ChatStatus chatStatus;

    public ChatManager(Minecordbot mcb) {
        savedMessage = new ArrayList<>();
        this.mcb = mcb;
        chatStatus = new ChatStatus();
    }

    public List<Message> getSavedMessage() {
        return savedMessage;
    }

    public void addSavedMessage(Message message) {
        savedMessage.add(message);
    }

    public void removeSavedMessage(Message message) {
        savedMessage.remove(message);
    }

    public ChatStatus getChatStatus() {
        return chatStatus;
    }
}
