package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_ASSIGNMENT;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.assignment.Deadline;
import seedu.address.model.assignment.ModuleCode;
import seedu.address.model.assignment.Name;
import seedu.address.model.assignment.Remind;


/**
 * Removes reminders for an assignment identified using it's displayed index from the address book.
 */
public class UnremindCommand extends NegateCommand {

    public static final String COMMAND_WORD_SUFFIX = "remind";
    public static final String COMMAND_WORD = NegateCommand.COMMAND_WORD + COMMAND_WORD_SUFFIX;

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes the reminder from the assignment identified by the index number"
            + "used in the displayed assignment list."
            + " Assignments will no longer have reminders set and will be removed from the displayed reminders list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_UNREMIND_ASSIGNMENT_SUCCESS = "Removed reminder for Assignment: %1$s";
    public static final String MESSAGE_UNREMINDED_ASSIGNMENT = "This assignment already does not have reminders set.";

    /**
     * Constructs an UnremindCommand to remove reminders from the specified assignment.
     * @param targetIndex index of the assignment in the filtered assignment list to remove reminders
     */
    public UnremindCommand(Index targetIndex) {
        super(targetIndex);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Assignment> lastShownList = model.getFilteredAssignmentList();

        if (getTargetIndex().getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ASSIGNMENT_DISPLAYED_INDEX);
        }

        Assignment assignmentToUnremind = lastShownList.get(getTargetIndex().getZeroBased());

        if (!assignmentToUnremind.isReminded() && model.hasAssignment(assignmentToUnremind)) {
            throw new CommandException(MESSAGE_UNREMINDED_ASSIGNMENT);
        }

        assert(assignmentToUnremind.isReminded());
        Assignment unremindedAssignment = createUnremindedAssignment(assignmentToUnremind);

        model.setAssignment(assignmentToUnremind, unremindedAssignment);
        model.updateFilteredAssignmentList(PREDICATE_SHOW_ALL_ASSIGNMENT);
        return new CommandResult(String.format(MESSAGE_UNREMIND_ASSIGNMENT_SUCCESS, unremindedAssignment));
    }

    /**
     * Creates and returns an {@code Assignment} with the details of {@code assignmentToUnremind}.
     */
    private static Assignment createUnremindedAssignment(Assignment assignmentToUnremind) {
        assert assignmentToUnremind != null;

        Name updatedName = assignmentToUnremind.getName();
        Deadline updatedDeadline = assignmentToUnremind.getDeadline();
        ModuleCode updatedModuleCode = assignmentToUnremind.getModuleCode();
        Remind updatedRemind = new Remind();

        return new Assignment(updatedName, updatedDeadline, updatedModuleCode, updatedRemind);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UnremindCommand // instanceof handles nulls
                && getTargetIndex().equals(((UnremindCommand) other).getTargetIndex())); // state check
    }
}
