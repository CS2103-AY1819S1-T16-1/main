package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showWishAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_WISH;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_WISH;
import static seedu.address.testutil.TypicalWishes.getTypicalWishBook;
import static seedu.address.testutil.TypicalWishes.getTypicalWishTransaction;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.wish.Wish;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalWishBook(), getTypicalWishTransaction(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Wish wishToDelete = model.getFilteredWishList().get(INDEX_FIRST_WISH.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_WISH);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_WISH_SUCCESS, wishToDelete);

        ModelManager expectedModel = new ModelManager(model.getWishBook(), model.getWishTransaction(), new UserPrefs());
        expectedModel.deleteWish(wishToDelete);
        expectedModel.commitWishBook();

        assertCommandSuccess(deleteCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredWishList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, commandHistory, Messages.MESSAGE_INVALID_WISH_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showWishAtIndex(model, INDEX_FIRST_WISH);

        Wish wishToDelete = model.getFilteredWishList().get(INDEX_FIRST_WISH.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_WISH);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_WISH_SUCCESS, wishToDelete);

        Model expectedModel = new ModelManager(model.getWishBook(), model.getWishTransaction(), new UserPrefs());
        expectedModel.deleteWish(wishToDelete);
        expectedModel.commitWishBook();
        showNoWish(expectedModel);

        assertCommandSuccess(deleteCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showWishAtIndex(model, INDEX_FIRST_WISH);

        Index outOfBoundIndex = INDEX_SECOND_WISH;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getWishBook().getWishList().size());

        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, commandHistory, Messages.MESSAGE_INVALID_WISH_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Wish wishToDelete = model.getFilteredWishList().get(INDEX_FIRST_WISH.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_WISH);
        Model expectedModel = new ModelManager(model.getWishBook(), model.getWishTransaction(), new UserPrefs());
        expectedModel.deleteWish(wishToDelete);
        expectedModel.commitWishBook();

        // delete -> first wish deleted
        deleteCommand.execute(model, commandHistory);

        // undo -> reverts wishbook back to previous state and filtered wish list to show all wish
        expectedModel.undoWishBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first wish deleted again
        expectedModel.redoWishBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredWishList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        // execution failed -> wish book state not added into model
        assertCommandFailure(deleteCommand, model, commandHistory, Messages.MESSAGE_INVALID_WISH_DISPLAYED_INDEX);

        // single wish book state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Deletes a {@code Wish} from a filtered list.
     * 2. Undo the deletion.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously deleted wish in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the deletion. This ensures {@code RedoCommand} deletes the wish object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_sameWishDeleted() throws Exception {
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_WISH);
        Model expectedModel = new ModelManager(model.getWishBook(), model.getWishTransaction(), new UserPrefs());

        showWishAtIndex(model, INDEX_SECOND_WISH);
        Wish wishToDelete = model.getFilteredWishList().get(INDEX_FIRST_WISH.getZeroBased());
        expectedModel.deleteWish(wishToDelete);
        expectedModel.commitWishBook();

        // delete -> deletes second wish in unfiltered wish list / first wish in filtered wish list
        deleteCommand.execute(model, commandHistory);

        // undo -> reverts wishbook back to previous state and filtered wish list to show all wishes
        expectedModel.undoWishBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        assertNotEquals(wishToDelete, model.getFilteredWishList().get(INDEX_FIRST_WISH.getZeroBased()));
        // redo -> deletes same second wish in unfiltered wish list
        expectedModel.redoWishBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(INDEX_FIRST_WISH);
        DeleteCommand deleteSecondCommand = new DeleteCommand(INDEX_SECOND_WISH);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(INDEX_FIRST_WISH);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different wish -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoWish(Model model) {
        model.updateFilteredWishList(p -> false);

        assertTrue(model.getFilteredWishList().isEmpty());
    }
}
