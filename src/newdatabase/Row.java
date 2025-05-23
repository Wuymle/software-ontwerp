package newdatabase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Row extends DatabaseObject {

    public interface TableRowChangeListener {
        void onTableRowsChanged();
    }

    private Set<TableRowChangeListener> tableRowsChangeListeners = new HashSet<>();

    private Map<Column, Cell> cells = new HashMap<>();

    Row(History history) {
        super(history);
    }

    public void addTableRowChangeListener(TableRowChangeListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("listener cannot be null");
        tableRowsChangeListeners.add(listener);
    }

    public void removeTableRowChangeListener(TableRowChangeListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("listener cannot be null");
        tableRowsChangeListeners.remove(listener);
    }

    public Cell getCell(Column column) {
        if (column == null)
            throw new IllegalArgumentException("column cannot be null");
        return cells.get(column);
    }

    public boolean allowUpdateCellValue(Column column, String value) {
        if (column == null || !cells.containsKey(column))
            throw new IllegalArgumentException("column does not exist");
        return column.allowCellValue(value);
    }

    public void updateCellValue(Column column, String value) {
        if (column == null)
            throw new IllegalArgumentException("column cannot be null");
        if (!allowUpdateCellValue(column, value))
            throw new IllegalArgumentException("Invalid cell value");
        history.record(
                cells.get(column).updateValue(value).setCallback(() -> tableRowsChangeListeners
                        .forEach(TableRowChangeListener::onTableRowsChanged)));
    }

    Action createCell(Column column) {
        if (column == null)
            throw new IllegalArgumentException("column cannot be null");
        if (cells.keySet().contains(column))
            throw new IllegalArgumentException("Cell already exists");
        final Cell cell = new Cell(column);
        return new Action(() -> {
            cells.remove(column);
        }, () -> {
            cells.put(column, cell);
        });
    }

    Action deleteCell(Column column) {
        if (column == null || !cells.containsKey(column))
            throw new IllegalArgumentException("column does not exist");
        final Cell cell = cells.remove(column);
        return new Action(() -> {
            cells.put(column, cell);
        }, () -> {
            cells.remove(column);
        });

    }
}
