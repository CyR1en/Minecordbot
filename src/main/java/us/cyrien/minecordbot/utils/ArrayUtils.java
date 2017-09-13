package us.cyrien.minecordbot.utils;


import net.dv8tion.jda.core.Permission;

import java.util.List;

public class ArrayUtils {

    public static String concatenateArgs(int first, int last, String[] args) {
        String out = "";
        for(int i = first; i < last; i++) {
            out += args[i] + " ";
        }
        return out.trim();
    }

    public static String concatenateArgs(String[] args, int last) {
        return concatenateArgs(0, last, args);
    }

    public static String concatenateArgs(int first, String[] args) {
        return concatenateArgs(first, args.length, args);
    }

    public static String concatenateArgs(String[] args) {
        return concatenateArgs(0, args.length, args);
    }

    public static Permission[] permsAsArray(List<Permission> perms) {
        Permission[] tempArr = new Permission[perms.size()];
        for(int i = 0; i < tempArr.length; i++) {
            tempArr[0] = perms.get(i);
        }
        return tempArr;
    }
}
