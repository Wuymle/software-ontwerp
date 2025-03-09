package dataBase;

import java.util.ArrayList;
import java.util.Collection;

public class Row {
    private ArrayList<Cell> cells = new ArrayList<Cell>();

    public void createCell(Column column) {
        cells.add(new Cell(column));
    }

    public void createCells(Collection<Column> columns) {
        for (Column column : columns) {
            createCell(column);
        }
    }

    public void deleteCell(int index){
        cells.remove(index);
    }


    public ArrayList<Cell> getCells() {
        return cells;
    }
}
