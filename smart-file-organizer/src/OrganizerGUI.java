import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.Path;
import java.util.List;
import java.io.File;

public class OrganizerGUI extends JFrame {
    private final JTextField folderField = new JTextField(30);
    private final JButton browseBtn     = new JButton("Browse…");
    private final JButton startBtn      = new JButton("Start");
    private final JButton undoBtn       = new JButton("Undo");
    private final JTextArea logArea     = new JTextArea(10, 40);

    private FileOrganizer organizer;

    public OrganizerGUI() {
        super("Smart File Organizer");
        initUI();
    }

    private void initUI() {
        JPanel top = new JPanel();
        top.add(new JLabel("Folder:"));
        top.add(folderField);
        top.add(browseBtn);
        top.add(startBtn);
        top.add(undoBtn);

        logArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(logArea);

        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        browseBtn.addActionListener(e -> onBrowse());
        startBtn .addActionListener(e -> onStart());
        undoBtn  .addActionListener(e -> onUndo());

        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void onBrowse() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(
            JFileChooser.DIRECTORIES_ONLY
        );
        int res = chooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            File dir = chooser.getSelectedFile();
            folderField.setText(dir.getAbsolutePath());
        }
    }

    private void onStart() {
        Path folder = Path.of(folderField.getText());
        organizer = new FileOrganizer(folder);
        startBtn.setEnabled(false);

        // run in background so GUI stays responsive
        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                organizer.organize();
                publish(">>> Organizing complete.");
                return null;
            }

            @Override
            protected void process(List<String> msgs) {
                msgs.forEach(m -> logArea.append(m + "\n"));
            }

            @Override
            protected void done() {
                startBtn.setEnabled(true);
            }
        };
        worker.execute();
    }

    private void onUndo() {
        if (organizer == null) {
            logArea.append("Nothing to undo.\n");
        } else {
            String result = organizer.undo();
            logArea.append(result + "\n");
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new OrganizerGUI().setVisible(true);
        });
    }
}
