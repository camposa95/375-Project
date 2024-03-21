package SavingAndLoading;

import java.io.File;

public interface Memento {
    public void save(File folder);
    public void restore();
}
