package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CommandHistoryTest {

    @Test
    public void previous_noCommands_returnsInputUnchanged() {
        CommandHistory history = new CommandHistory();
        assertEquals("draft", history.previous("draft"));
    }

    @Test
    public void next_noCommands_returnsInputUnchanged() {
        CommandHistory history = new CommandHistory();
        assertEquals("draft", history.next("draft"));
    }

    @Test
    public void previous_fromDraft_retrievesMostRecent() {
        CommandHistory history = new CommandHistory();
        history.add("a");
        history.add("b");
        history.add("c");

        assertEquals("c", history.previous(""));
    }

    @Test
    public void previous_atOldest_staysAtOldest() {
        CommandHistory history = new CommandHistory();
        history.add("a");
        history.add("b");

        assertEquals("b", history.previous(""));
        assertEquals("a", history.previous("b"));
        assertEquals("a", history.previous("a"));
    }

    @Test
    public void next_afterPrevious_movesForward_andRestoresDraftAtEnd() {
        CommandHistory history = new CommandHistory();
        history.add("a");
        history.add("b");
        history.add("c");

        assertEquals("c", history.previous("my draft"));
        assertEquals("my draft", history.next("c"));
    }

    @Test
    public void next_middleOfHistory_movesToMoreRecent() {
        CommandHistory history = new CommandHistory();
        history.add("a");
        history.add("b");
        history.add("c");

        assertEquals("c", history.previous(""));
        assertEquals("b", history.previous("c"));
        assertEquals("c", history.next("b"));
    }

    @Test
    public void resetNavigation_afterTyping_capturesNewDraft() {
        CommandHistory history = new CommandHistory();
        history.add("a");
        history.add("b");

        assertEquals("b", history.previous("draft1"));
        history.resetNavigation();

        assertEquals("b", history.previous("draft2"));
        assertEquals("draft2", history.next("b"));
    }
}

