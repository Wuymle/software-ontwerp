package clutter.layoutwidgets;

import java.util.function.BiConsumer;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;

public class OverflowBox extends SingleChildWidget {
    private BiConsumer<Double, Double> onOverflowChange;

    public OverflowBox(Widget child, BiConsumer<Double, Double> onOverflowChange) {
        super(child);
        this.onOverflowChange = onOverflowChange;
    }

    @Override
    protected void runMeasure() {
        child.measure();
        preferredSize = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    protected void runLayout(Dimension minsize, Dimension maxSize) {
        preferredSize = child.getPreferredSize();
        if (preferredSize.x() == Integer.MAX_VALUE || preferredSize.y() == Integer.MAX_VALUE)
            throw new Error("OverflowBox child cannot have unbounded size");
        onOverflowChange.accept((double) preferredSize.x() / maxSize.x(),
                (double) preferredSize.y() / maxSize.y());
        super.runLayout(maxSize, new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }
}
