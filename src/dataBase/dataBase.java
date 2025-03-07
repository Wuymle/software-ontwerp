package dataBase;

import java.util.ArrayList;

public class dataBase {
    private ArrayList<Table> tables;
    private Table currentTable;

    public void createTable() {
        Table table = new Table();
        tables.add(table);
    }

    public ArrayList<Table> getTables() {
        return tables;
    }

    // Methods for the current table

    public void openTable(Table table) {
        currentTable = table;
    }

    public void deleteTable() {
        tables.remove(currentTable);
    }

    public void editTableName(String name) {
        currentTable.setName(name);
    }

    public void addColumn(columnType type, boolean allowBlank) {
        currentTable.createColumn(type, allowBlank);
    }
}
