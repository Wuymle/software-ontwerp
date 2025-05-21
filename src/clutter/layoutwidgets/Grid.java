package clutter.layoutwidgets;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.List;
import clutter.abstractwidgets.MultiChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Dimension;
import clutter.core.Orientation;
import clutter.debug.Debug;
import clutter.debug.DebugMode;

public class Grid extends MultiChildWidget {
    private int numRows;
    private int numColumns;
    private Orientation direction;
    private boolean header;

    int[] preferredColumnWidths;
    int[] preferredRowHeights;


    public Grid(int numArrays, Orientation direction, boolean header, List<Widget> children) {
        super(children);
        init(numArrays, children.size(), direction, header);
    }

    public Grid(int numArrays, Orientation direction, boolean header, Widget... children) {
        super(children);
        init(numArrays, children.length, direction, header);
    }

    private int getCol(int index) {
        return direction == Orientation.VERTICAL ? index % numColumns : index / numRows;
    }

    private int getRow(int index) {
        return direction == Orientation.VERTICAL ? index / numColumns : index % numRows;
    }

    @Override
    protected void runPaint(Graphics g) {
        super.runPaint(g);
        g.setColor(Color.lightGray);
        double ratioX = (double) size.x() / preferredSize.x();
        int x = position.x();
        for (int col = 1; col < numColumns; col++) {
            int colWidth = (int) (preferredColumnWidths[col - 1] * ratioX);
            x += colWidth;
            g.drawLine(x, position.y(), x, position.y() + size.y());
        }
        g.setColor(getDecoration().getBorderColor());
        if (header && numRows > 0) {
            double ratioY = (double) size.y() / preferredSize.y();
            int headerHeight = (int) (preferredRowHeights[0] * ratioY);
            int y = position.y() + headerHeight;
            g.drawLine(position.x(), y, position.x() + size.x(), y);
        }
    }

    @Override
    protected void positionChildren() {
        int yOffset = 0;
        int xOffset = 0;
        for (int i = 0; i < children.size(); i++) {
            int row = getRow(i);
            int col = getCol(i);
            double ratioX = (double) size.x() / preferredSize.x();
            double ratioY = (double) size.y() / preferredSize.y();
            yOffset = Arrays.stream(preferredRowHeights).limit(row).map(h -> (int) (h * ratioY))
                    .sum();
            xOffset = Arrays.stream(preferredColumnWidths).limit(col).map(w -> (int) (w * ratioX))
                    .sum();
            children.get(i).setPosition(position.add(new Dimension(xOffset, yOffset)));
        }
    }

    @Override
    protected void runMeasure() {
        for (int i = 0; i < children.size(); i++) {
            int row = getRow(i);
            int col = getCol(i);
            children.get(i).measure();
            Dimension childPreferredSize = children.get(i).getPreferredSize();
            preferredColumnWidths[col] =
                    Math.max(preferredColumnWidths[col], childPreferredSize.x());
            preferredRowHeights[row] = Math.max(preferredRowHeights[row], childPreferredSize.y());
        }
        preferredSize = new Dimension(Arrays.stream(preferredColumnWidths).sum(),
                Arrays.stream(preferredRowHeights).sum());
    }

    @Override
    protected void runLayout(Dimension minSize, Dimension maxSize) {
        size = Dimension.max(minSize, Dimension.min(maxSize, preferredSize));
        double ratioX = (double) size.x() / preferredSize.x();
        double ratioY = (double) size.y() / preferredSize.y();

        for (int i = 0; i < children.size(); i++) {
            Debug.log(this, DebugMode.LAYOUT, "ratioX:", ratioX, "ratioY:", ratioY);
            Dimension childSize = new Dimension((int) (ratioX * preferredColumnWidths[getCol(i)]),
                            (int) (ratioY * preferredRowHeights[getRow(i)]));
            children.get(i).layout(childSize, childSize);
        }
    }

    private void init(int numArrays, int size, Orientation direction, boolean header) {
        this.direction = direction;
        this.header = header;
        if (direction == Orientation.HORIZONTAL) {
            numRows = numArrays;
            numColumns = 1 + (size / numArrays);
        } else {
            numColumns = numArrays;
            numRows = 1 + (size / numArrays);
        }
        preferredColumnWidths = new int[numColumns];
        preferredRowHeights = new int[numRows];
    }
}
