package newdatabase;

import java.util.HashMap;
import java.util.Map;

public class Row {
    private Map<Column, Cell> cells = new HashMap<>();

    public Cell getCell(Column column) {
        if (column == null)
            throw new IllegalArgumentException("column cannot be null");
        return cells.get(column);
    }

    public void createCell(Column column) {
        if (column == null)
            throw new IllegalArgumentException("column cannot be null");
        if (cells.keySet().contains(column))
            throw new IllegalArgumentException("Cell already exists");
        cells.put(column, new Cell());
    }

    public void deleteCell(Column column) {
        if (column == null || !cells.containsKey(column))
            throw new IllegalArgumentException("column does not exist");
        cells.remove(column);
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
        cells.get(column).updateValue(value);
    }
}
