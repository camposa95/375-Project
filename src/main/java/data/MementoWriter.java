package data;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class MementoWriter {
    private final File targetFile;

    public MementoWriter(final File folder, final String fileName) throws SaveException {
        // Ensure that the parent directory exists
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                throw new SaveException("Failed to make folder: " + folder.getName());
            }
        }

        // Clear and recreate the target file
        this.targetFile = new File(folder, fileName);
        if (targetFile.exists()) {
            if (!targetFile.delete()) { // Delete existing file
                throw new SaveException("Failed to make clear existing data from: " + targetFile.getName());
            }
        }
        try {
            if (!targetFile.createNewFile()) { // Create a new empty file
                throw new SaveException("Failed to make recreate file: " + targetFile.getName());
            }
        } catch (IOException e) {
            throw new SaveException("Failed to make recreate file: " + targetFile.getName());
        }
    }

    public void writeField(final String fieldName, final String contents) throws SaveException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(targetFile, StandardCharsets.UTF_8, true))) {
            writer.println(fieldName + ": " + contents);
        } catch (IOException e) {
            throw new SaveException("Failed to write field: " + fieldName);
        }
    }

    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    public File getSubFolder(final String subFolderName) throws SaveException {
        File subFolder = new File(targetFile.getParent(), subFolderName);
        if (!subFolder.exists()) {
            if (!subFolder.mkdirs()) {
                throw new SaveException("Failed to make sub folder: " + subFolder.getName());
            }
        }
        return subFolder;
    }
}
