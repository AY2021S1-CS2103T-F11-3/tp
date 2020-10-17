package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.assignment.Task;
import seedu.address.model.lesson.Lesson;
import seedu.address.timetable.TimetableData;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Assignment> filteredAssignments;
    private final FilteredList<Assignment> remindedAssignments;
    private final FilteredList<Lesson> lessons;
    private final FilteredList<Task> filteredTasks;
    private Model previousModel;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs, Model previousModel) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredAssignments = new FilteredList<>(this.addressBook.getAssignmentList());
        remindedAssignments = new FilteredList<>(
                this.addressBook.getAssignmentList(), PREDICATE_SHOW_ALL_REMINDED_ASSIGNMENTS);
        lessons = new FilteredList<>(this.addressBook.getLessonList());
        filteredTasks = new FilteredList<>(this.addressBook.getTaskList());
        this.previousModel = previousModel;
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs(), null);
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void preUpdateModel() {
        System.out.println("CAK");
        this.previousModel = new ModelManager(this.addressBook, this.userPrefs, this.previousModel);
    }

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public void setPreviousModel(Model previousModel) {
        this.previousModel = previousModel;
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public void importTimetable(TimetableData data) {
        addressBook.importTimetable(data);
    }

    @Override
    public boolean hasAssignment(Assignment assignment) {
        requireNonNull(assignment);
        return addressBook.hasAssignment(assignment);
    }

    @Override
    public void deleteAssignment(Assignment target) {
        preUpdateModel();
        addressBook.removeAssignment(target);
    }

    @Override
    public void addAssignment(Assignment assignment) {
        preUpdateModel();
        addressBook.addAssignment(assignment);
        updateFilteredAssignmentList(PREDICATE_SHOW_ALL_ASSIGNMENT);
    }

    @Override
    public void setAssignment(Assignment target, Assignment editedAssignment) {
        preUpdateModel();
        requireAllNonNull(target, editedAssignment);
        addressBook.setAssignment(target, editedAssignment);
    }

    @Override
    public Model getPreviousModel() {
        return this.previousModel;
    }

    //=========== Filtered Assignment List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Assignment} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Assignment> getFilteredAssignmentList() {
        return filteredAssignments;
    }

    @Override
    public void updateFilteredAssignmentList(Predicate<Assignment> predicate) {
        requireNonNull(predicate);
        filteredAssignments.setPredicate(predicate);
    }

    //=========== Task List Accessors =============================================================

    @Override
    public ObservableList<Task> getFilteredTaskList() {
        return filteredTasks;
    }

    /**
     * Updates filtered task list with given predicate.
     * @param predicate Returns a true if a task matches the predicate, false otherwise.
     */
    public void updateFilteredTaskList(Predicate<Task> predicate) {
        requireNonNull(predicate);
        filteredTasks.setPredicate(predicate);
    }

    //=========== Reminded Assignments List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Assignment} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Assignment> getRemindedAssignmentsList() {
        return remindedAssignments;
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return addressBook.equals(other.addressBook)
                && userPrefs.equals(other.userPrefs)
                && filteredAssignments.equals(other.filteredAssignments)
                && (previousModel == null || previousModel.equals(other.previousModel))
                && (other.previousModel == null || other.previousModel.equals(previousModel));
    }

}
