package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRICE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
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
import seedu.address.logic.commands.EditCommand.EditWishDescriptor;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.WishBook;
import seedu.address.model.wish.Wish;
import seedu.address.testutil.EditWishDescriptorBuilder;
import seedu.address.testutil.WishBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalWishBook(), getTypicalWishTransaction(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Wish editedWish = new WishBuilder().build();
        EditWishDescriptor descriptor = new EditWishDescriptorBuilder(editedWish).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_WISH, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_WISH_SUCCESS, editedWish);

        Model expectedModel = new ModelManager(
                new WishBook(model.getWishBook()), model.getWishTransaction(), new UserPrefs());
        expectedModel.updateWish(model.getFilteredWishList().get(0), editedWish);
        expectedModel.commitWishBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastWish = Index.fromOneBased(model.getFilteredWishList().size());
        Wish lastWish = model.getFilteredWishList().get(indexLastWish.getZeroBased());

        WishBuilder wishInList = new WishBuilder(lastWish);
        Wish editedWish = wishInList.withName(VALID_NAME_BOB).withPrice(VALID_PRICE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditWishDescriptor descriptor = new EditWishDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPrice(VALID_PRICE_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(indexLastWish, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_WISH_SUCCESS, editedWish);

        Model expectedModel = new ModelManager(
                new WishBook(model.getWishBook()), model.getWishTransaction(), new UserPrefs());
        expectedModel.updateWish(lastWish, editedWish);
        expectedModel.commitWishBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_WISH, new EditWishDescriptor());
        Wish editedWish = model.getFilteredWishList().get(INDEX_FIRST_WISH.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_WISH_SUCCESS, editedWish);

        Model expectedModel = new ModelManager(
                new WishBook(model.getWishBook()), model.getWishTransaction(), new UserPrefs());
        expectedModel.commitWishBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showWishAtIndex(model, INDEX_FIRST_WISH);

        Wish wishInFilteredList = model.getFilteredWishList().get(INDEX_FIRST_WISH.getZeroBased());
        Wish editedWish = new WishBuilder(wishInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_WISH,
                new EditWishDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_WISH_SUCCESS, editedWish);

        Model expectedModel = new ModelManager(
                new WishBook(model.getWishBook()), model.getWishTransaction(), new UserPrefs());
        expectedModel.updateWish(model.getFilteredWishList().get(0), editedWish);
        expectedModel.commitWishBook();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateWishUnfilteredList_failure() {
        Wish firstWish = model.getFilteredWishList().get(INDEX_FIRST_WISH.getZeroBased());
        EditWishDescriptor descriptor = new EditWishDescriptorBuilder(firstWish).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_WISH, descriptor);

        assertCommandFailure(editCommand, model, commandHistory, EditCommand.MESSAGE_DUPLICATE_WISH);
    }

    @Test
    public void execute_duplicateWishFilteredList_failure() {
        showWishAtIndex(model, INDEX_FIRST_WISH);

        // edit wish in filtered list into a duplicate in wish book
        Wish wishInList = model.getWishBook().getWishList().get(INDEX_SECOND_WISH.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_WISH,
                new EditWishDescriptorBuilder(wishInList).build());

        assertCommandFailure(editCommand, model, commandHistory, EditCommand.MESSAGE_DUPLICATE_WISH);
    }

    @Test
    public void execute_invalidWishIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredWishList().size() + 1);
        EditWishDescriptor descriptor = new EditWishDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_WISH_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of wish book
     */
    @Test
    public void execute_invalidWishIndexFilteredList_failure() {
        showWishAtIndex(model, INDEX_FIRST_WISH);
        Index outOfBoundIndex = INDEX_SECOND_WISH;
        // ensures that outOfBoundIndex is still in bounds of wish book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getWishBook().getWishList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditWishDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_WISH_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Wish editedWish = new WishBuilder().build();
        Wish wishToEdit = model.getFilteredWishList().get(INDEX_FIRST_WISH.getZeroBased());
        EditWishDescriptor descriptor = new EditWishDescriptorBuilder(editedWish).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_WISH, descriptor);
        Model expectedModel = new ModelManager(
                new WishBook(model.getWishBook()), model.getWishTransaction(), new UserPrefs());
        expectedModel.updateWish(wishToEdit, editedWish);
        expectedModel.commitWishBook();

        // edit -> first wish edited
        editCommand.execute(model, commandHistory);

        // undo -> reverts wishbook back to previous state and filtered wish list to show all wishes
        expectedModel.undoWishBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first wish edited again
        expectedModel.redoWishBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredWishList().size() + 1);
        EditWishDescriptor descriptor = new EditWishDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        // execution failed -> wish book state not added into model
        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_WISH_DISPLAYED_INDEX);

        // single wish book state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Edits a {@code Wish} from a filtered list.
     * 2. Undo the edit.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously edited wish in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the edit. This ensures {@code RedoCommand} edits the wish object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_sameWishEdited() throws Exception {
        Wish editedWish = new WishBuilder().build();
        EditWishDescriptor descriptor = new EditWishDescriptorBuilder(editedWish).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_WISH, descriptor);
        Model expectedModel = new ModelManager(
                new WishBook(model.getWishBook()), model.getWishTransaction(), new UserPrefs());

        showWishAtIndex(model, INDEX_SECOND_WISH);
        Wish wishToEdit = model.getFilteredWishList().get(INDEX_FIRST_WISH.getZeroBased());
        expectedModel.updateWish(wishToEdit, editedWish);
        expectedModel.commitWishBook();

        // edit -> edits second wish in unfiltered wish list / first wish in filtered wish list
        editCommand.execute(model, commandHistory);

        // undo -> reverts wishbook back to previous state and filtered wish list to show all wishes
        expectedModel.undoWishBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        assertNotEquals(model.getFilteredWishList().get(INDEX_FIRST_WISH.getZeroBased()), wishToEdit);
        // redo -> edits same second wish in unfiltered wish list
        expectedModel.redoWishBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_WISH, DESC_AMY);

        // same values -> returns true
        EditWishDescriptor copyDescriptor = new EditWishDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_WISH, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_WISH, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_WISH, DESC_BOB)));
    }

}
