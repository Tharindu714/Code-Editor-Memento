import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.util.Stack;

public class CodeEditorApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(EditorFrame::new);
    }
}

class EditorFrame extends JFrame {
    private final JTextArea textArea = new JTextArea();
    private final Originator originator = new Originator();
    private final Caretaker caretaker = new Caretaker(originator);
    private boolean isRestoring = false;

    EditorFrame() {
        super("🌒 Dark Code Editor with Undo/Redo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        initUI();
        setLocationRelativeTo(null);
        setVisible(true);

        // Save the initial empty state
        caretaker.saveState();
    }

    private void initUI() {
        // Dark theme
        textArea.setBackground(Color.DARK_GRAY);
        textArea.setForeground(Color.LIGHT_GRAY);
        textArea.setCaretColor(Color.WHITE);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        // Listen for edits
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                recordState();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                recordState();
            }

            @Override
            public void changedUpdate(DocumentEvent e) { /* no-op for plain text */ }

            private void recordState() {
                if (!isRestoring) {
                    originator.setState(textArea.getText());
                    caretaker.saveState();
                }
            }
        });

        // Toolbar with Undo/Redo
        JToolBar toolBar = new JToolBar();
        JButton undoBtn = new JButton("↶ Undo");
        JButton redoBtn = new JButton("↷ Redo");
        toolBar.add(undoBtn);
        toolBar.add(redoBtn);

        undoBtn.addActionListener(e -> doUndo());
        redoBtn.addActionListener(e -> doRedo());

        // Layout
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(toolBar, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    private void doUndo() {
        if (caretaker.canUndo()) {
            isRestoring = true;
            caretaker.undo();
            textArea.setText(originator.getState());
            isRestoring = false;
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private void doRedo() {
        if (caretaker.canRedo()) {
            isRestoring = true;
            caretaker.redo();
            textArea.setText(originator.getState());
            isRestoring = false;
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
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
        // move current to redo
        Memento current = undoStack.pop();
        redoStack.push(current);
        // restore previous
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