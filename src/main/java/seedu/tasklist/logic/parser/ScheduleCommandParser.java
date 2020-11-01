package seedu.tasklist.logic.parser;

import static seedu.tasklist.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.tasklist.logic.parser.CliSyntax.PREFIX_DO_BEFORE;
import static seedu.tasklist.logic.parser.CliSyntax.PREFIX_EXPECTED_HOURS;

import java.util.stream.Stream;

import seedu.tasklist.commons.core.index.Index;
import seedu.tasklist.logic.commands.ScheduleCommand;
import seedu.tasklist.logic.parser.exceptions.ParseException;
import seedu.tasklist.model.task.Time;

/**
 * Parses input arguments and creates a new ScheduleCommand object
 */
public class ScheduleCommandParser implements Parser<ScheduleCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ScheduleCommand
     * and returns an ScheduleCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ScheduleCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap;
        argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_EXPECTED_HOURS, PREFIX_DO_BEFORE);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ScheduleCommand.MESSAGE_USAGE), pe);
        }
        if (!arePrefixesPresent(argMultimap, PREFIX_EXPECTED_HOURS, PREFIX_DO_BEFORE)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ScheduleCommand.MESSAGE_USAGE));
        }

        Time doBefore = ParserUtil.parseDeadline(argMultimap.getValue(PREFIX_DO_BEFORE).get());
        int expectedTime = ParserUtil.parseExpectedTime(argMultimap.getValue(PREFIX_EXPECTED_HOURS).get());

        return new ScheduleCommand(index, expectedTime, doBefore);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
