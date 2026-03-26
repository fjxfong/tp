[![codecov](https://codecov.io/gh/AY2526S2-CS2103T-T13-2/tp/graph/badge.svg?token=M0OYDBZOB6)](https://codecov.io/gh/AY2526S2-CS2103T-T13-2/tp)

[![Java CI](https://github.com/AY2526S2-CS2103T-T13-2/tp/actions/workflows/gradle.yml/badge.svg)](https://github.com/AY2526S2-CS2103T-T13-2/tp/actions/workflows/gradle.yml)

# CLI-Tacts

![Ui](docs/images/Ui.png)

**CLI-Tacts** (Command Line Interface Contacts) is a desktop application designed for **CS2040S Teaching Assistants** who manage multiple tutorial groups and prefer fast, keyboard-driven workflows.

Instead of juggling fragmented spreadsheets or slow GUI-based portals, CLI-Tacts gives TAs a single, locally-stored system to manage student records and track attendance in real time — without ever leaving the keyboard.

---

## Target User

CLI-Tacts is built for university Teaching Assistants (TAs) in CS2040S who:

- Manage 3–5 tutorial groups of ~20 students each
- Need to mark or unmark attendance quickly during live sessions
- Are comfortable with CLI tools and value speed over clicking
- Prefer locally-stored data over cloud-dependent portals

---

## Value Proposition

CLI-Tacts helps TAs efficiently manage student details and attendance across multiple tutorial groups through a fast CLI. By centralising student records locally, TAs can perform administrative tasks — adding, finding, marking/unmarking attendance, and removing students — without disrupting their real-time teaching flow.

**What CLI-Tacts does:**
- Add and manage student records (name, student ID, email, tutorial group)
- Delete students who drop or change class
- Search for students by name (partial matching supported)
- Mark or unmark weekly attendance by index
- Automatically save all changes to disk

**What CLI-Tacts does NOT do:**
- Replace university administrative or grading systems
- Handle assignment grading or submissions
- Communicate directly with students
- Manage students outside a TA's assigned groups
- Integrate with university authentication systems

---

## Features

### 1. Add a Student

```
add n/<NAME> i/<STUDENT_ID> e/<EMAIL> t/<TUTORIAL_GROUP>
```

Example: `add n/John Doe i/A0123456X e/e0123456@nus.edu.sg t/T12`

Adds a new student record. Duplicate student IDs are rejected.

---

### 2. Delete a Student

```
delete <INDEX>
```

Example: `delete 3`

Removes the student at the specified index in the currently displayed list.

---

### 3. Find a Student by Name or Tutorial Group

```
find n/<NAME_KEYWORD> [MORE_KEYWORDS]... [t/<TUTORIAL_GROUP>]...
```

Examples:

- `find n/John` - finds all students with "John" in their name.
- `find t/T01` - finds all students in tutorial group T01.
- `find n/John t/T01` - finds students named "John" in tutorial group T01.

Supports partial, case-insensitive matching.

---

### 4. Mark Attendance

```
mark <INDEX> w/<WEEK_NUMBER>
```

Example: `mark 1 w/3` — marks the student as present for Week 3.

---

### 5. Unmark Attendance

```
unmark <INDEX> w/<WEEK_NUMBER>
```

Example: `unmark 1 w/3` — unmarks the student's attendance for Week 3.

---

### 6. Auto-Save

All changes (add, delete, edit, mark, unmark, clear) are saved automatically to a local `data.json` file. Data is loaded on startup — no manual save needed.

---

## Command Summary

| Action           | Format                                              |
|------------------|-----------------------------------------------------|
| Add student      | `add n/<NAME> i/<STUDENT_ID> e/<EMAIL> t/<TUTORIAL_GROUP>` |
| Delete student   | `delete <INDEX>`                                    |
| Find by name or tutorial group | `find n/<NAME_KEYWORD> [MORE_KEYWORDS]... [t/<TUTORIAL_GROUP>]...` |
| Mark attendance  | `mark <INDEX> w/<WEEK>`                             |
| Unmark attendance | `unmark <INDEX> w/<WEEK>`                          |
| List all         | `list`                                              |
| Clear all        | `clear`                                             |
| Exit             | `exit`                                              |

---

## Notes on Command Format

- Words in `UPPER_CASE` are parameters to be supplied by the user (e.g. `n/NAME` → `n/John Doe`).
- Parameters can be entered in any order.
- Extraneous parameters for commands that take no parameters (e.g. `list`, `exit`, `clear`) will be ignored.
- Student IDs are case-insensitive (`A0123456X` = `a0123456x`).
- Tutorial groups follow the format `T` followed by digits (e.g. `T01`, `T12`).

---

## Documentation

For full documentation, see the [CLI-Tacts Product Website](https://AY2526S2-CS2103T-T13-2.github.io/tp/).

---

## Acknowledgements

This project is based on the [AddressBook-Level3](https://se-education.org/addressbook-level3) project created by the [SE-EDU initiative](https://se-education.org).
