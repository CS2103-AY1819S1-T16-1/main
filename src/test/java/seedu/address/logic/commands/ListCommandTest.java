package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showWishAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_WISH;
import static seedu.address.testutil.TypicalWishes.getTypicalWishBook;
import static seedu.address.testutil.TypicalWishes.getTypicalWishTransaction;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalWishBook(), getTypicalWishTransaction(), new UserPrefs());
        expectedModel = new ModelManager(model.getWishBook(), model.getWishTransaction(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListCommand(ListCommand.ListType.SHOW_ALL),
                model, commandHistory, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showWishAtIndex(model, INDEX_FIRST_WISH);
        assertCommandSuccess(new ListCommand(ListCommand.ListType.SHOW_ALL),
                model, commandHistory, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
