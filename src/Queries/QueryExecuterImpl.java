package src.Queries;

import src.Authentication.AuthenticationImpl;
import src.Data.FileManager;
import src.Data.TableFormatter;
import src.Data.TableReader;
import src.Interfaces.QueryExecuter;
import src.Main;
import src.Transactions.TransactionManagerImpl;
import src.Utils.Logs;
import src.Utils.RegexUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The QueryExecuterImpl class implements the QueryExecuter interface and is responsible for executing various SQL queries.
 */
public class QueryExecuterImpl implements QueryExecuter {
    public static String basePath = "Databases/";

    QueryParser queryParser = new QueryParser();
    FileManager fileManager = new FileManager();
    RegexUtils regexUtils = new RegexUtils();
    TableReader tableReader = new TableReader();
    TableFormatter tableFormatter = new TableFormatter();

    /**
     * Creates a table based on the specified SQL query.
     *
     * @param query The SQL query for creating the table.
     */
    public void createTable(String query) {
        try {
            String tableName = queryParser.extractTableName(query);
            String columnDefinitions = queryParser.extractColumnDefinitions(query);

            Path filePath = Paths.get(String.format(QueryExecuterImpl.basePath + "%s.txt", tableName));

            if (!Files.exists(filePath)) {
                Files.createFile(filePath);

                try (FileWriter fileWriter = new FileWriter(filePath.toString(), true)) {
                    assert columnDefinitions != null;
                    fileManager.writeHeader(fileWriter, columnDefinitions);
                }

                System.out.println(Main.ANSI_GREEN + "Table " + tableName + " created successfully" + Main.ANSI_RESET);
                Logs.writeLog(AuthenticationImpl.username, " Created ", tableName);
            } else {
                System.out.println(Main.ANSI_RED + "Table " + tableName + " already exists." + Main.ANSI_RESET);
            }
        } catch (Exception e) {
            System.out.println(Main.ANSI_RED + "Invalid Query" + Main.ANSI_RESET);
        }
    }

    /**
     * Sets the current working database based on the specified SQL query.
     *
     * @param query The SQL query for setting the current database.
     */
    public void useDatabase(String query) {
        String[] chunks = query.split("\\s+");
        System.out.println(String.format("%s", chunks[1]));
        File directory = new File(basePath + String.format("%s", chunks[1]));
        if (directory.exists()) {
            basePath = basePath + String.format("%s/", chunks[1]);
            System.out.printf("Using %s%n", chunks[1]);
            Logs.writeLog(AuthenticationImpl.username, " Performed use on ", chunks[1]);
        } else {
            System.out.println(Main.ANSI_RED + "No Such database found" + Main.ANSI_RESET);
        }
    }

    /**
     * Displays a list of all databases.
     */
    public void showDatabases() {
        File directory = new File(basePath);

        // Get all files in the directory
        File[] files = directory.listFiles();

        // Check if files are not null (directory exists and is not empty)
        if (files != null) {
            // Iterate through the files and print their names
            for (File file : files) {
                System.out.println(file.getName());
                Logs.writeLog(AuthenticationImpl.username, " Performed show ", "");
            }
        } else {
            System.out.println("Either the directory doesn't exist or it is empty.");
        }
    }

    /**
     * Creates a new database based on the specified SQL query.
     *
     * @param query The SQL query for creating a new database.
     */
    public void createDatabase(String query) {
        try {
            String[] chunks = query.split("\\s+");
            File directory = new File(basePath);

            File[] files = directory.listFiles();

            String directoryPath = String.format(basePath + "/%s", chunks[2]);

            Path database = Paths.get(directoryPath);

            assert files != null;
            if (files.length == 0) {
                Files.createDirectory(database);
                Logs.writeLog(AuthenticationImpl.username, " Created  ", chunks[2]);
                System.out.println(Main.ANSI_GREEN + "New Database created successfully" + Main.ANSI_RESET);
            } else {
                System.out.println(Main.ANSI_RED + "1 Database already exists !!" + Main.ANSI_RESET);
            }
        } catch (Exception e) {
            System.out.println(Main.ANSI_RED + "Invalid Query" + Main.ANSI_RESET);
        }
    }

    /**
     * Inserts data into a table based on the specified SQL query.
     *
     * @param query The SQL query for inserting data into a table.
     */
    public void insert(String query) {
        String regex = "INSERT\\s+INTO\\s+(\\w+)\\s*\\(([^)]+)\\)\\s*VALUES\\s*\\(([^)]+)\\);";
        Matcher matcher = regexUtils.getMatcher(query, regex);

        if (matcher.matches()) {
            String tableName = matcher.group(1);
            String columnNames = matcher.group(2);
            String values = matcher.group(3);

            String[] cols = columnNames.split("\\s*,\\s*");
            String[] vals = values.split("\\s*,\\s*");

            Map<String, String> row = tableReader.getRow(cols, vals);

            if (!TransactionManagerImpl.inTransaction) {
                queryParser.handleInsertWithoutTransaction(tableName, cols, row);
            } else {
                queryParser.handleInsertWithTransaction(tableName, cols, row);
                TransactionManagerImpl.queries.add(query);
            }
        } else {
            System.out.println(Main.ANSI_RED + "Invalid Query" + Main.ANSI_RESET);
        }
    }

    /**
     * Retrieves data from a table based on the specified SQL query and displays the result.
     *
     * @param query The SQL query for retrieving data from a table.
     */
    public void select(String query) {
        String[] parts = query.split("(?i)\\bWHERE\\b", 2);
        String select;
        String where = null;
        String whereCol = null;
        String whereVal = null;

        if (parts.length > 1) {
            select = parts[0].trim();
            where = parts[1].trim();
        } else {
            select = parts[0].trim();
        }

        String regexSelect = "^SELECT\\s+(.+?)\\s+FROM\\s+(\\w+);$";
        String regexWhere = "\\s*(\\w+)\\s*=\\s*(\\w+)\\s*";

        Pattern patternSelect = Pattern.compile(regexSelect, Pattern.CASE_INSENSITIVE);
        Matcher matcherSelect = patternSelect.matcher(select + ";");

        Pattern patternWhere = Pattern.compile(regexWhere, Pattern.CASE_INSENSITIVE);
        if (where != null) {
            Matcher matcherWhere = patternWhere.matcher(where);
            if (matcherWhere.matches()) {
                whereCol = matcherWhere.group(1);
                whereVal = matcherWhere.group(2);
            } else {
                System.out.println(Main.ANSI_RED + "Invalid Query" + Main.ANSI_RESET);
            }
        }

        if (matcherSelect.matches()) {
            String columnsStr = matcherSelect.group(1);
            String[] columns = columnsStr.split("\\s*,\\s*");
            String tableName = matcherSelect.group(2);

            if (!TransactionManagerImpl.isInTransaction() || TransactionManagerImpl.buffer.get(tableName + ".txt") == null) {
                try (BufferedReader reader = new BufferedReader(new FileReader(String.format(basePath + "%s.txt", tableName)))) {
                    tableFormatter.displayTable(columns, tableName, reader, whereCol, whereVal);
                    Logs.writeLog(AuthenticationImpl.username, " Performed select on ", tableName);
                } catch (IOException e) {
                    System.out.println(e);
                }
            } else {
                ArrayList<ArrayList<String>> table = TransactionManagerImpl.buffer.get(tableName + ".txt");
                tableFormatter.displayTable(columns, tableName, table, whereCol, whereVal);
                Logs.writeLog(AuthenticationImpl.username, " Performed select on ", tableName);
            }
        } else {
            System.out.println(Main.ANSI_RED + "Invalid Query" + Main.ANSI_RESET);
        }
    }
}
