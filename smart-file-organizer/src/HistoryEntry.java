import java.nio.file.Path;
import java.time.LocalDateTime;

public class HistoryEntry {
    private final Path source;
    private final Path destination;
    private final LocalDateTime timestamp;

    public HistoryEntry(Path source, Path destination, LocalDateTime timestamp) {
        this.source = source;
        this.destination = destination;
        this.timestamp = timestamp;
    }

    public Path getSource() { return source; }
    public Path getDestination() { return destination; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
