import java.awt.Color;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.decoratedwidgets.DecoratedBox;

public class Application extends SingleChildWidget {
    private Widget child;

    public Application() {
        super(new DecoratedBox(null).setColor(Color.red));
        setPosition(0, 0);
    }

    @Override
    public void layout(int maxWidth, int maxHeight) {
        child.layout(maxWidth, maxHeight);
    }
}
