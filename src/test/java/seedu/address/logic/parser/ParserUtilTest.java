package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.TeleHandle;
import seedu.address.model.person.TutorialGroup;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_TELE_HANDLE = "notAHandle";
    private static final String INVALID_EMAIL = "u.nus.edu.sg";
    private static final String INVALID_TUTORIAL_GROUP = "#friend";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "12345678";
    private static final String VALID_TELE_HANDLE = "@rachel_walker";
    private static final String VALID_EMAIL = "rachel@u.nus.edu";
    private static final String VALID_TUTORIAL_GROUP = "T01";
    private static final String VALID_STUDENT_ID = "A0123456X";

    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
            -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parseTeleHandle_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTeleHandle((String) null));
    }

    @Test
    public void parseTeleHandle_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTeleHandle(INVALID_TELE_HANDLE));
    }

    @Test
    public void parseTeleHandle_validValueWithoutWhitespace_returnsTeleHandle() throws Exception {
        TeleHandle expectedTeleHandle = new TeleHandle(VALID_TELE_HANDLE);
        assertEquals(expectedTeleHandle, ParserUtil.parseTeleHandle(VALID_TELE_HANDLE));
    }

    @Test
    public void parseTeleHandle_validValueWithWhitespace_returnsTrimmedTeleHandle() throws Exception {
        String teleHandleWithWhitespace = WHITESPACE + VALID_TELE_HANDLE + WHITESPACE;
        TeleHandle expectedTeleHandle = new TeleHandle(VALID_TELE_HANDLE);
        assertEquals(expectedTeleHandle, ParserUtil.parseTeleHandle(teleHandleWithWhitespace));
    }

    @Test
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((String) null));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace));
    }

    @Test
    public void parseStudentId_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseStudentId(null));
    }

    @Test
    public void parseStudentId_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, StudentId.MESSAGE_CONSTRAINTS, () -> ParserUtil.parseStudentId("invalid"));
    }

    @Test
    public void parseStudentId_validValue_returnsUppercaseStudentId() throws Exception {
        StudentId expected = new StudentId(VALID_STUDENT_ID);
        assertEquals(expected, ParserUtil.parseStudentId(VALID_STUDENT_ID));
    }

    @Test
    public void parseStudentId_mixedCase_normalizesToUppercase() throws Exception {
        StudentId expected = new StudentId("A0123456X");
        assertEquals(expected, ParserUtil.parseStudentId("a0123456x"));
        assertEquals(expected, ParserUtil.parseStudentId("  a0123456x  "));
    }

    @Test
    public void parseTutorialGroup_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTutorialGroup(null));
    }

    @Test
    public void parseTutorialGroup_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTutorialGroup(INVALID_TUTORIAL_GROUP));
    }

    @Test
    public void parseTutorialGroup_validValueWithoutWhitespace_returnsTutorialGroup() throws Exception {
        TutorialGroup expectedTutorialGroup = new TutorialGroup(VALID_TUTORIAL_GROUP);
        assertEquals(expectedTutorialGroup, ParserUtil.parseTutorialGroup(VALID_TUTORIAL_GROUP));
    }

    @Test
    public void parseTutorialGroup_validValueWithWhitespace_returnsTrimmedTutorialGroup() throws Exception {
        String tutorialGroupWithWhitespace = WHITESPACE + VALID_TUTORIAL_GROUP + WHITESPACE;
        TutorialGroup expectedTutorialGroup = new TutorialGroup(VALID_TUTORIAL_GROUP);
        assertEquals(expectedTutorialGroup, ParserUtil.parseTutorialGroup(tutorialGroupWithWhitespace));
    }
}
