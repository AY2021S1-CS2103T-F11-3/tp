package seedu.tasklist.logic.parser;

import static seedu.tasklist.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.tasklist.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.tasklist.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.tasklist.testutil.TypicalIndexes.INDEX_FIRST_ASSIGNMENT;

import org.junit.jupiter.api.Test;

import seedu.tasklist.logic.commands.UnremindCommand;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the RemindCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the UnremindCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class UnremindCommandParserTest {

    private UnremindCommandParser parser = new UnremindCommandParser();

    @Test
    public void parse_validArgs_returnsUnremindCommand() {
        assertParseSuccess(parser, "1", new UnremindCommand(INDEX_FIRST_ASSIGNMENT));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "abc", String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnremindCommand.MESSAGE_USAGE));
    }
}