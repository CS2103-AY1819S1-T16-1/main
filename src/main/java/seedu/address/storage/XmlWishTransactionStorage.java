package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.WishTransaction;

/**
 * A class to access WishTransaction data stored as an xml file on the hard disk.
 */
public class XmlWishTransactionStorage implements WishTransactionStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlWishBookStorage.class);

    private Path filePath;

    public XmlWishTransactionStorage(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public Path getWishTransactionFilePath() {
        return filePath;
    }

    @Override
    public Optional<WishTransaction> readWishTransaction() throws DataConversionException, IOException {
        return readWishTransaction(filePath);
    }

    @Override
    public Optional<WishTransaction> readWishTransaction(Path filePath) throws DataConversionException, IOException {
        requireNonNull(filePath);

        logger.info("Reading wishTransaction in XmlWishTransactionStorage.");
        if (!Files.exists(filePath)) {
            logger.info("WishTransaction file " + filePath + " not found");
            return Optional.empty();
        }

        XmlWishTransactions xmlWishTransactions = XmlFileStorage.loadWishTransactionDataFromFile(filePath);
        try {
            logger.fine("Converting WishTransaction from XML to model type in XmlWishTransactionStorage.");
            logger.info(xmlWishTransactions.toString());
            return Optional.of(xmlWishTransactions.toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in WishTransaction file at " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveWishTransaction(WishTransaction wishTransaction) throws IOException {
        saveWishTransaction(wishTransaction, filePath);
    }

    @Override
    public void saveWishTransaction(WishTransaction wishTransaction, Path filePath) throws IOException {
        requireNonNull(wishTransaction);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        XmlFileStorage.saveDataToFile(filePath, new XmlWishTransactions(wishTransaction));
    }
}
