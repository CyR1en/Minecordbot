package us.cyrien.minecordbot.commands.minecraftCommand;

import io.github.hedgehog1029.frame.annotations.Command;
import io.github.hedgehog1029.frame.annotations.Permission;
import io.github.hedgehog1029.frame.annotations.Sender;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.cyrien.minecordbot.accountSync.Authentication.AuthManager;
import us.cyrien.minecordbot.accountSync.Authentication.AuthSession;
import us.cyrien.minecordbot.accountSync.Authentication.AuthToken;
import us.cyrien.minecordbot.Minecordbot;

public class DConfirm {
    @Command(aliases = "dconfirm", usage = "/dconfirm <verification code>", desc = "Confirm Minecraft and Discord account sync")
    @Permission("minecordbot.discordsync")
    public void syncConfirm(@Sender CommandSender commandSender, String verificationCode) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players can use this command");
            return;
        }
        AuthToken authToken = new AuthToken((Player)commandSender, verificationCode);
        AuthManager authManager = Minecordbot.getInstance().getAuthManager();
        AuthSession authSession = authManager.getSession(authToken.toString());
        authSession.authorize((Player) commandSender, authToken);
    }
}
