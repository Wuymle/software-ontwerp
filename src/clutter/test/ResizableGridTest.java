package clutter.test;

import java.awt.Color;
import clutter.ApplicationWindow;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Decoration;
import clutter.core.Dimension;
import clutter.core.ResizableGridController;
import clutter.layoutwidgets.ResizableGrid;
import clutter.layoutwidgets.SizedBox;



public class ResizableGridTest {
    public static void main(String[] args) {
        ApplicationWindow window = new ApplicationWindow("ResizableGrid Test", context -> {
            int numArrays = 5;
            Widget[] children = new Widget[9];
            for (int i = 0; i < children.length; i++) {
                children[i] = new SizedBox(new Dimension(15 + (i % 3) * 15, 50 - (i % 4) * 10))
                        .setDecoration(new Decoration().setColor(
                                switch ((i / numArrays) % 2 == 0 && numArrays % 2 == 0 ? i % 2
                                        : 1 - i % 2) {
                                    case 0 -> Color.RED;
                                    case 1 -> Color.BLUE;
                                    default -> throw new IllegalStateException(
                                            "Unexpected value: " + i);
                                }));
            }
            return new ResizableGrid(context, new ResizableGridController(context, numArrays, 5), children)
                    .setDecoration(new Decoration().setBorderColor(Color.black));
        }, appWindow -> new Context(appWindow));
        java.awt.EventQueue.invokeLater(() -> {
            window.show();
        });
    }
}
