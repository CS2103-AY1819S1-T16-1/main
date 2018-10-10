package seedu.address.logic.commands;
import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.wish.Remark;
import seedu.address.model.wish.Wish;

/**
 * Adds remark to existing wish in wish book.
 */
public class RemarkCommand extends Command {
    public static final String COMMAND_WORD = "remark";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the remark of the wish identified "
            + "by the index number used in the displayed wish list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_REMARK + "REMARK]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_REMARK + "random remark";

    public static final String MESSAGE_REMARK_ADD_SUCCESS = "Added remark to Wish: ";

    private final Index index;
    private final Remark remark;

    public RemarkCommand(Index index, Remark remark) {
        requireNonNull(index);
        requireNonNull(remark);

        this.index = index;
        this.remark = remark;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        List<Wish> lastShownList = model.getFilteredWishList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_WISH_DISPLAYED_INDEX);
        }

        Wish wishToEdit = lastShownList.get(index.getZeroBased());
        Wish updatedRemarkWish = createUpdatedRemarkWish(wishToEdit, this.remark);

        model.updateWish(wishToEdit, updatedRemarkWish);
        model.updateFilteredWishList(Model.PREDICATE_SHOW_ALL_WISHES);
        model.commitWishBook();
        return new CommandResult(String.format(MESSAGE_REMARK_ADD_SUCCESS, updatedRemarkWish));
    }

    /**
     * Creates and returns a {@code Wish} with the details of {@code wishToEdit}
     * with updated Remark.
     */
    private static Wish createUpdatedRemarkWish(Wish wishToEdit, Remark remark) {
        return new Wish(wishToEdit.getName(), wishToEdit.getPrice(), wishToEdit.getEmail(),
                wishToEdit.getUrl(), wishToEdit.getSavedAmount(), remark, wishToEdit.getTags());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof RemarkCommand)) {
            return false;
        }

        if (((RemarkCommand) other).remark.equals(this.remark)
                && ((RemarkCommand) other).index.equals(index)) {
            return true;
        }

        return false;
    }
}
