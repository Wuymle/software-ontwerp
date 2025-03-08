package clutter;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Dimension;

public abstract class WidgetBuilder<C extends Context> extends SingleChildWidget {
	protected C context;
	private boolean firstBuild = false;

	public WidgetBuilder(C context) {
		super(null);
		this.context = context;
	}

	@Override
	public void layout(Dimension maxSize) {
		if (!firstBuild) {
			child = build();
			firstBuild = true;
		}
		super.layout(maxSize);
	}

	public abstract Widget build();
}
