package clutter.test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import clutter.ApplicationWindow;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.decoratedwidgets.Text;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.ConstrainedBox;
import clutter.layoutwidgets.ScrollableView;

public class ScrollableViewTest {
    public static void main(String[] args) {
        ApplicationWindow window = new ApplicationWindow("Test Window",
                context -> {
                    List<Widget> children = new ArrayList<Widget>();
                    children.add(new Text("Header"));
                    children.add(new Text("Subheader"));
                    IntStream.range(0, 100).forEach(i -> {
                        children.add(new Text("Item " + i));
                    });
                    children.add(new Text("Footer"));

                    return new ScrollableView(context, new ConstrainedBox(new Column(children)).setMinWidth(1500));
                },
                appWindow -> new Context(appWindow));
        java.awt.EventQueue.invokeLater(() -> {
            window.show();
        });
    }
}