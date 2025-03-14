package clutter;

import static clutter.core.Dimension.max;
import static clutter.core.Dimension.min;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Debug;
import clutter.core.Dimension;

public abstract class WidgetBuilder<C extends Context> extends SingleChildWidget {
	protected C context;
	protected boolean firstBuild = false;

	public WidgetBuilder(C context) {
		super(null);
		this.context = context;
	}

	@Override
	public void measure() {
		if (!firstBuild) {
			child = build();
			firstBuild = true;
		}
		super.measure();
	}

	@Override
	public void layout(Dimension minSize, Dimension maxSize) {
		Debug.log(this, "minSize:", minSize, "maxSize:", maxSize);
		size = min(maxSize, max(minSize, preferredSize));
		if (child != null)
			child.layout(minSize, maxSize);
		Debug.log(this, "Chosen size:", size);
	}

	public abstract Widget build();
}
