package us.cyrien.minecordbot.utils;

import net.dv8tion.jda.core.entities.Icon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

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
        try {
            URL u = new URL(url);
            URLConnection uC = u.openConnection();
            uC.addRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
            return Icon.from(u.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

