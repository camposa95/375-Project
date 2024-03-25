package saving;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MementoReader {
    private final File sourceFile;

    public MementoReader(final File folder, final String fileName) {
        this.sourceFile = new File(folder, fileName);
        if (!this.sourceFile.exists() || !this.sourceFile.isFile()) {
            throw new IllegalArgumentException("File does not exist: " + sourceFile.getAbsolutePath());
        }
    }

    public String readField(final String fieldName) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(fieldName + ":")) {
                    // Return the contents after the field name
                    return line.substring(fieldName.length() + 1).trim();
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read from file: " + e.getMessage());
        }

        // If the loop completes without finding the field, throw an exception
        throw new IllegalArgumentException("Field not found: " + fieldName);
    }


    public Map<String, String> readAllFields() {
        Map<String, String> fields = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split each line into key and value based on the first ':'
                int index = line.indexOf(':');
                if (index != -1) {
                    String key = line.substring(0, index).trim();
                    String value = line.substring(index + 1).trim();
                    fields.put(key, value);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read from file: " + e.getMessage());
        }
        return fields;
    }

    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    public File getSubFolder(final String subFolderName) {
        File parentFolder = sourceFile.getParentFile();
        File subFolder = new File(parentFolder, subFolderName);
        if (!subFolder.exists() || !subFolder.isDirectory()) {
            throw new IllegalArgumentException("Folder does not exist: " + subFolder.getAbsolutePath());
        }
        return subFolder;
    }
}

