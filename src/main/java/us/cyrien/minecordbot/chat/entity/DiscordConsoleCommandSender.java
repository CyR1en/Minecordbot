package us.cyrien.minecordbot.chat.entity;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ManuallyAbandonedConversationCanceller;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.Set;

public class DiscordConsoleCommandSender implements ConsoleCommandSender {

    protected final ConvoTracker conversationTracker = new ConvoTracker();
    private final PermissibleBase perm;
    private CommandEvent e;

    public DiscordConsoleCommandSender(CommandEvent e) {
        this.e = e;
        perm = new PermissibleBase(this);
    }

    @Override
    public void sendMessage(String s) {
        e.getTextChannel().sendMessage("`" + ChatColor.stripColor(s) + "`").queue();

    }

    @Override
    public void sendMessage(String[] strings) {
        for (String message : strings) {
            sendMessage(message);
        }
    }

    @Override
    public Server getServer() {
        return Bukkit.getServer();
    }

    @Override
    public String getName() {
        return e.getGuild().getName();
    }


    @Override
    public boolean isPermissionSet(String s) {
        return this.perm.isPermissionSet(s);
    }

    @Override
    public boolean isPermissionSet(Permission permission) {
        return this.perm.isPermissionSet(permission);
    }

    @Override
    public boolean hasPermission(String s) {
        return this.perm.hasPermission(s);
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return this.perm.hasPermission(permission);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b) {
        return this.perm.addAttachment(plugin, s, b);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return this.perm.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b, int i) {
        return this.perm.addAttachment(plugin, s, b, i);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int i) {
        return this.perm.addAttachment(plugin, i);
    }

    @Override
    public void removeAttachment(PermissionAttachment permissionAttachment) {
        this.perm.removeAttachment(permissionAttachment);
    }

    @Override
    public void recalculatePermissions() {
        this.perm.recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return this.perm.getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return true;
    }

    public boolean isPlayer() {
        return false;
    }

    @Override
    public void setOp(boolean b) {
        throw new UnsupportedOperationException("Cannot change operator status of Minecordbot");
    }

    public void sendRawMessage(String message) {
        e.getTextChannel().sendMessage("`" + ChatColor.stripColor(message) + "`").queue();
    }

    public boolean beginConversation(Conversation conversation) {
        return false;
    }

    public void abandonConversation(Conversation conversation) {
        this.conversationTracker.abandonConversation(conversation, new ConversationAbandonedEvent(conversation, new ManuallyAbandonedConversationCanceller()));
    }

    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        this.conversationTracker.abandonConversation(conversation, details);
    }

    public void acceptConversationInput(String input) {
        this.conversationTracker.acceptConversationInput(input);
    }

    public boolean isConversing() {
        return this.conversationTracker.isConversing();
    }

}
