package application.screens.screenTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import application.Application;
import application.DatabaseAppContext;
import application.screens.TableDesignView;
import clutter.ApplicationWindow;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.WindowController;
import clutter.layoutwidgets.TopWindow;
import clutter.test.TestWidgets;
import java.awt.event.KeyEvent;
import java.util.List;

import application.test.TestHelper;

public class TableDesignTest {

    private DatabaseAppContext context;
    private TableDesignView tableDesignView;

    @BeforeEach
    public void setUp() {
        ApplicationWindow window = new ApplicationWindow("SuperDBMS",
                (DatabaseAppContext appContext) -> new Application(appContext),
                (ApplicationWindow appWindow) -> new DatabaseAppContext(appWindow));

        context = new DatabaseAppContext(window);
        context.getDatabase().createTable();
        context.getDatabase().addColumn("Table1");
        tableDesignView = new TableDesignView(context, "Table1");
    }

    @Test
    public void testBuildView() {
        Widget view = tableDesignView.build();
        assertNotNull(view, "The built view should not be null.");
    }

    @Test
    public void testDeleteColumn() throws Exception {
        context.getDatabase().addColumn("Table1");
        List<String> columns = context.getDatabase().getColumnNames("Table1");
        String columnToDelete = columns.get(0);
        
        @SuppressWarnings("unchecked")
        List<String> selectedColumns = (List<String>) TestHelper.getPrivateField(tableDesignView, "selectedColumns");
        selectedColumns.add(columnToDelete);

        tableDesignView.onKeyPress(KeyEvent.KEY_PRESSED, KeyEvent.VK_DELETE, '\0');
        assertFalse(context.getDatabase().getColumnNames("Table1").contains(columnToDelete),
                "The selected column should be deleted.");
    }

    @Test
    public void testEscapeKey() {
        boolean result = tableDesignView.onKeyPress(KeyEvent.KEY_PRESSED, KeyEvent.VK_ESCAPE, '\0');
        assertTrue(result, "The escape key should be handled.");
    }
}
