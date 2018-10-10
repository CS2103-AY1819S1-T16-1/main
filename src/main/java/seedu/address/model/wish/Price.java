package seedu.address.model.wish;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Wish's phone number in the wish book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPrice(String)}
 */
public class Price {

    public static final String MESSAGE_PRICE_CONSTRAINTS =
            "Phone numbers should only contain numbers, and at most two numbers after the decimal point.";
    public static final String PRICE_VALIDATION_REGEX = "[0-9]+([,.][0-9]{1,2})?";
    public final Double value;

    /**
     * Constructs a {@code Price}.
     *
     * @param phone A valid phone number.
     */
    public Price(String phone) {
        requireNonNull(phone);
        checkArgument(isValidPrice(phone), MESSAGE_PRICE_CONSTRAINTS);
        value = Double.parseDouble(phone); // TO-DO: check before allowing.
    }

    /**
     * Returns true if a given string is a valid phone number.
     */
    public static boolean isValidPrice(String test) {
        return test.matches(PRICE_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return String.format("%.2f", value);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Price // instanceof handles nulls
                && value.equals(((Price) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
