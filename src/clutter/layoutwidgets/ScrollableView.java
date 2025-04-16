package clutter.layoutwidgets;

import java.awt.Color;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Decoration;
import clutter.core.Dimension;
import clutter.core.Direction;
import clutter.core.ScrollController;
import clutter.core.ScrollController.ScrollSubscriber;
import clutter.decoratedwidgets.Clip;
import clutter.decoratedwidgets.Icon;
import clutter.inputwidgets.Clickable;
import clutter.inputwidgets.Scrollbar;
import clutter.layoutwidgets.enums.Alignment;
import clutter.resources.Icons;

public class ScrollableView extends StatefulWidget<Context> implements ScrollSubscriber {
	private final ScrollController scrollController = new ScrollController(context);
	private Widget content;
	private Widget remainingSpaceWidget = new NullWidget();
	private double scrollX = 0;
	private double scrollY = 0;
	private final int scrollbarWidth = 25;

	public ScrollableView(Context context, Widget content, Widget remainingSpaceWidget) {
		super(context);
		this.content = content;
		this.remainingSpaceWidget = remainingSpaceWidget;
		scrollController.addSubscriber(this);
	}

	public ScrollableView(Context context, Widget content) {
		super(context);
		this.content = content;
		scrollController.addSubscriber(this);
	}

	@Override
	public Widget build() {
		return new Row(new Flexible(new Column(new Flexible(new Column(new Clip(new ScrollBox(
				new OverflowBox(content, (Double relContentWidth, Double relContentHeight) -> {
					scrollController.setRelContentWidth(relContentWidth);
					scrollController.setRelContentHeight(relContentHeight);
				}), scrollX, scrollY)), new Flexible(remainingSpaceWidget))),
				(scrollController.getRelContentWidth() > 1) ? new ConstrainedBox(new Row(
						new Clickable(new Icon(Icons.CARET_LEFT).setFontSize(
								scrollbarWidth * 3 / 4),
								() -> scrollController.scrollHorizontalPages(-1), 1),
						new Flexible(new Padding(new ScrollBox(new Scrollbar(context,
								new Expanded().setDecoration(new Decoration()
										.setColor(Color.gray).setBorderRadius(scrollbarWidth / 3)),
								scrollController, Direction.HORIZONTAL), scrollX, 0))
										.vertical(scrollbarWidth / 3)),
						new Clickable(
								new Icon(Icons.CARET_RIGHT).setFontSize(scrollbarWidth * 3 / 4),
								() -> scrollController.scrollHorizontalPages(1), 1))
										.setCrossAxisAlignment(Alignment.CENTER)
										.setDecoration(new Decoration().setColor(Color.lightGray)))
												.setHeight(scrollbarWidth)
						: new NullWidget())).setHorizontalAlignment(Alignment.STRETCH),
				(scrollController.getRelContentHeight() > 1) ? new Column(
						new Flexible(new ConstrainedBox(new Column(
								new Clickable(
										new Icon(Icons.CARET_UP).setFontSize(scrollbarWidth * 3
												/ 4),
										() -> scrollController.scrollVerticalPages(-1), 1),
								new Flexible(
										new Padding(new ScrollBox(new Scrollbar(context,
												new Expanded().setDecoration(new Decoration()
														.setColor(Color.gray)
														.setBorderRadius(scrollbarWidth / 3)),
												scrollController, Direction.VERTICAL), 0, scrollY))
														.horizontal(scrollbarWidth / 3)),
								new Clickable(
										new Icon(Icons.CARET_DOWN)
												.setFontSize(scrollbarWidth * 3 / 4),
										() -> scrollController.scrollVerticalPages(1), 1))
												.setCrossAxisAlignment(Alignment.CENTER)
												.setDecoration(
														new Decoration().setColor(Color.lightGray)))
																.setWidth(scrollbarWidth))
																		.setVerticalAlignment(
																				Alignment.STRETCH),
						(scrollController.getRelContentWidth() > 1)
								? new SizedBox(Dimension.square(scrollbarWidth))
										.setDecoration(new Decoration().setColor(Color.lightGray))
								: new NullWidget())
						: new NullWidget());
	}

	@Override
	public void onHorizontalScroll(double scrollX) {
		setState(() -> {
			this.scrollX = scrollX;
		});
	}

	@Override
	public void onVerticalScroll(double scrollY) {
		setState(() -> {
			this.scrollY = scrollY;
		});
	}
}
