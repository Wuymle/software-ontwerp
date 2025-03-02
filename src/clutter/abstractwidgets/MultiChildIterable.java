package clutter.abstractwidgets;

import java.util.Iterator;
import java.util.function.Predicate;
import java.util.function.BiFunction;

public class MultiChildIterable implements Iterable<Widget> {
    private final Widget[] children;
    private Predicate<Widget> filter;

    public MultiChildIterable(Widget[] children) {
        this.children = children;
    }

    public MultiChildIterable filter(Predicate<Widget> filter) {
        this.filter = filter;
        return this;
    }

    public <T> T reduce(T identity, BiFunction<T, Widget, T> accumulator) {
        T result = identity;
        for (Widget child : this) {
            result = accumulator.apply(result, child);
        }
        return result;
    }

    @Override
    public Iterator<Widget> iterator() {
        return new Iterator<Widget>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                while (index < children.length) {
                    if (filter == null || filter.test(children[index])) {
                        return true;
                    }
                    index++;
                }
                return false;
            }

            @Override
            public Widget next() {
                return children[index++];
            }
        };
    }
}
