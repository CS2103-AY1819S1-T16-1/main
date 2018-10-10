package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.address.testutil.TypicalWishes.ALICE;
import static seedu.address.testutil.TypicalWishes.HOON;
import static seedu.address.testutil.TypicalWishes.IDA;
import static seedu.address.testutil.TypicalWishes.getTypicalWishTransaction;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.WishTransaction;

public class XmlWishTransactionStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "XmlWishTransactionStorageTest");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readWishTransaction_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readWishTransaction(null);
    }

    private Optional<WishTransaction> readWishTransaction(String filePath) throws IOException, DataConversionException {
        return new XmlWishTransactionStorage(Paths.get(filePath))
                .readWishTransaction(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readWishTransaction("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {
        thrown.expect(DataConversionException.class);
        readWishTransaction("NotXmlFormatWishBook.xml");
    }

    @Test
    public void readAndSaveWishTransaction() throws Exception {
        Path filePath = TEST_DATA_FOLDER.resolve("TempWishTransaction.xml");
        WishTransaction original = getTypicalWishTransaction();

        XmlWishTransactionStorage xmlWishTransactionStorage = new XmlWishTransactionStorage(filePath);

        // Save in new file and read back
        xmlWishTransactionStorage.saveWishTransaction(original, filePath);
        WishTransaction read = xmlWishTransactionStorage.readWishTransaction(filePath).get();

        assertEquals(original, read);

        //Modify data, overwrite exiting file, and read back
        original.addWish(HOON);
        original.removeWish(ALICE);
        xmlWishTransactionStorage.saveWishTransaction(original, filePath);
        read = xmlWishTransactionStorage.readWishTransaction(filePath).get();
        assertEquals(original, read);

        //Save and read without specifying file path
        original.addWish(IDA);
        xmlWishTransactionStorage.saveWishTransaction(original); //file path not specified
        read = xmlWishTransactionStorage.readWishTransaction().get(); //file path not specified
        assertEquals(original, read);
    }

    @Test
    public void saveWishTransaction_nullWishTransaction_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveWishTransaction(null, "SomeFile.xml");
    }

    /**
     * Saves {@code wishTransaction} at the specified {@code filePath}.
     */
    private void saveWishTransaction(WishTransaction wishTransaction, String filePath) {
        try {
            new XmlWishTransactionStorage(Paths.get(filePath))
                    .saveWishTransaction(wishTransaction, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveWishBook_nullFilePath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveWishTransaction(new WishTransaction(), null);
    }
}
