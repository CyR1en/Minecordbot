package us.cyrien.minecordbot.core.module;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.core.enums.CommandType;
import us.cyrien.minecordbot.core.enums.PermissionLevel;
import us.cyrien.minecordbot.entity.Messenger;
import us.cyrien.minecordbot.main.Localization;
import us.cyrien.minecordbot.main.Minecordbot;

import java.awt.*;
import java.util.List;

public abstract class DiscordCommand implements Comparable{

    public static final int HELP_COMMAND_DURATION = 30;

    private String name;
    private List<String> aliases;
    protected String description;
    protected String usage;
    protected PermissionLevel permission;

    private Messenger messenger;
    private CommandType commandType;
    private boolean nullified;

    protected DiscordCommand(String name, String description, String usageMessage, List<String> aliases, CommandType commandType) {
        this.name = name;
        this.description = description;
        this.usage = usageMessage;
        this.aliases = aliases;
        this.commandType = commandType;
        messenger = Minecordbot.getMessenger();
        this.nullified = false;
    }

    public abstract boolean execute(MessageReceivedEvent var1, String[] var2);

    public String getName() {
        return this.name;
    }

    public boolean setName(String name) {
        this.name = name;
        return true;
    }

    public void sendMessageEmbed(MessageReceivedEvent e, MessageEmbed me, int length) {
        messenger.sendCommandEmbedResponse(e, me, length);
    }

    public void sendMessage(MessageReceivedEvent e, String message, int length) {
        messenger.sendCommandResponse(e, message, length);
    }

    public MessageEmbed getHelpCard(MessageReceivedEvent e) {
        EmbedBuilder eb = new EmbedBuilder().setColor(e.getGuild().getMember(e.getJDA().getSelfUser()).getColor());
        eb.setTitle(this.getName() + " Command Help Card:", null);
        eb.addField("Usage", MCBConfig.get("trigger") + getUsage(), false);
        eb.addField("Description", getDescription(), false);
        return eb.build();
    }

    public MessageEmbed getInvalidHelpCard(MessageReceivedEvent e) {
        EmbedBuilder eb = new EmbedBuilder(getHelpCard(e));
        eb.setColor(new Color(217, 83, 79));
        return eb.build();
    }


    public String noPermissionMessage() {
        return "`" + Localization.getTranslatedMessage("mcb.command.no-perm-message") + "`";
    }

    public String invalidArgumentsMessage() {
        return "`" + Localization.getTranslatedMessage("mcb.command.invalid-arguments") + "`";
    }

    public String invalidTcMessage() {
        return "`" + Localization.getTranslatedMessage("mcb.command.invalid-tc") + "`";
    }

    public PermissionLevel getPermission() {
        return this.permission;
    }

    public CommandType getCommandType() {
        return this.commandType;
    }

    public void setPermission(PermissionLevel permission) {
        this.permission = permission;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    public List<String> getAliases() {
        return this.aliases;
    }

    public String getDescription() {
        return this.description;
    }

    public String getUsage() {
        return this.usage;
    }

    public boolean isNullified() {
        return nullified;
    }

    public DiscordCommand nullify(String psuedoName) {
        this.name = psuedoName;
        this.description = null;
        this.usage = null;
        this.aliases = null;
        this.commandType = null;
        nullified = true;
        return this;
    }

    public DiscordCommand setAliases(List<String> aliases) {
        this.aliases = aliases;
        return this;
    }

    public DiscordCommand setDescription(String description) {
        this.description = description;
        return this;
    }

    public DiscordCommand setUsage(String usage) {
        this.usage = usage;
        return this;
    }

    public String toString() {
        return this.getClass().getName() + '(' + this.name + ')';
    }

    @Override
    public int compareTo(Object o) {
        DiscordCommand temp = (DiscordCommand)o;
        return this.getName().compareTo(temp.getName());
    }

}
