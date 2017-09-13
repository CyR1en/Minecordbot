package us.cyrien.minecordbot.commands.discordCommand;

import us.cyrien.jdautilities.commandclient.CommandEvent;
import us.cyrien.minecordbot.Minecordbot;
import us.cyrien.minecordbot.commands.MCBCommand;
import us.cyrien.minecordbot.entity.AnimatedGifEncoder;
import us.cyrien.minecordbot.localization.Locale;
import us.cyrien.minecordbot.utils.WrapUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class SpoilerCmd extends MCBCommand {

    private final Color DARKTHEME = new Color(54, 57, 62);
    private final Color HOVERCOLOR = new Color(155, 155, 155);
    private final Color TEXTCOLOR = Color.WHITE;
    private final Font FONT = new Font("Arial", Font.PLAIN, 15);
    private final int BUFFER = 3;

    public SpoilerCmd(Minecordbot minecordbot) {
        super(minecordbot);
        this.name = "spoiler";
        this.help = Locale.getCommandsMessage("spoiler.description").finish();
        this.arguments = "<spoiler text>";
        this.category = minecordbot.ADMIN;
        this.type = Type.EMBED;
    }

    @Override
    protected void doCommand(CommandEvent event) {
        String input = event.getArgs();
        if (input == null || input.isEmpty()) {
            respond(event, Locale.getCommandsMessage("spoiler.includeText").finish());
            return;
        }
        try {
            Canvas c = new Canvas();
            List<String> lines = WrapUtil.wrap(input.replace("\n", " "), c.getFontMetrics(FONT), 400 - (2 * BUFFER) - 2);
            if (lines.size() > 8) {
                respond(event, Locale.getCommandsMessage("spoiler.tooManyLines").finish());
                return;
            }
            BufferedImage text = new BufferedImage(400, lines.size() * (FONT.getSize() + BUFFER) + 2 * BUFFER + 2, BufferedImage.TYPE_INT_RGB);
            BufferedImage hover = new BufferedImage(400, lines.size() * (FONT.getSize() + BUFFER) + 2 * BUFFER + 2, BufferedImage.TYPE_INT_RGB);
            Graphics2D textG = text.createGraphics();
            Graphics2D hoverG = hover.createGraphics();
            textG.setFont(FONT);
            hoverG.setFont(FONT);
            textG.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            hoverG.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            textG.setColor(DARKTHEME);
            hoverG.setColor(DARKTHEME);
            textG.fillRect(1, 1, text.getWidth() - 2, text.getHeight() - 2);
            hoverG.fillRect(1, 1, text.getWidth() - 2, text.getHeight() - 2);
            textG.setColor(TEXTCOLOR);
            hoverG.setColor(HOVERCOLOR);
            for (int i = 0; i < lines.size(); i++)
                textG.drawString(lines.get(i), BUFFER + 1, (i + 1) * (FONT.getSize() + BUFFER) - 1);//text.getHeight()-BUFFER);
            hoverG.drawString(Locale.getCommandsMessage("spoiler.spoilerPreview").finish(), BUFFER + 1, FONT.getSize() + BUFFER - 1);
            //ImageIO.write(text,"png", new File("text.png"));
            //ImageIO.write(hover,"png", new File("hover.png"));
            AnimatedGifEncoder e = new AnimatedGifEncoder();
            e.setRepeat(0);
            e.start("spoiler.gif");
            e.setDelay(1);
            e.addFrame(hover);
            e.setDelay(60000);
            e.addFrame(text);
            e.setDelay(60000);
            e.finish();
            event.getChannel().sendFile(new File("spoiler.gif"), null).queue();
            textG.dispose();
            hoverG.dispose();
        } catch (Exception e) {
            respond(event, Locale.getCommandsMessage("spoiler.failedToGenerate").finish());
            //e.printStackTrace();
        }
    }

}
