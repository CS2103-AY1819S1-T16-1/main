package seedu.address.model.wish.exceptions;

/**
 * Signals that the operation will result in duplicate Wishes (Wishes are considered duplicates if they have the same
 * identity).
 */
public class DuplicateWishException extends RuntimeException {
    public DuplicateWishException() {
        super("Operation would result in duplicate wishes.");
    }
}
