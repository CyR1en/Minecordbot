package us.cyrien.minecordbot.event;

import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import us.cyrien.minecordbot.main.Minecordbot;

public class BotReadyEvent extends ListenerAdapter {

    private Minecordbot mcb;

    public BotReadyEvent(Minecordbot mcb) {
        this.mcb = mcb;
    }

    @Override
    public void onReady(ReadyEvent event) {
        /*
        String version = Bukkit.getPluginManager().getPlugin("MineCordBot").getDescription().getVersion();
        StringBuilder sb = new StringBuilder();
        sb.append("\n" + "========================================================\n" );
        sb.append("|   __  __ _           ___            _ ___      _     |\n"+
                  "|  |  \\/  (_)_ _  ___ / __|___ _ _ __| | _ ) ___| |_   |\n" +
                  "|  | |\\/| | | ' \\/ -_) (__/ _ \\ '_/ _` | _ \\/ _ \\  _|  |\n" +
                  "|  |_|  |_|_|_||_\\___|\\___\\___/_| \\__,_|___/\\___/\\__|  |\n" +
                  "|                      E N A B LE D                    |\n" +
                  "|                         "+version+"                        |\n");
        sb.append("========================================================");
        Minecordbot.LOGGER.info(sb.toString());
        */
    }
}
