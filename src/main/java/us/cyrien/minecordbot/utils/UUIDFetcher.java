/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2016 DaPorkchop_
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from DaPorkchop_.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package us.cyrien.minecordbot.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import us.cyrien.minecordbot.Minecordbot;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class UUIDFetcher {
    private static final double PROFILES_PER_REQUEST = 100;
    private static final String PROFILE_URL = "https://api.mojang.com/profiles/minecraft";
    private static final JsonParser jsonParser = new JsonParser();
    private static final Gson gson = new Gson();
    private static ArrayList<UUIDRequest> requests = new ArrayList<>();

    private static void writeBody(HttpURLConnection connection, String body) throws Exception {
        OutputStream stream = connection.getOutputStream();
        stream.write(body.getBytes());
        stream.flush();
        stream.close();
    }

    private static HttpURLConnection createConnection() throws Exception {
        URL url = new URL(PROFILE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        return connection;
    }

    public static UUID getUUID(String id) {
        return UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id.substring(20, 32));
    }

    public static String removeDiscriminant(String uuid) {
        return uuid.replaceAll("-", "");
    }

    public static byte[] toBytes(UUID uuid) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());
        return byteBuffer.array();
    }

    public static UUID fromBytes(byte[] array) {
        if (array.length != 16) {
            throw new IllegalArgumentException("Illegal byte array length: " + array.length);
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(array);
        long mostSignificant = byteBuffer.getLong();
        long leastSignificant = byteBuffer.getLong();
        return new UUID(mostSignificant, leastSignificant);
    }

    public static void run() {
        try {
            ArrayList<UUIDRequest> temp = new ArrayList<>();
            ArrayList<String> jsonArray = new ArrayList<>();
            for (int i = 0; i < requests.size() && i < 100; i++) {
                UUIDRequest request = requests.get(i);
                jsonArray.add(request.name);
                temp.add(request);
            }

            String json = HTTPUtils.performPostRequest(new URL(PROFILE_URL), gson.toJson(jsonArray), "application/json");
            JsonArray array = jsonParser.parse(json).getAsJsonArray();
            for (Object profile : array) {
                JsonObject jsonProfile = (JsonObject) profile;
                String id = jsonProfile.get("id").getAsString();
                String name = jsonProfile.get("name").getAsString();
                UUIDRequest[] uuidRequest = getRequestByName(name);
                for (UUIDRequest uuidRequest1 : uuidRequest) {
                    uuidRequest1.uuidCompletable.run(id);
                    requests.remove(uuidRequest1);
                    temp.remove(uuidRequest1);
                }
            }
            for (UUIDRequest request : temp) {
                UUIDRequest[] uuidRequest = getRequestByName(request.name);
                for (UUIDRequest uuidRequest1 : uuidRequest) {
                    requests.remove(uuidRequest1);
                    uuidRequest1.uuidCompletable.run("11111111-1111-1111-1111-111111111111");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        Minecordbot.getInstance().getScheduler().schedule(() -> {
            if (requests.isEmpty()) {
                return;
            }
            UUIDFetcher.run();
            //requests.clear(); //TODO fix this
        } ,5, TimeUnit.SECONDS);

    }

    public static void enqeueRequest(String name, StringImplicit completableFuture) {
        requests.add(new UUIDRequest(name, completableFuture));
    }

    private static UUIDRequest[] getRequestByName(String name) {
        ArrayList<UUIDRequest> arrayList = new ArrayList<>();
        for (UUIDRequest request : requests) {
            if (request.name.equals(name)) {
                arrayList.add(request);
            }
        }

        return arrayList.toArray(new UUIDRequest[arrayList.size()]);
    }

    public static class UUIDRequest {
        public String name;
        public StringImplicit uuidCompletable;

        public UUIDRequest(String a, StringImplicit b) {
            name = a;
            uuidCompletable = b;
        }
    }
}
