package newdatabase;

import java.util.HashMap;
import java.util.Map;

public class Row extends DatabaseObject {
    private Map<Column, Cell> cells = new HashMap<>();

    Row(History history) {
        super(history);
    }

    public Cell getCell(Column column) {
        if (column == null)
            throw new IllegalArgumentException("column cannot be null");
        return cells.get(column);
    }

    Action createCell(Column column) {
        if (column == null)
            throw new IllegalArgumentException("column cannot be null");
        if (cells.keySet().contains(column))
            throw new IllegalArgumentException("Cell already exists");
        final Cell cell = new Cell();
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
        history.record(cells.get(column).updateValue(value));
    }
}
