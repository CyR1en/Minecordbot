package us.cyrien.minecordbot.utils;

import org.json.JSONArray;

public class JsonUtils {

    public static int indexOf(String s, JSONArray jsonArray) {
        for(int i = 0; i < jsonArray.length(); i++)
            if(jsonArray.get(i).equals(s))
                return i;
        return -1;
    }
}
