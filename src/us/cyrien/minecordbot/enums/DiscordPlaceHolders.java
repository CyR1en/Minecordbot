package us.cyrien.minecordbot.enums;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import us.cyrien.minecordbot.entity.User;

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
