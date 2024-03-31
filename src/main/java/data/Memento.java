package data;

import java.io.File;

public interface Memento {
    void save(final File folder) throws SaveException;
    void restore();
}
