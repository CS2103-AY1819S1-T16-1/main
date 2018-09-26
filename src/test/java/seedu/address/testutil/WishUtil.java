package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.EditCommand.EditWishDescriptor;
import seedu.address.model.wish.Wish;
import seedu.address.model.tag.Tag;

/**
 * A utility class for Wish.
 */
public class WishUtil {

    /**
     * Returns an add command string for adding the {@code wish}.
     */
    public static String getAddCommand(Wish wish) {
        return AddCommand.COMMAND_WORD + " " + getWishDetails(wish);
    }

    /**
     * Returns the part of command string for the given {@code wish}'s details.
     */
    public static String getWishDetails(Wish wish) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + wish.getName().fullName + " ");
        sb.append(PREFIX_PHONE + wish.getPhone().value + " ");
        sb.append(PREFIX_EMAIL + wish.getEmail().value + " ");
        sb.append(PREFIX_ADDRESS + wish.getAddress().value + " ");
        wish.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditWishDescriptor}'s details.
     */
    public static String getEditWishDescriptorDetails(EditWishDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(PREFIX_PHONE).append(phone.value).append(" "));
        descriptor.getEmail().ifPresent(email -> sb.append(PREFIX_EMAIL).append(email.value).append(" "));
        descriptor.getAddress().ifPresent(address -> sb.append(PREFIX_ADDRESS).append(address.value).append(" "));
        if (descriptor.getTags().isPresent()) {
            Set<Tag> tags = descriptor.getTags().get();
            if (tags.isEmpty()) {
                sb.append(PREFIX_TAG);
            } else {
                tags.forEach(s -> sb.append(PREFIX_TAG).append(s.tagName).append(" "));
            }
        }
        return sb.toString();
    }
}