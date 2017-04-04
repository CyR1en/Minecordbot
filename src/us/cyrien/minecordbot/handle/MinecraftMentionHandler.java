package us.cyrien.minecordbot.handle;

import net.dv8tion.jda.core.JDA;
import us.cyrien.minecordbot.main.Minecordbot;

public class MinecraftMentionHandler {

    private JDA jda;

    public MinecraftMentionHandler(Minecordbot mcb) {
        jda = mcb.getJDA();
    }

    public String handleMention(String s) {
        String[] strings = s.split(" ");
        for (int i = 0; i < strings.length; i++)
            if(strings[i].startsWith("@")) {
                strings[i] = "<@" + jda.getUsersByName(strings[i].replaceAll("@", "").replaceAll("_", " "), false).get(0).getId() + ">";
            }
        return concat(strings);
    }

    private String concat(String[] arr) {
        String out = "";
        if(arr.length > 1)
            for(String s : arr)
                out += s + " ";
        else
            out = arr[0];
        return out;
    }
}
