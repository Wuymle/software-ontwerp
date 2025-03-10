package clutter;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;

public abstract class WidgetBuilder extends SingleChildWidget {
	protected Context context;
	private boolean firstBuild = false;

	public WidgetBuilder(Context context) {
		super(null);
		this.context = context;
	}

	@Override
	public void measure() {
		if (!firstBuild) {
			child = build(context);
			firstBuild = true;
		}
		super.measure();
	}

	public abstract Widget build(Context context);
}
