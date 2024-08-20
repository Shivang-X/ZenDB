package src.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The TableFormatter class is responsible for formatting and displaying tables.
 */
public class TableFormatter {
    TableReader tableReader = new TableReader();

    /**
     * Displays the header of the table with the provided columns.
     *
     * @param columns The columns of the table.
     */
    public void displayTableHeader(String[] columns) {
        for (String column : columns) {
            System.out.print("| " + column + " ");
        }
        System.out.println();
    }

    /**
     * Displays a row of the table with the provided columns, line values, and header mapping.
     *
     * @param columns The columns of the table.
     * @param lineVal The values of the current line.
     * @param head    The mapping of column names to their indices.
     */
    public void displayTableRow(String[] columns, ArrayList<String> lineVal, Map<String, Integer> head) {
        for (String column : columns) {
            System.out.print("| " + lineVal.get(head.get(column)) + " ");
        }
        System.out.print("|");
        System.out.println("");
    }

    /**
     * Displays a filtered row of the table based on the provided WHERE clause.
     *
     * @param columns  The columns of the table.
     * @param lineVal  The values of the current line.
     * @param head     The mapping of column names to their indices.
     * @param whereCol The column in the WHERE clause.
     * @param whereVal The value in the WHERE clause.
     */
    public void displayTableRowWhere(String[] columns, ArrayList<String> lineVal, Map<String, Integer> head,
                                     String whereCol, String whereVal) {
        for (String column : columns) {
            if (lineVal.get(head.get(whereCol)).equals(whereVal)) {
                System.out.print("| " + lineVal.get(head.get(column)) + " ");
            }
        }
    }

    /**
     * Displays the entire table or filtered rows based on the provided WHERE clause.
     *
     * @param columns   The columns to display.
     * @param tableName The name of the table.
     * @param reader    The BufferedReader to read from the file.
     * @param whereCol  The column in the WHERE clause.
     * @param whereVal  The value in the WHERE clause.
     * @throws IOException If an I/O error occurs.
     */
    public void displayTable(String[] columns, String tableName, BufferedReader reader, String whereCol, String whereVal)
            throws IOException {
        String tableHead = reader.readLine();
        String[] headStr = tableHead.split("--");
        if (columns[0].equals("*")) {
            columns = headStr;
        }

        Map<String, Integer> head = new HashMap<>();
        for (int i = 0; i < headStr.length; i++) {
            head.put(headStr[i], i);
        }

        displayTableHeader(columns);

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.replaceAll("'", "");
            String[] lineval = line.split("--");
            ArrayList<String> lineVal = new ArrayList<>(Arrays.asList(lineval));

            if (whereCol != null) {
                displayTableRowWhere(columns, lineVal, head, whereCol, whereVal);
            } else {
                displayTableRow(columns, lineVal, head);
            }
        }
    }

    /**
     * Displays the entire table or filtered rows based on the provided WHERE clause.
     *
     * @param columns   The columns to display.
     * @param tableName The name of the table.
     * @param table     The table data as a list of lists.
     * @param whereCol  The column in the WHERE clause.
     * @param whereVal  The value in the WHERE clause.
     */
    public void displayTable(String[] columns, String tableName, ArrayList<ArrayList<String>> table, String whereCol, String whereVal) {
        if (columns[0].equals("*")) {
            columns = table.get(0).toArray(new String[0]);
        }
        Map<String, Integer> head = tableReader.getTableHead(table);

        for (ArrayList<String> row : table) {
            if (whereCol != null && row.get(head.get(whereCol)).equals(whereVal)) {
                displayTableRow(columns, row, head);
            } else if (whereCol == null) {
                displayTableRow(columns, row, head);
            }
        }
    }
}
