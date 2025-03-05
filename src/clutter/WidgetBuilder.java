package clutter;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Dimension;

public abstract class WidgetBuilder extends SingleChildWidget {
	protected Context context;
	private boolean firstBuild = false;

	public WidgetBuilder(Context context) {
		super(null);
		this.context = context;
	}

	@Override
	public void layout(Dimension maxSize) {
		if (!firstBuild) {
			child = build(context);
			firstBuild = true;
		}
		super.layout(maxSize);
	}

	public abstract Widget build(Context context);
}
