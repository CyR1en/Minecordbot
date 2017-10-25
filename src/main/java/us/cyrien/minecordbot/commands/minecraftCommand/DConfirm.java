package us.cyrien.minecordbot.commands.minecraftCommand;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.cyrien.mcutils.annotations.Command;
import us.cyrien.mcutils.annotations.Permission;
import us.cyrien.mcutils.annotations.Sender;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.accountSync.Authentication.AuthManager;
import us.cyrien.minecordbot.accountSync.Authentication.AuthSession;
import us.cyrien.minecordbot.accountSync.Authentication.AuthToken;
import us.cyrien.minecordbot.accountSync.exceptions.IllegalConfirmKeyException;

public class DConfirm {
    @Command(aliases = "syncconfirm", usage = "/syncconfirm <verification code>", desc = "Confirm Minecraft and Discord account sync")
    @Permission("minecordbot.discordsync")
    public void syncConfirm(@Sender CommandSender commandSender, String verificationCode) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players can use this command");
            return;
        }
        AuthToken authToken = null;
        try {
            authToken = new AuthToken((Player) commandSender, verificationCode);
            AuthManager authManager = Minecordbot.getInstance().getAuthManager();
            AuthSession authSession = authManager.getSession(authToken.toString());
            authSession.authorize((Player) commandSender, authToken);
        } catch (IllegalConfirmKeyException e) {
           commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                   "&6[MCBSync] &cThe verification code that you entered is invalid."));
        }
    }
}
