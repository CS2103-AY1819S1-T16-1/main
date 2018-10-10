package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRICE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRICE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_URL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_URL_BOB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.WishBook;
import seedu.address.model.WishTransaction;
import seedu.address.model.wish.Wish;

/**
 * A utility class containing a list of {@code Wish} objects to be used in tests.
 */
public class TypicalWishes {

    public static final Wish ALICE = new WishBuilder().withName("Alice Pauline")
            .withUrl("https://www.lazada.sg/products/"
                    + "ps4-092-hori-real-arcade-pron-hayabusaps4ps3pc-i223784444-s340908955.html")
            .withEmail("alice@example.com")
            .withPrice("94.35")
            .withTags("friends")
            .withSavedAmountIncrement("0.00")
            .build();
    public static final Wish BENSON = new WishBuilder().withName("Benson Meier")
            .withUrl("https://www.amazon.com/Apple-iPhone-Fully-Unlocked-32GB/dp/B0731HBTZ7")
            .withEmail("johnd@example.com")
            .withPrice("98.76")
            .withTags("owesMoney", "friends")
            .withSavedAmountIncrement("0.00")
            .build();
    public static final Wish CARL = new WishBuilder().withName("Carl Kurz")
            .withPrice("98.76")
            .withEmail("heinz@example.com")
            .withUrl("https://www.amazon.com/EVGA-GeForce-Gaming-GDDR5X-Technology/dp/B0762Q49NV")
            .withSavedAmountIncrement("0.00")
            .build();
    public static final Wish DANIEL = new WishBuilder().withName("Daniel Meier")
            .withPrice("87.65")
            .withEmail("cornelia@example.com")
            .withUrl("https://www.amazon.com/Logitech-Mechanical-Keyboard-Romer-Switches/dp/B071VHYZ62")
            .withTags("friends")
            .withSavedAmountIncrement("0.00")
            .build();
    public static final Wish ELLE = new WishBuilder().withName("Elle Meyer")
            .withPrice("94.82")
            .withEmail("werner@example.com")
            .withUrl("https://www.amazon.com/EVGA-GeForce-Gaming-GDDR5X-Technology/dp/B0762Q49NV")
            .withSavedAmountIncrement("0.00")
            .build();
    public static final Wish FIONA = new WishBuilder().withName("Fiona Kunz")
            .withPrice("94.82")
            .withEmail("lydia@example.com")
            .withUrl("https://www.lazada.sg/products/"
                    + "nintendo-switch-neon-console-1-year-local-warranty-best-seller-i180040203-s230048296.html")
            .withSavedAmountIncrement("0.00")
            .build();
    public static final Wish GEORGE = new WishBuilder().withName("George Best")
            .withPrice("94.82")
            .withEmail("anna@example.com")
            .withUrl("https://www.amazon.com/EVGA-GeForce-Gaming-GDDR5X-Technology/dp/B0762Q49NV")
            .withSavedAmountIncrement("0.00")
            .build();

    // Manually added
    public static final Wish HOON = new WishBuilder().withName("Hoon Meier")
            .withPrice("84.82")
            .withEmail("stefan@example.com")
            .withUrl("https://www.amazon.com/EVGA-GeForce-Gaming-GDDR5X-Technology/dp/B0762Q49NV")
            .withSavedAmountIncrement("0.00")
            .build();
    public static final Wish IDA = new WishBuilder().withName("Ida Mueller")
            .withPrice("84.82")
            .withEmail("hans@example.com")
            .withUrl("https://www.amazon.com/EVGA-GeForce-Gaming-GDDR5X-Technology/dp/B0762Q49NV")
            .withSavedAmountIncrement("0.00")
            .build();

    // Manually added - Wish's details found in {@code CommandTestUtil}
    public static final Wish AMY = new WishBuilder().withName(VALID_NAME_AMY).withPrice(VALID_PRICE_AMY)
            .withEmail(VALID_EMAIL_AMY).withUrl(VALID_URL_AMY).withTags(VALID_TAG_FRIEND)
            .withSavedAmountIncrement("0.00").build();
    public static final Wish BOB = new WishBuilder().withName(VALID_NAME_BOB).withPrice(VALID_PRICE_BOB)
            .withEmail(VALID_EMAIL_BOB).withUrl(VALID_URL_BOB)
            .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).withSavedAmountIncrement("0.00")
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalWishes() {} // prevents instantiation1

    /**
     * Returns an {@code WishBook} with all the typical wishes.
     */
    public static WishBook getTypicalWishBook() {
        WishBook ab = new WishBook();
        for (Wish wish : getTypicalWishes()) {
            ab.addWish(wish);
        }
        return ab;
    }

    /**
     * Returns a {@code WishTransaction} with all the typical wish transactions.
     * Assumption: no previous wish transaction available other than current state of {@code WishBook}.
     */
    public static WishTransaction getTypicalWishTransaction() {
        return new WishTransaction(getTypicalWishBook());
    }

    public static List<Wish> getTypicalWishes() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
