package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalWishes.getTypicalWishBook;
import static seedu.address.testutil.TypicalWishes.getTypicalWishTransaction;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.events.model.WishBookChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.model.ReadOnlyWishBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.WishBook;
import seedu.address.model.WishTransaction;
import seedu.address.ui.testutil.EventsCollectorRule;

public class StorageManagerTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private StorageManager storageManager;

    @Before
    public void setUp() {
        XmlWishBookStorage wishBookStorage = new XmlWishBookStorage(getTempFilePath("ab"));
        XmlWishTransactionStorage wishTransactionStorage = new XmlWishTransactionStorage(getTempFilePath("wt"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageManager = new StorageManager(wishBookStorage, wishTransactionStorage, userPrefsStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.getRoot().toPath().resolve(fileName);
    }

    // ================ UserPrefs tests ==============================

    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(300, 600, 4, 6);
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    // ================ WishBook tests ==============================

    @Test
    public void wishBookReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link XmlWishBookStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link XmlWishBookStorageTest} class.
         */
        WishBook original = getTypicalWishBook();
        storageManager.saveWishBook(original);
        ReadOnlyWishBook retrieved = storageManager.readWishBook().get();
        assertEquals(original, new WishBook(retrieved));
    }

    @Test
    public void getWishBookFilePath() {
        assertNotNull(storageManager.getWishBookFilePath());
    }

    @Test
    public void handleWishBookChangedEvent_exceptionThrown_eventRaised() {
        // Create a StorageManager while injecting a stub that  throws an exception when the save method is called
        Storage storage = new StorageManager(new XmlWishBookStorageExceptionThrowingStub(Paths.get("dummy")),
                                             new JsonUserPrefsStorage(Paths.get("dummy")));
        storage.handleWishBookChangedEvent(new WishBookChangedEvent(new WishBook()));
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof DataSavingExceptionEvent);
    }

    // ================ WishTransaction tests ==============================

    @Test
    public void wishTransactionReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link XmlWishTransactionStorage} class.
         * More extensive testing of WishTransaction saving/reading is done in {@link XmlWishTransactionStorageTest}.
         */
        WishTransaction original = getTypicalWishTransaction();
        storageManager.saveWishTransaction(original);
        WishTransaction retrieved = storageManager.readWishTransaction().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void getWishTransactionFilePath() {
        assertNotNull(storageManager.getWishTransactionFilePath());
    }

    /**
     * A Stub class to throw an exception when the save method is called
     */
    class XmlWishBookStorageExceptionThrowingStub extends XmlWishBookStorage {

        public XmlWishBookStorageExceptionThrowingStub(Path filePath) {
            super(filePath);
        }

        @Override
        public void backupWishBook(ReadOnlyWishBook wishBook, Path filePath) throws IOException {
            throw new IOException("dummy exception");
        }

        @Override
        public void saveWishBook(ReadOnlyWishBook wishBook, Path filePath) throws IOException {
            throw new IOException("dummy exception");
        }
    }


}
