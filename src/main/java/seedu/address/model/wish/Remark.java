package seedu.address.model.wish;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Wish's remark in the wish book.
 */
public class Remark {
    public static final String MESSAGE_REMARK_CONSTRAINTS =
            "Remarks can take any values, and they can be blank";

    public final String value;

    /**
     * Constructs a {@code Remark}.
     *
     * @param remark A valid remark.
     */
    public Remark(String remark) {
        requireNonNull(remark);
        this.value = remark;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Remark)
                && this.value.equals(((Remark) other).value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
