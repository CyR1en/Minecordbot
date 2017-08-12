package us.cyrien.minecordbot.commands.minecraftCommand;

import io.github.hedgehog1029.frame.annotations.Command;
import io.github.hedgehog1029.frame.annotations.Permission;
import io.github.hedgehog1029.frame.annotations.Sender;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.cyrien.minecordbot.accountSync.Authentication.AuthSession;
import us.cyrien.minecordbot.accountSync.Authentication.AuthToken;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.utils.FinderUtil;

public class DSync {
    @Command(aliases = "dsync", usage = "/dsync <Discord user id>", desc = "Sync Minecraft and Discord account")
    @Permission("minecordbot.discordsync")
    public void syncRequest(@Sender CommandSender commandSender, String discordID) {
        JDA jda = Minecordbot.getInstance().getJDA();
        User dUser = jda.getUserById(discordID);
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("only players can use this");
            return;
        }
        if (dUser == null)
            dUser = FinderUtil.findMember(discordID, jda.getSelfUser().getMutualGuilds().get(0)).get(0).getUser();
        if (dUser != null) {
            PrivateChannel uPrivateChannel = dUser.openPrivateChannel().complete();
            AuthSession authSession = new AuthSession((Player) commandSender, dUser);
            AuthToken token = authSession.getAuthToken();
            uPrivateChannel.sendMessage("**MCBSync Verification Code**: \n ```" + token.toString() + "``` \n\n" +
                "Copy the verification code above and do __**/dconfirm <verification code>**__ in-game").queue();
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6[MCBSync] &rAccount sync &6pending!"));
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&' ,
                    "&6[MCBSync] &rA verification code have been sent to your Discord account " +
                            dUser.getName() + "(" + dUser.getId() + ")" ));
        } else {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6[MCBSync] &rUser not found"));
        }
    }
}
