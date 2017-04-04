package us.cyrien.minecordbot.configuration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MCBConfig {

    private static final Path configPath = Paths.get("plugins/Minecordbot/config.json");
    private static JSONObject config;

    public static void set(String key, Object val) {
        if (val == null) {
            config.remove(key);
        } else {
            config.put(key, val);
        }
        save();
    }

    @SuppressWarnings("unchecked")
    public static <T> T get( String key) {
        if (config.has(key)) {
            try {
                return (T) config.get(key);
            } catch (JSONException | ClassCastException ignored) {
            }
        }
        return null;
    }

    public static <T> T get( String key,  T def) {
        T obj = get(key);
        if (obj == null) {
            obj = def;
            config.put(key, def);
            save();
        }
        return obj;
    }

    public static JSONObject getJSONObject( String key) {
        return config.getJSONObject(key);
    }

    public static void reload() {
        try {
            config = new JSONObject(new String(Files.readAllBytes(configPath), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        save();
    }

    public static void save() {
        try {
            Files.write(configPath, config.toString(4).getBytes(StandardCharsets.UTF_8));
        } catch (IOException ignored) {
        }
    }

    // FIXME: 3/30/2017
    public static boolean load() {
        boolean exists = Files.exists(configPath);
        try {
            JSONObject def = getDefault();
            if (!exists) {
                Files.createDirectories(Paths.get("plugins/Minecordbot"));
                new File(String.valueOf(configPath)).createNewFile();
                Files.write(configPath, def.toString(4).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
            } else {
                config = new JSONObject(new String(Files.readAllBytes(configPath), StandardCharsets.UTF_8));
                for (String key : def.keySet()) {
                    if (!config.has(key)) {
                        config.put(key, def.get(key));
                        exists = false;
                    }
                }
                if (!exists) {
                    save();
                }
            }
        } catch (IOException ex) {
            System.err.println("Error reading/writing config file: ");
            ex.printStackTrace();
            return false;
        }
        return exists;
    }

    private static JSONObject getDefault() {
        JSONObject perms = new JSONObject();
        perms.put("level_1", new String[]{"12332131231231"});
        perms.put("level_2", new String[]{"12332131231231"});
        perms.put("level_3", new String[]{"12332131231231"});
        JSONObject broadcast = new JSONObject();
        broadcast.put("death_event", true);
        broadcast.put("join_event", true);
        broadcast.put("leave_event", true);
        broadcast.put("hide_incognito_users", true);
        JSONObject ctc = new JSONObject();
        ctc.put("misc", new String[]{" "});
        ctc.put("help", new String[]{" "});
        ctc.put("fun", new String[]{" "});
        ctc.put("info", new String[]{" "});
        ctc.put("mod", new String[]{" "});
        return new JSONObject()
                .put("bot_token", "replace this with your bot token")
                .put("client_id", "replace this with your bot's client id")
                .put("owner_id", "replace this with your server owner id")
                .put("trigger", ",")
                .put("auto_delete_command_response", false)
                .put("auto_update", false)
                .put("localization", "en")
                .put("text_channels", new String[]{"923823", "3232323"})
                .put("permissions", perms)
                .put("message_prefix_discord", "Minecraft {SENDER}:")
                .put("message_prefix_minecraft", "Discord {SENDER}:")
                .put("broadcasts", broadcast)
                .put("command_text_channel", ctc);
    }

}