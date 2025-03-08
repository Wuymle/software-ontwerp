package clutter.decoratedwidgets;

import java.awt.Font;
import java.io.InputStream;

import clutter.core.Dimension;

public class Icon extends Text {

    public Icon(String icon) {
        super(icon);
        Font customFont = null;
        try (InputStream fontStream = getClass().getResourceAsStream("/assets/uicons-bold-straight.ttf")) {
            if (fontStream == null) {
                throw new RuntimeException("Font file not found");
            }
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            customFont = customFont.deriveFont(24f); // Set desired size
        } catch (Exception e) {
            e.printStackTrace();
        }
        // InputStream fontStream =
        // getClass().getResourceAsStream("/assets/uicons-bold-straight.ttf");
        this.setFont(customFont);
    }

    @Override
    protected Dimension getTextDimensions() {
        return new Dimension(metrics.stringWidth(text), metrics.getAscent());
    }

}
