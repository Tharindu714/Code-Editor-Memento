import javax.swing.*;
import java.util.Stack;

public class CodeEditorApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(EditorFrame::new);
    }
}

class Originator {
    private String state = "";

    void setState(String state) {
        this.state = state;
    }

    String getState() {
        return state;
    }

    Memento saveToMemento() {
        return new Memento(state);
    }

    void restoreFromMemento(Memento m) {
        this.state = m.getSavedState();
    }
}

class Caretaker {
    private final Originator originator;
    private final Stack<Memento> undoStack = new Stack<>();
    private final Stack<Memento> redoStack = new Stack<>();

    Caretaker(Originator originator) {
        this.originator = originator;
    }

    void saveState() {
        originator.setState(originator.getState()); // update before saving
        undoStack.push(originator.saveToMemento());
        redoStack.clear();
    }

    boolean canUndo() {
        // keep at least one state so you can’t undo past initial
        return undoStack.size() > 1;
    }

    void undo() {
        if (!canUndo()) return;
        Memento current = undoStack.pop();
        redoStack.push(current);
        Memento previous = undoStack.peek();
        originator.restoreFromMemento(previous);
    }

    boolean canRedo() {
        return !redoStack.isEmpty();
    }

    void redo() {
        if (!canRedo()) return;
        Memento m = redoStack.pop();
        originator.restoreFromMemento(m);
        undoStack.push(m);
    }
}

class Memento {
    private final String state;

    Memento(String state) {
        this.state = state;
    }

    String getSavedState() {
        return state;
    }
}