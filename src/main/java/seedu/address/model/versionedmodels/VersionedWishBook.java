package seedu.address.model.versionedmodels;

import java.util.ArrayList;
import java.util.List;

import seedu.address.model.ReadOnlyWishBook;
import seedu.address.model.WishBook;

/**
 * {@code WishBook} that keeps track of its own history.
 */
public class VersionedWishBook extends WishBook implements VersionedModel {

    private final List<ReadOnlyWishBook> wishBookStateList;
    private int currentStatePointer;

    public VersionedWishBook(ReadOnlyWishBook initialState) {
        super(initialState);

        wishBookStateList = new ArrayList<>();
        wishBookStateList.add(new WishBook(initialState));
        currentStatePointer = 0;
    }

    /**
     * Saves a copy of the current {@code WishBook} state at the end of the state list.
     * Undone states are removed from the state list.
     */
    @Override
    public void commit() {
        removeStatesAfterCurrentPointer();
        wishBookStateList.add(new WishBook(this));
        currentStatePointer++;
    }

    private void removeStatesAfterCurrentPointer() {
        wishBookStateList.subList(currentStatePointer + 1, wishBookStateList.size()).clear();
    }

    /**
     * Restores the address book to its previous state.
     */
    @Override
    public void undo() {
        if (!canUndo()) {
            throw new NoUndoableStateException();
        }
        currentStatePointer--;
        resetData(wishBookStateList.get(currentStatePointer));
    }

    /**
     * Restores the address book to its previously undone state.
     */
    @Override
    public void redo() {
        if (!canRedo()) {
            throw new NoRedoableStateException();
        }
        currentStatePointer++;
        resetData(wishBookStateList.get(currentStatePointer));
    }

    /**
     * Returns true if {@code undo()} has address book states to undo.
     */
    public boolean canUndo() {
        return currentStatePointer > 0;
    }

    /**
     * Returns true if {@code redo()} has address book states to redo.
     */
    public boolean canRedo() {
        return currentStatePointer < wishBookStateList.size() - 1;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof VersionedWishBook)) {
            return false;
        }

        VersionedWishBook otherVersionedWishBook = (VersionedWishBook) other;

        // state check
        return super.equals(otherVersionedWishBook)
                && wishBookStateList.equals(otherVersionedWishBook.wishBookStateList)
                && currentStatePointer == otherVersionedWishBook.currentStatePointer;
    }

}
