package seedu.tracker.model.module;

import static java.util.Objects.requireNonNull;
import static seedu.tracker.commons.util.CollectionUtil.requireAllNonNull;

/**
 * Represents the completion status of a course requirement
 */
public class McProgress {
    private Mc target;
    private Mc completed;
    private String tagName;

    /**
     * Constructs a McProgress.
     * @param completed completed Mcs.
     * @param target target Mcs.
     * @param tagName tag tracked to calculate completed Mcs.
     */
    public McProgress(Mc completed, Mc target, String tagName) {
        requireAllNonNull(completed, target, tagName);
        this.completed = completed;
        this.target = target;
        this.tagName = tagName;
    }

    /**
     * Constructs a McProgress copy from another McProgress object.
     * @param toCopy McProgress object to be copied from.
     */
    public McProgress(McProgress toCopy) {
        requireNonNull(toCopy);
        this.completed = toCopy.completed;
        this.target = toCopy.target;
        this.tagName = toCopy.tagName;
    }

    public void setTarget(Mc newTarget) {
        this.target = newTarget;
    }

    public Mc getTarget() {
        return target;
    }

    public void setCompleted(Mc newCompletedMcs) {
        this.completed = newCompletedMcs;
    }

    public Mc getCompleted() {
        return completed;
    }

    public double getCompletionRatio() {
        return completed.value >= target.value
                ? 1
                : (double) completed.value / target.value;
    }

    public boolean isCompleted() {
        return getCompletionRatio() == 1;
    }
}
