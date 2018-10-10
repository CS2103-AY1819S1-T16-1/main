package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.events.model.WishBookChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyWishBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.WishTransaction;

/**
 * API of the Storage component
 */
public interface Storage extends WishBookStorage, WishTransactionStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

    @Override
    Path getWishTransactionFilePath();

    @Override
    Optional<WishTransaction> readWishTransaction() throws DataConversionException, IOException;

    @Override
    void saveWishTransaction(WishTransaction wishTransaction) throws IOException;

    @Override
    Path getWishBookFilePath();

    @Override
    Optional<ReadOnlyWishBook> readWishBook() throws DataConversionException, IOException;

    @Override
    void saveWishBook(ReadOnlyWishBook wishBook) throws IOException;

    @Override
    void saveBackup() throws IOException, DataConversionException;

    /**
     * Saves the current version of the Address Book to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleWishBookChangedEvent(WishBookChangedEvent abce);
}
