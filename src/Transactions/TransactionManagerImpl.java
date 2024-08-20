package src.Transactions;

import src.Authentication.AuthenticationImpl;
import src.Interfaces.TransactionManager;
import src.Queries.Query;
import src.Utils.Logs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The TransactionManagerImpl class implements the TransactionManager interface
 * and provides methods for managing transactions.
 */
public class TransactionManagerImpl implements TransactionManager {

    Query query = new Query();

    // Flag indicating whether a transaction is currently in progress
    public static boolean inTransaction = false;

    // List to store queries in a transaction
    public static ArrayList<String> queries = new ArrayList<>();

    // Buffer to store data during a transaction
    public static Map<String, ArrayList<ArrayList<String>>> buffer = new HashMap<>();

    /**
     * Checks if a transaction is currently in progress.
     *
     * @return True if a transaction is in progress, false otherwise.
     */
    public static boolean isInTransaction() {
        return inTransaction;
    }

    /**
     * Sets the flag indicating whether a transaction is in progress.
     *
     * @param inTransaction True if a transaction is in progress, false otherwise.
     */
    public static void setInTransaction(boolean inTransaction) {
        TransactionManagerImpl.inTransaction = inTransaction;
    }

    /**
     * Begins a new transaction.
     */
    public void beginTransaction() {
        setInTransaction(true);
        Logs.writeLog(AuthenticationImpl.username, " Began ", "Transaction");
    }

    /**
     * Commits the ongoing transaction.
     */
    public void commit() {
        setInTransaction(false);
        for (String query : queries) {
            this.query.execute(query);
        }
        queries.clear();
        buffer.clear();
        setInTransaction(true);
        Logs.writeLog(AuthenticationImpl.username, " Committed ", "Transaction");
    }

    /**
     * Rolls back the ongoing transaction.
     */
    public void rollback() {
        setInTransaction(false);
        queries.clear();
        buffer.clear();
        setInTransaction(true);
        Logs.writeLog(AuthenticationImpl.username, " Rollbacked ", "Transaction");
    }

    /**
     * Ends the ongoing transaction.
     */
    public void endTransaction() {
        setInTransaction(false);
        Logs.writeLog(AuthenticationImpl.username, " Ended ", "Transaction");
    }
}
