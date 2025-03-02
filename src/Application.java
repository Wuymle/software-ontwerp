import java.awt.Color;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.decoratedwidgets.DecoratedBox;
import clutter.inputwidgets.Clickable;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.Flexible;
import clutter.layoutwidgets.Row;

public class Application extends SingleChildWidget {

    public Application() {
        super(new Row(
                new Flexible(
                        new Column(
                                new Flexible(new Clickable(new DecoratedBox(null).setColor(Color.red), () -> {
                                    System.out.println("RED BUTTON PRESSED");
                                }), 1),
                                new DecoratedBox(null).setColor(Color.blue)),
                        1),
                new Flexible(
                        new Column(
                                new DecoratedBox(null).setColor(Color.green),
                                new DecoratedBox(null).setColor(Color.yellow)),
                        1)));
        setPosition(0, 0);
    }

    @Override
    public void layout(int maxWidth, int maxHeight) {
        child.layout(maxWidth, maxHeight);
    }
}
