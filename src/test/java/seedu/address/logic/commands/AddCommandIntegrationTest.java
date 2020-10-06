package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalAssignments.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Assignment;
import seedu.address.testutil.AssignmentBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newAssignment_success() {
        Assignment validAssignment = new AssignmentBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addAssignment(validAssignment);

        assertCommandSuccess(new AddCommand(validAssignment), model,
                String.format(AddCommand.MESSAGE_SUCCESS, validAssignment), expectedModel);
    }

    @Test
    public void execute_duplicateAssignment_throwsCommandException() {
        Assignment assignmentInList = model.getAddressBook().getAssignmentList().get(0);
        assertCommandFailure(new AddCommand(assignmentInList), model, AddCommand.MESSAGE_DUPLICATE_ASSIGNMENT);
    }

}
