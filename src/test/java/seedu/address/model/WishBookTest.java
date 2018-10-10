package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_URL_BOB;
import static seedu.address.testutil.TypicalWishes.ALICE;
import static seedu.address.testutil.TypicalWishes.getTypicalWishBook;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.wish.Wish;
import seedu.address.model.wish.exceptions.DuplicateWishException;
import seedu.address.testutil.WishBuilder;

public class WishBookTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final WishBook wishBook = new WishBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), wishBook.getWishList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        wishBook.resetData(null);
    }

    @Test
    public void resetData_withValidReadOnlyWishBook_replacesData() {
        WishBook newData = getTypicalWishBook();
        wishBook.resetData(newData);
        assertEquals(newData, wishBook);
    }

    @Test
    public void resetData_withDuplicateWishes_throwsDuplicateWishException() {
        // Two Wishes with the same identity fields
        Wish editedAlice = new WishBuilder(ALICE).withUrl(VALID_URL_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Wish> newWishes = Arrays.asList(ALICE, editedAlice);
        WishBookStub newData = new WishBookStub(newWishes);

        thrown.expect(DuplicateWishException.class);
        wishBook.resetData(newData);
    }

    @Test
    public void hasWish_nullWish_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        wishBook.hasWish(null);
    }

    @Test
    public void hasWish_wishNotInWishBook_returnsFalse() {
        assertFalse(wishBook.hasWish(ALICE));
    }

    @Test
    public void hasWish_wishInWishBook_returnsTrue() {
        wishBook.addWish(ALICE);
        assertTrue(wishBook.hasWish(ALICE));
    }

    @Test
    public void hasWish_wishWithSameIdentityFieldsInWishBook_returnsTrue() {
        wishBook.addWish(ALICE);
        Wish editedAlice = new WishBuilder(ALICE).withUrl(VALID_URL_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(wishBook.hasWish(editedAlice));
    }

    @Test
    public void getWishList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        wishBook.getWishList().remove(0);
    }

    /**
     * A stub ReadOnlyWishBook whose wishes list can violate interface constraints.
     */
    private static class WishBookStub implements ReadOnlyWishBook {
        private final ObservableList<Wish> wishes = FXCollections.observableArrayList();

        WishBookStub(Collection<Wish> wishes) {
            this.wishes.setAll(wishes);
        }

        @Override
        public ObservableList<Wish> getWishList() {
            return wishes;
        }
    }

}
