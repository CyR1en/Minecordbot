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

package us.cyrien.minecordbot.utils.mcpinger.query;

import org.xbill.DNS.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Send a query to a given minecraft server and store any metadata and the
 * player list.
 *
 * @author Ryan Shaw, Jonas Konrad
 */
public class Query {
    /**
     * The target address and port
     */
    public InetSocketAddress address;
    /**
     * <code>null</code> if no successful request has been sent, otherwise a Map
     * containing any metadata received except the player list
     */
    public Map<String, String> values;
    /**
     * <code>null</code> if no successful request has been sent, otherwise an
     * array containing all online player usernames
     */
    public String[] onlineUsernames;
    public InetSocketAddress queryAddress;

    /**
     * Convenience constructor
     *
     * @param host The target host
     * @param port The target port
     * @see Query(InetSocketAddress)
     */
    public Query(String host, int port, int queryport) {
        this(new InetSocketAddress(host, queryport), new InetSocketAddress(host, port));
    }

    /**
     * Create a new instance of this class
     *
     * @param address The servers IP-address
     */
    public Query(InetSocketAddress queryAddress, InetSocketAddress address) {
        this.address = address;
        this.queryAddress = queryAddress;
        if (queryAddress.getPort() == 0) {
            this.queryAddress = new InetSocketAddress(queryAddress.getHostName(), address.getPort());
        }
    }

    /**
     * Helper method to send a datagram packet
     *
     * @param socket        The connection the packet should be sent through
     * @param targetAddress The target IP
     * @param data          The byte data to be sent
     * @throws IOException
     */
    private final static void sendPacket(DatagramSocket socket, InetSocketAddress targetAddress, byte... data) throws IOException {
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, targetAddress.getAddress(), targetAddress.getPort());
        socket.send(sendPacket);
    }

    /**
     * Helper method to send a datagram packet
     *
     * @param socket        The connection the packet should be sent through
     * @param targetAddress The target IP
     * @param data          The byte data to be sent, will be cast to bytes
     * @throws IOException
     * @see Query#sendPacket(DatagramSocket, InetSocketAddress, byte...)
     */
    private final static void sendPacket(DatagramSocket socket, InetSocketAddress targetAddress, int... data) throws IOException {
        final byte[] d = new byte[data.length];
        int i = 0;
        for (int j : data)
            d[i++] = (byte) (j & 0xff);
        sendPacket(socket, targetAddress, d);
    }

    /**
     * Receive a packet from the given socket
     *
     * @param socket the socket
     * @param buffer the buffer for the information to be written into
     * @return the entire packet
     * @throws IOException
     */
    private final static DatagramPacket receivePacket(DatagramSocket socket, byte[] buffer) throws IOException {
        final DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
        socket.receive(dp);
        return dp;
    }

    /**
     * Read a String until 0x00
     *
     * @param array  The byte array
     * @param cursor The mutable cursor (will be increased)
     * @return The string
     */
    private final static String readString(byte[] array, AtomicInteger cursor) {
        final int startPosition = cursor.incrementAndGet();
        for (; cursor.get() < array.length && array[cursor.get()] != 0; cursor.incrementAndGet())
            ;
        return new String(Arrays.copyOfRange(array, startPosition, cursor.get()));
    }

    /**
     * Try pinging the server and then sending the query
     *
     * @throws IOException If the server cannot be pinged
     * @see Query#pingServer()
     * @see Query#sendQueryRequest()
     */
    public void sendQuery() throws IOException {
        sendQueryRequest();
    }

    /**
     * Try pinging the server
     *
     * @return <code>true</code> if the server can be reached within 1.5 second
     */
    public boolean pingServer() {
        // try pinging the given server
        String service = "_minecraft._tcp." + address.getHostName();
        try {
            Record[] records = new Lookup(service, Type.SRV).run();
            if (records != null)
                for (Record record : records) {
                    SRVRecord srv = (SRVRecord) record;
                    String hostname = srv.getTarget().toString();
                    int port = srv.getPort();
                    address = new InetSocketAddress(hostname, port);
                    queryAddress = new InetSocketAddress(hostname, port);
                }
        } catch (TextParseException e1) {
            e1.printStackTrace();
        }
        try {
            final Socket socket = new Socket();
            socket.connect(address, 1500);
            socket.close();
            return true;
        } catch (IOException e) {
        }
        return false;
    }

    /**
     * Get the additional values if the Query has been sent
     *
     * @return The data
     * @throws IllegalStateException if the query has not been sent yet or there has been an error
     */
    public Map<String, String> getValues() {
        if (values == null)
            throw new IllegalStateException("Query has not been sent yet!");
        else
            return values;
    }

    /**
     * Get the online usernames if the Query has been sent
     *
     * @return The username array
     * @throws IllegalStateException if the query has not been sent yet or there has been an error
     */
    public String[] getOnlineUsernames() {
        if (onlineUsernames == null)
            throw new IllegalStateException("Query has not been sent yet!");
        else
            return onlineUsernames;
    }

    /**
     * Request the UDP query
     *
     * @throws IOException if anything goes wrong during the request
     */
    private void sendQueryRequest() throws IOException {
        InetSocketAddress local = queryAddress;
        if (queryAddress.getPort() == 0) {
            local = new InetSocketAddress(queryAddress.getAddress(), address.getPort());
        }
        //System.out.println(local);
        final DatagramSocket socket = new DatagramSocket();
        try {
            final byte[] receiveData = new byte[10240];
            socket.setSoTimeout(2500);
            sendPacket(socket, local, 0xFE, 0xFD, 0x09, 0x01, 0x01, 0x01, 0x01);
            final int challengeInteger;
            {
                receivePacket(socket, receiveData);
                byte byte1 = -1;
                int i = 0;
                byte[] buffer = new byte[11];
                for (int count = 5; (byte1 = receiveData[count++]) != 0; )
                    buffer[i++] = byte1;
                challengeInteger = Integer.parseInt(new String(buffer).trim());
            }
            sendPacket(socket, local, 0xFE, 0xFD, 0x00, 0x01, 0x01, 0x01, 0x01, challengeInteger >> 24, challengeInteger >> 16, challengeInteger >> 8, challengeInteger, 0x00, 0x00, 0x00, 0x00);

            final int length = receivePacket(socket, receiveData).getLength();
            values = new HashMap<String, String>();
            final AtomicInteger cursor = new AtomicInteger(5);
            while (cursor.get() < length) {
                final String s = readString(receiveData, cursor);
                if (s.length() == 0)
                    break;
                else {
                    final String v = readString(receiveData, cursor);
                    values.put(s, v);
                }
            }
            readString(receiveData, cursor);
            final Set<String> players = new HashSet<String>();
            while (cursor.get() < length) {
                final String name = readString(receiveData, cursor);
                if (name.length() > 0)
                    players.add(name);
            }
            onlineUsernames = players.toArray(new String[players.size()]);
        } finally {
            socket.close();
        }
    }

    public String getMOTD() {
        return values.getOrDefault("hostname", "A Minecraft server");
    }

    public int getOnlinePlayers()   {
        return Integer.parseInt(values.getOrDefault("numplayers", "0"));
    }

    public int getMaxPlayers()   {
        return Integer.parseInt(values.getOrDefault("maxplayers", "20"));
    }

    public String getPlugins() {
        return values.getOrDefault("plugins", "A Minecraft server");
    }

    public String getMapName() {
        return values.getOrDefault("map", "New World");
    }

    public String getGameMode() {
        return values.getOrDefault("gametype", "SMP");
    }
}
