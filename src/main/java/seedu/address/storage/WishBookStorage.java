package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyWishBook;
import seedu.address.model.WishBook;

/**
 * Represents a storage for {@link WishBook}.
 */
public interface WishBookStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getWishBookFilePath();

    /**
     * Returns WishBook data as a {@link ReadOnlyWishBook}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyWishBook> readWishBook() throws DataConversionException, IOException;

    /**
     * @see #getWishBookFilePath()
     */
    Optional<ReadOnlyWishBook> readWishBook(Path filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyWishBook} to the storage.
     * @param wishBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveWishBook(ReadOnlyWishBook wishBook) throws IOException;

    /**
     * @see #saveWishBook(ReadOnlyWishBook)
     */
    void saveWishBook(ReadOnlyWishBook wishBook, Path filePath) throws IOException;

    /**
     * @see #saveBackup(Path)
     */
    void saveBackup() throws IOException, DataConversionException;

    /**
     * Saves backup copy of wishbook at the corresponding filepath.
     * @param path filepath of backup file.
     */
    void saveBackup(Path path) throws IOException, DataConversionException;

    /**
     * Backs up the given {@link ReadOnlyWishBook} to local storage.
     * @param wishBook wishBook cannot be null.
     * @throws IOException if there was any problem writing to the file
     */
    void backupWishBook(ReadOnlyWishBook wishBook) throws IOException;

    /**
     * @see #saveWishBook(ReadOnlyWishBook)
     */
    void backupWishBook(ReadOnlyWishBook wishBook, Path filePath) throws IOException;
}
