package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_WISH_DISPLAYED_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.*;
import static seedu.address.logic.commands.MoveCommand.MESSAGE_MOVE_FROM_UNUSED_FUNDS_SUCCESS;
import static seedu.address.logic.commands.MoveCommand.MESSAGE_MOVE_TO_UNUSED_FUNDS_SUCCESS;
import static seedu.address.logic.commands.MoveCommand.MESSAGE_MOVE_WISH_SUCCESS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_WISHES;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_WISH;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_WISH;
import static seedu.address.testutil.TypicalWishes.getTypicalWishBook;
import static seedu.address.testutil.TypicalWishes.getTypicalWishTransaction;

import org.junit.Test;

import seedu.address.commons.core.amount.Amount;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.WishBook;
import seedu.address.model.wish.Wish;
import seedu.address.testutil.WishBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code MoveCommand}.
 */
public class MoveCommandTest {
    private Model model = new ModelManager(getTypicalWishBook(), getTypicalWishTransaction(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_moveValidAmountFromWishToWish_success() {
        model.updateFilteredWishList(PREDICATE_SHOW_ALL_WISHES);

        Index fromIndex = INDEX_FIRST_WISH;
        Index toIndex = INDEX_SECOND_WISH;
        Wish fromWish = model.getFilteredSortedWishList().get(fromIndex.getZeroBased());
        Wish toWish = model.getFilteredSortedWishList().get(toIndex.getZeroBased());
        Amount amountInFromWish = new Amount(fromWish.getSavedAmountToPriceDifference().getAbsoluteAmount().toString());
        Amount amountToMove = new Amount("" + (amountInFromWish.value - 1));

        //Load fromWish with some savedAmount
        Wish editedFromWish = new WishBuilder(fromWish)
                .withSavedAmountIncrement(amountToMove.toString()).build();
        model.updateWish(fromWish, editedFromWish);

        MoveCommand moveCommandToTest = new MoveCommand(fromIndex, toIndex, amountToMove,
                MoveCommand.MoveType.WISH_TO_WISH);

        Model expectedModel = new ModelManager(
                new WishBook(model.getWishBook()), model.getWishTransaction(), new UserPrefs());

        //Transfer funds from fromWish to ToWish
        Wish expectedFromWish = new WishBuilder(editedFromWish)
                .withSavedAmountIncrement(amountToMove.getNegatedAmount().toString()).build();
        Wish expectedToWish = new WishBuilder(toWish)
                .withSavedAmountIncrement(amountToMove.toString()).build();

        expectedModel.updateWish(editedFromWish, expectedFromWish);
        expectedModel.updateWish(toWish, expectedToWish);
        expectedModel.commitWishBook();

        String expectedMessage = String.format(MESSAGE_MOVE_WISH_SUCCESS, amountToMove.toString(),
                fromIndex.getOneBased(), toIndex.getOneBased());
        assertCommandSuccess(moveCommandToTest, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_moveValidAmountFromUnusedFundsToWish_success() {
        model.updateFilteredWishList(PREDICATE_SHOW_ALL_WISHES);

        Index toIndex = INDEX_SECOND_WISH;
        Wish toWish = model.getFilteredSortedWishList().get(toIndex.getZeroBased());

        Amount amountToWishPrice = new Amount(toWish.getSavedAmountToPriceDifference().getAbsoluteAmount().toString());
        Amount amountToMove = new Amount("" + (amountToWishPrice.value - 1));

        //Load some unused funds to model first
        model.updateUnusedFunds(amountToMove);

        MoveCommand moveCommandToTest = new MoveCommand(null, toIndex, amountToMove,
                MoveCommand.MoveType.WISH_FROM_UNUSED_FUNDS);

        Model expectedModel = new ModelManager(
                new WishBook(model.getWishBook()), model.getWishTransaction(), new UserPrefs());

        //Transfer funds from unusedFunds to toWish
        Wish expectedToWish = new WishBuilder(toWish)
                .withSavedAmountIncrement(amountToMove.toString()).build();

        expectedModel.updateUnusedFunds(amountToMove.getNegatedAmount());
        expectedModel.updateWish(toWish, expectedToWish);
        expectedModel.commitWishBook();

        String expectedMessage = String.format(MESSAGE_MOVE_FROM_UNUSED_FUNDS_SUCCESS, amountToMove.toString(),
                toIndex.getOneBased(), expectedModel.getUnusedFunds());
        assertCommandSuccess(moveCommandToTest, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_moveValidAmountFromWishToUnusedFunds_success() {
        model.updateFilteredWishList(PREDICATE_SHOW_ALL_WISHES);

        Index fromIndex = INDEX_FIRST_WISH;
        Wish fromWish = model.getFilteredSortedWishList().get(fromIndex.getZeroBased());
        Amount amountInFromWish = new Amount(fromWish.getSavedAmountToPriceDifference().getAbsoluteAmount().toString());
        Amount amountToMove = new Amount("" + (amountInFromWish.value - 1));

        //Load fromWish with some savedAmount
        Wish editedFromWish = new WishBuilder(fromWish)
                .withSavedAmountIncrement(amountToMove.toString()).build();
        model.updateWish(fromWish, editedFromWish);

        MoveCommand moveCommandToTest = new MoveCommand(fromIndex, null, amountToMove,
                MoveCommand.MoveType.WISH_TO_UNUSED_FUNDS);

        Model expectedModel = new ModelManager(
                new WishBook(model.getWishBook()), model.getWishTransaction(), new UserPrefs());

        //Transfer funds from fromWish to ToWish
        Wish expectedFromWish = new WishBuilder(editedFromWish)
                .withSavedAmountIncrement(amountToMove.getNegatedAmount().toString()).build();

        expectedModel.updateWish(editedFromWish, expectedFromWish);
        expectedModel.updateUnusedFunds(amountToMove);
        expectedModel.commitWishBook();

        String expectedMessage = String.format(MESSAGE_MOVE_TO_UNUSED_FUNDS_SUCCESS, amountToMove.toString(),
                fromIndex.getOneBased(), expectedModel.getUnusedFunds());
        assertCommandSuccess(moveCommandToTest, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void executeUndoRedo_moveFromInvalidIndex_failure() {
        model.updateFilteredWishList(PREDICATE_SHOW_ALL_WISHES);
        Index fromIndexOutOfBounds = Index.fromOneBased(model.getFilteredSortedWishList().size() + 1);
        Index toIndex = INDEX_FIRST_WISH;
        Amount amountToMove = new Amount(VALID_SAVED_AMOUNT_AMY);
        MoveCommand moveCommandToFail = new MoveCommand(fromIndexOutOfBounds, toIndex, amountToMove,
                MoveCommand.MoveType.WISH_TO_WISH);

        String expectedMessage = MESSAGE_INVALID_WISH_DISPLAYED_INDEX;

        // ensure that the command fails
        assertCommandFailure(moveCommandToFail, model, commandHistory, expectedMessage);

        // test undo -> undo should fail as there is nothing in history
        String expectedFailureMessage = UndoCommand.MESSAGE_FAILURE;
        assertCommandFailure(new UndoCommand(), model, commandHistory, expectedFailureMessage);

        // test redo -> nothing should be undone
        expectedFailureMessage = RedoCommand.MESSAGE_FAILURE;
        assertCommandFailure(new RedoCommand(), model, commandHistory, expectedFailureMessage);
    }

    @Test
    public void equals() {
        final Amount amountAmy = new Amount(VALID_SAVED_AMOUNT_AMY);
        final Amount amountBob = new Amount(VALID_SAVED_AMOUNT_BOB);
        MoveCommand.MoveType moveType1a = MoveCommand.MoveType.WISH_TO_WISH;
        MoveCommand.MoveType moveType1b = MoveCommand.MoveType.WISH_TO_UNUSED_FUNDS;

        final MoveCommand moveCommand1a = new MoveCommand(INDEX_FIRST_WISH, INDEX_SECOND_WISH, amountAmy, moveType1a);
        final MoveCommand moveCommand1b = new MoveCommand(INDEX_FIRST_WISH, INDEX_SECOND_WISH, amountAmy, moveType1a);
        final MoveCommand moveCommand1c = new MoveCommand(INDEX_FIRST_WISH, INDEX_SECOND_WISH, amountBob, moveType1a);
        final MoveCommand moveCommand1d = new MoveCommand(INDEX_FIRST_WISH, null, amountAmy, moveType1b);

        //Same object
        assertTrue(moveCommand1a.equals(moveCommand1a));

        //Same values
        assertTrue(moveCommand1a.equals(moveCommand1b));

        //Different values
        assertFalse(moveCommand1c.equals(moveCommand1a));
        assertFalse(moveCommand1a.equals(moveCommand1d));

        //null
        assertFalse(moveCommand1a.equals(null));

        //Different command
        assertFalse(moveCommand1a.equals(new ClearCommand()));
    }
}
