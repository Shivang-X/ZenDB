package src.Interfaces;

/**
 * This interface defines methods for managing transactions in a database.
 */
public interface TransactionManager {

    /**
     * Begins a new transaction.
     */
    public void beginTransaction();

    /**
     * Commits the current transaction, making changes permanent.
     */
    public void commit();

    /**
     * Rolls back the current transaction, undoing any changes made.
     */
    public void rollback();

    /**
     * Ends the current transaction, either committing or rolling back based on success.
     */
    public void endTransaction();
}
