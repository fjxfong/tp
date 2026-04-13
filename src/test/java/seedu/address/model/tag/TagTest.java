package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Tag(""));
        assertThrows(IllegalArgumentException.class, () -> new Tag("bad tag"));
        assertThrows(IllegalArgumentException.class, () -> new Tag("#friend"));
    }

    @Test
    public void isValidTagName() {
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));

        assertFalse(Tag.isValidTagName(""));
        assertFalse(Tag.isValidTagName("hello world"));
        assertFalse(Tag.isValidTagName("a-b"));

        assertTrue(Tag.isValidTagName("friend"));
        assertTrue(Tag.isValidTagName("T01"));
        assertTrue(Tag.isValidTagName("tag123"));
    }

}
