package saving;

import java.io.File;

public interface Memento {
    public void save(final File folder) throws SaveException;
    public void restore();
}
