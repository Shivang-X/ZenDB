package src.Interfaces;

/**
 * This interface defines methods for executing various SQL queries.
 */
public interface QueryExecuter {

    /**
     * Creates a table based on the provided SQL query.
     *
     * @param query The SQL query to create a table.
     */
    public void createTable(String query);

    /**
     * Switches to the specified database using the provided SQL query.
     *
     * @param query The SQL query to use a database.
     */
    public void useDatabase(String query);

    /**
     * Displays a list of existing databases.
     */
    public void showDatabases();

    /**
     * Creates a new database based on the provided SQL query.
     *
     * @param query The SQL query to create a database.
     */
    public void createDatabase(String query);

    /**
     * Inserts data into a table using the provided SQL query.
     *
     * @param query The SQL query to insert data.
     */
    public void insert(String query);

    /**
     * Retrieves and displays data from a table based on the provided SQL query.
     *
     * @param query The SQL query to select data from a table.
     */
    public void select(String query);
}
