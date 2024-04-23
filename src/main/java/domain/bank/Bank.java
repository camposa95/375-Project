package domain.bank;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import data.*;
import domain.game.NotEnoughResourcesException;
import domain.player.Player;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class Bank implements Restorable {

    private static final int MAX_RESOURCES = 19;
    private final HashMap<Resource, Integer> bank = new HashMap<>();
    private final HashMap<Player, Loan> loans = new HashMap<>();

    public Bank() {
        bank.put(Resource.LUMBER, MAX_RESOURCES);
        bank.put(Resource.BRICK, MAX_RESOURCES);
        bank.put(Resource.WOOL, MAX_RESOURCES);
        bank.put(Resource.GRAIN, MAX_RESOURCES);
        bank.put(Resource.ORE, MAX_RESOURCES);
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

    public void reset() {
        bank.put(Resource.LUMBER, MAX_RESOURCES);
        bank.put(Resource.BRICK, MAX_RESOURCES);
        bank.put(Resource.WOOL, MAX_RESOURCES);
        bank.put(Resource.GRAIN, MAX_RESOURCES);
        bank.put(Resource.ORE, MAX_RESOURCES);
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

    public void takeOutLoan(final Player player, final Resource[] resources) throws NotEnoughResourcesException {
        if (this.loans.containsKey(player)) {
            throw new IllegalStateException("Cannot take out a new loan when one already exists for this player");
        }

        if (!Loan.loanIsValid(resources)) {
            throw new IllegalArgumentException("Loan resources are invalid");
        }

        Loan l = new Loan(resources);
        l.giveLoan(this, player);
        this.loans.put(player, l);
    }

    protected Map<Player, Loan> getLoans() {
        return this.loans;
    }

    public void updateLoanDueTimes(final Player currentPlayer) {
        Loan l = this.loans.get(currentPlayer);
        if (l != null) {
            l.decrementLoanTime();
        }
    }

    public void payLoanIfDue(final Player currentPlayer) {
        Loan l = this.loans.get(currentPlayer);
        if (l != null) {
            if (!l.loanIsPaid()) {
                l.payLoan(this, currentPlayer);
            } else {
                this.loans.remove(currentPlayer);
            }
        }
    }

    // -----------------------------------
    //
    // Restorable implementation
    //
    // -----------------------------------

    public class BankMemento implements Memento {

        private final HashMap<Resource, Integer> bank;

        // Storage Constants
        private static final String TARGET_FILE_NAME = "bank.txt";

        private BankMemento() {
            this.bank = new HashMap<>();
            this.bank.putAll(Bank.this.bank);
        }

        @SuppressFBWarnings("EI_EXPOSE_REP2")
        public BankMemento(final File folder) {
            this.bank = new HashMap<>();
            MementoReader reader = new MementoReader(folder, TARGET_FILE_NAME);

            for (Map.Entry<String, String> entry : reader.readAllFields().entrySet()) {
                this.bank.put(Resource.valueOf(entry.getKey()),
                        Integer.parseInt(entry.getValue()));
            }
        }

        public void save(final File folder) throws IOException {
            // Create a MementoWriter for writing memento data
            MementoWriter writer = new MementoWriter(folder, TARGET_FILE_NAME);

            // Write the state of the bank's attributes to the file
            for (Map.Entry<Resource, Integer> entry : bank.entrySet()) {
                writer.writeField(entry.getKey().toString(), entry.getValue().toString());
            }
        }

        public void restore() {
            // Restore the bank state from the memento
            Bank.this.bank.clear();
            Bank.this.bank.putAll(this.bank);
        }
    }

    @Override
    public Memento createMemento() {
        return new BankMemento();
    }
}
