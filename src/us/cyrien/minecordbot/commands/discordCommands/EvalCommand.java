package us.cyrien.minecordbot.commands.discordCommands;

import io.github.hedgehog1029.frame.annotations.Text;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import us.cyrien.minecordbot.core.annotation.DCmd;
import us.cyrien.minecordbot.core.annotation.DCommand;
import us.cyrien.minecordbot.core.annotation.DMessageReceive;
import us.cyrien.minecordbot.core.annotation.DPermission;
import us.cyrien.minecordbot.core.enums.CommandType;
import us.cyrien.minecordbot.core.enums.PermissionLevel;
import us.cyrien.minecordbot.core.module.DiscordCommand;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import static us.cyrien.minecordbot.core.module.DiscordCommand.HELP_COMMAND_DURATION;

public class EvalCommand {
    @DCommand(aliases = {"eval", "e"}, usage = "mcb.commands.eval.usage", desc = "mcb.commands.eval.description", type = CommandType.SPECIAL)
    @DPermission(PermissionLevel.OWNER)
    public void command(@DMessageReceive MessageReceivedEvent e, @DCmd DiscordCommand command, @Text String str) {
        String arg = str.trim();
        arg = arg.replaceAll("```js", "");
        arg = arg.replaceAll("```", "");
        if (StringUtils.isBlank(arg)) {
            command.sendMessageEmbed(e, command.getInvalidHelpCard(e), HELP_COMMAND_DURATION);
            return;
        }

        ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("Nashorn");
        scriptEngine.put("event", e);
        scriptEngine.put("jda", e.getJDA());
        scriptEngine.put("guild", e.getGuild());
        scriptEngine.put("channel", e.getTextChannel());
        EmbedBuilder eb = new EmbedBuilder();
        try {
            eb.setColor(e.getGuild().getMemberById(e.getJDA().getSelfUser().getId()).getColor());
            eb.addField("Result:", "" + scriptEngine.eval(arg.trim()), false);
            command.sendMessageEmbed(e, eb.build(), 50);
        } catch (Exception e1) {
            eb.setColor(e.getGuild().getMemberById(e.getJDA().getSelfUser().getId()).getColor());
            eb.addField("Error:" + e1.getClass().getSimpleName(), e1.getCause().toString(), false);
            command.sendMessageEmbed(e, eb.build(), 120);
        }
        /*
        Expression ex = new Expression(arg);
        EmbedBuilder eb = new EmbedBuilder();
        boolean correctSyntax = ex.checkSyntax();
        if (!correctSyntax) {
            eb.setColor(new Color(217, 83, 79));
            eb.addField("SYNTAX ERROR", ex.getErrorMessage(), false);
            command.sendMessageEmbed(e, eb.build(), 120);
        } else {
            eb.setColor(e.getGuild().getMemberById(e.getJDA().getSelfUser().getId()).getColor());
            eb.addField("Result", "(" + ex.getExpressionString() + ") = " + ex.calculate(), false);
            command.sendMessageEmbed(e, eb.build(), 25);
        }
        */
    }
}
