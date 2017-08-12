package us.cyrien.minecordbot.handle;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.User;
import us.cyrien.minecordbot.Minecordbot;

import java.util.ArrayList;

public class MinecraftMentionHandler {

    private JDA jda;

    public MinecraftMentionHandler(Minecordbot mcb) {
        jda = mcb.getJDA();
    }

    public String handleMention(String s) {
        ArrayList<User> list;
        String[] strings = s.split(" ");
        for (int i = 0; i < strings.length; i++)
            if (strings[i].startsWith("@")) {
                list = (ArrayList) jda.getUsersByName(strings[i].replaceAll("@", "").replaceAll("_", " "), false);
                if (list.size() > 0) {
                    String uID = list.get(0).getId();
                    strings[i] = "<@" + uID + ">";
                }
            }
        return concat(strings);
    }

    private String concat(String[] arr) {
        String out = "";
        if (arr.length > 1)
            for (String s : arr)
                out += s + " ";
        else
            out = arr[0];
        return out;
    }
}
