package us.cyrien.minecordbot.chat;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.entities.Message;
import us.cyrien.minecordbot.Minecordbot;

import java.util.HashMap;
import java.util.Map;

public class ChatManager {

    private Map<Message, CommandEvent> savedLists;
    private Map<Message, CommandEvent> savedTPS;

    private Minecordbot mcb;
    private ChatStatus chatStatus;

    public ChatManager(Minecordbot mcb) {
        savedLists = new HashMap<>();
        savedTPS = new HashMap<>();
        this.mcb = mcb;
        chatStatus = new ChatStatus();
    }

    public void clearCache(){
        savedLists.putAll(savedTPS);
        savedLists.forEach((k, v) -> k.delete().queue());
    }

    public Map<Message, CommandEvent> getSavedLists() {
        return savedLists;
    }

    public void addSavedList(Message message, CommandEvent event) {
        savedLists.put(message, event);
    }

    public void removeSavedList(Message message) {
        savedLists.remove(message);
    }

    public Map<Message, CommandEvent> getSavedTPS() {
        return savedTPS;
    }

    public void addSavedTPS(Message message, CommandEvent event) {
        savedTPS.put(message, event);
    }

    public void removeSavedTPS(Message message) {
        savedTPS.remove(message);
    }


    public ChatStatus getChatStatus() {
        return chatStatus;
    }
}
