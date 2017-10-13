package us.cyrien.minecordbot.chat.listeners.mcListeners;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.IUser;
import com.earth2me.essentials.User;
import com.earth2me.essentials.textreader.IText;
import com.earth2me.essentials.textreader.SimpleTextInput;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.chat.Messenger;
import us.cyrien.minecordbot.entity.UnifiedUser;
import us.cyrien.minecordbot.HookContainer;
import us.cyrien.minecordbot.utils.ArrayUtils;

import java.util.Collection;

public class HelpOpListener extends MCBListener {

    private final Essentials essentialsHook = HookContainer.getEssentialsHook().getPlugin();
    private Messenger messenger;

    public HelpOpListener(Minecordbot mcb) {
        super(mcb);
        messenger = new Messenger(mcb);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onServerCommand(PlayerCommandPreprocessEvent event) {
        if(essentialsHook != null) {
            String[] msgSplit = event.getMessage().split(" ");
            boolean isHelpOp = msgSplit[0].equals("/helpop") || msgSplit[0].contains("helpop");
            String message = ArrayUtils.concatenateArgs(1, msgSplit);
            IUser sender = essentialsHook.getUser(event.getPlayer());
            if (isHelpOp) {
                broadcastMessage(sender, "essentials.helpop.receive", message);
            }
        }
    }

    private void broadcastMessage(IUser sender, final String permission, final String message) {
        if(essentialsHook != null) {
            IText broadcast = new SimpleTextInput(message);
            final Collection<Player> players = (Collection<Player>) Bukkit.getOnlinePlayers();
            for (Player player : players) {
                final User user = essentialsHook.getUser(player);
                if ((permission == null && (sender == null || !user.isIgnoredPlayer(sender))) || (permission != null && user.isAuthorized(permission))) {
                    for (String messageText : broadcast.getLines()) {
                        UnifiedUser unifiedUser = new UnifiedUser(player);
                        if (unifiedUser.isSynced())
                            messenger.sendMessageToDM(unifiedUser.getMcbUser().getUser(), messageText);
                    }
                }
            }
        }
    }
}
