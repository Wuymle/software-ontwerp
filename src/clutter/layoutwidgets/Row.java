package clutter.layoutwidgets;

import static clutter.core.Dimension.max;
import static clutter.core.Dimension.min;

import clutter.abstractwidgets.FlexibleWidget;
import clutter.abstractwidgets.MultiChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;

public class Row extends MultiChildWidget {

    public Row(Widget... children) {
        super(children);
    }

    @Override
    public void layout(Dimension maxSize) {
        layoutInflexibleWidgets(maxSize);
        int remainingwidth = inflexibleChildren().reduce(maxSize.x(), (width, child) -> width - child.getSize().x());
        layoutFlexibleWidgets(maxSize.withY(remainingwidth));
    }

    @Override
    protected void layoutFlexibleWidgets(Dimension maxSize) {
        int totalFlex = flexibleChildren().reduce(0, (flex, child) -> flex + ((FlexibleWidget) child).getFlex());
        for (Widget child : flexibleChildren()) {
            int maxChildwidth = maxSize.x() * ((FlexibleWidget) child).getFlex() / totalFlex;
            child.layout(maxSize.withY(maxChildwidth));
            size = min(max(size, child.getSize()), maxSize);
            // size = size.withX(Math.min(Math.max(size.y(), child.getSize().y()),
            // maxSize.y()));
        }
    }

    @Override
    protected void layoutInflexibleWidgets(Dimension maxSize) {
        int remainingwidth = maxSize.x();
        for (Widget child : inflexibleChildren()) {
            child.layout(maxSize.withY(remainingwidth));
            size = min(max(size, child.getSize()), maxSize);
            // height = Math.min(Math.max(height, child.getheight()), maxheight);
            remainingwidth = Math.max(0, remainingwidth - child.getSize().x());
        }
    }

    @Override
    protected void positionChildren() {
        int childY = size.x();
        for (Widget child : children) {
            child.setPosition(position.withY(childY));
            childY += child.getSize().x();
        }
    }

}
