package seedu.address.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_WISHES;
import static seedu.address.testutil.TypicalWishes.ALICE;
import static seedu.address.testutil.TypicalWishes.BENSON;

import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.wish.NameContainsKeywordsPredicate;
import seedu.address.testutil.WishBookBuilder;

public class ModelManagerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ModelManager modelManager = new ModelManager();

    @Test
    public void hasWish_nullWish_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.hasWish(null);
    }

    @Test
    public void hasWish_wishNotInWishBook_returnsFalse() {
        assertFalse(modelManager.hasWish(ALICE));
    }

    @Test
    public void hasWish_wishInWishBook_returnsTrue() {
        modelManager.addWish(ALICE);
        assertTrue(modelManager.hasWish(ALICE));
    }

    @Test
    public void getFilteredWishList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        modelManager.getFilteredWishList().remove(0);
    }

    @Test
    public void equals() {
        WishBook wishBook = new WishBookBuilder().withWish(ALICE).withWish(BENSON).build();
        WishTransaction wishTransaction = new WishTransaction(wishBook);
        WishBook differentWishBook = new WishBook();
        WishTransaction differentWishTransaction = new WishTransaction();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(wishBook, wishTransaction, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(wishBook, wishTransaction, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different wishBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentWishBook, differentWishTransaction, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredWishList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(wishBook, wishTransaction, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredWishList(PREDICATE_SHOW_ALL_WISHES);

        // different userPrefs -> returns true
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setWishBookFilePath(Paths.get("differentFilePath"));
        assertTrue(modelManager.equals(new ModelManager(wishBook, wishTransaction, differentUserPrefs)));
    }
}
