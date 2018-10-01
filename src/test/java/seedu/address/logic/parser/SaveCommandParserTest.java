package seedu.address.logic.parser;

import org.junit.Test;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.CommandTestUtil;
import seedu.address.logic.commands.SaveCommand;
import seedu.address.model.wish.SavedAmount;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.*;
import static seedu.address.logic.commands.SaveCommand.COMMAND_WORD;
import static seedu.address.logic.commands.SaveCommand.MESSAGE_USAGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SAVING;
import static seedu.address.logic.parser.CommandParserTestUtil.*;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_WISH;

public class SaveCommandParserTest {
    private SaveCommandParser saveCommandParser = new SaveCommandParser();
    private final Index targetIndex = INDEX_FIRST_WISH;
    private final SavedAmount savedAmount = new SavedAmount(VALID_SAVED_AMOUNT_AMY);

    @Test
    public void parse_indexSpecified_success() {
        final String userInput = targetIndex.getOneBased() + " " + PREFIX_SAVING
                + VALID_SAVED_AMOUNT_AMY;

        final SaveCommand expectedCommand = new SaveCommand(targetIndex, savedAmount);

        assertParseSuccess(saveCommandParser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE);

        final String userInputMissingIndex = PREFIX_SAVING + VALID_SAVED_AMOUNT_AMY;
        assertParseFailure(saveCommandParser, userInputMissingIndex,
                expectedMessage);

        final String userInputMissingSavingPrefix = targetIndex + " " + VALID_SAVED_AMOUNT_AMY;
        assertParseFailure(saveCommandParser, userInputMissingSavingPrefix,
                expectedMessage);

        final String userInputMissingSavingAmount = targetIndex + " " + PREFIX_SAVING;
        assertParseFailure(saveCommandParser, userInputMissingSavingAmount,
                expectedMessage);

        final String userInputMissingSavingAmountAndSavingPrefix = targetIndex + "";
        assertParseFailure(saveCommandParser, userInputMissingSavingAmountAndSavingPrefix,
                expectedMessage);
    }
}
