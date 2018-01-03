package us.cyrien.minecordbot.commands.discordCommand;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.MessageBuilder;
import us.cyrien.mcutils.diagnosis.Diagnostics;
import us.cyrien.mcutils.diagnosis.DiagnosticsBuilder;
import us.cyrien.mcutils.diagnosis.TypeDiagnosis;
import us.cyrien.minecordbot.Bot;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.localization.Locale;

public class DiagnosticsCmd extends MCBCommand {

    public DiagnosticsCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "diagnostics";
        this.help = Locale.getCommandsMessage("diagnostics.description").finish();
        this.aliases = new String[]{"diagnose", "report"};
        this.ownerCommand = true;
        this.category = Bot.OWNER;
        this.type = Type.EMBED;
    }

    @Override
    protected void doCommand(CommandEvent e) {
        DiagnosticsBuilder dB = new DiagnosticsBuilder();
        dB = dB.setTypeDiagnosis(TypeDiagnosis.ALL);
        Diagnostics diagnostics = dB.build();
        e.getTextChannel().sendFile(diagnostics.fullReport(), new MessageBuilder().append("Diagnostics report: ").build()).queue();
    }
}
