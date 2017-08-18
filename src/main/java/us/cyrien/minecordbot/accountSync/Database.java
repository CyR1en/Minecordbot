package us.cyrien.minecordbot.accountSync;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Database {

    private static final Path filePath = Paths.get("plugins/MineCordBot/Users/Accounts.json");
    private static JSONObject data;

    public static void set(String key, Object val) {
        if (val == null) {
            data.remove(key);
            SimplifiedDatabase.set(key, val);
        } else {
            data.put(key, val);
            JSONObject object = (JSONObject) val;
            Object s = object.get(DataKey.DISCORD_ID.toString());
            SimplifiedDatabase.set(key, s);
        }
        save();
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        if (data.has(key)) {
            try {
                return (T) data.get(key);
            } catch (JSONException | ClassCastException ignored) {
            }
        }
        return null;
    }

    public static <T> T get(String key, T def) {
        T obj = get(key);
        if (obj == null) {
            obj = def;
            data.put(key, def);
            save();
        }
        return obj;
    }

    public static JSONObject getJSONObject(String key) {
        return data.getJSONObject(key);
    }

    public static void reload() {
        try {
            data = new JSONObject(new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        save();
    }

    public static void save() {
        try {
            Files.write(filePath, data.toString(4).getBytes(StandardCharsets.UTF_8));
        } catch (IOException ignored) {
        }
    }

    public static boolean load() {
        boolean exists = Files.exists(filePath);
        try {
            JSONObject def = getDefault();
            if (!exists) {
                Files.createDirectories(Paths.get("plugins/MineCordBot/Users"));
                new File(String.valueOf(filePath)).createNewFile();
                Files.write(filePath, def.toString(4).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
            } else {
                data = new JSONObject(new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8));
                for (String key : def.keySet()) {
                    if (!data.has(key)) {
                        data.put(key, def.get(key));
                        exists = false;
                    }
                }
                if (!exists) {
                    save();
                }
            }
        } catch (IOException ex) {
            System.err.println("Error reading/writing data file: ");
            ex.printStackTrace();
            return false;
        }
        SimplifiedDatabase.load();
        return exists;
    }

    public static JSONObject getData() {
        return data;
    }

    public static  Path getConfigPath() {
        return filePath;
    }

    public static JSONObject getDefault() {
        JSONObject jsonObject = new JSONObject();
        return jsonObject;
    }
}
