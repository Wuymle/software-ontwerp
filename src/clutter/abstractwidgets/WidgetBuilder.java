package clutter.abstractwidgets;

import static clutter.core.Dimension.max;
import static clutter.core.Dimension.min;

import clutter.core.Context;
import clutter.core.Dimension;
import clutter.debug.Debug;
import clutter.debug.DebugMode;

/**
 * A widget builder that can have only one child widget.
 */
public abstract class WidgetBuilder<C extends Context> extends SingleChildWidget {
	protected C context;
	protected boolean requireBuild = true;

	/**
	 * Constructor for the single child widget.
	 * 
	 * @param child the child widget
	 */
	public WidgetBuilder(C context) {
		this.context = context;
	}

	/**
	 * measure the widget
	 */
	@Override
	protected void runMeasure() {
		if (requireBuild) {
			Debug.log(this, DebugMode.BUILD, () -> child = build());
			requireBuild = false;
		}
		super.runMeasure();
	}

	/**
	 * layout the widget
	 * 
	 * @param minSize the minimum size
	 * @param maxSize the maximum size
	 */
	@Override
	protected void runLayout(Dimension minSize, Dimension maxSize) {
		size = min(maxSize, max(minSize, preferredSize));
		child.layout(size, size);
	}

	public abstract Widget build();
}
