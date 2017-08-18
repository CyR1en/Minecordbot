package us.cyrien.minecordbot.commands.discordCommands;

import io.github.hedgehog1029.frame.annotations.Text;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
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
import us.cyrien.minecordbot.entity.MCBUser;
import us.cyrien.minecordbot.localization.Localization;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.utils.FinderUtil;

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
                case "set":
                    set(e, args[1], args[2]);
                    break;
                case "get":
                    User user = FinderUtil.findMember(args[1], e.getGuild()) == null ?
                            e.getJDA().getUserById(args[1]) : FinderUtil.findMember(args[1], e.getGuild()).get(0).getUser();
                    command.sendMessage(e, getPerm(user, e.getGuild()), 15);
                    break;
            }
        }
    }

    private boolean checkArguments(MessageReceivedEvent e, String[] args) {
        if (args.length == 0 || args.length > 3 || args.length == 1) {
            command.sendMessageEmbed(e, command.getInvalidHelpCard(e), HELP_COMMAND_DURATION);
            return false;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("get")) {
                if (e.getGuild().getMemberById(args[1]) == null && FinderUtil.findMember(args[1], e.getGuild()).size() == 0) {
                    String response = Localization.getTranslatedMessage("mcb.commands.permission.user-not-found");
                    command.sendMessage(e, String.format(response, args[1]), 30);
                    return false;
                }
            } else {
                command.sendMessageEmbed(e, getHelp(e), HELP_COMMAND_DURATION);
                return false;
            }
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("set")) {
                int permLvl;
                try {
                    permLvl = Integer.parseInt(args[2]);
                } catch (NumberFormatException ne) {
                    command.sendMessageEmbed(e, getHelp(e), HELP_COMMAND_DURATION);
                    return false;
                }
                if (permLvl < 0 || permLvl > 3) {
                    command.sendMessageEmbed(e, setHelp(e), HELP_COMMAND_DURATION);
                    return false;
                }
                if (e.getGuild().getMemberById(args[1]) == null && FinderUtil.findMember(args[1], e.getGuild()).size() == 0) {
                    String response = Localization.getTranslatedMessage("mcb.commands.permission.user-not-found");
                    command.sendMessage(e, String.format(response, args[1]), 30);
                    return false;
                }
            } else {
                command.sendMessageEmbed(e, setHelp(e), HELP_COMMAND_DURATION);
                return false;
            }
        }
        return true;
    }

    private void set(MessageReceivedEvent e, String arg1, String arg2) {
        User user = FinderUtil.findMember(arg1, e.getGuild()) == null ?
                e.getJDA().getUserById(arg1) : FinderUtil.findMember(arg1, e.getGuild()).get(0).getUser();
        MCBUser u = new MCBUser(user, e.getGuild());
        if (u.getPermissionLevel().ordinal() > 0) {
            removePerm(user, e);
            addPerm(Integer.parseInt(arg2), user, e);
        } else {
            addPerm(Integer.parseInt(arg2), user, e);
        }
    }

    private void addPerm(int permLevel, User user, MessageReceivedEvent e) {
        JSONArray pl = MCBConfig.getJSONObject("permissions").getJSONArray("level_" + permLevel);
        String response;
        pl.put(user.getId());
        MCBConfig.getJSONObject("permissions").remove("level_" + permLevel);
        MCBConfig.getJSONObject("permissions").put("level_" + permLevel, pl);
        MCBConfig.save();
        response = Localization.getTranslatedMessage("mcb.commands.permission.added-perm");
        command.sendMessage(e, String.format(response, user.getName(), permLevel), 30);
        Minecordbot.LOGGER.info("Added user " + user.getName() + " to permission level_" + permLevel);
    }

    private void removePerm(User user, MessageReceivedEvent e) {
        if (getPermissionLevel(user.getId()) == PermissionLevel.LEVEL_0) {
            command.sendMessage(e, "No permission to be removed from `" + user.getId() + "`", 30);
            return;
        }
        PermissionLevel permLevel = getPermissionLevel(user.getId());
        JSONArray pl = MCBConfig.getJSONObject("permissions").getJSONArray("level_" + permLevel.ordinal());
        String response;
        if (pl.length() == 1) {
            response = Localization.getTranslatedMessage("mcb.commands.permission.last-user");
            command.sendMessage(e, String.format(response, user.getName(), permLevel.toString().toLowerCase()), 30);
            return;
        }
        pl.remove(getIndex(pl, user.getId()));
        MCBConfig.getJSONObject("permissions").remove("level_" + permLevel.ordinal());
        MCBConfig.getJSONObject("permissions").put("level_" + permLevel.ordinal(), pl);
        MCBConfig.save();
        response = Localization.getTranslatedMessage("mcb.commands.permission.remove-perm");
        command.sendMessage(e, String.format(response, permLevel.toString().toLowerCase(), user.getName()), 30);
        Minecordbot.LOGGER.info("Removed user " + user.getName() + " to permission " + permLevel);
    }

    private String getPerm(User u, Guild g) {
        MCBUser mcbUser = new MCBUser(u, g);
        return String.format(Localization.getTranslatedMessage("mcb.commands.permission.get-perm"), mcbUser.getName(), mcbUser.getPermissionLevel().toString().toLowerCase());
    }

    private int getIndex(JSONArray jsonArray, Object obj) {
        int i = 0;
        for (Object o : jsonArray) {
            if (o.equals(obj))
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

    private MessageEmbed setHelp(MessageReceivedEvent e) {
        EmbedBuilder eb = new EmbedBuilder(command.getInvalidHelpCard(e));
        eb.clearFields();
        eb.addField("Usage:", MCBConfig.get("trigger") + " <set> <user> <level>", false);
        return eb.build();
    }

}