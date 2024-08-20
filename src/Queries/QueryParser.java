package src.Queries;

import src.Authentication.AuthenticationImpl;
import src.Data.TableReader;
import src.Main;
import src.Transactions.TransactionManagerImpl;
import src.Utils.Logs;
import src.Utils.RegexUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * The QueryParser class is responsible for parsing and handling SQL queries.
 */
public class QueryParser {
    RegexUtils regexUtils = new RegexUtils();
    TableReader tableReader = new TableReader();

    /**
     * Extracts the table name from the CREATE TABLE SQL query.
     *
     * @param query The CREATE TABLE SQL query.
     * @return The table name.
     */
    public String extractTableName(String query) {
        Matcher matcher = regexUtils.getMatcher(query, "\\bCREATE\\s+TABLE\\s+(\\w+)\\s*\\(([^;]+)\\);");
        return matcher.find() ? matcher.group(1) : null;
    }

    /**
     * Extracts the column definitions from the CREATE TABLE SQL query.
     *
     * @param query The CREATE TABLE SQL query.
     * @return The column definitions.
     */
    public String extractColumnDefinitions(String query) {
        Matcher matcher = regexUtils.getMatcher(query, "\\bCREATE\\s+TABLE\\s+(\\w+)\\s*\\(([^;]+)\\);");
        return matcher.find() ? matcher.group(2) : null;
    }

    /**
     * Handles the insertion of data into a table based on the SQL query.
     *
     * @param reader    The BufferedReader for the table file.
     * @param tableName The name of the table.
     * @param cols      The columns to insert into.
     * @param row       The data to be inserted.
     * @throws IOException If an I/O error occurs.
     */
    public void handleInsert(BufferedReader reader, String tableName, String[] cols, Map<String, String> row) throws IOException {

        String table_head = reader.readLine();
        String[] table_cols = table_head.split("--");
        ArrayList<String> list_cols = new ArrayList<>(Arrays.asList(table_cols));

        if (cols.length != table_cols.length) {
            System.out.println(Main.ANSI_RED + "Invalid Query" + Main.ANSI_RESET);
        } else {
            for (int i = 0; i < table_cols.length; i++) {
                if (!list_cols.contains(cols[i])) {
                    System.out.println(Main.ANSI_RED + "Invalid Query" + Main.ANSI_RESET);
                }
            }
        }

        try (FileWriter fileWriter = new FileWriter(String.format(QueryExecuterImpl.basePath + "%s.txt", tableName), true)) {

            for (String col : table_cols) {
                fileWriter.write((String.format("%s--", row.get(col))).replace("'", ""));
            }
            fileWriter.write(System.lineSeparator());
            fileWriter.close();

            System.out.println(Main.ANSI_GREEN + "Inserted successfully, 1 row affected" + Main.ANSI_RESET);
            Logs.writeLog(AuthenticationImpl.username, " Inserted into ", tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the insertion of data into a table without a transaction.
     *
     * @param tableName The name of the table.
     * @param cols      The columns to insert into.
     * @param row       The data to be inserted.
     */
    public void handleInsertWithoutTransaction(String tableName, String[] cols, Map<String, String> row) {
        try (BufferedReader reader = new BufferedReader(new FileReader(String.format(QueryExecuterImpl.basePath + "/%s.txt", tableName)))) {
            handleInsert(reader, tableName, cols, row);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the insertion of data into a table with a transaction.
     *
     * @param tableName The name of the table.
     * @param cols      The columns to insert into.
     * @param row       The data to be inserted.
     */
    public void handleInsertWithTransaction(String tableName, String[] cols, Map<String, String> row) {
        try (BufferedReader reader = new BufferedReader(new FileReader(String.format(QueryExecuterImpl.basePath + "%s.txt", tableName)))) {
            ArrayList<ArrayList<String>> table = tableReader.getTable(reader);
            TransactionManagerImpl.buffer.put(String.format("%s.txt", tableName), table);
            ArrayList<String> newRow = new ArrayList<>();

            for (int i = 0; i < cols.length; i++) {
                String key = TransactionManagerImpl.buffer.get(String.format("%s.txt", tableName)).getFirst().get(i);
                newRow.add(row.get(key).replace("'", ""));
            }

            TransactionManagerImpl.buffer.get(String.format("%s.txt", tableName)).add(newRow);
            System.out.println(Main.ANSI_GREEN + "Inserted successfully, 1 row affected" + Main.ANSI_RESET);
            Logs.writeLog(AuthenticationImpl.username, " Performed use on ", tableName);
        } catch (IOException e) {
            System.err.println("Error reading file: " + String.format(QueryExecuterImpl.basePath + "%s.txt", tableName));
        }
    }
}
