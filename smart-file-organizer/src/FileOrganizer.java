import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;

public class FileOrganizer {
    private final Path root;
    private final HistoryManager history = new HistoryManager();

    public FileOrganizer(Path rootFolder) {
        this.root = rootFolder;
    }

    /**
     * Scan root folder, classify by extension, and move files into subfolders.
     */
    public void organize() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(root)) {
            for (Path file : stream) {
                if (!Files.isRegularFile(file)) continue;

                String name = file.getFileName().toString();
                String ext = getExtension(name);
                String folder = FileClassifier.classify(ext);
                Path destDir = root.resolve(folder);

                try {
                    if (!Files.exists(destDir)) {
                        Files.createDirectory(destDir);
                    }
                    Path target = destDir.resolve(name);
                    Path finalTarget = resolveConflict(target);

                    Files.move(file, finalTarget, StandardCopyOption.REPLACE_EXISTING);
                    MoveLogger.logMove(file, finalTarget);
                    history.record(new HistoryEntry(file, finalTarget, LocalDateTime.now()));
                } catch (IOException e) {
                    System.err.println("Failed moving " + file + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Cannot scan folder: " + e.getMessage());
        }
    }

    /**
     * Undo the most recent move, if any, and delete the created folder if it becomes empty.
     * @return a message describing the result.
     */
    public String undo() {
        if (!history.hasHistory()) {
            return "Nothing to undo.";
        }
        HistoryEntry last = history.undoLast();
        Path movedFrom = last.getDestination();
        Path movedTo = last.getSource();
        try {
            // Move file back
            Files.move(movedFrom, movedTo, StandardCopyOption.REPLACE_EXISTING);
            MoveLogger.logMove(movedFrom, movedTo);

            // After undo, delete the directory if empty
            Path parentDir = movedFrom.getParent();
            if (parentDir != null && Files.isDirectory(parentDir)) {
                try (DirectoryStream<Path> ds = Files.newDirectoryStream(parentDir)) {
                    if (!ds.iterator().hasNext()) {
                        Files.delete(parentDir);
                        return "Reverted: " + movedFrom.getFileName()
                            + " → " + movedTo.getFileName()
                            + " and deleted empty folder: " + parentDir.getFileName();
                    }
                } catch (IOException e) {
                    // Ignore errors during folder deletion
                }
            }

            return "Reverted: " + movedFrom.getFileName()
                + " → " + movedTo.getFileName();
        } catch (IOException e) {
            return "Undo failed for " + movedFrom.getFileName()
                + ": " + e.getMessage();
        }
    }

    private static String getExtension(String name) {
        int idx = name.lastIndexOf('.');
        return (idx > 0 ? name.substring(idx) : "");
    }

    /**
     * Resolve name conflicts by appending (1), (2), ... before the extension.
     */
    private Path resolveConflict(Path target) {
        if (!Files.exists(target)) return target;
        String fileName = target.getFileName().toString();
        String base = fileName;
        String ext = "";
        int dot = fileName.lastIndexOf('.');
        if (dot > 0) {
            base = fileName.substring(0, dot);
            ext = fileName.substring(dot);
        }
        int count = 1;
        Path parent = target.getParent();
        Path candidate;
        do {
            candidate = parent.resolve(base + "(" + count++ + ")" + ext);
        } while (Files.exists(candidate));
        return candidate;
    }
}
