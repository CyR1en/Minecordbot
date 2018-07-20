package us.cyrien.minecordbot.utils;

import net.dv8tion.jda.core.entities.Icon;
import us.cyrien.mcutils.logger.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageUtil {

    public static BufferedImage loadBufferedImage(String s) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(ImageUtil.class.getResourceAsStream(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    public static Icon getIcon(String url) {
        if (url == null)
            return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = null;
        URL u = null;
        try {
            u = new URL(url);
            is = u.openStream();
            byte[] byteChunk = new byte[4096];
            int n;
            while ((n = is.read(byteChunk)) > 0) {
                baos.write(byteChunk, 0, n);
            }
            return Icon.from(baos.toByteArray());
        } catch (IOException e) {
            Logger.err(String.format("Failed while reading bytes from %s: %s", u.toExternalForm(), e.getMessage()));
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}

