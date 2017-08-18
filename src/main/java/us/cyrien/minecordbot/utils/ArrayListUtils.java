package us.cyrien.minecordbot.utils;

import us.cyrien.minecordbot.core.module.DiscordCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ArrayListUtils {

    private static List<DiscordCommand> merge(final List<DiscordCommand> left, final List<DiscordCommand> right) {
        final List<DiscordCommand> merged = new ArrayList<>();
        while (!left.isEmpty() && !right.isEmpty()) {
            if (left.get(0).getName().compareTo(right.get(0).getName()) <= 0) {
                merged.add(left.remove(0));
            } else {
                merged.add(right.remove(0));
            }
        }
        merged.addAll(left);
        merged.addAll(right);
        return merged;
    }

    public static void sort(final List<DiscordCommand> input) {
        if (input.size() != 1) {
            final List<DiscordCommand> left = new ArrayList<>();
            final List<DiscordCommand> right = new ArrayList<>();
            boolean logicalSwitch = true;
            while (!input.isEmpty()) {
                if (logicalSwitch) {
                    left.add(input.remove(0));
                } else {
                    right.add(input.remove(0));
                }
                logicalSwitch = !logicalSwitch;
            }
            sort(left);
            sort(right);
            input.addAll(merge(left, right));
        }
    }

    public static int getIndexOf(String str, List<DiscordCommand> list) {
        ListIterator<DiscordCommand> it = list.listIterator();
        while (it.hasNext()) {
            DiscordCommand dc = it.next();
            if (dc.getName().equalsIgnoreCase(str)) {
                return it.nextIndex() - 1;
            } else if (dc.getAliases().contains(str)) {
                return it.nextIndex() - 1;
            }
        }
        return -1;
    }
}
