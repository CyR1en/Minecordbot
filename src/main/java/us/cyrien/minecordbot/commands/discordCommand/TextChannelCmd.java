package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.json.JSONArray;
import us.cyrien.mcutils.logger.Logger;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.localization.Locale;
import us.cyrien.minecordbot.utils.FinderUtil;
import us.cyrien.minecordbot.utils.JsonUtils;

public class TextChannelCmd extends MCBCommand {

    public TextChannelCmd(Minecordbot minecordbot) {
        super( minecordbot);
        this.name = "textchannel";
        this.aliases = new String[]{"tchannel","tc"};
        this.arguments = "<list | add | remove> [sub command args]...";
        this.help = Locale.getCommandsMessage("textchannel.description").finish();
        this.category = minecordbot.ADMIN;
        this.children = new Command[]{new Add(minecordbot), new List(minecordbot), new Remove(minecordbot)};
        this.type = Type.EMBED;
    }

    @Override
    protected void doCommand(CommandEvent e) {

    }

    private class List extends MCBCommand {

        public List(Minecordbot minecordbot) {
            super(minecordbot);
            this.name = "list";
            this.help = Locale.getCommandsMessage("textchannel.subcommand.list.description").finish();
            this.category = minecordbot.MISC;
        }

        @Override
        protected void doCommand(CommandEvent e) {
            respond(e, generateListEmbed(e));
        }

        private MessageEmbed generateListEmbed(CommandEvent e) {
            String path = "textchannel.list.";
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("- " + Locale.getCommandsMessage((path + "header")).finish() + " -", null);
            eb.setColor(e.getGuild().getMember(e.getJDA().getSelfUser()).getColor());
            int i = 1;
            JSONArray tcArray = MCBConfig.get("text_channels");
            for (Object tc : tcArray) {
                net.dv8tion.jda.core.entities.TextChannel tc1 = e.getJDA().getTextChannelById(tc.toString());
                if (tc1 != null) {
                    String gName = tc1.getGuild().getName();
                    String tcName = tc1.getName();
                    String str = Locale.getCommandsMessage(path + "guild_name").finish() + ": " + gName + "\n";
                    str += Locale.getCommandsMessage(path + "channel_name").finish() + ": " + tcName;
                    eb.addField(i++ + ". " + "[" + tc + "]" + ": ", str, false);
                }
            }
            return eb.build();
        }
    }

    private class Add extends MCBCommand {

        public Add(Minecordbot minecordbot) {
            super(minecordbot);
            this.name = "add";
            this.arguments = "<text channel id or channel name>";
            this.help = Locale.getCommandsMessage("textchannel.subcommand.add.description").finish();
            this.category = minecordbot.ADMIN;
        }

        @Override
        protected void doCommand(CommandEvent e) {
            addTextChannel(e);
        }

        private void addTextChannel(CommandEvent e) {
            String arg = e.getArgs();
            JSONArray tcArray = MCBConfig.get("text_channels");
            String response;
            boolean withID = e.getJDA().getTextChannelById(arg) != null;
            if(withID) {
                net.dv8tion.jda.core.entities.TextChannel tc = e.getJDA().getTextChannelById(arg);
                if (tc != null) {
                    tcArray.put(arg);
                    MCBConfig.set("text_channels", tcArray);
                } else {
                    response = Locale.getCommandsMessage("textchannel.invalid-tc").finish();
                    respond(e, String.format(response, arg));
                    return;
                }
            } else {
                java.util.List<net.dv8tion.jda.core.entities.TextChannel> result = FinderUtil.findTextChannel(arg, e.getGuild());
                net.dv8tion.jda.core.entities.TextChannel tc = result.size() > 0 ? result.get(0): null;
                if(tc != null) {
                    tcArray.put(tc.getId());
                    MCBConfig.set("text_channels", tcArray);
                } else {
                    response = Locale.getCommandsMessage("textchannel.invalid-tc").finish();
                    respond(e, String.format(response, arg));
                    return;
                }
            }
            response = Locale.getCommandsMessage("textchannel.added-tc").finish();
            respond(e, String.format(response, arg));
            Logger.info("Added text channel " + arg);
        }
    }

    private class Remove extends MCBCommand {

        public Remove(Minecordbot minecordbot) {
            super(minecordbot);
            this.name = "remove";
            this.arguments = "<text channel id or channel name>";
            this.help = Locale.getCommandsMessage("textchannel.subcommand.remove.description").finish();
            this.category = minecordbot.ADMIN;
        }

        @Override
        protected void doCommand(CommandEvent e) {
            removeTextChannel(e);
        }

        private void removeTextChannel(CommandEvent e) {
            String arg = e.getArgs();
            JSONArray tcArray = MCBConfig.get("text_channels");
            String response;
            boolean withID = e.getJDA().getTextChannelsByName(arg, true).size() > 0;
            String tcID = withID ? arg : FinderUtil.findTextChannel(arg, e.getGuild()).get(0).getId();
            if (!containsID(tcID)) {
                response = Locale.getCommandsMessage("textchannel.tc-not-bound").finish();
                respond(e, String.format(response, arg));
                return;
            }
            if (tcArray.length() == 1) {
                response = Locale.getCommandsMessage("textchannel.last-tc").finish();
                respond(e, String.format(response, arg));
                return;
            }
            tcArray.remove(JsonUtils.indexOf(tcID, tcArray));
            MCBConfig.set("text_channels", tcArray);
            response = Locale.getCommandsMessage("textchannel.removed-tc").finish();
            respond(e, String.format(response, arg));
            Logger.info("Removed text channel " + arg);
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
}
