package src.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The TableReader class is responsible for reading and extracting information from tables.
 */
public class TableReader {

    /**
     * Reads the table from the BufferedReader and returns it as a list of lists.
     *
     * @param reader The BufferedReader to read from.
     * @return The table as a list of lists.
     * @throws IOException If an I/O error occurs.
     */
    public ArrayList<ArrayList<String>> getTable(BufferedReader reader) throws IOException {
        String line;
        ArrayList<ArrayList<String>> table = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            String[] lineArray = line.split("--");
            ArrayList<String> newRow = new ArrayList<>(Arrays.asList(lineArray));
            table.add(newRow);
        }

        return table;
    }

    /**
     * Creates a map representing a row from the provided columns and values.
     *
     * @param cols The columns of the table.
     * @param vals The values of the row.
     * @return A map representing the row.
     */
    public Map<String, String> getRow(String[] cols, String[] vals) {
        Map<String, String> row = new HashMap<>();
        for (int i = 0; i < cols.length; i++) {
            row.put(cols[i], vals[i]);
        }
        return row;
    }

    /**
     * Creates a mapping of column names to their corresponding indices in the table.
     *
     * @param table The table as a list of lists.
     * @return The mapping of column names to indices.
     */
    public Map<String, Integer> getTableHead(ArrayList<ArrayList<String>> table) {
        Map<String, Integer> head = new HashMap<>();
        for (int i = 0; i < table.get(0).toArray().length; i++) {
            head.put(table.get(0).get(i), i);
        }
        return head;
    }
}
