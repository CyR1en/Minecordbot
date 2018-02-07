package us.cyrien.minecordbot.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import us.cyrien.minecordbot.configuration.BroadcastConfig;

public class ShutdownEvent extends Event {

    private static HandlerList handlerList = new HandlerList();
    private BroadcastConfig broadcastConfig;

    public ShutdownEvent(BroadcastConfig broadcastConfig) {}

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
