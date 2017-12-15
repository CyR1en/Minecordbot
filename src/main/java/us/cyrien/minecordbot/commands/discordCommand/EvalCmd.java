package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import org.bukkit.Bukkit;
import us.cyrien.minecordbot.Bot;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class EvalCmd extends MCBCommand {

    public EvalCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "eval";
        this.arguments = "<script...>";
        this.help = "js eval function";
        this.aliases = new String[]{"ev"};
        this.ownerCommand = true;
        this.category = Bot.OWNER;
        this.type = Type.EMBED;
    }

    @Override
    public void doCommand(CommandEvent event) {
        String arg = event.getArgs();
        arg = arg.replaceAll("```js", "");
        arg = arg.replaceAll("```", "");
        if (event.getArgs().isEmpty()) {
            event.replyInDM(event.getClient().getError() + " please provide something to evaluate");
            return;
        }

        ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("Nashorn");
        scriptEngine.put("mcb", mcb);
        scriptEngine.put("event", event);
        scriptEngine.put("jda", event.getJDA());
        scriptEngine.put("guild", event.getGuild());
        scriptEngine.put("cfgMngr", mcb.getMcbConfigsManager());
        scriptEngine.put("channel", event.getTextChannel());
        scriptEngine.put("server", Bukkit.getServer());
        StringBuilder builder = new StringBuilder();
        try {
            builder.append("**Result**: ").append(scriptEngine.eval(arg.trim()));
            respond(builder.toString(), event).queue();
        } catch (Exception e1) {
            builder = new StringBuilder();
            builder.append("**Error**: ").append(e1.getClass().getSimpleName()).append("\n");
            builder.append("**Cause**: \n").append(e1.getCause().toString());
            respond(builder.toString(), event).queue();
        }
    }
}

