package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Undoes the most recent command.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + "Undo the most recent command. "
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_UNDO_SUCCESS = "Successfully undo the most recent command.";
    public static final String MESSAGE_INCORRECT_FORMAT = "Invalid command format! \nFormat: undo";
    public static final String MESSAGE_UNDO_FAIL = "No recent command.";

    private final String userInput;

    /**
     * Constructs a UndoCommand to undo the most recent command.
     */
    public UndoCommand(String userInput) {
        this.userInput = userInput;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        boolean hasNoArgument = userInput.trim().contentEquals("undo");
        Model previousModel = model.getPreviousModel();

        if (!hasNoArgument) {
            throw new CommandException(MESSAGE_INCORRECT_FORMAT);
        }

        if (previousModel == null) {
            throw new CommandException(MESSAGE_UNDO_FAIL);
        }

        model.goToPreviousModel();
        return new CommandResult(MESSAGE_UNDO_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this; // short circuit if same object
    }
}
