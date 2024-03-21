package SavingAndLoading;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MementoWriter {
    private final File targetFile;

    public MementoWriter(File folder, String fileName) {
        // Ensure that the parent directory exists
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Create or clear the target file
        this.targetFile = new File(folder, fileName);
        if (targetFile.exists()) {
            targetFile.delete(); // Delete existing file
        }
        try {
            targetFile.createNewFile(); // Create a new empty file
        } catch (IOException e) {
            System.err.println("Failed to create new file: " + e.getMessage());
        }
    }

    public void writeField(String fieldName, String contents) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(targetFile, true))) {
            writer.println(fieldName + ": " + contents);
        } catch (IOException e) {
            System.err.println("Failed to write to file: " + e.getMessage());
        }
    }

    public File getSubFolder(String subFolderName) {
        File subFolder = new File(targetFile.getParent(), subFolderName);
        if (!subFolder.exists()) {
            subFolder.mkdirs();
        }
        return subFolder;
    }
}
