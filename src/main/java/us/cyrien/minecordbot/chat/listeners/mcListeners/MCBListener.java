package us.cyrien.minecordbot.chat.listeners.mcListeners;

import org.bukkit.event.Listener;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.chat.Messenger;
import us.cyrien.minecordbot.handle.MinecraftMentionHandler;
import us.cyrien.minecordbot.localization.MultiLangMessageParser;

public class MCBListener implements Listener {

    protected final Minecordbot mcb;
    protected final Messenger messenger;
    protected final MultiLangMessageParser langMessageParser;
    protected final MinecraftMentionHandler mentionHandler;

    public MCBListener(Minecordbot mcb) {
        this.mcb = mcb;
        messenger = mcb.getMessenger();
        langMessageParser = new MultiLangMessageParser();
        mentionHandler = new MinecraftMentionHandler(mcb);
    }
}
