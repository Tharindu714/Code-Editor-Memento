import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class EditorFrame extends JFrame {
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
