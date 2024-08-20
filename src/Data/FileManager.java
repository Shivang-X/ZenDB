package src.Data;

import src.Main;
import src.Queries.QueryExecuterImpl;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * This class manages file operations related to database tables.
 */
public class FileManager {

    /**
     * Writes the header containing column names to the specified file.
     *
     * @param fileWriter        The FileWriter object for the file.
     * @param columnDefinitions The string containing column definitions.
     * @throws IOException If an I/O error occurs.
     */
    public void writeHeader(FileWriter fileWriter, String columnDefinitions) throws IOException {
        String[] columns = columnDefinitions.split(",");
        for (int i = 0; i < columns.length; i++) {
            fileWriter.write(String.format("%s--", columns[i].trim().split("\\s+")[0]));
        }
        fileWriter.write(System.lineSeparator());
    }

    /**
     * Handles the insertion of data into a table.
     *
     * @param reader    The BufferedReader object for reading input.
     * @param tableName The name of the table.
     * @param cols      The array of column names.
     * @param row       The Map containing column-value pairs for the new row.
     * @throws IOException If an I/O error occurs.
     */
    public void handleInsert(BufferedReader reader, String tableName, String[] cols, Map<String, String> row) throws IOException {

        String tableHead = reader.readLine();
        String[] tableCols = tableHead.split("--");
        ArrayList<String> listCols = new ArrayList<>(Arrays.asList(tableCols));
        System.out.println(listCols);

        if (cols.length != tableCols.length) {
            System.out.println(Main.ANSI_RED + "Invalid Query" + Main.ANSI_RESET);
        } else {
            for (int i = 0; i < tableCols.length; i++) {
                if (!listCols.contains(cols[i])) {
                    System.out.println(Main.ANSI_RED + "Invalid Query" + Main.ANSI_RESET);
                }
            }
        }

        try (FileWriter fileWriter = new FileWriter(String.format(QueryExecuterImpl.basePath+"%s.txt", tableName), true)) {

            for (String col : tableCols) {
                fileWriter.write((String.format("%s--", row.get(col))).replace("'", ""));
            }
            fileWriter.write(System.lineSeparator());
            fileWriter.close();

            System.out.println("Data has been written to the file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
