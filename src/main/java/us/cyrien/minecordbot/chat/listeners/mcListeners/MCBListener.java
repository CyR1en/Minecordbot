package us.cyrien.minecordbot.chat.listeners.mcListeners;

import org.bukkit.event.Listener;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.chat.Messenger;
import us.cyrien.minecordbot.configuration.MCBConfigsManager;
import us.cyrien.minecordbot.handle.MinecraftMentionHandler;

public class MCBListener implements Listener {

    protected final Minecordbot mcb;
    protected final Messenger messenger;
    protected final MinecraftMentionHandler mentionHandler;
    protected MCBConfigsManager configsManager;

    public MCBListener(Minecordbot mcb) {
        this.mcb = mcb;
        messenger = mcb.getMessenger();
        mentionHandler = new MinecraftMentionHandler(mcb);
        configsManager = mcb.getMcbConfigsManager();
    }

    public MCBConfigsManager getConfigsManager() {
        return configsManager;
    }
}
