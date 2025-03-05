package assets.dummy;

import java.util.ArrayList;
import java.util.List;

public class DummyRows {
    public static List<String[]> generateDummyRows(int numberOfRows) {
        List<String[]> dummyRows = new ArrayList<>();
        for (int i = 0; i < numberOfRows; i++) {
            String[] row = new String[] {
                    "Row" + i,
                    "Data" + i,
                    "MoreData" + i,
                    "EvenMoreData" + i
            };
            dummyRows.add(row);
        }
        return dummyRows;
    }
}
