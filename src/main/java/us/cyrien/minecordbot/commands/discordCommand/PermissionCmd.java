package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.jagrosh.jdautilities.utils.FinderUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.Role;
import us.cyrien.minecordbot.Bot;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.localization.Locale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermissionCmd extends MCBCommand {

    private final Command.Category[] CATEGORIES = {Bot.ADMIN, Bot.FUN, Bot.HELP,
            Bot.INFO, Bot.MISC, Bot.OWNER};

    public PermissionCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "permission";
        this.aliases = new String[]{"perm"};
        this.help = Locale.getCommandsMessage("perm.description").finish();
        this.arguments = "<set|check|remove> [sub command arguments]...";
        this.children = new Command[]{new Set(mcb), new Check(mcb), new Remove(mcb)};
        this.category = Bot.OWNER;
    }

    @Override
    protected void doCommand(CommandEvent e) {
        if (!(e.getArgs().equalsIgnoreCase("set") && e.getArgs().equalsIgnoreCase("check") && e.getArgs().equalsIgnoreCase("remove")))
            respond(e, Locale.getCommandsMessage("perm.invalid-sub").finish());
    }

    private String cleanConcat(List<String> perms) {
        return concatenateList(perms, "");
    }

    private String formatList(List<String> perms) {
        return concatenateList(perms, null);
    }

    private String concatenateList(List<String> perms, String regex) {
        regex = regex == null ? ", " : regex;
        if (perms.size() == 0)
            return "";
        StringBuilder s = new StringBuilder(perms.get(0));
        if (perms.size() == 1)
            return s.toString();
        for (int i = 1; i < perms.size(); i++)
            s.append(regex).append(perms.get(i).trim());
        return s.toString();
    }

    private boolean exists(Role role) {
        java.util.Set<String> keys = mcb.getMcbConfigsManager().getPermConfig().getKeys();
        return keys.contains(role.getId());
    }

    private String addEscapeCharacters(String s) {
        s = s.replaceAll("[{}]", "\\\\$0");
        s = s.replaceAll("[+|-]", "\\\\$0");
        return s;
    }

    private ArrayList<String> omitInvalid(ArrayList<String> s) {
        for (int i = 0; i < s.size(); i++) {
            String arg = s.get(i).trim();
            if ((arg.startsWith("{-") || arg.startsWith("{+")) && arg.endsWith("}")) {
                boolean valid = false;
                arg = arg.replaceAll("\\W", "");
                for (Command.Category c : CATEGORIES) {
                    if (arg.equalsIgnoreCase(c.getName())) {
                        valid = true;
                        break;
                    }
                }
                if (!valid) {
                    for (Command c : mcb.getBot().getClient().getCommands()) {
                        if (arg.equalsIgnoreCase(c.getName())) {
                            valid = true;
                            break;
                        }
                    }
                }
                if (!valid && !arg.equalsIgnoreCase("all")) {
                    s.remove(i);
                    i--;
                }
            } else {
                s.remove(i);
                i--;
            }
        }
        return s;
    }

    private class Set extends MCBCommand {

        public Set(Minecordbot minecordbot) {
            super(minecordbot);
            this.name = "set";
            this.help = Locale.getCommandsMessage("perm.sub.set.description").finish();
            this.arguments = "<role|check> <flag>|<flag>|<flag>...";
            this.type = Type.EMBED;
            this.category = Bot.MISC;
        }

        @Override
        protected void doCommand(CommandEvent e) {
            ArrayList<String> args = new ArrayList<>(Arrays.asList(e.getArgs().split(" ")));
            System.out.println(e.getArgs());
            Role role = FinderUtil.findRoles(args.get(0), e.getGuild()).size() > 0 ?
                    FinderUtil.findRoles(args.get(0), e.getGuild()).get(0) : null;
            if (role == null) {
                respond(e, Locale.getCommandsMessage("perm.no-role").f("`" + args.get(0) + "`"));
                return;
            }
            if (args.size() < 2) {
                respond(e, Locale.getCommandsMessage("perm.sub.set.flag-not-null").finish());
                return;
            }
            String[] perms;
            if (args.size() > 1 && args.size() < 3) {
                perms = args.get(1).split("\\|");
            } else {
                ArrayList<String> ps = new ArrayList<>(args.subList(1, args.size()));
                perms = cleanConcat(ps).split("\\|");
            }
            ArrayList<String> permsList = new ArrayList<>(Arrays.asList(perms));
            permsList = omitInvalid(permsList);
            String msg;
            if (permsList.size() == 0) {
                msg = Locale.getCommandsMessage("perm.sub.set.all-invalid-flags").finish();
                respond(e, msg, ResponseLevel.LEVEL_3);
                return;
            } else if (permsList.size() < perms.length) {
                ArrayList<String> omitted = new ArrayList<>(Arrays.asList(perms));
                for (String s : permsList)
                    if (omitted.contains(s))
                        omitted.remove(s);
                msg = Locale.getCommandsMessage("perm.sub.set.omitted-flags").f(formatList(permsList));
            } else {
                msg = Locale.getCommandsMessage("perm.sub.set.added").f(formatList(permsList));
            }
            String perm = formatList(permsList);
            if (exists(role)) {
                mcb.getMcbConfigsManager().getPermConfig().set(role.getId() + ".Permission", perm);
                configsManager.getPermConfig().saveConfig();
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle(Locale.getCommandsMessage("perm.sub.set.success").f(role.getName()));
                eb.setDescription(msg);
                respond(e, embedMessage(e, eb.build(), ResponseLevel.LEVEL_1));
            } else {
                mcb.getMcbConfigsManager().getPermConfig().set(role.getId() + ".RoleName", role.getName());
                mcb.getMcbConfigsManager().getPermConfig().set(role.getId() + ".Permission", perm);
                configsManager.getPermConfig().saveConfig();
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle(Locale.getCommandsMessage("perm.sub.set.success").f(role.getName()));
                eb.setDescription(msg);
                respond(e, embedMessage(e, eb.build(), ResponseLevel.LEVEL_1));
            }
            configsManager.reloadAllConfig();
        }
    }

    private class Check extends MCBCommand {

        public Check(Minecordbot minecordbot) {
            super(minecordbot);
            this.name = "check";
            this.help = Locale.getCommandsMessage("perm.sub.check.description").finish();
            this.arguments = "<role|all>";
            this.type = Type.EMBED;
            this.category = Bot.MISC;
        }

        @Override
        protected void doCommand(CommandEvent e) {
            Role role = FinderUtil.findRoles(e.getArgs(), e.getGuild()).size() > 0 ?
                    FinderUtil.findRoles(e.getArgs(), e.getGuild()).get(0) : null;
            if (role != null) {
                String perms = mcb.getMcbConfigsManager().getPermConfig().getString(role.getId() + ".Permission");
                if (perms != null) {
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setTitle(Locale.getCommandsMessage("perm.sub.check.success").f(role.getName()));
                    embedBuilder.setDescription("`" + perms + "`");
                    MessageEmbed formatted = embedMessage(e, embedBuilder.build(), ResponseLevel.LEVEL_1);
                    respond(e, formatted);
                } else {
                    respond(e, Locale.getCommandsMessage("perm.sub.check.role-no-perm").f("`" + role.getName() + "`"), ResponseLevel.LEVEL_2);
                }
            } else if (e.getArgs().equalsIgnoreCase("all")) {
                java.util.Set<String> keys = mcb.getMcbConfigsManager().getPermConfig().getKeys();
                if(keys.contains("RoleID"))
                    keys.remove("RoleID");
                EmbedBuilder eb = new EmbedBuilder();
                eb.setDescription(Locale.getCommandsMessage("perm.sub.check.").finish());
                System.out.println(keys.toString());
                for(String s : keys) {
                    String roleName = mcb.getMcbConfigsManager().getPermConfig().getString(s + ".RoleName");
                    String perms = mcb.getMcbConfigsManager().getPermConfig().getString(s + ".Permission");
                    eb.addField("**" + roleName + "**", "`" + perms + "`", false);
                }
                MessageEmbed formatted = embedMessage(e, eb.build(), ResponseLevel.DEFAULT, "Permissions");
                respond(e, formatted);
            } else {
                respond(e, Locale.getCommandsMessage("perm.no-role").f("`" + e.getArgs() + "`"), ResponseLevel.LEVEL_2);
            }
        }
    }

    private class Remove extends MCBCommand {

        public Remove(Minecordbot minecordbot) {
            super(minecordbot);
            this.name = "remove";
            this.help = Locale.getCommandsMessage("perm.sub.remove.description").finish();
            this.arguments = "<role> [flag]|[flag]|[flag]...";
            this.type = Type.EMBED;
            this.category = Bot.MISC;
        }

        @Override
        protected void doCommand(CommandEvent e) {
            ArrayList<String> args = new ArrayList<>(Arrays.asList(e.getArgs().split(" ")));
            Role role = FinderUtil.findRoles(args.get(0), e.getGuild()).size() > 0 ?
                    FinderUtil.findRoles(args.get(0), e.getGuild()).get(0) : null;
            if (role == null) {
                respond(e, Locale.getCommandsMessage("perm.no-role").f("`" + args.get(0) + "`"));
                return;
            }
            if (!exists(role)) {
                respond(e, Locale.getCommandsMessage("perm.sub.remove.no-role").f("`" + args.get(0) + "`"));
            }
            if (args.size() < 2) {
                if (exists(role)) {
                    String perms = mcb.getMcbConfigsManager().getPermConfig().getString(role.getId() + ".Permission");
                    removeAll(role, e, perms);
                }
                respond(e, Locale.getCommandsMessage("perm.sub.set.flag-not-null").finish());
            } else {
                String[] perms;
                if (args.size() > 1 && args.size() < 3) {
                    perms = args.get(1).split("\\|");
                } else {
                    ArrayList<String> ps = new ArrayList<>(args.subList(1, args.size()));
                    perms = cleanConcat(ps).split("\\|");
                }
                ArrayList<String> permsList = new ArrayList<>(Arrays.asList(perms));
                String savedPerm = mcb.getMcbConfigsManager().getPermConfig().getString(role.getId() + ".Permission");
                savedPerm = savedPerm.replaceAll(",\\s+", " ");
                ArrayList<String> ignored = new ArrayList<>();
                for (String s : permsList) {
                    if (savedPerm.contains(s)) {
                        s = addEscapeCharacters(s);
                        System.out.println("regex: " + s);
                        System.out.println("Saved Perms: " + savedPerm);
                        savedPerm = savedPerm.replaceAll(s, "");
                    } else
                        ignored.add(s);
                }
                savedPerm = savedPerm.replaceAll("\\s+", " ");
                savedPerm = savedPerm.trim();
                if(savedPerm.isEmpty())  {
                    removeAll(role, e, cleanConcat(permsList));
                } else {
                    perms = savedPerm.split(" ");
                    permsList = new ArrayList<>(Arrays.asList(perms));
                    String newPerm = formatList(permsList);
                    if (ignored.size() > 0) {
                        mcb.getMcbConfigsManager().getPermConfig().set(role.getId() + ".Permission", newPerm);
                        configsManager.getPermConfig().saveConfig();
                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setTitle(Locale.getCommandsMessage("perm.sub.remove.success-ignore").f(role.getName()));
                        embedBuilder.setDescription(Locale.getCommandsMessage("perm.sub.remove.success-ignore-detail").f(role.getName(), formatList(ignored)));
                        MessageEmbed formatted = embedMessage(e, embedBuilder.build(), ResponseLevel.LEVEL_1);
                        respond(e, formatted);
                    } else {
                        mcb.getMcbConfigsManager().getPermConfig().set(role.getId() + ".Permission", newPerm);
                        configsManager.getPermConfig().saveConfig();
                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setTitle(Locale.getCommandsMessage("perm.sub.remove.success-provided").f(role.getName()));
                        embedBuilder.setDescription(Locale.getCommandsMessage("perm.sub.remove.success-provided-detail").f(newPerm));
                        MessageEmbed formatted = embedMessage(e, embedBuilder.build(), ResponseLevel.LEVEL_1);
                        respond(e, formatted);
                    }
                }
            }
        }

        private void removeAll(Role role, CommandEvent e, String removedPerms) {
            mcb.getMcbConfigsManager().getPermConfig().removeKey(role.getId());
            configsManager.getPermConfig().saveConfig();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(Locale.getCommandsMessage("perm.sub.remove.success-all").f(role.getName()));
            embedBuilder.setDescription(Locale.getCommandsMessage("perm.sub.remove.success-all-detail").f(removedPerms));
            MessageEmbed formatted = embedMessage(e, embedBuilder.build(), ResponseLevel.LEVEL_1);
            respond(e, formatted);
        }
    }
}
