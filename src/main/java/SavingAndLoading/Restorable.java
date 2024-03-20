package SavingAndLoading;

public interface Restorable {
    public Memento createMemento();
    public void restore(Memento m);
}
