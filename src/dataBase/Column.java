package database;

import java.util.ArrayList;

public class Column {
    private ColumnType type;
    private boolean allowBlank = false;
    private ArrayList<Cell> cells;

    public Column(ColumnType type, boolean allowBlank) {
        this.type = type;
        this.allowBlank = allowBlank;
        this.cells = new ArrayList<>();
    }

    public ColumnType getType() {
        return type;
    }

    public void registerCell(Cell cell) {
        this.cells.add(cell);
    }

    public boolean getAllowBlank() {
        return allowBlank;
    }

    public void editColumnType(ColumnType type) {
        this.type = type;

        for (Cell cell : this.cells) {
            cell.setValue(cell.getValue());
        }
    }

    public ArrayList<Cell> getCells() {
        return cells;
    }
}
