package newdatabase;

import java.util.HashSet;
import java.util.Set;

public class Action {
    static final Action NONE = new Action(() -> {
    }, () -> {
    });


    private final Runnable undo;
    private final Runnable redo;
    private Set<Runnable> callback = new HashSet<>();

    public Action(Runnable undo, Runnable redo) {
        this.undo = undo;
        this.redo = redo;
    }

    public void undo() {
        undo.run();
        callback.forEach(Runnable::run);
    }

    public void redo() {
        redo.run();
        callback.forEach(Runnable::run);
    }

    public Action setCallback(Runnable callback) {
        this.callback.add(callback);
        return this;
    }
}

