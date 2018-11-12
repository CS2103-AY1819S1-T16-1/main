package systemtests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DATE_DESC_1;
import static seedu.address.logic.commands.CommandTestUtil.DATE_DESC_2;
import static seedu.address.logic.commands.CommandTestUtil.DATE_DESC_3;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PRICE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_URL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_CHARLES;
import static seedu.address.logic.commands.CommandTestUtil.PRICE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PRICE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PRICE_DESC_CHARLES;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.URL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.URL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.URL_DESC_CHARLES;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRICE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_WISHES;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_WISH;
import static seedu.address.testutil.TypicalWishes.AMY;
import static seedu.address.testutil.TypicalWishes.BOB;
import static seedu.address.testutil.TypicalWishes.CHARLES;
import static seedu.address.testutil.TypicalWishes.KEYWORD_MATCHING_MEIER;

import java.util.stream.Collectors;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.tag.Tag;
import seedu.address.model.wish.Date;
import seedu.address.model.wish.Name;
import seedu.address.model.wish.Price;
import seedu.address.model.wish.Url;
import seedu.address.model.wish.Wish;
import seedu.address.testutil.WishBuilder;
import seedu.address.testutil.WishUtil;

public class EditCommandSystemTest extends WishBookSystemTest {

    @Test
    public void edit() {
        Model expectedModel = getModel();


        /* ----------------- Performing edit operation while an unfiltered list is being shown ---------------------- */

        /* Case: edit all fields (excluding the id), command with leading spaces, trailing spaces and multiple spaces
         * between each field -> edited
         */
        Index index = INDEX_FIRST_WISH;
        Wish wishToEdit = getModel().getFilteredSortedWishList().get(index.getZeroBased());
        String command = " " + EditCommand.COMMAND_WORD + "  " + index.getOneBased() + "  " + NAME_DESC_BOB + "  "
                + PRICE_DESC_BOB + " " + DATE_DESC_2 + "  " + URL_DESC_BOB
                + " " + TAG_DESC_FRIEND + " " + TAG_DESC_HUSBAND + " ";
        String[] tags = { VALID_TAG_FRIEND, VALID_TAG_HUSBAND };
        Wish editedWish = new WishBuilder(BOB).withId(wishToEdit.getId().toString()).withTags(tags).build();
        //assertCommandSuccess(command, index, editedWish);


        /* Case: undo editing the last wish in the list -> last wish restored */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: redo editing the last wish in the list -> last wish edited again */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        expectedModel.updateWish(
                getModel().getFilteredSortedWishList().get(INDEX_FIRST_WISH.getZeroBased()), editedWish);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);



        /* Case: edit a wish with new values same as existing values -> edited */
        Index sameIndex = index;
        command = EditCommand.COMMAND_WORD + " " + sameIndex.getOneBased()
                + NAME_DESC_BOB + PRICE_DESC_BOB + DATE_DESC_2
                + URL_DESC_BOB + TAG_DESC_FRIEND + TAG_DESC_HUSBAND;
        assertCommandSuccess(command, index, editedWish);

        /* Case: edit a wish with new values same as another wish's values but with different name -> edited */
        index = INDEX_FIRST_WISH;
        wishToEdit = getModel().getFilteredSortedWishList().get(index.getZeroBased());
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_AMY + PRICE_DESC_BOB + DATE_DESC_2
                + URL_DESC_BOB + TAG_DESC_FRIEND + TAG_DESC_HUSBAND;
        editedWish = new WishBuilder(BOB).withId(wishToEdit.getId().toString()).withName(VALID_NAME_AMY).build();
        assertCommandSuccess(command, index, editedWish);

        /* Case: undo editing the last wish in the list -> last wish restored */
        command = UndoCommand.COMMAND_WORD;
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, expectedModel, expectedResultMessage);


        /* Case: edit a wish with new values same as another wish's values but with different price and date
         * -> edited
         */
        index = INDEX_FIRST_WISH;
        wishToEdit = getModel().getFilteredSortedWishList().get(index.getZeroBased());
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB + PRICE_DESC_AMY + DATE_DESC_1
                + URL_DESC_BOB + TAG_DESC_FRIEND + TAG_DESC_HUSBAND;
        editedWish = new WishBuilder(BOB).withId(wishToEdit.getId().toString())
                .withPrice(VALID_PRICE_AMY).withDate(VALID_DATE_1).build();
        assertCommandSuccess(command, index, editedWish);

        /* Case: undo editing the last wish in the list -> last wish restored */
        command = UndoCommand.COMMAND_WORD;
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: edit a wish with new values same as another wish's values (except for id) -> edited */
        executeCommand(WishUtil.getAddCommand(CHARLES));
        index = INDEX_FIRST_WISH;
        wishToEdit = getModel().getFilteredSortedWishList().get(index.getZeroBased());
        assertFalse(wishToEdit.equals(CHARLES));
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_CHARLES + PRICE_DESC_CHARLES
                + DATE_DESC_3
                + URL_DESC_CHARLES;
        String[] wishToEditTags = wishToEdit.getTags().stream().map(tag -> tag.tagName)
                .collect(Collectors.toList()).toArray(new String[0]);

        editedWish = new WishBuilder(CHARLES)
                .withId(wishToEdit.getId().toString()).withTags(wishToEditTags)
                .withRemark(wishToEdit.getRemark().value).build();
        assertCommandSuccess(command, index, editedWish);

        /* Case: undo editing the last wish in the list, and then undo last add -> last wish restored */
        command = UndoCommand.COMMAND_WORD;
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        // Undo once to revert last edit
        executeCommand(command);
        // Undo again to revert last add command
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: clear tags -> cleared */
        index = INDEX_FIRST_WISH;
        wishToEdit = getModel().getFilteredSortedWishList().get(index.getZeroBased());
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + PREFIX_TAG.getPrefix();
        editedWish = new WishBuilder(wishToEdit).withTags().build();
        assertCommandSuccess(command, index, editedWish);

        /* ------------------ Performing edit operation while a filtered list is being shown ------------------------ */

        /* Case: filtered wish list, edit index within bounds of wish book and wish list -> edited */
        showWishesWithName(KEYWORD_MATCHING_MEIER);
        index = INDEX_FIRST_WISH;
        wishToEdit = getModel().getFilteredSortedWishList().get(index.getZeroBased());
        assertTrue(index.getZeroBased() < getModel().getFilteredSortedWishList().size());
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + NAME_DESC_BOB;
        editedWish = new WishBuilder(wishToEdit).withName(VALID_NAME_BOB).build();
        assertCommandSuccess(command, index, editedWish);

        /* Case: filtered wish list, edit index within bounds of wish book but out of bounds of wish list
         * -> rejected
         */
        showWishesWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getWishBook().getWishList().size();
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + NAME_DESC_BOB,
                Messages.MESSAGE_INVALID_WISH_DISPLAYED_INDEX);

        /* --------------------- Performing edit operation while a wish card is selected -------------------------- */

        /* Case: selects first card in the wish list, edit a wish -> edited, card selection remains unchanged but
         * browser url changes
         */
        showAllWishes();
        index = INDEX_FIRST_WISH;
        selectWish(index);
        wishToEdit = getModel().getFilteredSortedWishList().get(index.getZeroBased());
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_AMY + PRICE_DESC_AMY + DATE_DESC_1
                + URL_DESC_AMY + TAG_DESC_FRIEND;
        editedWish = new WishBuilder(AMY).withId(wishToEdit.getId().toString()).build();
        // this can be misleading: card selection actually remains unchanged but the
        // browser's url is updated to reflect the new wish's name
        assertCommandSuccess(command, index, editedWish, index);

        /* --------------------------------- Performing invalid edit operation -------------------------------------- */

        // TODO remove this when excess amount is handled
        /* Case: invalid index (0) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " 0" + NAME_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (-1) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " -1" + NAME_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (size + 1) -> rejected */
        invalidIndex = getModel().getFilteredSortedWishList().size() + 1;
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + NAME_DESC_BOB,
                Messages.MESSAGE_INVALID_WISH_DISPLAYED_INDEX);

        /* Case: missing index -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + NAME_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: missing all fields -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_WISH.getOneBased(),
                EditCommand.MESSAGE_NOT_EDITED);

        /* Case: invalid name -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_WISH.getOneBased() + INVALID_NAME_DESC,
                Name.MESSAGE_NAME_CONSTRAINTS);

        /* Case: invalid price -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_WISH.getOneBased() + INVALID_PRICE_DESC,
                Price.MESSAGE_PRICE_CONSTRAINTS);

        /* Case: invalid date -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_WISH.getOneBased() + INVALID_DATE_DESC,
                Date.MESSAGE_DATE_CONSTRAINTS);

        /* Case: invalid wish -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_WISH.getOneBased() + INVALID_URL_DESC,
                Url.MESSAGE_URL_CONSTRAINTS);

        /* Case: invalid tag -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_WISH.getOneBased() + INVALID_TAG_DESC,
                Tag.MESSAGE_TAG_CONSTRAINTS);

    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Index, Wish, Index)} except that
     * the browser url and selected card remain unchanged.
     * @param toEdit the index of the current model's filtered list
     * @see EditCommandSystemTest#assertCommandSuccess(String, Index, Wish, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Wish editedWish) {
        assertCommandSuccess(command, toEdit, editedWish, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} and in addition,<br>
     * 1. Asserts that result display box displays the success message of executing {@code EditCommand}.<br>
     * 2. Asserts that the model related components are updated to reflect the wish at index {@code toEdit} being
     * updated to values specified {@code editedWish}.<br>
     * @param toEdit the index of the current model's filtered list.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Wish editedWish,
            Index expectedSelectedCardIndex) {
        Model expectedModel = getModel();
        expectedModel.updateWish(expectedModel.getFilteredSortedWishList().get(toEdit.getZeroBased()), editedWish);
        expectedModel.updateFilteredWishList(PREDICATE_SHOW_ALL_WISHES);

        assertCommandSuccess(command, expectedModel,
                String.format(EditCommand.MESSAGE_EDIT_WISH_SUCCESS, editedWish), expectedSelectedCardIndex);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} except that the
     * browser url and selected card remain unchanged.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the browser url and selected card update accordingly depending on the card at
     * {@code expectedSelectedCardIndex}.<br>
     * 4. Asserts that the status bar's sync status changes.<br>
     * 5. Asserts that the command box has the default style class.<br>
     * Verifications 1 and 2 are performed by
     * {@code WishBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see WishBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     * @see WishBookSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
            Index expectedSelectedCardIndex) {
        executeCommand(command);
        expectedModel.updateFilteredWishList(PREDICATE_SHOW_ALL_WISHES);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the browser url, selected card and status bar remain unchanged.<br>
     * 4. Asserts that the command box has the error style.<br>
     * Verifications 1 and 2 are performed by
     * {@code WishBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see WishBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
