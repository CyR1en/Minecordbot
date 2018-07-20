package us.cyrien.minecordbot.handle;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Member;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.utils.SearchUtil;
import us.cyrien.minecordbot.utils.SRegex;

import java.util.List;
import java.util.regex.Pattern;

public class MinecraftMentionHandler {

    private JDA jda;

    public MinecraftMentionHandler(Minecordbot mcb) {
        jda = mcb.getBot().getJda();
    }

    public String handleMention(String s) {
        List<String> mentions = new SRegex(s).find(Pattern.compile("@[!-\\/:-@A-Z\\[-~\\d]+")).getResultsList();
        for (String m : mentions) {
            m = m.replaceAll("_", " ").replaceAll("-", "_").replaceAll("@", "");
            Member member = SearchUtil.findMember(m);
            if(member != null) {
                m = m.replaceAll("[^\\w\\s]", "\\\\$0");
                s = s.replaceAll("@" + m, "<@" + member.getUser().getId() + ">");
            } else {
            }
        }
        return s;
    }
}
