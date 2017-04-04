package us.cyrien.minecordbot.enums;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

// FIXME: 3/30/2017 
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
    };

    private static MessageReceivedEvent mre;

    public void init(MessageReceivedEvent mre) {
        DiscordPlaceHolders.mre = mre;
    }

}
