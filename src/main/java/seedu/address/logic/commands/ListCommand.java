package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_WISHES;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.wish.WishCompletedPredicate;

/**
 * Lists all wishes in the wish book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String COMMAND_ALIAS = "l";
    public static final String SHOW_COMPLETED_COMMAND = "-c";
    public static final String SHOW_UNCOMPLETED_COMMAND = "-u";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all wishes that are in the WishBook.\n"
            + COMMAND_WORD + " -c: Lists all wishes have been completed.\n"
            + COMMAND_WORD + " -u: Lists all wishes that are still in progress.\n";

    /**
     * Types of lists that can be shown.
     */
    public enum ListType { SHOW_ALL, SHOW_COMPLETED, SHOW_UNCOMPLETED }

    public static final String MESSAGE_SUCCESS = "Listed all wishes";

    private final ListType listType;

    public ListCommand(ListType listType) {
        this.listType = listType;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);

        if (this.listType.equals(ListType.SHOW_ALL)) {
            model.updateFilteredWishList(PREDICATE_SHOW_ALL_WISHES);
        } else if (this.listType.equals(ListType.SHOW_COMPLETED)) {
            model.updateFilteredWishList(new WishCompletedPredicate(true));
        } else {
            model.updateFilteredWishList(new WishCompletedPredicate(false));
        }

        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ListCommand // instanceof handles nulls
                && listType.equals(((ListCommand) other).listType)); // state check
    }
}
