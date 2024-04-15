package data;

import java.io.File;
import java.io.IOException;

public interface Memento {

    /**
     * Saves the state held within this Memento to the given folder
     * in the form of .txt files with field->data mappings
     */
    void save(final File folder) throws IOException;

    /**
     * Restores the parent of this Memento to the state captured by this Memento
     */
    void restore();
}
