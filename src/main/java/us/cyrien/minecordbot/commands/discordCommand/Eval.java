package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import org.apache.commons.lang3.StringUtils;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.DiscordCommand;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Eval extends DiscordCommand{

    public Eval(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "eval";
        this.help = "js eval function";
        this.aliases = new String[]{"ev"};
        this.ownerCommand = true;
        this.category = minecordbot.OWNER;
    }

    @Override
    public void doCommand(CommandEvent event) {
        String arg = event.getArgs();
        arg = arg.replaceAll("```js", "");
        arg = arg.replaceAll("```", "");
        if (StringUtils.isBlank(arg)) {
            event.replyInDM(event.getClient().getError() + " please provide something to evaluate");
            return;
        }

        ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("Nashorn");
        scriptEngine.put("event", event);
        scriptEngine.put("jda", event.getJDA());
        scriptEngine.put("guild", event.getGuild());
        scriptEngine.put("channel", event.getTextChannel());
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(event.getGuild().getMemberById(event.getJDA().getSelfUser().getId()).getColor());
        try {
            eb.addField("Result:", "" + scriptEngine.eval(arg.trim()), false);
            event.reply(eb.build());
            return;
        } catch (Exception e1) {
            eb.addField("Error:" + e1.getClass().getSimpleName(), e1.getCause().toString(), false);
            event.reply(eb.build());
            return;
        }
    }
}

