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

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MessageUtils {
    /**
     * Sends a message to a channel
     *
     * @param s
     * @param channel
     */
    public static void sendMessage(String s, TextChannel channel) {
        try {
            Thread.sleep(500);
            channel.sendMessage(s).queue();
        } catch (PermissionException | InterruptedException e) {
            //we can't do anything about it
        }
    }

    /**
     * Sends a message to a channel
     *
     * @param builder
     * @param channel
     */
    public static void sendMessage(EmbedBuilder builder, TextChannel channel) {
        try {
            Thread.sleep(500);
            channel.sendMessage(builder.build()).queue();
        } catch (PermissionException e) {
            //we can't do anything about it
            if (e.getPermission() == Permission.MESSAGE_EMBED_LINKS) {
                channel.sendMessage("Lacking permission to embed links!").queue();
            }
        } catch (InterruptedException e) {
            //wtf java
        }
    }

    /**
     * Sends an image with the embed
     *
     * @param builder
     * @param image
     * @param name
     * @param channel
     */
    public static void sendImage(EmbedBuilder builder, byte[] image, String name, TextChannel channel) {
        try {
            channel.sendFile(image, name, new MessageBuilder().setEmbed(builder.build()).build()).queue();
        } catch (PermissionException e) {
            //we can't do anything about it
            if (e.getPermission() == Permission.MESSAGE_EMBED_LINKS) {
                channel.sendMessage("Lacking permission to embed links!").queue();
            }
        }
    }

    /**
     * Sends an excepion using the given MessageReceivedEvent's channel
     *
     * @param e   the exception to print
     * @param evt the channel from this event is used to send the message
     */
    public static void sendException(Exception e, MessageReceivedEvent evt) {
        e.printStackTrace();
        sendMessage("Error running command: `" + evt.getMessage().getRawContent() + "`:\n`" + e.getClass().getCanonicalName() + "`", evt.getTextChannel());
    }

    /**
     * Downloads an image
     *
     * @param address
     * @return
     */
    public static byte[] downloadImage(String address) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = null;
        URL url = null;
        try {
            url = new URL(address);
            is = url.openStream();
            byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
            int n;

            while ((n = is.read(byteChunk)) > 0) {
                baos.write(byteChunk, 0, n);
            }
        } catch (IOException e) {
            System.err.printf("Failed while reading bytes from %s: %s", url.toExternalForm(), e.getMessage());
            e.printStackTrace();
            // Perform any other exception handling that's appropriate.
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {

            }
        }

        return baos.toByteArray();
    }
}
