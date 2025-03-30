package clutter.layoutwidgets;

import java.awt.Color;

import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Dimension;
import clutter.core.Direction;
import clutter.core.ScrollController;
import clutter.decoratedwidgets.Clip;
import clutter.decoratedwidgets.DecoratedBox;
import clutter.inputwidgets.Scrollbar;
import clutter.layoutwidgets.enums.Alignment;
import clutter.widgetinterfaces.ScrollSubscriber;

public class ScrollableView extends StatefulWidget<Context> implements ScrollSubscriber {
    private final ScrollController scrollController = new ScrollController(context);
    private Widget content;
    private double scrollX = 0;
    private double scrollY = 0;
    private final int scrollbarWidth = 25;

    public ScrollableView(Context context, Widget content) {
        super(context);
        this.content = content;
        scrollController.addSubscriber(this);
    }

    @Override
    public Widget build() {
        return new Row(
                new Flexible(
                        new Column(
                                new Flexible(
                                        new Clip(
                                                new ScrollBox(
                                                        new OverflowBox(content,
                                                                (Double relContentWidth,
                                                                        Double relContentHeight) -> {
                                                                    scrollController
                                                                            .setRelContentWidth(
                                                                                    relContentWidth);
                                                                    scrollController
                                                                            .setRelContentHeight(
                                                                                    relContentHeight);
                                                                }),
                                                        scrollX,
                                                        scrollY))),
                                new ConstrainedBox(
                                        new ScrollBox(
                                                new Scrollbar(
                                                        context,
                                                        new DecoratedBox(
                                                                new Expanded(null))
                                                                .setColor(Color.red),
                                                        scrollController,
                                                        Direction.HORIZONTAL),
                                                scrollX, 0))
                                        .setHeight(scrollbarWidth)))
                        .setHorizontalAlignment(Alignment.STRETCH),
                new Column(
                        new Flexible(
                                new ConstrainedBox(
                                        new ScrollBox(
                                                new Scrollbar(
                                                        context,
                                                        new DecoratedBox(
                                                                new Expanded(null))
                                                                .setColor(Color.red),
                                                        scrollController,
                                                        Direction.VERTICAL),
                                                0, scrollY))
                                        .setWidth(scrollbarWidth))
                                .setVerticalAlignment(Alignment.STRETCH),
                        new SizedBox(null, new Dimension(scrollbarWidth, scrollbarWidth))));
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
