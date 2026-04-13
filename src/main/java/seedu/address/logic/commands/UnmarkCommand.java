package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTORIAL_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEEK;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Attendance;
import seedu.address.model.person.Person;
import seedu.address.model.person.TutorialGroup;

/**
 * Unmarks the attendance of an existing person or tutorial group in the address book.
 */
public class UnmarkCommand extends Command {

    public static final String COMMAND_WORD = "unmark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Unmarks the student identified by the index number used in the displayed student list "
            + "as attended, or unmarks the entire tutorial group.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_WEEK + "WEEK (must be a positive integer)\n"
            + "OR: " + PREFIX_TUTORIAL_GROUP + "TUTORIAL_GROUP "
            + PREFIX_WEEK + "WEEK (must be a positive integer)\n"
            + "Examples: " + COMMAND_WORD + " 1 " + PREFIX_WEEK + "2"
            + ", " + COMMAND_WORD + " " + PREFIX_TUTORIAL_GROUP + "T01 " + PREFIX_WEEK + "2";

    public static final String MESSAGE_UNMARK_PERSON_SUCCESS = "Unmarked Student: %1$s";
    public static final String MESSAGE_UNMARK_GROUP_SUCCESS =
            "Unmarked %1$d student(s) in tutorial group %2$s for week %3$d.";
    public static final String MESSAGE_ALREADY_UNMARKED =
            "This student is already unmarked for this week.*";
    public static final String MESSAGE_GROUP_NOT_FOUND =
            "No students found in tutorial group: %1$s.";
    public static final String MESSAGE_GROUP_ALREADY_UNMARKED =
            "All students in tutorial group %1$s are already unmarked for week %2$d.";
    public static final String MESSAGE_INVALID_WEEK = "Week must be a positive integer between 1 and 13.";

    private final Index index;
    private final TutorialGroup tutorialGroup;
    private final int week;

    /**
     * Creates an UnmarkCommand for a single person.
     * @param index of the person in the filtered person list to unmark
     * @param week of the attendance to unmark
     */
    public UnmarkCommand(Index index, int week) {
        requireNonNull(index);
        requireNonNull(week);
        this.index = index;
        this.tutorialGroup = null;
        this.week = week;
    }

    /**
     * Creates an UnmarkCommand for an entire tutorial group.
     * @param tutorialGroup tutorial group to unmark
     * @param week of the attendance to unmark
     */
    public UnmarkCommand(TutorialGroup tutorialGroup, int week) {
        requireNonNull(tutorialGroup);
        requireNonNull(week);
        this.index = null;
        this.tutorialGroup = tutorialGroup;
        this.week = week;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        assertExactlyOneTargetSelected();
        validateWeekOrThrow(week);

        if (tutorialGroup != null) {
            return executeGroupUnmark(model);
        }

        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToUnmark = lastShownList.get(index.getZeroBased());

        if (!personToUnmark.getAttendance().isMarked(week)) {
            throw new CommandException(MESSAGE_ALREADY_UNMARKED);
        }

        Attendance updatedAttendance = personToUnmark.getAttendance().createCopyWithUnmarkedWeek(week);

        Person unmarkedPerson = new Person(
                personToUnmark.getName(),
                personToUnmark.getPhone(),
                personToUnmark.getEmail(),
                personToUnmark.getTeleHandle().orElse(null),
                personToUnmark.getStudentId(),
                personToUnmark.getTutorialGroup(),
                updatedAttendance
        );

        model.setPerson(personToUnmark, unmarkedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_UNMARK_PERSON_SUCCESS, Messages.format(unmarkedPerson)));
    }

    private CommandResult executeGroupUnmark(Model model) throws CommandException {
        List<Person> allPersons = new ArrayList<>(model.getAddressBook().getPersonList());
        List<Person> groupMembers = new ArrayList<>();
        for (Person person : allPersons) {
            if (person.getTutorialGroup().equals(tutorialGroup)) {
                groupMembers.add(person);
            }
        }

        if (groupMembers.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_GROUP_NOT_FOUND, tutorialGroup));
        }

        int unmarkedCount = 0;
        for (Person person : groupMembers) {
            if (!person.getAttendance().isMarked(week)) {
                continue;
            }
            Attendance updatedAttendance = person.getAttendance().createCopyWithUnmarkedWeek(week);
            Person unmarkedPerson = new Person(
                    person.getName(),
                    person.getPhone(),
                    person.getEmail(),
                    person.getTeleHandle().orElse(null),
                    person.getStudentId(),
                    person.getTutorialGroup(),
                    updatedAttendance
            );
            model.setPerson(person, unmarkedPerson);
            unmarkedCount++;
        }

        if (unmarkedCount == 0) {
            throw new CommandException(String.format(MESSAGE_GROUP_ALREADY_UNMARKED, tutorialGroup, week));
        }

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_UNMARK_GROUP_SUCCESS, unmarkedCount, tutorialGroup, week));
    }

    private void validateWeekOrThrow(int week) throws CommandException {
        if (week < 1 || week > Attendance.MAX_WEEKS) {
            throw new CommandException(MESSAGE_INVALID_WEEK);
        }
    }

    private void assertExactlyOneTargetSelected() {
        boolean hasIndex = index != null;
        boolean hasTutorialGroup = tutorialGroup != null;
        assert hasIndex ^ hasTutorialGroup : "Exactly one of index or tutorialGroup must be set.";
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UnmarkCommand)) {
            return false;
        }

        UnmarkCommand otherUnmarkCommand = (UnmarkCommand) other;
        return week == otherUnmarkCommand.week
                && (index == null ? otherUnmarkCommand.index == null : index.equals(otherUnmarkCommand.index))
                && (tutorialGroup == null ? otherUnmarkCommand.tutorialGroup == null
                : tutorialGroup.equals(otherUnmarkCommand.tutorialGroup));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("tutorialGroup", tutorialGroup)
                .add("week", week)
                .toString();
    }
}
