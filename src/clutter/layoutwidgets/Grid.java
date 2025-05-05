package clutter.layoutwidgets;

import java.util.List;
import clutter.abstractwidgets.MultiChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.core.Direction;

public class Grid extends MultiChildWidget {
    private int numArrays;
    private Direction direction;

    public Grid(int numArrays, Direction direction, List<Widget> children) {
        super(children);
        this.numArrays = numArrays;
        this.direction = direction;
    }

    public Grid(int numArrays, Direction direction, Widget... children) {
        super(children);
        this.numArrays = numArrays;
        this.direction = direction;
    }

    @Override
    protected void positionChildren() {
        throw new UnsupportedOperationException("Unimplemented method 'positionChildren'");
    }

    @Override
    protected void runMeasure() {
    }

    @Override
    protected void runLayout(Dimension minSize, Dimension maxSize) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'runLayout'");
    }
}