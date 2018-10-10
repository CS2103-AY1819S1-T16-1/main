package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.WishTransaction;

/**
 * Represents a storage for {@link WishTransaction}.
 */
public interface WishTransactionStorage {
    /**
     * Returns the file path of the data file.
     */
    Path getWishTransactionFilePath();

    /**
     * Returns WishTransaction data as a {@link WishTransaction}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<WishTransaction> readWishTransaction() throws DataConversionException, IOException;

    /**
     * @see #getWishTransactionFilePath()
     */
    Optional<WishTransaction> readWishTransaction(Path filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link WishTransaction} to the storage.
     * @param wishTransaction cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveWishTransaction(WishTransaction wishTransaction) throws IOException;

    /**
     * @see #saveWishTransaction(WishTransaction)
     */
    void saveWishTransaction(WishTransaction wishTransaction, Path filePath) throws IOException;
}
