package clutter.test;

import java.awt.Color;
import clutter.ApplicationWindow;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Decoration;
import clutter.core.Dimension;
import clutter.core.Orientation;
import clutter.debug.DebugMode;
import clutter.layoutwidgets.Grid;
import clutter.layoutwidgets.SizedBox;



public class GridTest {
    public static void main(String[] args) {
        ApplicationWindow window = new ApplicationWindow("Grid Test", context -> {
            int numArrays = 4;
            Widget[] children = new Widget[16];
            for (int i = 0; i < children.length; i++) {
                children[i] = new SizedBox(new Dimension(((i % 4) + 1) * 20, 50 - (i % 4) * 10))
                        .setDecoration(new Decoration().setColor(
                                switch ((i / numArrays) % 2 == 0 && numArrays % 2 == 0 ? i % 2
                                        : 1 - i % 2) {
                                    case 0 -> Color.RED;
                                    case 1 -> Color.BLUE;
                                    default -> throw new IllegalStateException(
                                            "Unexpected value: " + i);
                                }));
            }
            children[0].debug(DebugMode.LAYOUT);
            return new Grid(numArrays, Orientation.VERTICAL, children)
                    .setDecoration(new Decoration().setBorderColor(Color.black))
                    .debug(DebugMode.LAYOUT);
        }, appWindow -> new Context(appWindow));
        java.awt.EventQueue.invokeLater(() -> {
            window.show();
        });
    }
}
