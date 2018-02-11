package us.cyrien.minecordbot.configuration;

public interface Node {

    Object getDefaultValue();

    String[] getComment();

    String key();

}
