package us.cyrien.minecordbot.commands.discordCommands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.cyrien.minecordbot.core.annotation.DCmd;
import us.cyrien.minecordbot.core.annotation.DCommand;
import us.cyrien.minecordbot.core.annotation.DMessageReceive;
import us.cyrien.minecordbot.core.annotation.DPermission;
import us.cyrien.minecordbot.core.enums.CommandType;
import us.cyrien.minecordbot.core.enums.PermissionLevel;
import us.cyrien.minecordbot.core.module.DiscordCommand;

public class ListCommand {
    @DCommand(aliases = {"list", "ls"}, usage = "mcb.commands.list.usage", desc = "mcb.commands.list.description", type = CommandType.INFO)
    @DPermission(PermissionLevel.LEVEL_0)
    public void command(@DMessageReceive MessageReceivedEvent e, @DCmd DiscordCommand command) {
        EmbedBuilder eb = new EmbedBuilder();
        command.sendMessageEmbed(e, generateList(eb), 60);
    }

    private MessageEmbed generateList(EmbedBuilder eb) {
        StringBuilder out;
        if (Bukkit.getServer().getOnlinePlayers() == null || Bukkit.getServer().getOnlinePlayers().size() == 0) {
            eb.setTitle("There are no players online...", null);
        } else {
            out = new StringBuilder();
            int counter = 1;
            for (Player p : Bukkit.getOnlinePlayers())
                out.append(counter++).append(". ").append(p.getName()).append("\n");
            eb.addField("Online Players on Minecraft", out.toString(), false);
        }
        return eb.build();
    }
}
