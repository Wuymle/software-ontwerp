package clutter.abstractwidgets;

public abstract class FlexibleWidget extends SingleChildWidget {
    private int flex;

    protected FlexibleWidget(Widget child, int flex) {
        super(child);
        this.flex = flex;
    }

    public int getFlex() {
        return flex;
    }

    public FlexibleWidget setFlex(int flex) {
        this.flex = flex;
        return this;
    }

    public void layout(int maxWidth, int maxHeight) {
        super.layout(width, height);
        if (maxWidth == 0 | maxHeight == 0)
            System.err.println("WARNING: FLEXIBLE HAS SIZE 0");
        width = maxWidth;
        height = maxHeight;
    }
}
