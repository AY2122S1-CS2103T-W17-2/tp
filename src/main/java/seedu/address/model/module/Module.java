package seedu.address.model.module;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a Module in the module tracker.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Module {

    // Identity fields.
    private final Code code;
    private final Title title;
    private final Description description;

    // Data fields.
    private final Mc mc;
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Module(Code code, Title title, Description description, Mc mc, Set<Tag> tags) {
        requireAllNonNull(code, title, description, mc, tags);
        this.code = code;
        this.title = title;
        this.description = description;
        this.mc = mc;
        this.tags.addAll(tags);
    }

    public Code getCode() {
        return code;
    }

    public Title getTitle() {
        return title;
    }

    public Description getDescription() {
        return description;
    }

    public Mc getMc() {
        return mc;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both modules have the same module code.
     * This defines a weaker notion of equality between two modules.
     */
    public boolean isSameModule(Module otherModule) {
        if (otherModule == this) {
            return true;
        }

        return otherModule != null
                && otherModule.getCode().equals(getCode());
    }

    /**
     * Returns true if both modules have the same identity and data fields.
     * This defines a stronger notion of equality between two modules.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Module)) {
            return false;
        }

        Module otherModule = (Module) other;
        return otherModule.getCode().equals(getCode())
                && otherModule.getTitle().equals(getTitle())
                && otherModule.getDescription().equals(getDescription())
                && otherModule.getMc().equals(getMc())
                && otherModule.getTags().equals(getTags());
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, title, description, mc, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getCode())
                .append(": ")
                .append(getTitle())
                .append("; Description: ")
                .append(getDescription())
                .append("; ")
                .append(getMc());

        Set<Tag> tags = getTags();
        if (!tags.isEmpty()) {
            builder.append("; Tags: ");
            tags.forEach(builder::append);
        }
        return builder.toString();
    }
}
