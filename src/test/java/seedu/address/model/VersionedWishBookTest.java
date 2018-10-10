package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalWishes.AMY;
import static seedu.address.testutil.TypicalWishes.BOB;
import static seedu.address.testutil.TypicalWishes.CARL;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.address.model.versionedmodels.VersionedWishBook;
import seedu.address.testutil.WishBookBuilder;

public class VersionedWishBookTest {

    private final ReadOnlyWishBook wishBookWithAmy = new WishBookBuilder().withWish(AMY).build();
    private final ReadOnlyWishBook wishBookWithBob = new WishBookBuilder().withWish(BOB).build();
    private final ReadOnlyWishBook wishBookWithCarl = new WishBookBuilder().withWish(CARL).build();
    private final ReadOnlyWishBook emptyWishBook = new WishBookBuilder().build();

    @Test
    public void commit_singleWishBook_noStatesRemovedCurrentStateSaved() {
        VersionedWishBook versionedWishBook = prepareWishBookList(emptyWishBook);
        versionedWishBook.commit();
        assertWishBookListStatus(versionedWishBook,
                Collections.singletonList(emptyWishBook),
                emptyWishBook,
                Collections.emptyList());
    }

    @Test
    public void commit_multipleWishBookPointerAtEndOfStateList_noStatesRemovedCurrentStateSaved() {
        VersionedWishBook versionedWishBook = prepareWishBookList(
                emptyWishBook, wishBookWithAmy, wishBookWithBob);

        versionedWishBook.commit();
        assertWishBookListStatus(versionedWishBook,
                Arrays.asList(emptyWishBook, wishBookWithAmy, wishBookWithBob),
                wishBookWithBob,
                Collections.emptyList());
    }

    @Test
    public void commit_multipleWishBookPointerNotAtEndOfStateList_statesAfterPointerRemovedCurrentStateSaved() {
        VersionedWishBook versionedWishBook = prepareWishBookList(
                emptyWishBook, wishBookWithAmy, wishBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedWishBook, 2);

        versionedWishBook.commit();
        assertWishBookListStatus(versionedWishBook,
                Collections.singletonList(emptyWishBook),
                emptyWishBook,
                Collections.emptyList());
    }

    @Test
    public void canUndo_multipleWishBookPointerAtEndOfStateList_returnsTrue() {
        VersionedWishBook versionedWishBook = prepareWishBookList(
                emptyWishBook, wishBookWithAmy, wishBookWithBob);

        assertTrue(versionedWishBook.canUndo());
    }

    @Test
    public void canUndo_multipleWishBookPointerAtStartOfStateList_returnsTrue() {
        VersionedWishBook versionedWishBook = prepareWishBookList(
                emptyWishBook, wishBookWithAmy, wishBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedWishBook, 1);

        assertTrue(versionedWishBook.canUndo());
    }

    @Test
    public void canUndo_singleWishBook_returnsFalse() {
        VersionedWishBook versionedWishBook = prepareWishBookList(emptyWishBook);

        assertFalse(versionedWishBook.canUndo());
    }

    @Test
    public void canUndo_multipleWishBookPointerAtStartOfStateList_returnsFalse() {
        VersionedWishBook versionedWishBook = prepareWishBookList(
                emptyWishBook, wishBookWithAmy, wishBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedWishBook, 2);

        assertFalse(versionedWishBook.canUndo());
    }

    @Test
    public void canRedo_multipleWishBookPointerNotAtEndOfStateList_returnsTrue() {
        VersionedWishBook versionedWishBook = prepareWishBookList(
                emptyWishBook, wishBookWithAmy, wishBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedWishBook, 1);

        assertTrue(versionedWishBook.canRedo());
    }

    @Test
    public void canRedo_multipleWishBookPointerAtStartOfStateList_returnsTrue() {
        VersionedWishBook versionedWishBook = prepareWishBookList(
                emptyWishBook, wishBookWithAmy, wishBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedWishBook, 2);

        assertTrue(versionedWishBook.canRedo());
    }

    @Test
    public void canRedo_singleWishBook_returnsFalse() {
        VersionedWishBook versionedWishBook = prepareWishBookList(emptyWishBook);

        assertFalse(versionedWishBook.canRedo());
    }

    @Test
    public void canRedo_multipleWishBookPointerAtEndOfStateList_returnsFalse() {
        VersionedWishBook versionedWishBook = prepareWishBookList(
                emptyWishBook, wishBookWithAmy, wishBookWithBob);

        assertFalse(versionedWishBook.canRedo());
    }

    @Test
    public void undo_multipleWishBookPointerAtEndOfStateList_success() {
        VersionedWishBook versionedWishBook = prepareWishBookList(
                emptyWishBook, wishBookWithAmy, wishBookWithBob);

        versionedWishBook.undo();
        assertWishBookListStatus(versionedWishBook,
                Collections.singletonList(emptyWishBook),
                wishBookWithAmy,
                Collections.singletonList(wishBookWithBob));
    }

    @Test
    public void undo_multipleWishBookPointerNotAtStartOfStateList_success() {
        VersionedWishBook versionedWishBook = prepareWishBookList(
                emptyWishBook, wishBookWithAmy, wishBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedWishBook, 1);

        versionedWishBook.undo();
        assertWishBookListStatus(versionedWishBook,
                Collections.emptyList(),
                emptyWishBook,
                Arrays.asList(wishBookWithAmy, wishBookWithBob));
    }

    @Test
    public void undo_singleWishBook_throwsNoUndoableStateException() {
        VersionedWishBook versionedWishBook = prepareWishBookList(emptyWishBook);

        assertThrows(VersionedWishBook.NoUndoableStateException.class, versionedWishBook::undo);
    }

    @Test
    public void undo_multipleWishBookPointerAtStartOfStateList_throwsNoUndoableStateException() {
        VersionedWishBook versionedWishBook = prepareWishBookList(
                emptyWishBook, wishBookWithAmy, wishBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedWishBook, 2);

        assertThrows(VersionedWishBook.NoUndoableStateException.class, versionedWishBook::undo);
    }

    @Test
    public void redo_multipleWishBookPointerNotAtEndOfStateList_success() {
        VersionedWishBook versionedWishBook = prepareWishBookList(
                emptyWishBook, wishBookWithAmy, wishBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedWishBook, 1);

        versionedWishBook.redo();
        assertWishBookListStatus(versionedWishBook,
                Arrays.asList(emptyWishBook, wishBookWithAmy),
                wishBookWithBob,
                Collections.emptyList());
    }

    @Test
    public void redo_multipleWishBookPointerAtStartOfStateList_success() {
        VersionedWishBook versionedWishBook = prepareWishBookList(
                emptyWishBook, wishBookWithAmy, wishBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedWishBook, 2);

        versionedWishBook.redo();
        assertWishBookListStatus(versionedWishBook,
                Collections.singletonList(emptyWishBook),
                wishBookWithAmy,
                Collections.singletonList(wishBookWithBob));
    }

    @Test
    public void redo_singleWishBook_throwsNoRedoableStateException() {
        VersionedWishBook versionedWishBook = prepareWishBookList(emptyWishBook);

        assertThrows(VersionedWishBook.NoRedoableStateException.class, versionedWishBook::redo);
    }

    @Test
    public void redo_multipleWishBookPointerAtEndOfStateList_throwsNoRedoableStateException() {
        VersionedWishBook versionedWishBook = prepareWishBookList(
                emptyWishBook, wishBookWithAmy, wishBookWithBob);

        assertThrows(VersionedWishBook.NoRedoableStateException.class, versionedWishBook::redo);
    }

    @Test
    public void equals() {
        VersionedWishBook versionedWishBook = prepareWishBookList(wishBookWithAmy, wishBookWithBob);

        // same values -> returns true
        VersionedWishBook copy = prepareWishBookList(wishBookWithAmy, wishBookWithBob);
        assertTrue(versionedWishBook.equals(copy));

        // same object -> returns true
        assertTrue(versionedWishBook.equals(versionedWishBook));

        // null -> returns false
        assertFalse(versionedWishBook.equals(null));

        // different types -> returns false
        assertFalse(versionedWishBook.equals(1));

        // different state list -> returns false
        VersionedWishBook differentWishBookList = prepareWishBookList(wishBookWithBob, wishBookWithCarl);
        assertFalse(versionedWishBook.equals(differentWishBookList));

        // different current pointer index -> returns false
        VersionedWishBook differentCurrentStatePointer = prepareWishBookList(
                wishBookWithAmy, wishBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedWishBook, 1);
        assertFalse(versionedWishBook.equals(differentCurrentStatePointer));
    }

    /**
     * Asserts that {@code versionedWishBook} is currently pointing at {@code expectedCurrentState},
     * states before {@code versionedWishBook#currentStatePointer} is equal to {@code expectedStatesBeforePointer},
     * and states after {@code versionedWishBook#currentStatePointer} is equal to {@code expectedStatesAfterPointer}.
     */
    private void assertWishBookListStatus(VersionedWishBook versionedWishBook,
                                          List<ReadOnlyWishBook> expectedStatesBeforePointer,
                                          ReadOnlyWishBook expectedCurrentState,
                                          List<ReadOnlyWishBook> expectedStatesAfterPointer) {
        // check state currently pointing at is correct
        assertEquals(new WishBook(versionedWishBook), expectedCurrentState);

        // shift pointer to start of state list
        while (versionedWishBook.canUndo()) {
            versionedWishBook.undo();
        }

        // check states before pointer are correct
        for (ReadOnlyWishBook expectedWishBook : expectedStatesBeforePointer) {
            assertEquals(expectedWishBook, new WishBook(versionedWishBook));
            versionedWishBook.redo();
        }

        // check states after pointer are correct
        for (ReadOnlyWishBook expectedWishBook : expectedStatesAfterPointer) {
            versionedWishBook.redo();
            assertEquals(expectedWishBook, new WishBook(versionedWishBook));
        }

        // check that there are no more states after pointer
        assertFalse(versionedWishBook.canRedo());

        // revert pointer to original position
        expectedStatesAfterPointer.forEach(unused -> versionedWishBook.undo());
    }

    /**
     * Creates and returns a {@code VersionedWishBook} with the {@code wishBookStates} added into it, and the
     * {@code VersionedWishBook#currentStatePointer} at the end of list.
     */
    private VersionedWishBook prepareWishBookList(ReadOnlyWishBook... wishBookStates) {
        assertFalse(wishBookStates.length == 0);

        VersionedWishBook versionedWishBook = new VersionedWishBook(wishBookStates[0]);
        for (int i = 1; i < wishBookStates.length; i++) {
            versionedWishBook.resetData(wishBookStates[i]);
            versionedWishBook.commit();
        }

        return versionedWishBook;
    }

    /**
     * Shifts the {@code versionedWishBook#currentStatePointer} by {@code count} to the left of its list.
     */
    private void shiftCurrentStatePointerLeftwards(VersionedWishBook versionedWishBook, int count) {
        for (int i = 0; i < count; i++) {
            versionedWishBook.undo();
        }
    }
}
