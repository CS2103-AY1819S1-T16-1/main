package seedu.address.logic.parser;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.REMARK_DESC_SAMPLE_1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SAVED_AMOUNT_AMY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SAVING;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_WISH;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.amount.Amount;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditWishDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.RemarkCommand;
import seedu.address.logic.commands.SaveCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.wish.NameContainsKeywordsPredicate;
import seedu.address.model.wish.Wish;
import seedu.address.testutil.EditWishDescriptorBuilder;
import seedu.address.testutil.WishBuilder;
import seedu.address.testutil.WishUtil;

public class WishBookParserTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final WishBookParser parser = new WishBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Wish wish = new WishBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(WishUtil.getAddCommand(wish));
        assertEquals(new AddCommand(wish), command);
    }

    @Test
    public void parseCommand_addAlias() throws Exception {
        Wish wish = new WishBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(AddCommand.COMMAND_ALIAS + " "
                + WishUtil.getWishDetails(wish));
        assertEquals(new AddCommand(wish), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_clearAlias() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_ALIAS) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_ALIAS + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_WISH.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_WISH), command);
    }

    @Test
    public void parseCommand_deleteAlias() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_ALIAS + " " + INDEX_FIRST_WISH.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_WISH), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Wish wish = new WishBuilder().build();
        EditWishDescriptor descriptor = new EditWishDescriptorBuilder(wish).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_WISH.getOneBased() + " " + WishUtil.getEditWishDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_WISH, descriptor), command);
    }

    @Test
    public void parseCommand_editAlias() throws Exception {
        Wish wish = new WishBuilder().build();
        EditWishDescriptor descriptor = new EditWishDescriptorBuilder(wish).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_ALIAS + " "
                + INDEX_FIRST_WISH.getOneBased() + " " + WishUtil.getEditWishDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_WISH, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_findAlias() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_ALIAS + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_history() throws Exception {
        HistoryCommand commandHistoryCommand = (HistoryCommand) parser.parseCommand(HistoryCommand.COMMAND_WORD
                + " " + HistoryCommand.HISTORY_ALL_COMMANDS);
        HistoryCommand savingsHistoryCommand = (HistoryCommand) parser.parseCommand(HistoryCommand.COMMAND_WORD
                + " " + HistoryCommand.HISTORY_ALL_SAVINGS);
        assertTrue(commandHistoryCommand instanceof HistoryCommand);
        assertTrue(savingsHistoryCommand instanceof HistoryCommand);

        try {
            parser.parseCommand("histories");
            throw new AssertionError("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND, pe.getMessage());
        }
    }

    @Test
    public void parseCommand_historyAlias() throws Exception {
        HistoryCommand commandHistoryCommand = (HistoryCommand) parser.parseCommand(
                HistoryCommand.COMMAND_ALIAS + " " + HistoryCommand.HISTORY_ALL_COMMANDS);
        HistoryCommand savingsHistoryCommand = (HistoryCommand) parser.parseCommand(
                HistoryCommand.COMMAND_ALIAS + " " + HistoryCommand.HISTORY_ALL_SAVINGS);

        assertTrue(commandHistoryCommand instanceof HistoryCommand);
        assertTrue(savingsHistoryCommand instanceof HistoryCommand);

        try {
            parser.parseCommand("histories");
            fail("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND, pe.getMessage());
        }
    }

    @Test
    public void parseCommand_list() throws Exception {
        ListCommand listAllCommand = (ListCommand) parser.parseCommand(
                ListCommand.COMMAND_WORD);
        ListCommand listCompletedCommand = (ListCommand) parser.parseCommand(
                ListCommand.COMMAND_WORD + " " + ListCommand.SHOW_COMPLETED_COMMAND);
        ListCommand listUncompletedCommand = (ListCommand) parser.parseCommand(
                ListCommand.COMMAND_WORD + " " + ListCommand.SHOW_UNCOMPLETED_COMMAND);
        assertEquals(new ListCommand(ListCommand.ListType.SHOW_ALL), listAllCommand);
        assertEquals(new ListCommand(ListCommand.ListType.SHOW_COMPLETED), listCompletedCommand);
        assertEquals(new ListCommand(ListCommand.ListType.SHOW_UNCOMPLETED), listUncompletedCommand);
    }

    @Test
    public void parseCommand_listAlias() throws Exception {
        ListCommand listAllCommand = (ListCommand) parser.parseCommand(
                ListCommand.COMMAND_ALIAS);
        ListCommand listCompletedCommand = (ListCommand) parser.parseCommand(
                ListCommand.COMMAND_ALIAS + " " + ListCommand.SHOW_COMPLETED_COMMAND);
        ListCommand listUncompletedCommand = (ListCommand) parser.parseCommand(
                ListCommand.COMMAND_ALIAS + " " + ListCommand.SHOW_UNCOMPLETED_COMMAND);
        assertEquals(new ListCommand(ListCommand.ListType.SHOW_ALL), listAllCommand);
        assertEquals(new ListCommand(ListCommand.ListType.SHOW_COMPLETED), listCompletedCommand);
        assertEquals(new ListCommand(ListCommand.ListType.SHOW_UNCOMPLETED), listUncompletedCommand);
    }

    @Test
    public void parseCommand_select() throws Exception {
        SelectCommand command = (SelectCommand) parser.parseCommand(
                SelectCommand.COMMAND_WORD + " " + INDEX_FIRST_WISH.getOneBased());
        assertEquals(new SelectCommand(INDEX_FIRST_WISH), command);
    }

    @Test
    public void parseCommand_remark() throws Exception {
        Command remarkCommand = parser.parseCommand(RemarkCommand.COMMAND_WORD + " "
                + INDEX_FIRST_WISH.getOneBased() + REMARK_DESC_SAMPLE_1);
        assertTrue(remarkCommand instanceof RemarkCommand);
    }

    @Test
    public void parseCommand_selectAlias() throws Exception {
        SelectCommand command = (SelectCommand) parser.parseCommand(
                SelectCommand.COMMAND_ALIAS + " " + INDEX_FIRST_WISH.getOneBased());
        assertEquals(new SelectCommand(INDEX_FIRST_WISH), command);
    }

    @Test
    public void parseCommand_redoCommandWord_returnsRedoCommand() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_WORD) instanceof RedoCommand);
        assertTrue(parser.parseCommand("redo 1") instanceof RedoCommand);
    }

    @Test
    public void parseCommand_redoCommandAlias_returnsRedoCommand() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_ALIAS) instanceof RedoCommand);
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_ALIAS + " 1") instanceof RedoCommand);
    }

    @Test
    public void parseCommand_undoCommandWord_returnsUndoCommand() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD) instanceof UndoCommand);
        assertTrue(parser.parseCommand("undo 3") instanceof UndoCommand);
    }

    @Test
    public void parseCommand_undoCommandAlias_returnsUndoCommand() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD) instanceof UndoCommand);
        assertTrue(parser.parseCommand("undo 3") instanceof UndoCommand);
    }

    @Test
    public void parseCommand_saveCommand_returnsSaveCommand() throws Exception {
        Amount amount = new Amount(VALID_SAVED_AMOUNT_AMY);
        SaveCommand saveCommandFromParser = (SaveCommand) parser.parseCommand(
                SaveCommand.COMMAND_WORD + " " + INDEX_FIRST_WISH.getOneBased() + " "
            + PREFIX_SAVING + VALID_SAVED_AMOUNT_AMY);
        assertEquals(new SaveCommand(INDEX_FIRST_WISH, amount), saveCommandFromParser);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        parser.parseCommand("");
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_UNKNOWN_COMMAND);
        parser.parseCommand("unknownCommand");
    }
}
