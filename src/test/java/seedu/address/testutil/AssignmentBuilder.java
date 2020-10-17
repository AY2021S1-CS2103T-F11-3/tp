package seedu.address.testutil;

import seedu.address.model.assignment.Assignment;
import seedu.address.model.assignment.Deadline;
import seedu.address.model.assignment.ModuleCode;
import seedu.address.model.assignment.Name;
import seedu.address.model.assignment.Remind;
import seedu.address.model.assignment.Schedule;

/**
 * A utility class to help with building Assignment objects.
 */
public class AssignmentBuilder {

    public static final String DEFAULT_NAME = "CS1231S Homework";
    public static final String DEFAULT_DEADLINE = "01-02-2020 1800";
    public static final String DEFAULT_MODULE_CODE = "CS2103T";
    public static final boolean DEFAULT_REMIND = false;
    public static final Deadline DEFAULT_SUGGESTED_START_TIME = new Deadline("01-02-2020 1800");
    public static final Deadline DEFAULT_SUGGESTED_END_TIME = new Deadline("01-02-2020 2100");


    private Name name;
    private Deadline deadline;
    private ModuleCode moduleCode;
    private Remind remind;
    private Schedule schedule;

    /**
     * Creates a {@code AssignmentBuilder} with the default details.
     */
    public AssignmentBuilder() {
        name = new Name(DEFAULT_NAME);
        deadline = new Deadline(DEFAULT_DEADLINE);
        moduleCode = new ModuleCode(DEFAULT_MODULE_CODE);
        remind = new Remind(DEFAULT_REMIND);
        schedule = new Schedule(DEFAULT_SUGGESTED_START_TIME, DEFAULT_SUGGESTED_END_TIME);
    }

    /**
     * Initializes the AssignmentBuilder with the data of {@code assignmentToCopy}.
     */

    public AssignmentBuilder(Assignment assignmentToCopy) {
        name = assignmentToCopy.getName();
        deadline = assignmentToCopy.getDeadline();
        moduleCode = assignmentToCopy.getModuleCode();
        remind = assignmentToCopy.getRemind();
        schedule = assignmentToCopy.getSchedule();
    }

    /**
     * Sets the {@code Name} of the {@code Assignment} that we are building.
     */
    public AssignmentBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@code ModuleCode} of the {@code Assignment} that we are building.
     */
    public AssignmentBuilder withModuleCode(String moduleCode) {
        this.moduleCode = new ModuleCode(moduleCode);
        return this;
    }

    /**
     * Sets the {@code Deadline} of the {@code Assignment} that we are building.
     */
    public AssignmentBuilder withDeadline(String deadline) {
        this.deadline = new Deadline(deadline);
        return this;
    }

    /**
     * Sets the {@code Remind} of the {@code Assignment} that we are building.
     * @return
     */
    public AssignmentBuilder withRemindersSet() {
        this.remind = new Remind().setReminder();
        return this;
    }

    /**
     * Sets the {@code Schedule} of the {@code Assignment} that we are building.
     */
    public AssignmentBuilder withSchedule(Deadline suggestedStartTime, Deadline suggestedEndTime) {
        this.schedule = new Schedule(suggestedStartTime, suggestedEndTime);
        return this;
    }

    public Assignment build() {
        return new Assignment(name, deadline, moduleCode, remind, schedule);
    }

}
