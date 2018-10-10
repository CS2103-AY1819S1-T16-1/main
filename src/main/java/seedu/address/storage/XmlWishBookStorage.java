package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.StringUtil;
import seedu.address.model.ReadOnlyWishBook;

/**
 * A class to access WishBook data stored as an xml file on the hard disk.
 */
public class XmlWishBookStorage implements WishBookStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlWishBookStorage.class);

    private Path filePath;

    public XmlWishBookStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getWishBookFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyWishBook> readWishBook() throws DataConversionException, IOException {
        return readWishBook(filePath);
    }

    /**
     * Similar to {@link #readWishBook()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyWishBook> readWishBook(Path filePath) throws DataConversionException,
                                                                                 FileNotFoundException {
        requireNonNull(filePath);

        if (!Files.exists(filePath)) {
            logger.info("WishBook file " + filePath + " not found");
            return Optional.empty();
        }

        XmlSerializableWishBook xmlWishBook = XmlFileStorage.loadDataFromSaveFile(filePath);
        try {
            return Optional.of(xmlWishBook.toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveWishBook(ReadOnlyWishBook wishBook) throws IOException {
        saveWishBook(wishBook, filePath);
    }

    /**
     * Similar to {@link #saveWishBook(ReadOnlyWishBook)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveWishBook(ReadOnlyWishBook wishBook, Path filePath) throws IOException {
        requireNonNull(wishBook);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        XmlFileStorage.saveDataToFile(filePath, new XmlSerializableWishBook(wishBook));
    }

    /**
     * Saves backup wishBook at backup filepath.
     * Done by transferring backup copy into save file location.
     * @throws IOException
     * @throws DataConversionException
     */
    @Override
    public void saveBackup() throws IOException, DataConversionException {
        saveBackup(filePath);
    }

    /**
     * @see #saveBackup()
     * @param path file path at which the backup file is stored at.
     * @throws IOException
     * @throws DataConversionException
     */
    @Override
    public void saveBackup(Path path) throws IOException, DataConversionException {
        requireNonNull(path);
        readWishBook(path).ifPresent(book -> {
            try {
                saveWishBook(book);
            } catch (IOException e) {
                logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
            }
        });
    }

    @Override
    public void backupWishBook(ReadOnlyWishBook wishBook) throws IOException {
        backupWishBook(wishBook, filePath);
    }

    /**
     * Similar to {@link #backupWishBook(ReadOnlyWishBook)}
     * @param wishBook wishBook to backup. Cannot be null.
     * @param filePath location of the data. Cannot be null.
     * @throws IOException
     */
    @Override
    public void backupWishBook(ReadOnlyWishBook wishBook, Path filePath) throws IOException {
        requireNonNull(wishBook);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        XmlFileStorage.backupDataToFile(filePath, new XmlSerializableWishBook(wishBook));
    }

}
