package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores executed commands (per session) and provides terminal-like navigation.
 * This class is UI-agnostic: callers control when to capture draft and how to apply returned text.
 */
public class CommandHistory {

    private final List<String> commands = new ArrayList<>();
    /**
     * Pointer is an index into {@link #commands} when navigating, or {@code commands.size()} to represent the draft.
     * Invariant: 0 <= pointer <= commands.size()
     */
    private int pointer = 0;
    private String draft = "";
    private boolean navigating = false;

    /**
     * Adds an executed command to history.
     */
    public void add(String commandText) {
        commands.add(commandText);
        resetNavigation();
    }

    /**
     * Resets navigation state. Call when the user edits the input normally.
     */
    public void resetNavigation() {
        navigating = false;
        draft = "";
        pointer = commands.size();
    }

    /**
     * Moves to an older command (like pressing Up). Returns the text that should be displayed.
     * If there is no history, returns the current input unchanged.
     */
    public String previous(String currentInput) {
        if (commands.isEmpty()) {
            return currentInput;
        }

        ensureDraftCaptured(currentInput);
        if (pointer > 0) {
            pointer--;
        }
        return commands.get(pointer);
    }

    /**
     * Moves to a newer command (like pressing Down). Returns the text that should be displayed.
     * When moving past the newest command, returns the captured draft input.
     */
    public String next(String currentInput) {
        if (commands.isEmpty()) {
            return currentInput;
        }

        ensureDraftCaptured(currentInput);
        if (pointer < commands.size()) {
            pointer++;
        }
        if (pointer == commands.size()) {
            navigating = false;
            return draft;
        }
        return commands.get(pointer);
    }

    private void ensureDraftCaptured(String currentInput) {
        if (!navigating) {
            navigating = true;
            draft = currentInput;
            pointer = commands.size();
        }
    }
}

