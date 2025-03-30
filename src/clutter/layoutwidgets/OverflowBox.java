package clutter.layoutwidgets;

import java.util.function.BiConsumer;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;

public class OverflowBox extends SingleChildWidget {
    private BiConsumer<Double, Double> onOverflowChange;
    private Double relHorizontalOverflow = 1.0;
    private Double relVerticalOverflow = 1.0;

    public OverflowBox(Widget child, BiConsumer<Double, Double> onOverflowChange) {
        super(child);
        this.onOverflowChange = onOverflowChange;
    }

    @Override
    public void layout(Dimension minsize, Dimension maxSize) {
        Double newRelHorizontalOverflow = Math.max(1.0, ((double) preferredSize.x()) / maxSize.x());
        Double newRelVerticalOverflow = Math.max(1.0, (double) preferredSize.y() / maxSize.y());
        if (newRelHorizontalOverflow != relHorizontalOverflow || newRelVerticalOverflow != relVerticalOverflow) {
            relHorizontalOverflow = newRelHorizontalOverflow;
            relVerticalOverflow = newRelVerticalOverflow;
            onOverflowChange.accept(relHorizontalOverflow, relVerticalOverflow);
        }
        super.layout(minsize, new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }
}
