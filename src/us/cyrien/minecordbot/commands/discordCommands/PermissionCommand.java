package us.cyrien.minecordbot.commands.discordCommands;

import io.github.hedgehog1029.frame.annotations.Text;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.core.annotation.DCmd;
import us.cyrien.minecordbot.core.annotation.DCommand;
import us.cyrien.minecordbot.core.annotation.DMessageReceive;
import us.cyrien.minecordbot.core.annotation.DPermission;
import us.cyrien.minecordbot.core.enums.CommandType;
import us.cyrien.minecordbot.core.enums.PermissionLevel;
import us.cyrien.minecordbot.core.module.DiscordCommand;
import us.cyrien.minecordbot.main.Localization;
import us.cyrien.minecordbot.main.Minecordbot;

import static us.cyrien.minecordbot.core.module.DiscordCommand.HELP_COMMAND_DURATION;

public class PermissionCommand {

    private DiscordCommand command;

    @DCommand(aliases = {"permission", "perm", "p"}, usage = "mcb.commands.permission.usage", desc = "mcb.commands.permission.description", type = CommandType.MOD)
    @DPermission(PermissionLevel.LEVEL_3)
    public void command(@DMessageReceive MessageReceivedEvent e, @DCmd DiscordCommand command, @Text String s) {
        String[] args = s.split("\\s");
        this.command = command;
        if (checkArguments(e, args)) {
            switch (args[0]) {
                case "add":
                    addPerm(Integer.parseInt(args[1]), args[2], e);
                    break;
                case "remove":
                    removePerm(args[1], e);
                    break;
                case "get":
                    command.sendMessage(e, getPerm(args[1]), 15);
                    break;
            }
        }
    }

    public boolean checkArguments(MessageReceivedEvent e, String[] args) {
        if (args.length == 0 || args.length > 3 || args.length == 1) {
            command.sendMessageEmbed(e, command.getInvalidHelpCard(e), HELP_COMMAND_DURATION);
            return false;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("get") || args[0].equalsIgnoreCase("remove")) {
                if (e.getGuild().getMemberById(args[1]) == null) {
                    if (args[0].equalsIgnoreCase("get"))
                        command.sendMessageEmbed(e, getHelp(e), HELP_COMMAND_DURATION);
                    else
                        command.sendMessageEmbed(e, removeHelp(e), HELP_COMMAND_DURATION);
                    return false;
                }
            } else {
                command.sendMessageEmbed(e, removeHelp(e), HELP_COMMAND_DURATION);
                return false;
            }
            if (e.getJDA().getUserById(args[1]) == null)
                return false;
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("add")) {
                int permLvl = Integer.parseInt(args[1]);
                if (permLvl < 0 || permLvl > 3) {
                    command.sendMessageEmbed(e, addHelp(e), HELP_COMMAND_DURATION);
                    return false;
                }
            } else {
                command.sendMessageEmbed(e, addHelp(e), HELP_COMMAND_DURATION);
                return false;
            }
            if (e.getJDA().getUserById(args[2]) == null)
                return false;
        }
        return true;
    }

    private void addPerm(int permLevel, String id, MessageReceivedEvent e) {
        JSONArray pl = MCBConfig.getJSONObject("permissions").getJSONArray("level_" + permLevel);
        Member member = e.getGuild().getMember(e.getJDA().getUserById(id));
        String response;
        if (member != null) {
            pl.put(member.getUser().getId());
            MCBConfig.getJSONObject("permissions").remove("level_" + permLevel);
            MCBConfig.getJSONObject("permissions").put("level_" + permLevel, pl);
            MCBConfig.save();
        } else {
            response = Localization.getTranslatedMessage("mcb.commands.permission.user-not-found");
            command.sendMessage(e, String.format(response, id), 30);
            return;
        }
        response = Localization.getTranslatedMessage("mcb.commands.permission.added-perm");
        command.sendMessage(e, String.format(response, id, permLevel), 30);
        Minecordbot.LOGGER.info("Added User " + id + " to permission level_" + permLevel);
    }

    private void removePerm(String id, MessageReceivedEvent e) {
        User user = e.getJDA().getUserById(id);
        if (getPermissionLevel(user.getId()) == PermissionLevel.LEVEL_0) {
            command.sendMessage(e, "No permission to be removed from `" + user.getId() + "`", 30);
            return;
        }
        PermissionLevel permLevel = getPermissionLevel(user.getId());
        JSONArray pl = MCBConfig.getJSONObject("permissions").getJSONArray("level_" + permLevel.ordinal());
        String response;
        if (pl.length() == 1) {
            response = Localization.getTranslatedMessage("mcb.commands.permission.last-user");
            command.sendMessage(e, String.format(response, id, permLevel.toString().toLowerCase()), 30);
            return;
        }
        pl.remove(getIndex(pl, id));
        MCBConfig.getJSONObject("permissions").remove("level_" + permLevel.ordinal());
        MCBConfig.getJSONObject("permissions").put("level_" + permLevel.ordinal(), pl);
        MCBConfig.save();
        response = Localization.getTranslatedMessage("mcb.commands.permission.remove-perm");
        command.sendMessage(e, String.format(response, permLevel.toString().toLowerCase(), id), 30);
        Minecordbot.LOGGER.info("Removed User " + id + " to permission " + permLevel);
    }

    private String getPerm(String id) {
        PermissionLevel level = getPermissionLevel(id);
        return String.format(Localization.getTranslatedMessage("mcb.commands.permission.get-perm"), id, level.toString().toLowerCase());
    }

    private int getIndex(JSONArray jsonArray, Object obj) {
        int i = 0;
        for(Object o : jsonArray) {
            if(o.equals(obj))
                return i;
            i++;
        }
        return -1;
    }

    private PermissionLevel getPermissionLevel(String id) {
        JSONObject perm = MCBConfig.getJSONObject("permissions");
        if (containsID(perm.getJSONArray("level_3"), id))
            return PermissionLevel.LEVEL_3;
        else if (containsID(perm.getJSONArray("level_2"), id))
            return PermissionLevel.LEVEL_2;
        else if (containsID(perm.getJSONArray("level_1"), id))
            return PermissionLevel.LEVEL_1;
        else
            return PermissionLevel.LEVEL_0;
    }

    private boolean containsID(JSONArray str, String id) {
        for (Object s : str) {
            if (s.equals(id))
                return true;
        }
        return false;
    }

    private MessageEmbed getHelp(MessageReceivedEvent e) {
        EmbedBuilder eb = new EmbedBuilder(command.getInvalidHelpCard(e));
        eb.clearFields();
        eb.addField("Usage:", MCBConfig.get("trigger") + " <get> <userID>", false);
        return eb.build();
    }

    private MessageEmbed addHelp(MessageReceivedEvent e) {
        EmbedBuilder eb = new EmbedBuilder(command.getInvalidHelpCard(e));
        eb.clearFields();
        eb.addField("Usage:", MCBConfig.get("trigger") + " <add> <permLevel> <userID>", false);
        return eb.build();
    }

    private MessageEmbed removeHelp(MessageReceivedEvent e) {
        EmbedBuilder eb = new EmbedBuilder(command.getInvalidHelpCard(e));
        eb.clearFields();
        eb.addField("Usage:", MCBConfig.get("trigger") + " <remove> <userID>", false);
        return eb.build();
    }
}