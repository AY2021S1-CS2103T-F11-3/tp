package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Assignment;

/**
 * An UI component that displays information of a {@code Assignment}.
 */
public class AssignmentCard extends UiPart<Region> {

    private static final String FXML = "AssignmentListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Assignment assignment;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label deadline;
    @FXML
    private Label moduleCode;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code AssignmentCode} with the given {@code Assignment} and index to display.
     */
    public AssignmentCard(Assignment assignment, int displayedIndex) {
        super(FXML);
        this.assignment = assignment;
        id.setText(displayedIndex + ". ");
        name.setText(assignment.getName().fullName);
        deadline.setText(assignment.getDeadline().value);
        moduleCode.setText(assignment.getModuleCode().moduleCode);
        assignment.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AssignmentCard)) {
            return false;
        }

        // state check
        AssignmentCard card = (AssignmentCard) other;
        return id.getText().equals(card.id.getText())
                && assignment.equals(card.assignment);
    }
}
