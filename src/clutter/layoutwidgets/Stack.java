package clutter.layoutwidgets;

import java.util.List;
import clutter.abstractwidgets.MultiChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;


public class Stack extends MultiChildWidget {


    public Stack(Widget... children) {
        super(children);
    }

    public Stack(List<Widget> children) {
        super(children);
    }

    @Override
    public void runMeasure() {
        preferredSize = new Dimension(0, 0);
        for (Widget child : children) {
            child.measure();
            preferredSize = Dimension.max(preferredSize, child.getPreferredSize());
        }
    }

    @Override
    protected void runLayout(Dimension minSize, Dimension maxSize) {
        super.runLayout(minSize, maxSize);
        children.forEach(child -> child.layout(minSize, maxSize));
    }

    @Override
    protected void positionChildren() {
        children.forEach(child -> child.setPosition(position));
    }
}
