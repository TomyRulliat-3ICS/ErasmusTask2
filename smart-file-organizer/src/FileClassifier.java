public class FileClassifier {
    /**
     * Given an extension like ".jpg" or "", returns a folder name.
     * E.g. ".txt" → "txt", "" → "others"
     */
    public static String classify(String ext) {
        if (ext == null || ext.isBlank()) {
            return "others";
        }
        // drop the leading dot:
        return ext.substring(1).toLowerCase();
    }
}
