package us.cyrien.minecordbot.commands.discordCommands;

import io.github.hedgehog1029.frame.annotations.Text;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import us.cyrien.minecordbot.core.annotation.DCmd;
import us.cyrien.minecordbot.core.annotation.DCommand;
import us.cyrien.minecordbot.core.annotation.DMessageReceive;
import us.cyrien.minecordbot.core.enums.CommandType;
import us.cyrien.minecordbot.core.module.DiscordCommand;
import us.cyrien.minecordbot.handle.BingSearch;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ImageSearchCommand {
    @DCommand(aliases = {"imagesearch", "is"}, usage = "mcb.commands.imagesearch.usage", desc = "mcb.commands.imagesearch.description", type = CommandType.FUN)
    public void command(@DMessageReceive MessageReceivedEvent e, @DCmd DiscordCommand command, @Text String arg) {
        List<String> list = new ArrayList<>();
        list.add("f1e0eb15adb24e82926b9052974d7306");
        list.add("e26a282df54247d6a999a3b397da9207");
        BingSearch bingsearch = new BingSearch(list);
        e.getChannel().sendTyping();
        EmbedBuilder eb = new EmbedBuilder();
        List<String> urls = bingsearch.search(arg);
        if (urls == null) {
            eb.setColor(new Color(126, 26, 26));
            eb.addField("ERROR:", "An error occurred while searching", false);
            command.sendMessageEmbed(e, eb.build(), 60);
            return;
        }
        if (urls.isEmpty()) {
            eb.setColor(new Color(126, 26, 26));
            eb.addField("No result:", "No results found for \"" + arg + "\"", false);
            return;
        }
        command.sendMessage(e, urls.get((int) (Math.random() * urls.size())), 240);
    }
}
