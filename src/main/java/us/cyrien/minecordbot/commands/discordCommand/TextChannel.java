package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.json.JSONArray;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.DiscordCommand;
import us.cyrien.minecordbot.configuration.MCBConfig;
import us.cyrien.minecordbot.localization.Localization;
import us.cyrien.minecordbot.utils.FinderUtil;
import us.cyrien.minecordbot.utils.JsonUtils;

public class TextChannel extends DiscordCommand{

    public TextChannel(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "textchannel";
        this.aliases = new String[]{"tchannel","tc"};
        this.arguments = "<list | add | remove>";
        this.help = Localization.getTranslatedMessage("mcb.commands.textchannel.description");
        this.category = minecordbot.ADMIN;
        this.children = new Command[]{new Add(minecordbot), new List(minecordbot), new Remove(minecordbot)};
    }

    @Override
    protected void doCommand(CommandEvent e) {

    }

    private class List extends DiscordCommand {

        public List(Minecordbot minecordbot) {
            super(minecordbot);
            this.name = "list";
            this.help = Localization.getTranslatedMessage("mcb.commands.textchannel.subcommand.list.description");
        }

        @Override
        protected void doCommand(CommandEvent e) {
            respond(e, generateListEmbed(e));
        }

        private MessageEmbed generateListEmbed(CommandEvent e) {
            String path = "mcb.commands.textchannel.List.";
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("- " + Localization.getTranslatedMessage(path + "header") + " -", null);
            eb.setColor(e.getGuild().getMember(e.getJDA().getSelfUser()).getColor());
            int i = 1;
            JSONArray tcArray = MCBConfig.get("text_channels");
            for (Object tc : tcArray) {
                net.dv8tion.jda.core.entities.TextChannel tc1 = e.getJDA().getTextChannelById(tc.toString());
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
    }

    private class Add extends DiscordCommand {

        public Add(Minecordbot minecordbot) {
            super(minecordbot);
            this.name = "add";
            this.help = Localization.getTranslatedMessage("mcb.commands.textchannel.subcommand.add.description");
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
                    response = Localization.getTranslatedMessage("mcb.commands.textchannel.invalid-tc");
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
                    response = Localization.getTranslatedMessage("mcb.commands.textchannel.invalid-tc");
                    respond(e, String.format(response, arg));
                    return;
                }
            }
            response = Localization.getTranslatedMessage("mcb.commands.textchannel.added-tc");
            respond(e, String.format(response, arg));
            Minecordbot.LOGGER.info("Added text channel " + arg);
        }
    }

    private class Remove extends DiscordCommand {

        public Remove(Minecordbot minecordbot) {
            super(minecordbot);
            this.name = "remove";
            this.help = Localization.getTranslatedMessage("mcb.commands.textchannel.subcommand.remove.description");
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
                response = Localization.getTranslatedMessage("mcb.commands.textchannel.tc-not-bound");
                respond(e, String.format(response, arg));
                return;
            }
            if (tcArray.length() == 1) {
                response = Localization.getTranslatedMessage("mcb.commands.textchannel.last-tc");
                respond(e, String.format(response, arg));
                return;
            }
            tcArray.remove(JsonUtils.indexOf(tcID, tcArray));
            MCBConfig.set("text_channels", tcArray);
            response = Localization.getTranslatedMessage("mcb.commands.textchannel.removed-tc");
            respond(e, String.format(response, arg));
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
}
