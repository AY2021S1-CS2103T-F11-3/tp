package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_ASSIGNMENT;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.function.Predicate;

import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.assignment.Deadline;

/**
 * Lists all tasks in ProductiveNUS to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists the assignments in the next [INDEX] days, starting from the current date and time.\n"
            + "A day represents 24 hours.\n"
            + "If no index is keyed in, all your assignments will be displayed.\n"
            + "Parameters: [INDEX] (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 3\n"
            + "The above example will list all your assignments 72 hours (3 days) from the current date and time.";

    public static final String MESSAGE_SUCCESS = "Listed your assignments";

    private final Index numberOfDays;

    public ListCommand(Index numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    private Predicate<Assignment> showLimitedAssignments() {
        return assignment -> {
            DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern(Deadline.DEADLINE_DATE_TIME_FORMAT)
                    .withResolverStyle(ResolverStyle.STRICT);
            String dateAndTimeToParse = assignment.getDeadline().value;
            LocalDateTime currentDateAndTime = LocalDateTime.now();
            LocalDateTime lastDateAndTime = currentDateAndTime.plusDays(numberOfDays.getZeroBased());
            LocalDateTime parsedDateAndTime = LocalDateTime.parse(dateAndTimeToParse, inputFormat);

            boolean isAfterCurrentDateAndTime = parsedDateAndTime.isAfter(currentDateAndTime);
            boolean isBeforeLastDateAndTime = parsedDateAndTime.isBefore(lastDateAndTime);

            return isAfterCurrentDateAndTime && isBeforeLastDateAndTime;
        };
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        if (numberOfDays.getZeroBased() == 0) {
            model.updateFilteredAssignmentList(PREDICATE_SHOW_ALL_ASSIGNMENT);
        } else {
            model.updateFilteredAssignmentList((showLimitedAssignments()));
        }
        return new CommandResult(ListCommand.MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ListCommand // instanceof handles nulls
                && numberOfDays.equals(((ListCommand) other).numberOfDays)); // state check
    }
}
