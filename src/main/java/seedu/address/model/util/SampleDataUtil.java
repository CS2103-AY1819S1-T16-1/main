package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.ReadOnlyWishBook;
import seedu.address.model.WishBook;
import seedu.address.model.WishTransaction;
import seedu.address.model.tag.Tag;
import seedu.address.model.wish.Email;
import seedu.address.model.wish.Name;
import seedu.address.model.wish.Price;
import seedu.address.model.wish.Remark;
import seedu.address.model.wish.SavedAmount;
import seedu.address.model.wish.Url;
import seedu.address.model.wish.Wish;

/**
 * Contains utility methods for populating {@code WishBook} with sample data.
 */
public class SampleDataUtil {
    public static final Remark SAMPLE_REMARK_EMPTY = new Remark("");
    public static Wish[] getSampleWishes() {
        return new Wish[] {
            new Wish(new Name("Apple iPhone X"), new Price("700.00"), new Email("alexyeoh@example.com"),
                    new Url("https://www.amazon.com/Apple-iPhone-Fully-Unlocked-32GB/dp/B0731HBTZ7"),
                    new SavedAmount("700.00"),
                    SAMPLE_REMARK_EMPTY, getTagSet("friends")),
            new Wish(new Name("Logitech K840 Mechanical Keyboard"), new Price("450.00"),
                    new Email("berniceyu@example.com"),
                    new Url("https://www.amazon.com/Logitech-Mechanical-Keyboard-Romer-Switches/dp/B071VHYZ62"),
                    new SavedAmount("12.00"),
                    SAMPLE_REMARK_EMPTY, getTagSet("colleagues", "friends")),
            new Wish(new Name("Nintendo Switch"), new Price("540.00"), new Email("charlotte@example.com"),
                    new Url("https://www.lazada.sg/products/nintendo-switch-neon-console-"
                            + "1-year-local-warranty-best-seller-i180040203-s230048296.html"),
                    new SavedAmount("20.00"),
                    SAMPLE_REMARK_EMPTY, getTagSet("neighbours")),
            new Wish(new Name("PS4 Pro"), new Price("1200.00"), new Email("lidavid@example.com"),
                    new Url("https://www.lazada.sg/products/sony-playstation-4-pro-1tb-console-"
                            + "local-stock-with-sony-warranty-i100009437-s100011973.html"),
                    new SavedAmount("170.00"),
                    SAMPLE_REMARK_EMPTY, getTagSet("family")),
            new Wish(new Name("EVGA 1080 Ti Graphics Card"), new Price("70.00"),
                    new Email("irfan@example.com"),
                    new Url("https://www.amazon.com/EVGA-GeForce-Gaming-GDDR5X-Technology/dp/B0762Q49NV"),
                    new SavedAmount("4.50"),
                    SAMPLE_REMARK_EMPTY, getTagSet("classmates")),
            new Wish(new Name("1TB SSD"), new Price("55.00"),
                    new Email("royb@example.com"),
                    new Url("https://www.amazon.com/gp/product/B07D998212"),
                    new SavedAmount("45.00"),
                    SAMPLE_REMARK_EMPTY, getTagSet("colleagues"))
        };
    }

    public static ReadOnlyWishBook getSampleWishBook() {
        WishBook sampleWb = new WishBook();
        for (Wish sampleWish : getSampleWishes()) {
            sampleWb.addWish(sampleWish);
        }
        return sampleWb;
    }

    public static WishTransaction getSampleWishTransaction() {
        return new WishTransaction(getSampleWishBook());
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }
}
