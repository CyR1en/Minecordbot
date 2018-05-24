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

package us.cyrien.minecordbot.utils.mcpinger;

import com.whirvis.jraknet.identifier.Identifier;
import com.whirvis.jraknet.util.RakNetUtils;
import us.cyrien.minecordbot.utils.mcpinger.pcping.MinecraftPing;
import us.cyrien.minecordbot.utils.mcpinger.pcping.MinecraftPingReply;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.GregorianCalendar;

/**
 * A library for interacting with DaPorkchop_'s mcping api
 */
public abstract class MCPing {
    /**
     * The default favicon to use if there is none
     */
    public static final String fallbackFavicon = "data:image/png;base64,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";

    /**
     * Pings a PE server and returns a ping
     *
     * @param ip   the server's ip
     * @param port the server's port
     * @return a filled PePing
     */
    public static PePing pingPe(String ip, int port, boolean measurePing) {
        try {
            Identifier identifier;

            try {
                identifier = RakNetUtils.getServerIdentifier(ip, port);
            } catch (UnknownHostException e) {
                identifier = null;
            }

            return new PePing(false, false, null, null, null, null, 0, false, null);
        } catch (Exception e) {
            return new PePing(false, false, null, null, null, null, 0, true, e);
        }
    }

    /**
     * Queries a Minecraft server and pings it, returning info
     *
     * @param ip   the server's ip
     * @param port the server's port
     * @param pe   whether or not the server is a Pocket Edition server
     * @return a filled Query
     */
    public static Query query(String ip, int port, boolean pe, boolean getLatency) {
        try {
            if (pe) {
                PePing ping = pingPe(ip, port, getLatency);
                if (ping.status) {
                    try {
                        us.cyrien.minecordbot.utils.mcpinger.query.Query response = new us.cyrien.minecordbot.utils.mcpinger.query.Query(ip, port, 0);
                        response.sendQuery();
                        if (response.values == null) {
                            throw new IllegalStateException("Somehow or other there was no error while pinging, so we'll throw one now to make stuff do things!");
                        }
                        String playerNames = response.onlineUsernames.length > 0 ? response.onlineUsernames[0] : null;
                        if (playerNames != null) {
                            for (int i = 1; i < response.onlineUsernames.length; i++) {
                                playerNames += ", " + response.onlineUsernames[i];
                            }
                        }
                        return new Query(true, false, response.getMOTD(), response.getOnlinePlayers() + "/" + response.getMaxPlayers(), getLatency ? ping.ping : "0 ms", ping.version, ping.protocol, playerNames, response.getPlugins(), false, null, response.getMapName(), response.getGameMode(), null);
                    } catch (Exception e) {
                        //no query, as server is online
                        return new Query(true, true, null, null, null, null, 0, null, null, false, null, null, null, null);
                    }
                } else {
                    return new Query(false, false, null, null, null, null, 0, null, null, false, null, null, null, null);
                }
            } else {
                McPing ping = pingPc(ip, port, getLatency);
                if (ping.status) {
                    try {
                        us.cyrien.minecordbot.utils.mcpinger.query.Query response = new us.cyrien.minecordbot.utils.mcpinger.query.Query(ip, port, 0);
                        response.sendQuery();
                        if (response.values == null) {
                            throw new IllegalStateException("Somehow or other there was no error while pinging, so we'll throw one now to make stuff do things!");
                        }
                        String playerNames = response.onlineUsernames.length > 0 ? response.onlineUsernames[0] : null;
                        if (playerNames != null) {
                            for (int i = 1; i < response.onlineUsernames.length; i++) {
                                playerNames += ", " + response.onlineUsernames[i];
                            }
                        }
                        return new Query(true, false, response.getMOTD(), response.getOnlinePlayers() + "/" + response.getMaxPlayers(), getLatency ? ping.ping : "0 ms", ping.version, ping.protocol, playerNames, response.getPlugins(), false, null, response.getMapName(), response.getGameMode(), ping.favicon);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //no query, as the server is online
                        return new Query(false, true, null, null, null, null, 0, null, null, false, null, null, null, null);
                    }
                } else {
                    return new Query(false, false, null, null, null, null, 0, null, null, false, null, null, null, null);
                }
            }
        } catch (Exception e) {
            return new Query(false, false, null, null, null, null, 0, null, null, true, e, null, null, null);
        }
    }

    /**
     * Ping a Minecraft PC server, returning info
     *
     * @param ip   the server's ip
     * @param port the server's port
     * @return a filled McPing
     */
    public static McPing pingPc(String ip, int port, boolean measureping) {
        try {
            MinecraftPingReply reply = MinecraftPing.getPing(ip, port);

            if (reply != null) {
                return new McPing(true, reply.getDescription().getText(), reply.getPlayers().getOnline() + "/" + reply.getPlayers().getMax(), reply.getVersion().getProtocol(), reply.getVersion().getName(), measureping ? getPingToIP(ip) : "0 ms", reply.getFavicon() == null ? fallbackFavicon : reply.getFavicon(), false, null);
            } else {
                return new McPing(false, null, null, 0, null, null, null, false, null);
            }
        } catch (Exception e) {
            return new McPing(false, null, null, 0, null, null, null, true, e);
        }
    }

    /**
     * pings an ip address
     *
     * @param ipToPingNowPleaseGiveThisWhyIsThisFieldDescriptorSoLongLolEksDee idk lol
     * @return the ping
     */
    public static String getPingToIP(String ipToPingNowPleaseGiveThisWhyIsThisFieldDescriptorSoLongLolEksDee) {
        try {
            InetAddress inet = InetAddress.getByName(ipToPingNowPleaseGiveThisWhyIsThisFieldDescriptorSoLongLolEksDee);

            long finish = 0;
            long start = new GregorianCalendar().getTimeInMillis();

            if (inet.isReachable(2500)) {
                finish = new GregorianCalendar().getTimeInMillis();
                return finish - start + " ms";
            } else {
                return "-1 ms";
            }
        } catch (Exception e) {
            //System.out.println("Exception:" + e.getMessage());
        }

        return "-1 ms";
    }

    /**
     * Represents a ping. All pings extend this.
     */
    protected static abstract class Ping {
        public boolean status;
        public boolean errored;
        public Exception error;

        public Ping(boolean isOnline, boolean errored, Exception error) {
            this.status = isOnline;
            this.errored = errored;
            this.error = error;
        }
    }

    /**
     * A Minecraft: Pocket Edition ping.
     */
    public static class PePing extends Ping {
        public boolean notMCPE;
        public String motd;
        public String players;
        public String ping;
        public String version;
        public int protocol;

        public PePing(boolean isOnline, boolean notMCPE, String motd, String players, String ping, String version, int protocol, boolean errored, Exception error) {
            super(isOnline, errored, error);
            this.notMCPE = notMCPE;
            this.players = players;
            this.ping = ping;
            this.version = version;
            this.protocol = protocol;
            this.motd = motd;
        }
    }

    /**
     * A generic query, used by both PE and PC
     */
    public static class Query extends Ping {
        public boolean noQuery;
        public String motd;
        public String players;
        public String ping;
        public String version;
        public int protocol;
        public String playerSample;
        public String plugins;
        public String mapName;
        public String gamemode;
        public String favicon;

        public Query(boolean isOnline, boolean noQuery, String motd, String players, String ping, String version, int protocol, String playerSample, String plugins, boolean errored, Exception error, String mapName, String gamemode, String favicon) {
            super(isOnline, errored, error);
            this.noQuery = noQuery;
            this.players = players;
            this.ping = ping;
            this.version = version;
            this.protocol = protocol;
            this.playerSample = playerSample;
            this.plugins = plugins;
            this.motd = motd;
            this.mapName = mapName;
            this.gamemode = gamemode;
            this.favicon = favicon;
        }
    }

    /**
     * Represents a MCPC ping
     */
    public static class McPing extends Ping {
        public boolean old;
        public String motd;
        public String players;
        public int protocol;
        public String version;
        public String ping;
        public String favicon;

        public McPing(boolean isOnline, String motd, String players, int protocol, String version, String ping, String favicon, boolean errored, Exception error) {
            super(isOnline, errored, error);
            this.motd = motd;
            this.old = old;
            this.players = players;
            this.protocol = protocol;
            this.version = version;
            this.ping = ping;
            this.favicon = favicon;
        }
    }
}
