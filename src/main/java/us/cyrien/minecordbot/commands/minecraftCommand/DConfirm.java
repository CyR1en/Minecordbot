package us.cyrien.minecordbot.commands.minecraftCommand;

import io.github.hedgehog1029.frame.annotations.Command;
import io.github.hedgehog1029.frame.annotations.Permission;
import io.github.hedgehog1029.frame.annotations.Sender;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.cyrien.minecordbot.AccountSync.AuthManager;
import us.cyrien.minecordbot.AccountSync.Authentication.AuthSession;
import us.cyrien.minecordbot.AccountSync.Authentication.AuthToken;
import us.cyrien.minecordbot.AccountSync.exceptions.IIllegalAuthTokenFormatException;
import us.cyrien.minecordbot.main.Minecordbot;

public class DConfirm {
    @Command(aliases = "dconfirm", usage = "/dconfirm <verification code>", desc = "Confirm Minecraft and Discord account sync")
    @Permission("minecordbot.discordsync")
    public void syncConfirm(@Sender CommandSender commandSender, String arg1) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players can use this command");
            return;
        }
        AuthToken authToken = null;
        try {
            authToken = new AuthToken(arg1);
        } catch (IIllegalAuthTokenFormatException e) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6[MCBSync] &rUser not found"));
            return;
        }
        AuthManager authManager = Minecordbot.getInstance().getAuthManager();
        AuthSession authSession = authManager.getSession(authToken.getAuthID());
        authSession.authorize((Player) commandSender, authToken);
    }
}
