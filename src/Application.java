import java.awt.Color;
import java.awt.Graphics;

import ui.abstractwidgets.Widget;
import ui.decoratedwidgets.Rectangle;
import ui.layoutwidgets.Column;

public class Application extends Widget {
    private static Widget instance;
    private Widget child;

    private Application() {
        this.child = new Column(
                new Rectangle(100, 100, Color.red),
                new Rectangle(120, 80, Color.blue));

    }

    public static Widget getInstance() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }

    @Override
    public void layout(int maxWidth, int maxHeight) {
        System.out.println("Layout application " + maxWidth + " " + maxHeight);
        child.layout(maxWidth, maxHeight);
    }

    @Override
    public void paint(Graphics g) {
        System.out.println("Paint application");
        child.setPosition(0, 0);
        child.paint(g);
    }

}
