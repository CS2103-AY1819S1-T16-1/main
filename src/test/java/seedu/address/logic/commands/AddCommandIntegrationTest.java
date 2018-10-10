package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalWishes.getTypicalWishBook;
import static seedu.address.testutil.TypicalWishes.getTypicalWishTransaction;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.wish.Wish;
import seedu.address.testutil.WishBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalWishBook(), getTypicalWishTransaction(), new UserPrefs());
    }

    @Test
    public void execute_newWish_success() {
        Wish validWish = new WishBuilder().build();

        Model expectedModel = new ModelManager(model.getWishBook(), model.getWishTransaction(), new UserPrefs());
        expectedModel.addWish(validWish);
        expectedModel.commitWishBook();

        assertCommandSuccess(new AddCommand(validWish), model, commandHistory,
                String.format(AddCommand.MESSAGE_SUCCESS, validWish), expectedModel);
    }

    @Test
    public void execute_duplicateWish_throwsCommandException() {
        Wish wishInList = model.getWishBook().getWishList().get(0);
        assertCommandFailure(new AddCommand(wishInList), model, commandHistory,
                AddCommand.MESSAGE_DUPLICATE_WISH);
    }

}
