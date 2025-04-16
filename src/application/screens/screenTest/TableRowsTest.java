package application.screens.screenTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.List;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import application.Application;
import application.DatabaseAppContext;
import application.screens.TableRowsView;
import clutter.ApplicationWindow;
import clutter.abstractwidgets.Widget;
import clutter.layoutwidgets.Column;
import clutter.layoutwidgets.Flexible; // Import Flexible class
import clutter.inputwidgets.Clickable; // Import Clickable class

import application.test.TestHelper;

public class TableRowsTest {

    private DatabaseAppContext context;
    private TableRowsView tableRowsView;

    @BeforeEach
    public void setUp() {
        ApplicationWindow window = new ApplicationWindow("SuperDBMS",
                (DatabaseAppContext appContext) -> new Application(appContext),
                (ApplicationWindow appWindow) -> new DatabaseAppContext(appWindow));

        context = new DatabaseAppContext(window);
        context.getDatabase().createTable();
        context.getDatabase().addColumn("Table1");

        tableRowsView = new TableRowsView(context, "Table1");
    }

    @Test
    public void testTableRowsViewInitialization() {
        assertNotNull(tableRowsView, "TableRowsView should be initialized");
    }

    @Test
    public void testAddRow() throws Exception {
        int initSize = context.getDatabase().getRows("Table1").size();
        Column builtWidget = (Column) tableRowsView.build();
        Flexible flexible = (Flexible) builtWidget.children[1];
        Clickable clickable = (Clickable) TestHelper.getPrivateField(flexible, "child");
        ((Runnable) TestHelper.getPrivateField(clickable, "onClick")).run(); // Simulate a click to add a row
        assertEquals(initSize + 1, context.getDatabase().getRows("Table1").size(), "Row count should be 1 after adding a row");
        System.err.println(builtWidget);
    }

    // @Test
    // public void testRemoveRow() {
    //     context.getDatabase().addRow("Table1");
    //     Integer rowToDelete = 1;
        
    //     @SuppressWarnings("unchecked")
    //     ArrayList<Integer> selectedRows = (ArrayList<Integer>) TestHelper.getPrivateField(tableRowsView, "Rows");
    //     selectedRows.add(rowToDelete);

    //     tableRowsView.onKeyPress(KeyEvent.KEY_PRESSED, KeyEvent.VK_DELETE, '\0');
    //     assertFalse(context.getDatabase().getColumnNames("Table1").contains(columnToDelete),
    //             "The selected column should be deleted.");
    // }

    // @Test
    // public void testClearRows() {
    //     tableRowsView.addRow("Row 1");
    //     tableRowsView.addRow("Row 2");
    //     tableRowsView.clearRows();
    //     assertEquals(0, tableRowsView.getRowCount(), "Row count should be 0 after clearing rows");
    // }

    // @Test
    // public void testGetRow() {
    //     tableRowsView.addRow("Row 1");
    //     assertEquals("Row 1", tableRowsView.getRow(0), "Should return the correct row content");
    // }
}
