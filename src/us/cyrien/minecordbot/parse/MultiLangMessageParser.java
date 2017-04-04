package us.cyrien.minecordbot.parse;

public class MultiLangMessageParser {

    public String parsePlayer(String msg, String playerName) {
        return msg.replaceAll("\\{player}", playerName );
    }

    public String parse(String msg, String playerName, String killerName) {
        msg = msg.replaceAll("\\{player}", playerName);
        msg = msg.replaceAll("\\{killer}", killerName);
        return msg;
    }

}
