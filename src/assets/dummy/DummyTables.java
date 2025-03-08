package assets.dummy;

import java.util.ArrayList;
import java.util.List;

public class DummyTables {

    /**
     * Generates a list of dummy table names.
     * 
     * @param numberOfTables The number of table names to generate
     * @return A list of dummy table names
     */
    public static List<String> generateDummyTableNames(int numberOfTables) {
        List<String> tableNames = new ArrayList<>();
        for (int i = 0; i < numberOfTables; i++) {
            tableNames.add("Table" + i);
        }
        return tableNames;
    }

    /**
     * Generates a list of dummy table names with a custom prefix.
     * 
     * @param numberOfTables The number of table names to generate
     * @param prefix         The prefix to use for the table names
     * @return A list of dummy table names with custom prefix
     */
    public static List<String> generateDummyTableNames(int numberOfTables, String prefix) {
        List<String> tableNames = new ArrayList<>();
        for (int i = 0; i < numberOfTables; i++) {
            tableNames.add(prefix + i);
        }
        return tableNames;
    }
}
