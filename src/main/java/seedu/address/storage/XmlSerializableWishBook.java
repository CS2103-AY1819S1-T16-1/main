package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.ReadOnlyWishBook;
import seedu.address.model.WishBook;
import seedu.address.model.wish.Wish;

/**
 * An Immutable WishBook that is serializable to XML format
 */
@XmlRootElement(name = "wishbook")
public class XmlSerializableWishBook {

    public static final String MESSAGE_DUPLICATE_WISH = "Wish list contains duplicate wish(s).";

    @XmlElement
    private List<XmlAdaptedWish> wishes;

    /**
     * Creates an empty XmlSerializableWishBook.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableWishBook() {
        wishes = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public XmlSerializableWishBook(ReadOnlyWishBook src) {
        this();
        wishes.addAll(src.getWishList().stream().map(XmlAdaptedWish::new).collect(Collectors.toList()));
    }

    /**
     * Converts this wishBook into the model's {@code WishBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated or duplicates in the
     * {@code XmlAdaptedWish}.
     */
    public WishBook toModelType() throws IllegalValueException {
        WishBook wishBook = new WishBook();
        for (XmlAdaptedWish p : wishes) {
            Wish wish = p.toModelType();
            if (wishBook.hasWish(wish)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_WISH);
            }
            wishBook.addWish(wish);
        }
        return wishBook;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlSerializableWishBook)) {
            return false;
        }
        return wishes.equals(((XmlSerializableWishBook) other).wishes);
    }
}
