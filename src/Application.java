import java.awt.Color;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.decoratedwidgets.DecoratedBox;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.Flexible;

public class Application extends SingleChildWidget {

    public Application() {
        super(new Column(
                new Flexible(new DecoratedBox(null).setColor(Color.red), 1),
                new Flexible(new DecoratedBox(null).setColor(Color.blue), 1)));
        setPosition(0, 0);
    }

    @Override
    public void layout(int maxWidth, int maxHeight) {
        child.layout(maxWidth, maxHeight);
    }
}
