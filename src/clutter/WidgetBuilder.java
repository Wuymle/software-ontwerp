package clutter;

import clutter.abstractwidgets.SingleChildWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;

public abstract class WidgetBuilder extends SingleChildWidget {
	protected Context context;

	public WidgetBuilder(Context context) {
		super(null);
		this.context = context;
		this.child = build(this.context);
	}

	public abstract Widget build(Context context);
}
