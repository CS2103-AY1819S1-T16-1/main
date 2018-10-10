package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.WishBookChangedEvent;
import seedu.address.model.tag.Tag;
import seedu.address.model.versionedmodels.VersionedWishBook;
import seedu.address.model.versionedmodels.VersionedWishTransaction;
import seedu.address.model.wish.Wish;
import seedu.address.model.wish.exceptions.DuplicateWishException;


/**
 * Represents the in-memory model of the wish book data.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedWishBook versionedWishBook;
    private final VersionedWishTransaction versionedWishTransaction;
    private final FilteredList<Wish> filteredWishes;

    /**
     * Initializes a ModelManager with the given wishBook and userPrefs.
     */
    public ModelManager(ReadOnlyWishBook wishBook, WishTransaction wishTransaction, UserPrefs userPrefs) {
        super();
        requireAllNonNull(wishBook, wishTransaction, userPrefs);

        logger.fine("Initializing wish book: " + wishBook
                + ", wish transaction: " + wishTransaction
                + ", user prefs " + userPrefs);

        versionedWishBook = new VersionedWishBook(wishBook);
        versionedWishTransaction = getWishTransaction(wishBook, wishTransaction);
        filteredWishes = new FilteredList<>(versionedWishBook.getWishList());
    }

    public ModelManager() {
        this(new WishBook(), new WishTransaction(), new UserPrefs());
    }

    /**
     * @see ModelManager#getWishTransaction(ReadOnlyWishBook, WishTransaction)
     */
    public VersionedWishTransaction getWishTransaction() {
        return getWishTransaction(versionedWishBook, versionedWishTransaction);
    }

    /**
     * Returns {@code VersionedWishTransaction} initialized with data from {@code wishBook} or {@code wishTransaction}.
     */
    private VersionedWishTransaction getWishTransaction(ReadOnlyWishBook wishBook, WishTransaction wishTransaction) {
        if (wishTransaction == null) {
            return initWishTransactionWithWishBookData(wishBook);
        } else {
            return new VersionedWishTransaction(wishTransaction);
        }
    }

    private VersionedWishTransaction initWishTransactionWithWishBookData(ReadOnlyWishBook wishBook) {
        return new VersionedWishTransaction(wishBook);
    }

    @Override
    public void resetData(ReadOnlyWishBook newData) {
        versionedWishBook.resetData(newData);
        versionedWishTransaction.resetData();
        indicateWishBookChanged();
    }

    @Override
    public ReadOnlyWishBook getWishBook() {
        return versionedWishBook;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateWishBookChanged() {
        raise(new WishBookChangedEvent(versionedWishBook));
    }

    @Override
    public boolean hasWish(Wish wish) {
        requireNonNull(wish);
        return versionedWishBook.hasWish(wish);
    }

    @Override
    public void deleteWish(Wish target) {
        versionedWishBook.removeWish(target);
        versionedWishTransaction.removeWish(target);
        indicateWishBookChanged();

        //TODO

    }

    @Override
    public void addWish(Wish wish) {
        versionedWishBook.addWish(wish);
        versionedWishTransaction.addWish(wish);
        updateFilteredWishList(PREDICATE_SHOW_ALL_WISHES);
        indicateWishBookChanged();
    }

    @Override
    public void updateWish(Wish target, Wish editedWish) {
        requireAllNonNull(target, editedWish);
        versionedWishTransaction.updateWish(target, editedWish);
        versionedWishBook.updateWish(target, editedWish);
        indicateWishBookChanged();
    }

    /**
     * Removes {@code tag} from all {@code wish}s in this {@code WishBook}.
     * @throws DuplicateWishException if there's a duplicate {@code Wish} in this {@code WishBook}.
     */
    public void deleteTag(Tag tag) throws DuplicateWishException {
        versionedWishBook.removeTagFromAll(tag);
        versionedWishTransaction.removeTagFromAll(tag);
        indicateWishBookChanged();
    }

    //=========== Filtered Wish List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Wish} backed by the internal list of
     * {@code versionedWishBook}
     */
    @Override
    public ObservableList<Wish> getFilteredWishList() {
        return FXCollections.unmodifiableObservableList(filteredWishes);
    }

    @Override
    public void updateFilteredWishList(Predicate<Wish> predicate) {
        requireNonNull(predicate);
        filteredWishes.setPredicate(predicate);
    }

    //=========== Undo/Redo =================================================================================

    @Override
    public boolean canUndoWishBook() {
        return versionedWishBook.canUndo();
    }

    @Override
    public boolean canRedoWishBook() {
        return versionedWishBook.canRedo();
    }

    @Override
    public void undoWishBook() {
        versionedWishBook.undo();
        versionedWishTransaction.undo();
        indicateWishBookChanged();
    }

    @Override
    public void redoWishBook() {
        versionedWishBook.redo();
        versionedWishTransaction.redo();
        indicateWishBookChanged();
    }

    @Override
    public void commitWishBook() {
        versionedWishBook.commit();
        versionedWishTransaction.commit();
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return versionedWishBook.equals(other.versionedWishBook)
                && filteredWishes.equals(other.filteredWishes);
    }

}
