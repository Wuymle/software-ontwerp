package database.test;

import database.Database;
import database.Table;
import database.Column;
import database.ColumnType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import database.ColumnType;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
    private Database database;

    @BeforeEach
    public void setUp() {
        database = new Database();
        database.createTable();
        database.addColumn("Table1");
        database.addRow("Table1");
    }

    @Test
    public void testChangeColumnType() {
        database.updateCell("Table1", "Column1", 0, "Gyqttt");
        assertFalse(database.isValidColumnType("Table1", "Column1", ColumnType.INTEGER));
        assertThrows(Error.class, () -> database.updateColumnType("Table1", "Column1", ColumnType.INTEGER));
    }

    @Test
    public void testChangeColumnType2() {
        database.updateCell("Table1", "Column1", 0, "123");
        assertDoesNotThrow(() -> database.updateColumnType("Table1", "Column1", ColumnType.INTEGER));
    }
}
