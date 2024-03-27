package data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import domain.game.GameType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SavingTest {

    private static final String EXPECTED_OUTPUT_PATH = "src/test/java/saving/expected";
    private static final String ACTUAL_OUTPUT_PATH = "src/test/java/saving/actual";

    @Test
    public void testSaveAndLoadConsistency() throws IOException {
        // Load the game from the expected output folder
        GameLoader loader = GameLoader.getInstance();
        loader.setSlotsPath(EXPECTED_OUTPUT_PATH);
        loader.loadGame();

        // Now switch to the actual output folder and save the game there
        loader.setSlotsPath(ACTUAL_OUTPUT_PATH);
        loader.saveGame();

        // Now we will make assertions to see if we are able to have consistent results
        // between save and load cycles. We know saving and loading works if we are able to cycle between
        // the two with identical output in both folders.
        File expectedDir = new File(EXPECTED_OUTPUT_PATH);
        File actualDir = new File(ACTUAL_OUTPUT_PATH);
        compareDirectories(expectedDir, actualDir);

        // Used to reset the expected output when adding new features
        // Comment out above and run this first.
//        GameLoader loader = GameLoader.getInstance();
//        loader.createNewGame(GameType.Beginner, 4, "English");
//        loader.setSlotsPath(EXPECTED_OUTPUT_PATH);
//        loader.saveGame();
    }

    public void compareDirectories(File expectedDir, File actualDir) throws IOException {
        compareContents(expectedDir, actualDir);
    }

    private void compareContents(File expected, File actual) throws IOException {
        assertEquals(expected.isDirectory(), actual.isDirectory());

        if (expected.isDirectory()) {
            File[] expectedFiles = expected.listFiles();
            File[] actualFiles = actual.listFiles();

            assert expectedFiles != null;
            assert actualFiles != null;
            assertEquals(expectedFiles.length, actualFiles.length);

            for (File expectedFile : expectedFiles) {
                File actualFile = new File(actual, expectedFile.getName());

                compareContents(expectedFile, actualFile);
            }
        } else {
            assertFileContentEquals(expected, actual);
        }
    }

    private void assertFileContentEquals(File expectedFile, File actualFile) throws IOException {
        List<String> expectedLines = Files.readAllLines(expectedFile.toPath());
        List<String> actualLines = Files.readAllLines(actualFile.toPath());

        assertEquals(expectedLines.size(), actualLines.size());
        for (String expectedLine : expectedLines) {
            assertTrue(actualLines.contains(expectedLine));
        }
    }
}