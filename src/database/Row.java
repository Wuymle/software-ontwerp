package database;

import java.util.ArrayList;

/**
 * Represents a row in a table, containing a list of cells associated with columns.
 */
public class Row {
    private ArrayList<Cell> cells = new ArrayList<>();

    /**
     * Creates a new cell associated with a given column and adds it to the row.
     * Registers the cell with the corresponding column.
     *
     * @param column The column to associate the new cell with.
     */
    public void createCell(Column column) {
        Cell newCell = new Cell(column);
        cells.add(newCell);
        column.registerCell(newCell);
    }

    /**
     * Creates cells for a list of columns and adds them to the row.
     *
     * @param columns The list of columns for which cells should be created.
     */
    public void createCells(ArrayList<Column> columns) {
        for (Column column : columns) {
            createCell(column);
        }
    }

    /**
     * Deletes a cell from the row at the specified index.
     *
     * @param index The index of the cell to be removed.
     */
    public void deleteCell(int index){
        cells.remove(index);
    }

    /**
     * Returns the list of cells in this row.
     *
     * @return An ArrayList of cells in the row.
     */
    public ArrayList<Cell> getCells() {
        return cells;
    }
}
