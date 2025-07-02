import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MoveLogger {
    private static final Path LOG_FILE = Path.of("file_organizer.log");
    private static final DateTimeFormatter FMT = 
        DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

    public static synchronized void logMove(Path source, Path destination) {
        String line = String.join(",",
            source.toAbsolutePath().toString(),
            destination.toAbsolutePath().toString(),
            LocalDateTime.now().format(FMT)
        ) + System.lineSeparator();

        try {
            if (!Files.exists(LOG_FILE)) {
                Files.createFile(LOG_FILE);
            }
            Files.writeString(LOG_FILE, line,
                StandardOpenOption.APPEND);
        } catch (IOException e) {
            // If logging fails, write to stderr
            System.err.println("Logging failed: " + e.getMessage());
        }
    }
}
