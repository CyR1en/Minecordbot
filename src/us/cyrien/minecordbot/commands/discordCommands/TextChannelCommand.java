package us.cyrien.minecordbot.commands.discordCommands;

import io.github.hedgehog1029.frame.annotations.Text;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.core.annotation.DCmd;
import us.cyrien.minecordbot.core.annotation.DCommand;
import us.cyrien.minecordbot.core.annotation.DMessageReceive;
import us.cyrien.minecordbot.core.annotation.DPermission;
import us.cyrien.minecordbot.core.enums.CommandType;
import us.cyrien.minecordbot.core.enums.PermissionLevel;
import us.cyrien.minecordbot.core.module.DiscordCommand;
import us.cyrien.minecordbot.entity.User;
import us.cyrien.minecordbot.main.Localization;
import us.cyrien.minecordbot.main.Minecordbot;
import us.cyrien.minecordbot.utils.FinderUtil;
import us.cyrien.minecordbot.utils.JsonUtils;

import java.util.List;

import static us.cyrien.minecordbot.core.module.DiscordCommand.HELP_COMMAND_DURATION;

public class TextChannelCommand {
    @DCommand(aliases = {"textchannel", "tc"}, usage = "mcb.commands.textchannel.usage", desc = "mcb.commands.textchannel.description", type = CommandType.MOD)
    @DPermission(PermissionLevel.LEVEL_3)
    public void command(@DMessageReceive MessageReceivedEvent e, @DCmd DiscordCommand command, @Text String s) {
        String[] args = s.split(" ");
        if (checkArguments(e, command, args)) {
            if (args[0].equalsIgnoreCase("list"))
                command.sendMessageEmbed(e, generateListEmbed(e), 6 * args.length);
            else if(hasPermission(args, e)) {
                if (args[0].equalsIgnoreCase("add"))
                    addTextChannel(args[1], e, command);
                if (args[0].equalsIgnoreCase("remove"))
                    removeTextChannel(args[1], e, command);
            } else {
                command.sendMessageEmbed(e, command.noPermissionMessageEmbed(), 40);
            }
        }
    }

    public boolean hasPermission(String[] args, MessageReceivedEvent e) {
        User user = new User(e);
        if (args.length != 0) {
            if (user.getPermissionLevel() == PermissionLevel.OWNER)
                return true;
            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove"))
                return user.getPermissionLevel().ordinal() >= PermissionLevel.LEVEL_1.ordinal();
        }
        return true;
    }

    public boolean checkArguments(MessageReceivedEvent e, DiscordCommand command, String[] args) {
        if (args.length == 1) {
            if (!args[0].equalsIgnoreCase("list")) {
                command.sendMessageEmbed(e, command.getInvalidHelpCard(e), 50);
                return false;
            }
        }
        if (args.length == 0 || args.length > 2) {
            command.sendMessageEmbed(e, command.getInvalidHelpCard(e), HELP_COMMAND_DURATION);
            return false;
        }
        if (args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")) {
            return true;
        } else {
            command.sendMessageEmbed(e, command.getInvalidHelpCard(e), HELP_COMMAND_DURATION);
            return false;
        }
    }

    private MessageEmbed generateListEmbed(MessageReceivedEvent e) {
        String path = "mcb.commands.textchannel.list.";
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("- " + Localization.getTranslatedMessage(path + "header") + " -", null);
        eb.setColor(e.getGuild().getMember(e.getJDA().getSelfUser()).getColor());
        int i = 1;
        JSONArray tcArray = MCBConfig.get("text_channels");
        for (Object tc : tcArray) {
            TextChannel tc1 = e.getJDA().getTextChannelById(tc.toString());
            if (tc1 != null) {
                String gName = tc1.getGuild().getName();
                String tcName = tc1.getName();
                String str = Localization.getTranslatedMessage(path + "guild_name") + ": " + gName + "\n";
                str += Localization.getTranslatedMessage(path + "channel_name") + ": " + tcName;
                eb.addField(i++ + ". " + "[" + tc + "]" + ": ", str, false);
            }
        }
        return eb.build();
    }

    private void addTextChannel(String arg, MessageReceivedEvent e, DiscordCommand command) {
        JSONArray tcArray = MCBConfig.get("text_channels");
        String response;
        boolean withID = e.getJDA().getTextChannelById(arg) != null;
        if(withID) {
            TextChannel tc = e.getJDA().getTextChannelById(arg);
            if (tc != null) {
                tcArray.put(arg);
                MCBConfig.set("text_channels", tcArray);
            } else {
                response = Localization.getTranslatedMessage("mcb.commands.textchannel.invalid-tc");
                command.sendMessage(e, String.format(response, arg), 10);
                return;
            }
        } else {
            List<TextChannel> result = FinderUtil.findTextChannel(arg, e.getGuild());
            TextChannel tc = result.size() > 0 ? result.get(0): null;
            if(tc != null) {
                tcArray.put(tc.getId());
                MCBConfig.set("text_channels", tcArray);
            } else {
                response = Localization.getTranslatedMessage("mcb.commands.textchannel.invalid-tc");
                command.sendMessage(e, String.format(response, arg), 10);
                return;
            }
        }
        response = Localization.getTranslatedMessage("mcb.commands.textchannel.added-tc");
        command.sendMessage(e, String.format(response, arg), 10);
        Minecordbot.LOGGER.info("Added text channel " + arg);
    }

    private void removeTextChannel(String arg, MessageReceivedEvent e, DiscordCommand command) {
        JSONArray tcArray = MCBConfig.get("text_channels");
        String response;
        boolean withID = e.getJDA().getTextChannelsByName(arg, true).size() > 0;
        String tcID = withID ? arg : FinderUtil.findTextChannel(arg, e.getGuild()).get(0).getId();
        if (!containsID(tcID)) {
            response = Localization.getTranslatedMessage("mcb.commands.textchannel.tc-not-bound");
            command.sendMessage(e, String.format(response, arg), 20);
            return;
        }
        if (tcArray.length() == 1) {
            response = Localization.getTranslatedMessage("mcb.commands.textchannel.last-tc");
            command.sendMessage(e, String.format(response, arg), 20);
            return;
        }
        tcArray.remove(JsonUtils.indexOf(tcID, tcArray));
        MCBConfig.set("text_channels", tcArray);
        response = Localization.getTranslatedMessage("mcb.commands.textchannel.removed-tc");
        command.sendMessage(e, String.format(response, arg), 10);
        Minecordbot.LOGGER.info("Removed text channel " + arg);
    }

    private boolean containsID(String textChannelID) {
        JSONArray ids = MCBConfig.get("text_channels");
        assert ids != null;
        for (Object s : ids)
            if (s.toString().equalsIgnoreCase(textChannelID))
                return true;
        return false;
    }
}