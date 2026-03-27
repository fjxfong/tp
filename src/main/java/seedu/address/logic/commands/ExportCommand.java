package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Attendance;
import seedu.address.model.person.Person;

/**
 * Exports all student data to a CSV file.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Exports all student data to a CSV file named 'export.csv' in the current directory.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Exported %1$d student(s) to: %2$s";
    public static final String MESSAGE_FAILURE = "Failed to export data: %1$s";

    private static final Path DEFAULT_EXPORT_PATH = Paths.get("export.csv");

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> allPersons = model.getAddressBook().getPersonList();

        try {
            writeCsv(allPersons, DEFAULT_EXPORT_PATH);
        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_FAILURE, e.getMessage()));
        }

        return new CommandResult(
                String.format(MESSAGE_SUCCESS, allPersons.size(), DEFAULT_EXPORT_PATH.toAbsolutePath()));
    }

    private void writeCsv(List<Person> persons, Path path) throws IOException {
        Files.createDirectories(path.getParent() == null ? Paths.get(".") : path.getParent());

        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(path))) {
            // Write header
            StringBuilder header = new StringBuilder("Student,StudentID,Email,Tutorial");
            for (int week = 1; week <= Attendance.MAX_WEEKS; week++) {
                header.append(",Week").append(week);
            }
            writer.println(header);

            // Write one row per person
            for (Person person : persons) {
                StringBuilder row = new StringBuilder();
                row.append(escapeCsv(person.getName().fullName)).append(",");
                row.append(escapeCsv(person.getStudentId().value)).append(",");
                row.append(escapeCsv(person.getEmail().value)).append(",");
                row.append(escapeCsv(person.getTutorialGroup().value));

                for (int week = 1; week <= Attendance.MAX_WEEKS; week++) {
                    row.append(",").append(person.getAttendance().isMarked(week) ? "1" : "0");
                }
                writer.println(row);
            }
        }
    }

    /**
     * Wraps a field in double quotes and escapes any internal double quotes.
     */
    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }

    @Override
    public boolean equals(Object other) {
        return other == this || other instanceof ExportCommand;
    }

    @Override
    public int hashCode() {
        return ExportCommand.class.hashCode();
    }
}
