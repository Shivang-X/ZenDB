package src.Queries;

import src.Main;
import src.Transactions.TransactionManagerImpl;

/**
 * The Query class is responsible for executing different types of SQL queries.
 */
public class Query {

    /**
     * Executes the specified SQL query.
     *
     * @param query The SQL query to be executed.
     */
    public void execute(String query) {
        query = query.toLowerCase();
        String[] chunks = query.split("\\s+");

        QueryExecuterImpl queryExecuter = new QueryExecuterImpl();
        TransactionManagerImpl transactionManager = new TransactionManagerImpl();

        switch (chunks[0]) {
            case "use":
                queryExecuter.useDatabase(query);
                break;
            case "show":
                queryExecuter.showDatabases();
                break;
            case "create":
                if (chunks[1].equals("database")) queryExecuter.createDatabase(query);
                else if (QueryExecuterImpl.basePath.equals("Databases/")) {
                    System.out.println(Main.ANSI_RED + "No database selected" + Main.ANSI_RESET);
                } else queryExecuter.createTable(query);
                break;
            case "insert":
                if (QueryExecuterImpl.basePath.equals("Databases/")) {
                    System.out.println(Main.ANSI_RED + "No database selected" + Main.ANSI_RESET);
                } else queryExecuter.insert(query);
                break;
            case "select":
                if (QueryExecuterImpl.basePath.equals("Databases/")) {
                    System.out.println(Main.ANSI_RED + "No database selected" + Main.ANSI_RESET);
                } else queryExecuter.select(query);
                break;
            case "begin":
                transactionManager.beginTransaction();
                break;
            case "commit":
                transactionManager.commit();
                break;
            case "rollback":
                transactionManager.rollback();
                break;
            case "end":
                transactionManager.endTransaction();
                break;
        }
    }
}
