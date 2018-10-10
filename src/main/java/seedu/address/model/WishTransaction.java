package seedu.address.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.tag.Tag;
import seedu.address.model.wish.Wish;

/**
 * Each {@code WishTransaction} represents a state of the WishBook.
 * A state contains logs of undeleted wish histories.
 */
public class WishTransaction implements ActionCommandListener<WishTransaction> {

    /**
     * Stores a log of wish histories for this current state.
     */
    private HashMap<String, LinkedList<Wish>> wishMap;

    /**
     * Logger associated with this class.
     */
    private final Logger logger;

    public WishTransaction() {
        this.logger = getLogger();
        this.wishMap = new HashMap<>();
    }

    /**
     * Creates a WishTransaction using a saved copy of {@code WishTransaction}.
     *
     * @param savedCopy saved copy of {@code WishTransaction} stored in file.
     */
    public WishTransaction(WishTransaction savedCopy) {
        this.logger = savedCopy.logger;
        resetData(savedCopy);
    }

    /**
     * Creates a WishTransaction using a {@code ReadOnlyWishBook}.
     *
     * @param readOnlyWishBook referenced data-containing {@code ReadOnlyWishBook}.
     */
    public WishTransaction(ReadOnlyWishBook readOnlyWishBook) {
        this();
        extractData(readOnlyWishBook);
    }

    /**
     * Constructor to be called when converting XmlWishTransactions object to a WishTransaction object.
     */
    public WishTransaction(HashMap<String, LinkedList<Wish>> wishMap) {
        this.logger = getLogger();
        this.wishMap = wishMap;
    }

    private Logger getLogger() {
        return LogsCenter.getLogger(WishTransaction.class);
    }

    /**
     * Extracts data from a {@code ReadOnlyWishBook} object and seeds that data into {@code wishMap}.
     * @param wishBook object containing data to seed this object with.
     */
    protected void extractData(ReadOnlyWishBook wishBook) {
        logger.info("Extracting data from ReadOnlyWishBook");
        for (Wish wish : wishBook.getWishList()) {
            addWish(wish);
        }
    }

    /**
     * Adds a wish to {@code wishMap} using {@code wish} full name as key.
     * @param wish
     */
    @Override
    public void addWish(Wish wish) {
        String wishName = getKey(wish);
        LinkedList<Wish> wishList = updateWishes(getWishList(wishName), wish);
        setValueOfKey(wish, wishList);
    }

    /**
     * Returns the key corresponding to this wish.
     * @param wish queried wish.
     * @return key for the corresponding wish.
     */
    private String getKey(Wish wish) {
        return wish.getName().fullName;
    }

    /**
     * Retrieves the wishlist stored at {@code key}.
     * @param key name of the wish.
     * @return wishlist stored at {@code key}.
     */
    private LinkedList<Wish> getWishList(String key) {
        return this.wishMap.getOrDefault(key, new LinkedList<>());
    }

    /**
     * Sets the value of an existing wish to {@code wishes}.
     * @param existing an existing wish.
     * @param wishes value to be changed to.
     */
    private void setValueOfKey(Wish existing, LinkedList<Wish> wishes) {
        this.wishMap.put(getKey(existing), wishes);
    }

    /**
     * Appends the updated wish to the log of saving history for this wish.
     * @param existingWishes log of saving history.
     * @param editedWish wish to be updated to.
     * @return an updated log of saving history.
     */
    private LinkedList<Wish> updateWishes(LinkedList<Wish> existingWishes, Wish editedWish) {
        existingWishes.add(editedWish);
        return existingWishes;
    }

    /**
     * Replaces the given wish {@code target} in the list with {@code editedWish}.
     * {@code target} must exist in the wish book.
     * The wish identity of {@code editedWish} must not be the same as another existing wish in the wish book.
     */
    @Override
    public void updateWish(Wish target, Wish editedWish) {
        // get a reference to the stored wishes
        LinkedList<Wish> wishes = getWishList(getKey(target));
        // change the key of the target wish
        changeKey(target, editedWish);
        // update the stored wishes
        setValueOfKey(editedWish, updateWishes(wishes, editedWish));
    }

    /**
     * Changes the key for the entry of {@code existing} to key of {@code newWish}.
     * Assumption: {@code existing} must be an existing wish in the map.
     *
     * @param existing existing wish in the wishmap.
     * @param newWish wish to be changed to.
     */
    private void changeKey(Wish existing, Wish newWish) {
        wishMap.remove(getKey(existing));
        wishMap.put(getKey(newWish), null);
    }

    /**
     * @see WishTransaction#removeWish(String).
     */
    @Override
    public void removeWish(Wish wish) {
        removeWish(getKey(wish));
    }

    /**
     * Removes the wish specified by {@code key}.
     *
     * @param key name of wish to remove.
     * @throws NoSuchElementException if key does not exist.
     */
    private void removeWish(String key) throws NoSuchElementException {
        wishMap.remove(key);
    }

    /**
     * Changes recorded log of wish histories for the current state to the {@code newData}.
     *
     * @param newData Revisioned log of wish histories.
     */
    @Override
    public void resetData(WishTransaction newData) {
        setWishMap(newData.wishMap);
    }

    /**
     * @see WishTransaction#resetData(WishTransaction)
     */
    @Override
    public void resetData() {
        resetData(new WishTransaction());
    }

    /**
     * Responds to request to remove tag from all wishes.
     * @param tag tag to be removed.
     */
    @Override
    public void removeTagFromAll(Tag tag) {
        for (Map.Entry<String, LinkedList<Wish>> entries : wishMap.entrySet()) {
            LinkedList<Wish> wishes = entries.getValue();
            // associated wish has a recorded history
            if (wishes != null) {
                removeTagIfPresent(tag, wishes);
            }
        }
    }

    /**
     * Removes the given {@code tag} from the given list of {@code wish}es if the tag is present.
     * @param tag tag to be removed.
     * @param wishes list of wishes to be searched for tag.
     */
    private void removeTagIfPresent(Tag tag, LinkedList<Wish> wishes) {
        Wish mostRecent = getMostRecentWish(wishes);
        if (hasTag(tag, mostRecent)) {
            removeTag(tag, mostRecent);
        }
    }

    /**
     * Returns the most recent wish recorded.
     * @param wishes list to source for the most recent wish.
     * @return the most recent wish in {@code wishes}.
     */
    private Wish getMostRecentWish(LinkedList<Wish> wishes) {
        return wishes.get(wishes.size() - 1);
    }

    /**
     * Checks if the given wish contains the given tag.
     * @param tag to be checked in the wish.
     * @param mostRecent wish to be checked for tag.
     * @return true if the tag is found in the wish, false otherwise.
     */
    private boolean hasTag(Tag tag, Wish mostRecent) {
        return mostRecent.getTags().contains(tag);
    }

    /**
     * Removes the given tag from the given wish.
     * @param tag tag to be removed.
     * @param mostRecent wish to remove the tag from.
     */
    private void removeTag(Tag tag, Wish mostRecent) {
        Set<Tag> updatedTags = mostRecent.getTags();
        updatedTags.remove(tag);
        updateWish(mostRecent, getUpdatedWish(updatedTags, mostRecent));
    }

    /**
     * Returns a new wish with the tags changed to {@code updatedTags}.
     * @param updatedTags field to update.
     * @param target wish to be updated.
     * @return a new updated wish.
     */
    private Wish getUpdatedWish(Set<Tag> updatedTags, Wish target) {
        return new Wish(target.getName(), target.getPrice(), target.getEmail(),
                target.getUrl(), target.getSavedAmount(), target.getRemark(), updatedTags);
    }

    /**
     * Sets the current state's wish histories to {@code wishMap}.
     * @param wishMap updated wish history log.
     */
    public void setWishMap(HashMap<String, LinkedList<Wish>> wishMap) {
        this.wishMap = wishMap;
    }

    public HashMap<String, LinkedList<Wish>> getWishMap() {
        return wishMap;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WishTransaction) {
            for (Map.Entry<String, LinkedList<Wish>> entries : ((WishTransaction) obj).wishMap.entrySet()) {
                if (!wishMap.containsKey(entries.getKey()) || !wishMap.containsValue(entries.getValue())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        wishMap.entrySet().forEach(entry -> {
            System.out.println(entry.getKey());
            entry.getValue().forEach(System.out::println);
        });
        return null;
    }
}
