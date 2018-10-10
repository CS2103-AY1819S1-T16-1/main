package seedu.address.commons.core.amount;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.AppUtil;

/**
 * Represents an amount of money. It can be negative.
 * Guarantees: immutable
 */
public class Amount {

    private static final String AMOUNT_REGEX = "[+-]?[0-9]{1,14}([.]{1}[0-9]{1,2})?";
    public final Double value;

    /**
     * Constructs a {@code Amount}.
     *
     * @param amount A valid amount {@code String}.
     */
    public Amount(String amount) throws IllegalArgumentException {
        requireNonNull(amount);
        AppUtil.checkArgument(isValidAmount(amount));
        value = Double.parseDouble(amount);
    }

    /**
     * Returns true if a given string is a valid Amount.
     *
     * @param possibleAmount A possibly valid Amount as a {@code String}
     */
    public static boolean isValidAmount(String possibleAmount) {
        return possibleAmount.matches(AMOUNT_REGEX);
    }

    /**
     * Returns the sum of two Amounts.
     */
    public static Amount add(Amount a1, Amount a2) {
        requireNonNull(a1);
        requireNonNull(a2);
        return new Amount(Double.toString(a1.value + a2.value));
    }

    /**
     * Returns an absolute, non-negative Amount.
     */
    public Amount getAbsoluteAmount() {
        return new Amount(Double.toString(Math.abs(value)));
    }

    @Override
    public String toString() {
        return String.format("%.2f", value);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Amount)) {
            return false;
        }

        if (((Amount) other).value.equals(this.value)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
