package clutter.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResizableGridController extends DragController {
    private int[] colWidths;
    private int currentColIndex = 0;
    private int minColWidth = 0;

    private List<ResizableGridSubscriber> subscribers = new ArrayList<>();

    public interface ResizableGridSubscriber {
        void onColumnResize();
    }

    public void addSubscriber(ResizableGridSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public int getArrayCount() {
        return colWidths.length;
    }


    public ResizableGridController(Context context, int columnCount, int minColWidth) {
        super(context);
        colWidths = new int[columnCount];
        this.minColWidth = minColWidth;
        Arrays.fill(colWidths, 100);
    }

    @Override
    protected void updateDragging() {
        colWidths[currentColIndex] =
                Math.max(minColWidth, colWidths[currentColIndex] + (dragPosition.x() - startPosition.x()));
        startPosition = dragPosition;
        subscribers.forEach(ResizableGridSubscriber::onColumnResize);
    }

    public void startDragging(Dimension startPosition, int colIndex) {
        if (colIndex < 0 || colIndex >= colWidths.length)
            throw new IllegalArgumentException("Invalid column index: " + colIndex);
        currentColIndex = colIndex;
        super.startDragging(startPosition);
    }

    public int getColWidth(int colIndex) {
        if (colIndex < 0 || colIndex >= colWidths.length)
            throw new IllegalArgumentException("Invalid column index: " + colIndex);
        return colWidths[colIndex];
    }
}
