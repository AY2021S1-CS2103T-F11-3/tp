package seedu.address.model.assignment;

import static seedu.address.commons.util.AppUtil.checkArgument;

import seedu.address.model.task.Deadline;

public class Schedule {
    // message constraints occurs if saved data is modified wrongly
    public static final String MESSAGE_CONSTRAINTS = "Suggested start time should be"
            + "before suggested end time";
    public static final String START_TIME_MESSAGE_CONSTRAINS = "Suggested start time "
            + "should only be in the format 'dd-MM-uuuu HHmm', and contain a valid date and time";
    public static final String END_TIME_MESSAGE_CONSTRAINS = "Suggested end time "
            + "should only be in the format 'dd-MM-uuuu HHmm', and contain a valid date and time";
    public static final String NOT_SCHEDULED_CONSTRAINS = "Assignment is not scheduled but "
            + "suggest start time or end time is present";
    private final boolean schedule;
    private final Deadline suggestedStartTime;
    private final Deadline suggestedEndTime;

    /**
     * Constructs a false schedule
     */
    public Schedule() {
        schedule = false;
        suggestedStartTime = null;
        suggestedEndTime = null;
    }

    /**
     * Constructs a suggested schedule
     */
    public Schedule(Deadline suggestedStartTime, Deadline suggestedEndTime) {
        checkArgument(isValidSchedule(suggestedStartTime, suggestedEndTime), MESSAGE_CONSTRAINTS);
        schedule = true;
        this.suggestedStartTime = suggestedStartTime;
        this.suggestedEndTime = suggestedEndTime;
    }

    public boolean isValidSchedule(Deadline suggestedStartTime, Deadline suggestedEndTime) {
        return suggestedStartTime.isBefore(suggestedEndTime);
    }

    public boolean isScheduled() {
        return schedule;
    }

    public Deadline getSuggestedStartTime() {
        return suggestedStartTime;
    }

    public Deadline getSuggestedEndTime() {
        return suggestedEndTime;
    }
}
