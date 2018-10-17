package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input filter and creates a new HistoryCommand Object.
 */
public class HistoryCommandParser implements Parser<HistoryCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the HistoryCommand
     * and returns a HistoryCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public HistoryCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        switch (trimmedArgs) {

        case HistoryCommand.HISTORY_ALL_COMMANDS:
            return new HistoryCommand(HistoryCommand.HistoryType.SHOW_COMMANDS);

        case HistoryCommand.HISTORY_ALL_SAVINGS:
            return new HistoryCommand(HistoryCommand.HistoryType.SHOW_SAVINGS);

        default:
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HistoryCommand.MESSAGE_USAGE));
        }
    }
}
