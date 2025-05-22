package application.resources;

import java.awt.Color;
import clutter.core.Decoration;

public class Style {
    public static Color white = new Color(255, 255, 255);
    public static Color lightGray = new Color(245, 245, 245);
    public static Color lessLightGray = new Color(215, 215, 215);
    public static Color strokeTable = new Color(180, 180, 180);
    public static Color headerBlue = Color.blue;
    public static Color lesserBlue = new Color(150, 150, 255);
    public static Color inputBlue = new Color(70, 130, 180);
    public static Color strokeInput = new Color(70, 130, 180);
    public static Color inputBackground = new Color(250, 250, 250);
    public static Color headerYellow = new Color(255, 204, 0);

    public static Decoration decorationText = new Decoration()
            .setBorderWidth(2).setColor(new Color(255, 255, 255))
            .setBorderRadius(10);

    public static Decoration decorationInput = new Decoration()
            .setBorderColor(inputBlue)
            .setBorderWidth(2).setColor(new Color(255, 255, 255))
            .setBorderRadius(15);

    public static Decoration decorationPanel = new Decoration().setBorderRadius(0).setColor(lightGray)
                        .setBorderColor(headerBlue).setBorderWidth(10);

    public static Decoration decorationPanel2 = new Decoration().setBorderRadius(0)
                                        .setColor(lightGray)
                                        .setBorderColor(lessLightGray).setBorderWidth(5);

    public static Decoration decorationHeader = new Decoration().setBorderRadius(0)
                                        .setColor(headerBlue)
                                        .setBorderColor(headerBlue).setBorderWidth(5);
}
