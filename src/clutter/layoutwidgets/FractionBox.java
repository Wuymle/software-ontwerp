package clutter.layoutwidgets;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;

public class FractionBox extends SingleChildWidget {
    private double fractionX;
    private double fractionY;

    public FractionBox(Widget child) {
        super(child);
    }

    @Override
    protected void runLayout(Dimension minSize, Dimension maxSize) {
        Dimension fractionSize = maxSize.mulX(1 / fractionX).mulY(1 / fractionY);
        super.runLayout(fractionSize, fractionSize);
    }

    public FractionBox setFractionX(double fractionX) {
        this.fractionX = fractionX;
        return this;
    }

    public FractionBox setFractionY(double fractionY) {
        this.fractionY = fractionY;
        return this;
    }
}
