package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.WishBookChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyWishBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.WishTransaction;

/**
 * Manages storage of WishBook data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private WishBookStorage wishBookStorage;
    private WishTransactionStorage wishTransactionStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(WishBookStorage wishBookStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.wishBookStorage = wishBookStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    public StorageManager(WishBookStorage wishBookStorage, WishTransactionStorage wishTransactionStorage,
                          UserPrefsStorage userPrefsStorage) {
        super();
        this.wishBookStorage = wishBookStorage;
        this.wishTransactionStorage = wishTransactionStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }

    // ================ WishTransaction methods ==============================

    @Override
    public Path getWishTransactionFilePath() {
        return wishTransactionStorage.getWishTransactionFilePath();
    }

    @Override
    public Optional<WishTransaction> readWishTransaction() throws DataConversionException, IOException {
        return wishTransactionStorage.readWishTransaction(getWishTransactionFilePath());
    }

    @Override
    public Optional<WishTransaction> readWishTransaction(Path filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read wishTransaction data from file: " + filePath);
        return wishTransactionStorage.readWishTransaction(filePath);
    }

    @Override
    public void saveWishTransaction(WishTransaction wishTransaction) throws IOException {
        saveWishTransaction(wishTransaction, getWishTransactionFilePath());
    }

    @Override
    public void saveWishTransaction(WishTransaction wishTransaction, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        wishTransactionStorage.saveWishTransaction(wishTransaction, filePath);
    }

    // ================ WishBook methods ==============================

    @Override
    public Path getWishBookFilePath() {
        return wishBookStorage.getWishBookFilePath();
    }

    @Override
    public Optional<ReadOnlyWishBook> readWishBook() throws DataConversionException, IOException {
        return readWishBook(wishBookStorage.getWishBookFilePath());
    }

    @Override
    public Optional<ReadOnlyWishBook> readWishBook(Path filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return wishBookStorage.readWishBook(filePath);
    }

    @Override
    public void saveWishBook(ReadOnlyWishBook wishBook) throws IOException {
        saveWishBook(wishBook, wishBookStorage.getWishBookFilePath());
    }

    @Override
    public void saveWishBook(ReadOnlyWishBook wishBook, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        wishBookStorage.saveWishBook(wishBook, filePath);
    }

    @Override
    public void backupWishBook(ReadOnlyWishBook wishBook) throws IOException {
        backupWishBook(wishBook, wishBookStorage.getWishBookFilePath());
    }

    @Override
    public void backupWishBook(ReadOnlyWishBook wishBook, Path filePath) throws IOException {
        logger.fine("Attempting to write to backup data file: " + filePath);
        wishBookStorage.backupWishBook(wishBook, filePath);
    }

    @Override
    public void saveBackup() throws IOException, DataConversionException {
        wishBookStorage.saveBackup();
    }

    @Override
    public void saveBackup(Path path) throws IOException, DataConversionException {
        logger.fine("Attempting to save backup file: " + path);
        wishBookStorage.saveBackup(path);
    }


    @Override
    @Subscribe
    public void handleWishBookChangedEvent(WishBookChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            backupWishBook(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

}
