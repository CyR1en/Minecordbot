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
package us.cyrien.minecordbot.utils.mcpinger.pcping;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MinecraftPing {

    /**
     * Fetches a {@link MinecraftPingReply} for the supplied hostname.
     * <b>Assumed timeout of 2s and port of 25565.</b>
     *
     * @param hostname - a valid String hostname
     * @return {@link MinecraftPingReply}
     * @throws IOException
     */
    public static MinecraftPingReply getPing(final String hostname, final int pork) throws IOException {
        return getPing(new MinecraftPingOptions().setHostname(hostname).setPort(pork));
    }

    /**
     * Fetches a {@link MinecraftPingReply} for the supplied options.
     *
     * @param options - a filled instance of {@link MinecraftPingOptions}
     * @return {@link MinecraftPingReply}
     * @throws IOException
     */
    public static MinecraftPingReply getPing(final MinecraftPingOptions options) throws IOException {
        MinecraftPingUtil.validate(options.getHostname(), "Hostname cannot be null.");
        MinecraftPingUtil.validate(options.getPort(), "Port cannot be null.");

        final Socket socket = new Socket();
        socket.connect(new InetSocketAddress(options.getHostname(), options.getPort()), options.getTimeout());

        final DataInputStream in = new DataInputStream(socket.getInputStream());
        final DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        //> Handshake

        ByteArrayOutputStream handshake_bytes = new ByteArrayOutputStream();
        DataOutputStream handshake = new DataOutputStream(handshake_bytes);

        handshake.writeByte(MinecraftPingUtil.PACKET_HANDSHAKE);
        MinecraftPingUtil.writeVarInt(handshake, MinecraftPingUtil.PROTOCOL_VERSION);
        MinecraftPingUtil.writeVarInt(handshake, options.getHostname().length());
        handshake.writeBytes(options.getHostname());
        handshake.writeShort(options.getPort());
        MinecraftPingUtil.writeVarInt(handshake, MinecraftPingUtil.STATUS_HANDSHAKE);

        MinecraftPingUtil.writeVarInt(out, handshake_bytes.size());
        out.write(handshake_bytes.toByteArray());

        //> Status request

        out.writeByte(0x01); // Size of packet
        out.writeByte(MinecraftPingUtil.PACKET_STATUSREQUEST);

        //< Status response

        MinecraftPingUtil.readVarInt(in); // Size
        int id = MinecraftPingUtil.readVarInt(in);

        MinecraftPingUtil.io(id == -1, "Server prematurely ended stream.");
        MinecraftPingUtil.io(id != MinecraftPingUtil.PACKET_STATUSREQUEST, "Server returned invalid packet.");

        int length = MinecraftPingUtil.readVarInt(in);
        MinecraftPingUtil.io(length == -1, "Server prematurely ended stream.");
        MinecraftPingUtil.io(length == 0, "Server returned unexpected value.");

        byte[] data = new byte[length];
        in.readFully(data);
        String json = new String(data, options.getCharset());

        //> Ping

        out.writeByte(0x09); // Size of packet
        out.writeByte(MinecraftPingUtil.PACKET_PING);
        out.writeLong(System.currentTimeMillis());

        //< Ping

        MinecraftPingUtil.readVarInt(in); // Size
        id = MinecraftPingUtil.readVarInt(in);
        MinecraftPingUtil.io(id == -1, "Server prematurely ended stream.");
        MinecraftPingUtil.io(id != MinecraftPingUtil.PACKET_PING, "Server returned invalid packet.");

        // Close

        handshake.close();
        handshake_bytes.close();
        out.close();
        in.close();
        socket.close();

        return new Gson().fromJson(json, MinecraftPingReply.class);
    }

}