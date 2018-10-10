package seedu.address.storage;

import java.io.FileNotFoundException;
import java.nio.file.Path;

import javax.xml.bind.JAXBException;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.XmlUtil;

/**
 * Stores wishBook data in an XML file
 */
public class XmlFileStorage {

    /**
     * Saves the given wishBook data to the specified file.
     */
    public static void saveDataToFile(Path file, XmlSerializableWishBook wishBook)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, wishBook);
        } catch (JAXBException e) {
            throw new AssertionError("Unexpected exception " + e.getMessage(), e);
        }
    }

    /**
     * Saves the given wish transaction data to the specified file.
     */
    public static void saveDataToFile(Path file, XmlWishTransactions xmlWishTransactions)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, xmlWishTransactions);
        } catch (JAXBException e) {
            throw new AssertionError("Unexpected exception " + e.getMessage(), e);
        }
    }

    /**
     * Saves the given wishBook data to the backup file.
     * @param file filepath to save the data temporarily.
     * @param xmlSerializableWishBook data in XML format.
     * @throws FileNotFoundException
     */
    public static void backupDataToFile(Path file, XmlSerializableWishBook xmlSerializableWishBook)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, xmlSerializableWishBook);
        } catch (JAXBException e) {
            throw new AssertionError("Unexpected exception " + e.getMessage(), e);
        }
    }

    /**
     * Returns wish book in the file or an empty wish book
     */
    public static XmlSerializableWishBook loadDataFromSaveFile(Path file) throws DataConversionException,
                                                                            FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlSerializableWishBook.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

    /**
     * Returns wish transactions in the file or an empty wish transaction file.
     * @param file filepath to retrieve data from.
     * @return wish transactions file.
     * @throws DataConversionException if XML data in file is not properly formatted.
     * @throws FileNotFoundException if filepath does not exist.
     */
    public static XmlWishTransactions loadWishTransactionDataFromFile(Path file) throws DataConversionException,
                                                                            FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlWishTransactions.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

}
