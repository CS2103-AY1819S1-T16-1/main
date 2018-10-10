package seedu.address.model.wish;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.wish.exceptions.DuplicateWishException;
import seedu.address.model.wish.exceptions.WishNotFoundException;

/**
 * A list of wishes that enforces uniqueness between its elements and does not allow nulls.
 * A wish is considered unique by comparing using {@code Wish#isSameWish(Wish)}. As such, adding and updating of
 * wishes uses Wish#isSameWish(Wish) for equality so as to ensure that the wish being added or updated is
 * unique in terms of identity in the UniqueWishList. However, the removal of a wish uses Wish#equals(Object) so
 * as to ensure that the wish with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Wish#isSameWish(Wish)
 */
public class UniqueWishList implements Iterable<Wish> {

    private final ObservableList<Wish> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent wish as the given argument.
     */
    public boolean contains(Wish toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameWish);
    }

    /**
     * Adds a wish to the list.
     * The wish must not already exist in the list.
     */
    public void add(Wish toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateWishException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the wish {@code target} in the list with {@code editedWish}.
     * {@code target} must exist in the list.
     * The wish identity of {@code editedWish} must not be the same as another existing wish in the list.
     */
    public void setWish(Wish target, Wish editedWish) {
        requireAllNonNull(target, editedWish);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new WishNotFoundException();
        }

        if (!target.isSameWish(editedWish) && contains(editedWish)) {
            throw new DuplicateWishException();
        }

        internalList.set(index, editedWish);
    }

    /**
     * Removes the equivalent wish from the list.
     * The wish must exist in the list.
     */
    public void remove(Wish toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new WishNotFoundException();
        }
    }

    public void setWishes(UniqueWishList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code wishes}.
     * {@code wishes} must not contain duplicate wishes.
     */
    public void setWishes(List<Wish> wishes) {
        requireAllNonNull(wishes);
        if (!wishesAreUnique(wishes)) {
            throw new DuplicateWishException();
        }

        internalList.setAll(wishes);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Wish> asUnmodifiableObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<Wish> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueWishList // instanceof handles nulls
                        && internalList.equals(((UniqueWishList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code wishes} contains only unique wishes.
     */
    private boolean wishesAreUnique(List<Wish> wishes) {
        for (int i = 0; i < wishes.size() - 1; i++) {
            for (int j = i + 1; j < wishes.size(); j++) {
                if (wishes.get(i).isSameWish(wishes.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
