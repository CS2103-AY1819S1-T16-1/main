package seedu.address.model;

import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.model.wish.Wish;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Wish> PREDICATE_SHOW_ALL_WISHES = unused -> true;

    /** Returns the WishTransaction */
    WishTransaction getWishTransaction();

    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyWishBook newData);

    /** Returns the WishBook */
    ReadOnlyWishBook getWishBook();

    /**
     * Returns true if a wish with the same identity as {@code wish} exists in the wish book.
     */
    boolean hasWish(Wish wish);

    /**
     * Deletes the given wish.
     * The wish must exist in the wish book.
     */
    void deleteWish(Wish target);

    /**
     * Adds the given wish.
     * {@code wish} must not already exist in the wish book.
     */
    void addWish(Wish wish);

    /**
     * Replaces the given wish {@code target} with {@code editedWish}.
     * {@code target} must exist in the wish book.
     * The wish identity of {@code editedWish} must not be the same as another existing wish in the wish book.
     */
    void updateWish(Wish target, Wish editedWish);

    /** Returns an unmodifiable view of the filtered wish list */
    ObservableList<Wish> getFilteredWishList();

    /**
     * Updates the filter of the filtered wish list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredWishList(Predicate<Wish> predicate);

    /**
     * Returns true if the model has previous wish book states to restore.
     */
    boolean canUndoWishBook();

    /**
     * Returns true if the model has undone wish book states to restore.
     */
    boolean canRedoWishBook();

    /**
     * Restores the model's wish book to its previous state.
     */
    void undoWishBook();

    /**
     * Restores the model's wish book to its previously undone state.
     */
    void redoWishBook();

    /**
     * Saves the current wish book state for undo/redo.
     */
    void commitWishBook();
}
