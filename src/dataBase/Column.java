package database;

import java.util.ArrayList;

public class Column {
    private ColumnType type;
    private boolean allowBlank = false;
    private ArrayList<Cell> cells;
    private String defaultValue;

    public Column() {
        this.type = ColumnType.STRING;
        resetDefaultValue();
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
        resetDefaultValue();
        for (Cell cell : this.cells) {
            cell.setValue(cell.getValue());
        }
    }

    public void setAllowBlank(boolean allowBlank) {
        this.allowBlank = allowBlank;
        resetDefaultValue();
    }

    public ArrayList<Cell> getCells() {
        return cells;
    }

    public void resetDefaultValue() {
        if (getAllowBlank()) {
            defaultValue = null;
            return;
        }
        switch (getType()) {
            case INTEGER:
                defaultValue = "0";
                break;
            case STRING:
                defaultValue = "";
                break;
            case BOOLEAN:
                defaultValue = "false";
                break;
            case EMAIL:
                defaultValue = "@";
                break;
        }
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void toggleColumnType() {
        if (type == ColumnType.STRING) {
            editColumnType(ColumnType.INTEGER);
        } else if (type == ColumnType.INTEGER) {
            editColumnType(ColumnType.BOOLEAN);
        } else if (type == ColumnType.BOOLEAN) {
            editColumnType(ColumnType.EMAIL);
        } else if (type == ColumnType.EMAIL) {
            editColumnType(ColumnType.STRING);
        }
    }
}
