package database;


public class Action {
    private final Runnable undo;
    private final Runnable redo;

    public Action(Runnable undo, Runnable redo) {
        this.undo = undo;
        this.redo = redo;
    }

    public void undo() {
        undo.run();
    }

    public void redo() {
        redo.run();
    }
}

