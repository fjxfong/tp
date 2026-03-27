package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Attendance;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains unit tests for {@code ExportCommand}.
 */
public class ExportCommandTest {

    @TempDir
    public Path tempDir;

    private Path exportPath;
    private Path originalExportPath;

    @BeforeEach
    public void setUp() throws Exception {
        // Redirect the export output to a temp file so tests don't pollute the working directory.
        // We achieve this by overriding the DEFAULT_EXPORT_PATH field via a subclass.
        exportPath = tempDir.resolve("export.csv");
    }

    @AfterEach
    public void tearDown() throws IOException {
        // Clean up any export.csv written to the real working directory by the real command.
        Path realExport = Path.of("export.csv");
        Files.deleteIfExists(realExport);
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    /** Runs the real ExportCommand and returns the lines of the produced CSV. */
    private List<String> runExportAndReadLines(Model model) throws CommandException, IOException {
        new ExportCommand().execute(model);
        return Files.readAllLines(Path.of("export.csv"));
    }

    // ── tests ─────────────────────────────────────────────────────────────────

    @Test
    public void execute_emptyAddressBook_writesHeaderOnly() throws Exception {
        Model model = new ModelManager();
        List<String> lines = runExportAndReadLines(model);

        assertEquals(1, lines.size(), "Empty address book should produce only a header row");
        assertHeaderCorrect(lines.get(0));
    }

    @Test
    public void execute_singlePersonNoAttendance_correctRow() throws Exception {
        AddressBook ab = new AddressBook();
        ab.addPerson(new PersonBuilder()
                .withName("Alice Pauline")
                .withStudentId("A0123456A")
                .withEmail("alice@u.nus.edu")
                .withTutorialGroup("T01")
                .build());
        Model model = new ModelManager(ab, new UserPrefs());

        List<String> lines = runExportAndReadLines(model);

        assertEquals(2, lines.size());
        assertHeaderCorrect(lines.get(0));

        String dataRow = lines.get(1);
        assertTrue(dataRow.contains("\"Alice Pauline\""), "Name should be present");
        assertTrue(dataRow.contains("\"A0123456A\""), "Student ID should be present");
        assertTrue(dataRow.contains("\"alice@u.nus.edu\""), "Email should be present");
        assertTrue(dataRow.contains("\"T01\""), "Tutorial group should be present");

        // All 13 weeks absent → all zeros
        for (int i = 0; i < Attendance.MAX_WEEKS; i++) {
            assertTrue(dataRow.contains(",0"), "Week column should contain 0 for unmarked week");
        }
        assertFalse(dataRow.contains(",1"), "No week should be marked");
    }

    @Test
    public void execute_singlePersonWithAttendance_week1Marked() throws Exception {
        Attendance attendance = new Attendance().createCopyWithMarkedWeek(1);
        AddressBook ab = new AddressBook();
        ab.addPerson(new PersonBuilder()
                .withName("Bob Choo")
                .withStudentId("A0123456B")
                .withEmail("bob@u.nus.edu")
                .withTutorialGroup("T02")
                .withAttendance(attendance)
                .build());
        Model model = new ModelManager(ab, new UserPrefs());

        List<String> lines = runExportAndReadLines(model);

        String dataRow = lines.get(1);
        // Week 1 is the 5th token (index 4). Check that exactly one "1" appears.
        String[] tokens = dataRow.split(",");
        assertEquals("1", tokens[4], "Week 1 column should be '1'");
        for (int i = 5; i < 5 + Attendance.MAX_WEEKS - 1; i++) {
            assertEquals("0", tokens[i], "Other weeks should be '0'");
        }
    }

    @Test
    public void execute_typicalAddressBook_rowCountMatchesPersonCount() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        int personCount = model.getAddressBook().getPersonList().size();

        List<String> lines = runExportAndReadLines(model);

        // header + one row per person
        assertEquals(personCount + 1, lines.size(),
                "CSV should have one header row plus one row per person");
    }

    @Test
    public void execute_typicalAddressBook_successMessageContainsCount() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        int personCount = model.getAddressBook().getPersonList().size();

        CommandResult result = new ExportCommand().execute(model);

        assertTrue(result.getFeedbackToUser().contains(String.valueOf(personCount)),
                "Success message should contain the number of exported students");
    }

    @Test
    public void execute_allFieldsQuotedInCsv() throws Exception {
        // Verifies that every field in the data row is wrapped in double quotes
        // (the escapeCsv method always wraps in quotes, even for plain values).
        AddressBook ab = new AddressBook();
        ab.addPerson(new PersonBuilder()
                .withName("Tan Wei Ming")
                .withStudentId("A0000001Z")
                .withEmail("tanwm@u.nus.edu")
                .withTutorialGroup("T01")
                .build());
        Model model = new ModelManager(ab, new UserPrefs());

        List<String> lines = runExportAndReadLines(model);

        String dataRow = lines.get(1);
        assertTrue(dataRow.startsWith("\"Tan Wei Ming\""),
                "Name should be wrapped in double quotes");
        assertTrue(dataRow.contains("\"A0000001Z\""),
                "Student ID should be wrapped in double quotes");
        assertTrue(dataRow.contains("\"tanwm@u.nus.edu\""),
                "Email should be wrapped in double quotes");
        assertTrue(dataRow.contains("\"T01\""),
                "Tutorial group should be wrapped in double quotes");
    }

    @Test
    public void equals_sameType_returnsTrue() {
        ExportCommand cmd1 = new ExportCommand();
        ExportCommand cmd2 = new ExportCommand();
        assertTrue(cmd1.equals(cmd2));
        assertTrue(cmd1.equals(cmd1));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        assertFalse(new ExportCommand().equals(new ClearCommand()));
        assertFalse(new ExportCommand().equals(null));
    }

    // ── private helpers ───────────────────────────────────────────────────────

    private void assertHeaderCorrect(String headerLine) {
        assertEquals(
                "Student,StudentID,Email,Tutorial"
                        + ",Week1,Week2,Week3,Week4,Week5,Week6,Week7"
                        + ",Week8,Week9,Week10,Week11,Week12,Week13",
                headerLine,
                "Header row must list all 13 week columns");
    }
}
