package database;

public abstract class DatabaseObject {
    protected History history;

    DatabaseObject(History history) {
        if (history == null)
            throw new IllegalArgumentException("History cannot be null");
        this.history = history;
    }
}
