package database;

import java.util.Stack;

public class History {
    private final Stack<Action> undoStack = new Stack<>();
    private final Stack<Action> redoStack = new Stack<>();

    public void record(Action action) {
        undoStack.push(action);
        redoStack.clear(); // Invalidate redo history
    }

    public void undo() {
        if (undoStack.isEmpty()) return;
        Action action = undoStack.pop();
        action.undo();
        redoStack.push(action);
    }

    public void redo() {
        if (redoStack.isEmpty()) return;
        Action action = redoStack.pop();
        action.redo();
        undoStack.push(action);
    }

    public boolean canUndo() {
        System.out.println("Can undo: " + undoStack);
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
}