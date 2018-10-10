package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.address.testutil.TypicalWishes.ALICE;
import static seedu.address.testutil.TypicalWishes.HOON;
import static seedu.address.testutil.TypicalWishes.IDA;
import static seedu.address.testutil.TypicalWishes.getTypicalWishBook;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyWishBook;
import seedu.address.model.WishBook;

public class XmlWishBookStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "XmlWishBookStorageTest");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readWishBook_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readWishBook(null);
    }

    private java.util.Optional<ReadOnlyWishBook> readWishBook(String filePath) throws Exception {
        return new XmlWishBookStorage(Paths.get(filePath)).readWishBook(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readWishBook("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readWishBook("NotXmlFormatWishBook.xml");

        /* IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
         * That means you should not have more than one exception test in one method
         */
    }

    @Test
    public void readWishBook_invalidWishWishBook_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readWishBook("invalidWishWishBook.xml");
    }

    @Test
    public void readWishBook_invalidAndValidWishWishBook_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readWishBook("invalidAndValidWishWishBook.xml");
    }

    @Test
    public void readAndSaveWishBook_allInOrder_success() throws Exception {
        Path filePath = testFolder.getRoot().toPath().resolve("TempWishBook.xml");
        WishBook original = getTypicalWishBook();
        XmlWishBookStorage xmlWishBookStorage = new XmlWishBookStorage(filePath);

        //Save in new file and read back
        xmlWishBookStorage.saveWishBook(original, filePath);
        ReadOnlyWishBook readBack = xmlWishBookStorage.readWishBook(filePath).get();
        assertEquals(original, new WishBook(readBack));

        //Modify data, overwrite exiting file, and read back
        original.addWish(HOON);
        original.removeWish(ALICE);
        xmlWishBookStorage.saveWishBook(original, filePath);
        readBack = xmlWishBookStorage.readWishBook(filePath).get();
        assertEquals(original, new WishBook(readBack));

        //Save and read without specifying file path
        original.addWish(IDA);
        xmlWishBookStorage.saveWishBook(original); //file path not specified
        readBack = xmlWishBookStorage.readWishBook().get(); //file path not specified
        assertEquals(original, new WishBook(readBack));

    }

    @Test
    public void saveWishBook_nullAddressBook_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveWishBook(null, "SomeFile.xml");
    }

    /**
     * Saves {@code wishbook} at the specified {@code filePath}.
     */
    private void saveWishBook(ReadOnlyWishBook wishBook, String filePath) {
        try {
            new XmlWishBookStorage(Paths.get(filePath))
                    .saveWishBook(wishBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveWishBook_nullFilePath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveWishBook(new WishBook(), null);
    }

}
