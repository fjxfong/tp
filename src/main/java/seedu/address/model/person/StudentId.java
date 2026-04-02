package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's student ID in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidStudentId(String)}.
 * Input may use any letter casing; the stored {@link #value} is always uppercase (canonical form).
 */
public class StudentId {

    public static final String MESSAGE_CONSTRAINTS =
            "Invalid Student ID! Use 'A', 7 digits, and one letter (e.g. A0123456X). "
            + "Letters are case-insensitive; stored in uppercase.";

    /**
     * Canonical pattern after {@link #normalizeCandidate(String)}: A, seven digits, one letter A–Z.
     */
    public static final String VALIDATION_REGEX = "^A\\d{7}[A-Z]$";

    public final String value;

    /**
     * Constructs a {@code StudentId}.
     * Leading/trailing spaces are trimmed, internal spaces removed, letters uppercased.
     *
     * @param studentId A valid student ID in any letter casing.
     */
    public StudentId(String studentId) {
        requireNonNull(studentId);
        String normalized = normalizeCandidate(studentId);
        checkArgument(isValidStudentId(studentId), MESSAGE_CONSTRAINTS);
        value = normalized;
    }

    /**
     * Returns the canonical form used for validation and storage (trim, strip spaces, uppercase).
     * Does not check validity of the result.
     */
    private static String normalizeCandidate(String raw) {
        return raw.trim().replaceAll("\\s+", " ").replace(" ", "").toUpperCase();
    }

    /**
     * Returns true if {@code test} is non-null and normalizes to a valid student ID.
     */
    public static boolean isValidStudentId(String test) {
        if (test == null) {
            return false;
        }
        String normalized = normalizeCandidate(test);
        return !normalized.isEmpty() && normalized.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof StudentId)) {
            return false;
        }
        StudentId otherStudentId = (StudentId) other;
        return value.equals(otherStudentId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
