package SavingAndLoading;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MementoReader {
    private final File sourceFile;

    public MementoReader(File folder, String fileName) {
        this.sourceFile = new File(folder, fileName);
        if (!this.sourceFile.exists() || !this.sourceFile.isFile()) {
            throw new IllegalArgumentException("File does not exist: " + sourceFile.getAbsolutePath());
        }
    }

    public String readField(String fieldName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(sourceFile))) {
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
        try (BufferedReader reader = new BufferedReader(new FileReader(sourceFile))) {
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

    public File getSubFolder(String subFolderName) {
        File parentFolder = sourceFile.getParentFile();
        File subFolder = new File(parentFolder, subFolderName);
        if (!subFolder.exists() || !subFolder.isDirectory()) {
            throw new IllegalArgumentException("Folder does not exist: " + subFolder.getAbsolutePath());
        }
        return subFolder;
    }
}

