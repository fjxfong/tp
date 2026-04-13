package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TutorialGroupTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TutorialGroup(null));
    }

    @Test
    public void constructor_invalidTutorialGroup_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new TutorialGroup(""));
        assertThrows(IllegalArgumentException.class, () -> new TutorialGroup("ab"));
        assertThrows(IllegalArgumentException.class, () -> new TutorialGroup("abcdef"));
        assertThrows(IllegalArgumentException.class, () -> new TutorialGroup("a*b"));
    }

    @Test
    public void isValidTutorialGroup() {
        // null tutorial group
        assertThrows(NullPointerException.class, () -> TutorialGroup.isValidTutorialGroup(null));

        // invalid tutorial groups
        assertFalse(TutorialGroup.isValidTutorialGroup(""));
        assertFalse(TutorialGroup.isValidTutorialGroup("a"));
        assertFalse(TutorialGroup.isValidTutorialGroup("ab"));
        assertFalse(TutorialGroup.isValidTutorialGroup("abcdef"));
        assertFalse(TutorialGroup.isValidTutorialGroup("T01234"));
        assertFalse(TutorialGroup.isValidTutorialGroup("t-01"));
        assertFalse(TutorialGroup.isValidTutorialGroup("#ab"));
        assertFalse(TutorialGroup.isValidTutorialGroup("hubby*"));

        // valid tutorial groups (3 to 5 alphanumeric)
        assertTrue(TutorialGroup.isValidTutorialGroup("T01"));
        assertTrue(TutorialGroup.isValidTutorialGroup("T12"));
        assertTrue(TutorialGroup.isValidTutorialGroup("T123"));
        assertTrue(TutorialGroup.isValidTutorialGroup("t01"));
        assertTrue(TutorialGroup.isValidTutorialGroup("A01"));
        assertTrue(TutorialGroup.isValidTutorialGroup("TAB"));
        assertTrue(TutorialGroup.isValidTutorialGroup("Lab"));
        assertTrue(TutorialGroup.isValidTutorialGroup("CS204"));
    }

    @Test
    public void equals() {
        TutorialGroup tutorialGroup = new TutorialGroup("T01");

        // same values -> returns true
        assertTrue(tutorialGroup.equals(new TutorialGroup("T01")));

        // same object -> returns true
        assertTrue(tutorialGroup.equals(tutorialGroup));

        // null -> returns false
        assertFalse(tutorialGroup.equals(null));

        // different types -> returns false
        assertFalse(tutorialGroup.equals(5.0f));

        // different values -> returns false
        assertFalse(tutorialGroup.equals(new TutorialGroup("T02")));
    }

    @Test
    public void hashCodeTest() {
        TutorialGroup tutorialGroup = new TutorialGroup("T01");
        assertEquals(tutorialGroup.hashCode(), new TutorialGroup("T01").hashCode());
    }
}
