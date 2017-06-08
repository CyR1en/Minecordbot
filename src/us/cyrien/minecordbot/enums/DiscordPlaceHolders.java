package us.cyrien.minecordbot.enums;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import us.cyrien.minecordbot.entity.User;

import java.util.List;

public enum DiscordPlaceHolders {
    CHANNEL {
        @Override
        public String toString() {
            return mre.getTextChannel().getName();
        }
    },
    GUILD{
        @Override
        public String toString() {
            return mre.getGuild().getName();
        }
    },
    SENDER{
        @Override
        public String toString() {
            return mre.getAuthor().getName();
        }
    },
    NAME {
        @Override
        public String toString() {
            User user = new User(mre);
            return user.getName();
        }
    },
    ROLE {
      @Override
        public String toString() {
          Member member = mre.getGuild().getMember(mre.getAuthor());
          List<Role> roles = member.getRoles();
          return roles.get(0).getName();
      }
    },
    ENAME{
        @Override
        public String toString() {
            return mre.getMember().getEffectiveName();
        }
    };

    private static MessageReceivedEvent mre;

    public void init(MessageReceivedEvent mre) {
        DiscordPlaceHolders.mre = mre;
    }

}
