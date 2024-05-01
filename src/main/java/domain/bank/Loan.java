package domain.bank;

import data.*;
import domain.game.NotEnoughResourcesException;
import domain.player.Player;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Loan implements Restorable {
    public static final int MAX_LOAN_SIZE = 3;
    private static final int TURNS_UNTIL_DUE = 5;
    private Player loanPlayer;
    private Resource[] loanResourcesBorrowed;
    private Resource[] resourcesDue;
    private int turnsUntilDue;

    protected Loan() {

    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public Loan(final Player player, final Resource[] resourcesBorrowed) {
        this.loanPlayer = player;
        this.loanResourcesBorrowed = resourcesBorrowed;
        this.resourcesDue = calcResourcesDue();
        this.turnsUntilDue = TURNS_UNTIL_DUE;
    }

    public boolean isEmptyLoan() {
        return loanPlayer == null;
    }

    public static boolean loanIsValid(final Resource[] resourcesBorrowed) {
        return resourcesBorrowed.length <= MAX_LOAN_SIZE && resourcesBorrowed.length > 0;
    }

    public void decrementLoanTime() {
        if (this.turnsUntilDue > 0) {
            this.turnsUntilDue--;
        }
    }

    public boolean loanIsDue() {
        return this.turnsUntilDue == 0;
    }

    public boolean loanIsPaid() {
        return this.resourcesDue.length == 0;
    }

    public void payLoan(final Bank bank, final Player player) {
        if (loanIsDue()) {
            Resource[] removed = player.hand.removeExistingResources(this.resourcesDue);
            bank.addResources(removed);
            this.resourcesDue = getRemainingResourcesDue(this.resourcesDue, removed);
        }
    }

    protected static Resource[] getRemainingResourcesDue(final Resource[] due, final Resource[] removed) {
        ArrayList<Resource> remaining = new ArrayList<>();
        boolean found = false;
        for (int i = 0; i < due.length; i++) {
            for (int j = 0; j < removed.length; j++) {
                if (due[i] == removed[j]) {
                    found = true;
                    removed[j] = Resource.ANY;
                    break;
                }
            }

            if (!found) {
                remaining.add(due[i]);
            }
            found = false;
        }

        return remaining.toArray(new Resource[remaining.size()]);
    }

    public void giveLoan(final Bank bank, final Player player) throws NotEnoughResourcesException {
        if (!bank.removeResources(this.loanResourcesBorrowed)) {
            throw new NotEnoughResourcesException();
        }

        player.addResources(this.loanResourcesBorrowed);
    }

    protected Resource[] calcResourcesDue() {
        Resource minResource = Resource.ANY;
        int minCount = 0;
        for (Resource r : Resource.values()) {
            int contains = 0;
            for (Resource borrow : this.loanResourcesBorrowed) {
                if (borrow == r) {
                    contains++;
                }
            }

            if (contains > 0) {
                if (minResource == Resource.ANY || contains < minCount) {
                    minCount = contains;
                    minResource = r;
                }
            }
        }

        Resource[] due = new Resource[this.loanResourcesBorrowed.length + 1];
        int idx = 0;
        for (Resource r : this.loanResourcesBorrowed) {
            due[idx] = r;
            idx++;
        }
        due[idx] = minResource;

        return due;
    }

    public class LoanMemento implements Memento {
        private Player player;
        private Resource[] resourcesBorrowed;
        private Resource[] resourcesDue;
        private int turnsUntilDue;
        private static final String TARGET_FILE_NAME = "loan.txt";
        private static final String PLAYER = "Player";
        private static final String RESOURCES_BORROWED = "ResourcesBorrowed";
        private static final String RESOURCES_DUE = "ResourcesDue";
        private static final String TURNS_UNTIL_DUE = "TurnsUntilDue";

        private LoanMemento() {
            this.player = Loan.this.loanPlayer;
            this.resourcesBorrowed = Loan.this.loanResourcesBorrowed;
            this.resourcesDue = Loan.this.resourcesDue;
            this.turnsUntilDue = Loan.this.turnsUntilDue;
        }

        @SuppressFBWarnings("EI_EXPOSE_REP2")
        public LoanMemento(final File folder) {
            // Create a MementoReader for reading memento data
            MementoReader reader = new MementoReader(folder, TARGET_FILE_NAME);

            // Read simple fields from the file
            this.player = parsePlayer(reader.readField(PLAYER));
            this.resourcesBorrowed = parseResourceArray(reader.readField(RESOURCES_BORROWED));
            this.resourcesDue = parseResourceArray(reader.readField(RESOURCES_DUE));
            this.turnsUntilDue = Integer.parseInt(reader.readField(TURNS_UNTIL_DUE));
        }

        private Player parsePlayer(final String ownerString) {
            // Extract player number from the string representation
            int playerNum = Integer.parseInt(ownerString.substring(ownerString.lastIndexOf(" ") + 1));
            // Retrieve the player using the GameLoader
            return GameLoader.getInstance().getPlayerByNum(playerNum);
        }

        private Resource[] parseResourceArray(final String arrayString) {
            // [GRAIN, ORE]
            String contents = arrayString.substring(1, arrayString.length() - 1);
            String[] delineated = contents.split(", ");
            Resource[] resources = new Resource[delineated.length];
            for (int i = 0; i < delineated.length; i++) {
                resources[i] = Resource.valueOf(delineated[i]);
            }

            return resources;
        }

        @Override
        public void save(final File folder) throws IOException {
            // Create a MementoWriter for writing memento data
            MementoWriter writer = new MementoWriter(folder, TARGET_FILE_NAME);

            if (!isEmptyLoan()) {
                // Write simple fields to the file
                writer.writeField(PLAYER, player.toString());
                writer.writeField(RESOURCES_BORROWED, Arrays.toString(resourcesBorrowed));
                writer.writeField(RESOURCES_DUE, Arrays.toString(resourcesDue));
                writer.writeField(TURNS_UNTIL_DUE, Integer.toString(turnsUntilDue));
            }
        }

        @Override
        public void restore() {
            Loan.this.loanPlayer = this.player;
            Loan.this.loanResourcesBorrowed = this.resourcesBorrowed;
            Loan.this.resourcesDue = this.resourcesDue;
            Loan.this.turnsUntilDue = this.turnsUntilDue;
        }
    }

    @Override
    public Memento createMemento() {
        return new Loan.LoanMemento();
    }
}
