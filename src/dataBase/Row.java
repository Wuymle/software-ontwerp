package database;

import java.util.ArrayList;

/**
 * Represents a row in a database table consisting of multiple cells.
 */
public class Row {
    private ArrayList<Cell> cells = new ArrayList<>();

    /**
     * Creates a new cell for the specified column and adds it to the row.
     * Also registers the cell with the associated column.
     *
     * @param column The column for which the cell is created.
     */
    public void createCell(Column column) {
        Cell newCell = new Cell(column);
        cells.add(newCell);
        column.registerCell(newCell);
    }

    /**
     * Creates cells for a list of columns and adds them to the row.
     *
     * @param columns The list of columns for which cells are created.
     */
    public void createCells(ArrayList<Column> columns) {
        for (Column column : columns) {
            createCell(column);
        }
    }

    /**
     * Deletes a cell at the specified index from the row.
     *
     * @param index The index of the cell to be deleted.
     */
    public void deleteCell(int index) {
        cells.remove(index);
    }

    /**
     * Returns the list of cells in the row.
     *
     * @return The list of cells.
     */
    public ArrayList<Cell> getCells() {
        return cells;
    }
}
