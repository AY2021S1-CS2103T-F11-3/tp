package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DEADLINE_DESC_HW;
import static seedu.address.logic.commands.CommandTestUtil.DEADLINE_DESC_LAB;
import static seedu.address.logic.commands.CommandTestUtil.DESC_HW;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DEADLINE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_MODULE_CODE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.MODULE_CODE_DESC_HW;
import static seedu.address.logic.commands.CommandTestUtil.MODULE_CODE_DESC_LAB;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_HW;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_LAB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DEADLINE_LAB;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalAssignments.HW;
import static seedu.address.testutil.TypicalAssignments.LAB;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.CommandTestUtil;
import seedu.address.model.person.Assignment;
import seedu.address.model.person.Deadline;
import seedu.address.model.person.ModuleCode;
import seedu.address.model.person.Name;
import seedu.address.testutil.AssignmentBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Assignment expectedAssignment = new AssignmentBuilder(LAB).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_LAB + DEADLINE_DESC_LAB
                + MODULE_CODE_DESC_LAB, new AddCommand(expectedAssignment));

        // multiple names - last name accepted
        assertParseSuccess(parser, NAME_DESC_HW + NAME_DESC_LAB + DEADLINE_DESC_LAB
                + MODULE_CODE_DESC_LAB, new AddCommand(expectedAssignment));

        // multiple deadlines - last deadline accepted
        assertParseSuccess(parser, NAME_DESC_LAB + DEADLINE_DESC_HW + DEADLINE_DESC_LAB + MODULE_CODE_DESC_LAB,
                new AddCommand(expectedAssignment));

        // multiple module codes - last module code accepted
        assertParseSuccess(parser, NAME_DESC_LAB + DEADLINE_DESC_LAB + MODULE_CODE_DESC_LAB,
                new AddCommand(expectedAssignment));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Assignment expectedAssignment = new AssignmentBuilder(HW).build();
        assertParseSuccess(parser, NAME_DESC_HW + DEADLINE_DESC_HW + MODULE_CODE_DESC_HW,
                new AddCommand(expectedAssignment));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, CommandTestUtil.VALID_NAME_LAB + DEADLINE_DESC_LAB + MODULE_CODE_DESC_LAB,
                expectedMessage);

        // missing deadline prefix
        assertParseFailure(parser, NAME_DESC_LAB + VALID_DEADLINE_LAB + MODULE_CODE_DESC_LAB,
                expectedMessage);

        // missing module code prefix
        assertParseFailure(parser, NAME_DESC_LAB + DEADLINE_DESC_LAB + DESC_HW,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, CommandTestUtil.VALID_NAME_LAB + VALID_DEADLINE_LAB + DESC_HW,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + DEADLINE_DESC_LAB + MODULE_CODE_DESC_LAB,
                Name.MESSAGE_CONSTRAINTS);

        // invalid deadline
        assertParseFailure(parser, NAME_DESC_LAB + INVALID_DEADLINE_DESC + MODULE_CODE_DESC_LAB,
                Deadline.MESSAGE_CONSTRAINTS);

        // invalid module code
        assertParseFailure(parser, NAME_DESC_LAB + DEADLINE_DESC_LAB + INVALID_MODULE_CODE_DESC,
                ModuleCode.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + DEADLINE_DESC_LAB + INVALID_MODULE_CODE_DESC,
                Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_LAB + DEADLINE_DESC_LAB
                + MODULE_CODE_DESC_LAB, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
