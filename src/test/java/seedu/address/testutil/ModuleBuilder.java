package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.module.Code;
import seedu.address.model.module.Description;
import seedu.address.model.module.Mc;
import seedu.address.model.module.Module;
import seedu.address.model.module.Title;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class ModuleBuilder {

    public static final String DEFAULT_CODE = "CS2103T";
    public static final String DEFAULT_TITLE = "Software Engineering";
    public static final String DEFAULT_DESCRIPTION = "Covers the main areas of software development";
    public static final int DEFAULT_MC = 4;

    private Code code;
    private Title title;
    private Description description;
    private Mc mc;
    private Set<Tag> tags;

    /**
     * Creates a {@code ModuleBuilder} with the default details.
     */
    public ModuleBuilder() {
        code = new Code(DEFAULT_CODE);
        title = new Title(DEFAULT_TITLE);
        description = new Description(DEFAULT_DESCRIPTION);
        mc = new Mc(DEFAULT_MC);
        tags = new HashSet<>();
    }

    /**
     * Initializes the ModuleBuilder with the data of {@code moduleToCopy}.
     */
    public ModuleBuilder(Module moduleToCopy) {
        code = moduleToCopy.getCode();
        title = moduleToCopy.getTitle();
        description = moduleToCopy.getDescription();
        mc = moduleToCopy.getMc();
        tags = new HashSet<>(moduleToCopy.getTags());
    }

    /**
     * Sets the {@code Code} of the {@code Module} that we are building.
     */
    public ModuleBuilder withCode(String code) {
        this.code = new Code(code);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Module} that we are building.
     */
    public ModuleBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Title} of the {@code Module} that we are building.
     */
    public ModuleBuilder withTitle(String title) {
        this.title = new Title(title);
        return this;
    }

    /**
     * Sets the {@code Description} of the {@code Module} that we are building.
     */
    public ModuleBuilder withDescription(String description) {
        this.description = new Description(description);
        return this;
    }

    /**
     * Sets the {@code Mc} of the {@code Module} that we are building.
     */
    public ModuleBuilder withMc(int mc) {
        this.mc = new Mc(mc);
        return this;
    }

    public Module build() {
        return new Module(code, title, description, mc, tags);
    }

}
