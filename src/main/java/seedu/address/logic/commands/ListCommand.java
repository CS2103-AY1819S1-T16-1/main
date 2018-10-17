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
            + COMMAND_WORD + " " + SHOW_COMPLETED_COMMAND + ": Lists all wishes have been completed.\n"
            + COMMAND_WORD + " " + SHOW_UNCOMPLETED_COMMAND + ": Lists all wishes that are still in progress.\n";
    public static final String MESSAGE_SUCCESS = "Listed all wishes";
    public static final String MESSAGE_SHOWED_COMPLETED = "Listed completed wishes";
    public static final String MESSAGE_SHOWED_UNCOMPLETED = "Listed uncompleted wishes";

    /**
     * Types of lists that can be shown.
     */
    public enum ListType { SHOW_ALL, SHOW_COMPLETED, SHOW_UNCOMPLETED }

    private final ListType listType;

    public ListCommand(ListType listType) {
        this.listType = listType;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);

        if (listType.equals(ListType.SHOW_COMPLETED)) {
            model.updateFilteredWishList(new WishCompletedPredicate(true));
            return new CommandResult(MESSAGE_SHOWED_COMPLETED);
        }
        if (listType.equals(ListType.SHOW_UNCOMPLETED)) {
            model.updateFilteredWishList(new WishCompletedPredicate(false));
            return new CommandResult(MESSAGE_SHOWED_UNCOMPLETED);
        }

        model.updateFilteredWishList(PREDICATE_SHOW_ALL_WISHES);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ListCommand // instanceof handles nulls
                && listType.equals(((ListCommand) other).listType)); // state check
    }
}
