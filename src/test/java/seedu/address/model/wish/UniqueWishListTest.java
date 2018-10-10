package seedu.address.model.wish;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_URL_BOB;
import static seedu.address.testutil.TypicalWishes.ALICE;
import static seedu.address.testutil.TypicalWishes.BOB;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.wish.exceptions.DuplicateWishException;
import seedu.address.model.wish.exceptions.WishNotFoundException;
import seedu.address.testutil.WishBuilder;

public class UniqueWishListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final UniqueWishList uniqueWishList = new UniqueWishList();

    @Test
    public void contains_nullWish_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueWishList.contains(null);
    }

    @Test
    public void contains_wishNotInList_returnsFalse() {
        assertFalse(uniqueWishList.contains(ALICE));
    }

    @Test
    public void contains_wishInList_returnsTrue() {
        uniqueWishList.add(ALICE);
        assertTrue(uniqueWishList.contains(ALICE));
    }

    @Test
    public void contains_wishWithSameIdentityFieldsInList_returnsTrue() {
        uniqueWishList.add(ALICE);
        Wish editedAlice = new WishBuilder(ALICE).withUrl(VALID_URL_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(uniqueWishList.contains(editedAlice));
    }

    @Test
    public void add_nullWish_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueWishList.add(null);
    }

    @Test
    public void add_duplicateWish_throwsDuplicateWishException() {
        uniqueWishList.add(ALICE);
        thrown.expect(DuplicateWishException.class);
        uniqueWishList.add(ALICE);
    }

    @Test
    public void setWish_nullTargetWish_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueWishList.setWish(null, ALICE);
    }

    @Test
    public void setWish_nullEditedWish_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueWishList.setWish(ALICE, null);
    }

    @Test
    public void setWish_targetWishNotInList_throwsWishNotFoundException() {
        thrown.expect(WishNotFoundException.class);
        uniqueWishList.setWish(ALICE, ALICE);
    }

    @Test
    public void setWish_editedWishIsSameWish_success() {
        uniqueWishList.add(ALICE);
        uniqueWishList.setWish(ALICE, ALICE);
        UniqueWishList expectedUniqueWishList = new UniqueWishList();
        expectedUniqueWishList.add(ALICE);
        assertEquals(expectedUniqueWishList, uniqueWishList);
    }

    @Test
    public void setWish_editedWishHasSameIdentity_success() {
        uniqueWishList.add(ALICE);
        Wish editedAlice = new WishBuilder(ALICE).withUrl(VALID_URL_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        uniqueWishList.setWish(ALICE, editedAlice);
        UniqueWishList expectedUniqueWishList = new UniqueWishList();
        expectedUniqueWishList.add(editedAlice);
        assertEquals(expectedUniqueWishList, uniqueWishList);
    }

    @Test
    public void setWish_editedWishHasDifferentIdentity_success() {
        uniqueWishList.add(ALICE);
        uniqueWishList.setWish(ALICE, BOB);
        UniqueWishList expectedUniqueWishList = new UniqueWishList();
        expectedUniqueWishList.add(BOB);
        assertEquals(expectedUniqueWishList, uniqueWishList);
    }

    @Test
    public void setWish_editedWishHasNonUniqueIdentity_throwsDuplicateWishException() {
        uniqueWishList.add(ALICE);
        uniqueWishList.add(BOB);
        thrown.expect(DuplicateWishException.class);
        uniqueWishList.setWish(ALICE, BOB);
    }

    @Test
    public void remove_nullWish_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueWishList.remove(null);
    }

    @Test
    public void remove_wishDoesNotExist_throwsWishNotFoundException() {
        thrown.expect(WishNotFoundException.class);
        uniqueWishList.remove(ALICE);
    }

    @Test
    public void remove_existingWish_removesWish() {
        uniqueWishList.add(ALICE);
        uniqueWishList.remove(ALICE);
        UniqueWishList expectedUniqueWishList = new UniqueWishList();
        assertEquals(expectedUniqueWishList, uniqueWishList);
    }

    @Test
    public void setWishs_nullUniqueWishList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueWishList.setWishes((UniqueWishList) null);
    }

    @Test
    public void setWishs_uniqueWishList_replacesOwnListWithProvidedUniqueWishList() {
        uniqueWishList.add(ALICE);
        UniqueWishList expectedUniqueWishList = new UniqueWishList();
        expectedUniqueWishList.add(BOB);
        uniqueWishList.setWishes(expectedUniqueWishList);
        assertEquals(expectedUniqueWishList, uniqueWishList);
    }

    @Test
    public void setWishs_nullList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueWishList.setWishes((List<Wish>) null);
    }

    @Test
    public void setWishs_list_replacesOwnListWithProvidedList() {
        uniqueWishList.add(ALICE);
        List<Wish> wishList = Collections.singletonList(BOB);
        uniqueWishList.setWishes(wishList);
        UniqueWishList expectedUniqueWishList = new UniqueWishList();
        expectedUniqueWishList.add(BOB);
        assertEquals(expectedUniqueWishList, uniqueWishList);
    }

    @Test
    public void setWishs_listWithDuplicateWishs_throwsDuplicateWishException() {
        List<Wish> listWithDuplicateWishes = Arrays.asList(ALICE, ALICE);
        thrown.expect(DuplicateWishException.class);
        uniqueWishList.setWishes(listWithDuplicateWishes);
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        uniqueWishList.asUnmodifiableObservableList().remove(0);
    }
}
