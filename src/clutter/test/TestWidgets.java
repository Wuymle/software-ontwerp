package clutter.test;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import clutter.abstractwidgets.Screen;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Decoration;
import clutter.core.WindowController;
import clutter.decoratedwidgets.Text;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.ConstrainedBox;
import clutter.layoutwidgets.ScrollableView;
import clutter.layoutwidgets.SubWindow;

public class TestWidgets {
    public static Widget ScrollableViewTestWidget(Context context) {
        List<Widget> children = new ArrayList<Widget>();
        children.add(new Text("Header"));
        children.add(new Text("Subheader"));
        IntStream.range(0, 100).forEach(i -> {
            children.add(new ConstrainedBox(new Text("Item " + i))
                    .setDecoration(new Decoration().setBorderColor(Color.black)));
        });
        children.add(new Text("Footer"));

        return new ScrollableView(context,
                new ConstrainedBox(new Column(children)).setMinWidth(1500));
    }

    public static SubWindow SubWindowTestWidget(Context context, WindowController controller) {
        return new SubWindow(context, "TopWindow Test", controller,
                TestScreen(context, ScrollableViewTestWidget(context)));
    }

    public static Screen<?> TestScreen(Context context, Widget content) {
        return new Screen<Context>(context) {
            @Override
            public Widget build() {
                return content;
            }

            @Override
            public void onGetFocus() {}

            @Override
            public void onLoseFocus() {}
        };
    }

}
