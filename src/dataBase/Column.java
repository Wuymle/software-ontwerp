package dataBase;

import java.util.ArrayList;

public class Column {
    private columnType type;
    private boolean allowBlank = false;
    private ArrayList<Cell> cells;

    public Column() {
        allowBlank = true;
        this.type = columnType.STRING;
        this.cells = new ArrayList<>();
    }

    public columnType getType() {
        return type;
    }

    public void registerCell(Cell cell) {
        this.cells.add(cell);
    }

    public boolean getAllowBlank() {
        return allowBlank;
    }

    public void editColumnType(columnType type) {
        this.type = type;

        for (Cell cell : this.cells) {
            cell.setValue(cell.getValue());
        }
    }

    public void setAllowBlank(boolean allowBlank) {
        this.allowBlank = allowBlank;
    }

    public ArrayList<Cell> getCells() {
        return cells;
    }
}
