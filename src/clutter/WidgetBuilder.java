package clutter;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;

public abstract class WidgetBuilder<C extends Context> extends SingleChildWidget {
	protected C context;
	private boolean firstBuild = false;

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

	public abstract Widget build();
}
