package application.resources;

import java.awt.Color;
import clutter.abstractwidgets.Widget;
import clutter.core.Decoration;
import clutter.decoratedwidgets.Text;
import clutter.layoutwidgets.Padding;

public class Style {
        public static Color white = new Color(255, 255, 255);
        public static Color lightGray = new Color(245, 245, 245);
        public static Color lessLightGray = new Color(215, 215, 215);
        public static Color strokeTable = new Color(180, 180, 180);

        public static Color mainColor = new Color(90, 100, 110);

        // accentColor is a lighter shade of mainColor
        public static Color accentColor = new Color(
            Math.min(mainColor.getRed() + 60, 255),
            Math.min(mainColor.getGreen() + 60, 255),
            Math.min(mainColor.getBlue() + 60, 255)
        );

        // secondaryColor is a variation on mainColor
        public static Color secondaryColor = new Color(
            mainColor.getRed(),
            mainColor.getGreen(),
            Math.min(mainColor.getBlue() + 40, 255)
        );

        public static Color complementaryColor = new Color(
                255 - mainColor.getRed(),
                255 - mainColor.getGreen(),
                255 - mainColor.getBlue()
        );

        public static Color backgroundColor = white;

        public static Color lesserBlue = new Color(150, 150, 255);
        
        public static Color strokeInput = new Color(70, 130, 180);
        public static Color inputBackground = new Color(250, 250, 250);
        public static Color headerYellow = new Color(255, 204, 0);

        public static Decoration decorationText = new Decoration()
                .setBorderWidth(2).setColor(white)
                .setBorderRadius(10);

        public static Decoration decorationInput = new Decoration()
                .setBorderColor(accentColor)
                .setBorderWidth(2).setColor(white)
                .setBorderRadius(15);

        public static Decoration decorationPanel = new Decoration().setBorderRadius(0).setColor(lightGray)
                                .setBorderColor(secondaryColor).setBorderWidth(10);

        public static Decoration decorationPanel2 = new Decoration().setBorderRadius(0)
                                                .setColor(lightGray)
                                                .setBorderColor(white).setBorderWidth(5);

        public static Decoration decorationHeader = new Decoration().setBorderRadius(0)
                                                .setColor(secondaryColor)
                                                .setBorderColor(secondaryColor).setBorderWidth(5);

        public static Decoration background = new Decoration().setColor(backgroundColor);

}
