package gamedatastructures;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import SavingAndLoading.Memento;
import SavingAndLoading.MementoWriter;
import SavingAndLoading.Restorable;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

//SINGLETON CLASS
public class Bank implements Restorable {

    @SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
    private static Bank singleBankInstance = null;

    private static final int MAX_RESOURCES = 19;
    private HashMap<Resource, Integer> bank = new HashMap<>();

    private Bank() {
        bank.put(Resource.LUMBER, MAX_RESOURCES);
        bank.put(Resource.BRICK, MAX_RESOURCES);
        bank.put(Resource.WOOL, MAX_RESOURCES);
        bank.put(Resource.GRAIN, MAX_RESOURCES);
        bank.put(Resource.ORE, MAX_RESOURCES);
    }

    @SuppressFBWarnings("MS_EXPOSE_REP")
    public static synchronized Bank getInstance() {
        if (singleBankInstance == null) {
            singleBankInstance = new Bank();
        }
        return singleBankInstance;
    }

    public boolean removeResource(final Resource type, final int amount) {
        if (amount <= 0 || amount > MAX_RESOURCES) {
            throw new IllegalArgumentException("Must be a value between 1 and 19");
        }
        if (bank.get(type) - amount < 0) {
            return false;
        }
        bank.put(type, bank.get(type) - amount);
        return true;
    }

    public boolean addResource(final Resource type, final int amount) {
        if (amount <= 0 || amount > MAX_RESOURCES) {
            throw new IllegalArgumentException("Must be a value between 1 and 19");
        }
        if (bank.get(type) + amount > MAX_RESOURCES) {
            return false;
        }
        bank.put(type, bank.get(type) + amount);
        return true;
    }

    public int getResourceAmount(final Resource type) {
        return bank.get(type);
    }

    //For testing purposes, restock the bank to 19 of each card
    @SuppressFBWarnings("LI_LAZY_INIT_STATIC")
    public void resetBank() {
        singleBankInstance = new Bank();
    }

    public boolean addResources(final Resource[] resources) {
        for (int i = 0; i < resources.length; i++) {
            if (bank.get(resources[i]) == MAX_RESOURCES) {
                for (int removed = 0; removed < i; removed++) {
                    this.removeResource(resources[removed], 1);
                }
                return false;
            }
            bank.put(resources[i], bank.get(resources[i]) + 1);
        }
        return true;
    }

    public boolean removeResources(final Resource[] resources) {
        for (int i = 0; i < resources.length; i++) {
            if (this.getResourceAmount(resources[i]) == 0) {
                for (int added = 0; added < i; added++) {
                    this.addResource(resources[added], 1);
                }
                return false;
            }
            this.removeResource(resources[i], 1);
        }
        return true;
    }

    // -----------------------------------
    //
    // Restorable implementation
    //
    // -----------------------------------

    public class BankMemento implements Memento {

        private final HashMap<Resource, Integer> bank;

        private BankMemento() {
            this.bank = new HashMap<>();
            this.bank.putAll(Bank.this.bank);
        }

        @Override
        public void save(File folder) {
            // Create a MementoWriter for writing memento data
            MementoWriter writer = new MementoWriter(folder, "bank.txt");

            // Write the state of the bank's attributes to the file
            for (Map.Entry<Resource, Integer> entry : bank.entrySet()) {
                writer.writeField(entry.getKey().toString(), entry.getValue().toString());
            }
        }

    }

    @Override
    public Memento createMemento() {
        return new BankMemento();
    }

    @Override
    public void restore(Memento m) {

    }
}
