package us.cyrien.minecordbot.commands.minecraftCommand;

import com.jagrosh.jdautilities.waiter.EventWaiter;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.cyrien.mcutils.annotations.Command;
import us.cyrien.mcutils.annotations.Permission;
import us.cyrien.mcutils.annotations.Sender;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.accountSync.Authentication.AuthSession;
import us.cyrien.minecordbot.accountSync.Authentication.AuthToken;
import us.cyrien.minecordbot.utils.FinderUtil;

import java.util.concurrent.TimeUnit;

public class DSync {

    private static final String CANCEL = "\u274c";

    @Command(aliases = "mcbsync", usage = "/mcbsync <Discord user id or Discord username>", desc = "Sync Minecraft and Discord account")
    @Permission("minecordbot.discordsync")
    public void syncRequest(@Sender CommandSender commandSender, String discordID) {
        JDA jda = Minecordbot.getInstance().getBot().getJda();
        User dUser = StringUtils.isNumeric(discordID) ? jda.getUserById(discordID) : null;
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("only players can use this");
            return;
        }
        if (dUser == null)
            dUser = (FinderUtil.findMember(discordID) == null) ? null : FinderUtil.findMember(discordID).getUser();
        if (dUser != null) {
            AuthSession authSession = new AuthSession((Player) commandSender, dUser);
            AuthToken token = authSession.getAuthToken();
            User finalDUser1 = dUser;
            EventWaiter eventWaiter = Minecordbot.getInstance().getEventWaiter();
            dUser.openPrivateChannel().queue(pc -> pc.sendMessage(verificationCode(token)).queue(m -> {
                m.addReaction(CANCEL).complete();
                eventWaiter.waitForEvent(MessageReactionAddEvent.class, e -> e.getReaction().getReactionEmote().getName().equals(CANCEL) && e.getMessageId().equals(m.getId()) && !e.getUser().isBot(), a -> {
                    a.getReaction().removeReaction().queue();
                    authSession.cancel();
                }, AuthSession.SYNC_TIMEOUT, TimeUnit.MINUTES, authSession::cancel);
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[MCBSync] &rAccount sync &6pending!"));
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&6[MCBSync] &rA verification code have been sent to your Discord account " + finalDUser1.getName() + "(" + finalDUser1.getId() + ")"));
            }, t -> {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', cannotSendCode()));
                authSession.cancel();
            }), t -> {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', cannotSendCode()));
                authSession.cancel();
            });
        } else {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[MCBSync] &rUser not found"));
        }
    }

    private String verificationCode(AuthToken token) {
        return "**MCBSync Verification Code**: \n ```" + token.toString() + "``` \n" + "Copy the verification code above and do __**/syncconfirm <verification code>**__ in-game \n\n" +
                "_by syncing accounts, you agree that your account data(username, id) will be stored to the resource folder of MineCordBot. \n" +
                "If you don't give consent to store your data, press the cancel button bellow._";
    }

    private String cannotSendCode() {
        return "&6[MCBSync] &rVerification code cannot be sent because you are blocking Direct Messages. &aEnable Direct Messages and try again.";
    }
}
