package domain.bank;

import data.GameLoader;
import domain.game.NotEnoughResourcesException;
import domain.player.Player;

import java.util.ArrayList;

public class Loan {
    private static final int MAX_LOAN_SIZE = 3;
    private static final int TURNS_UNTIL_DUE = 5;
    private final Resource[] resourcesBorrowed;
    private Resource[] resourcesDue;
    private int turnsUntilDue;

    public Loan(final Resource[] resourcesBorrowed) {
        this.resourcesBorrowed = resourcesBorrowed;
        this.resourcesDue = calcResourcesDue();
        this.turnsUntilDue = TURNS_UNTIL_DUE;
    }

    public static boolean loanIsValid(final Resource[] resourcesBorrowed) {
        return resourcesBorrowed.length <= MAX_LOAN_SIZE;
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

    protected static Resource[] getRemainingResourcesDue(Resource[] due, Resource[] removed) {
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
        if (!bank.removeResources(this.resourcesBorrowed)) {
            throw new NotEnoughResourcesException();
        }

        player.hand.addResources(this.resourcesBorrowed);
    }

    protected Resource[] calcResourcesDue() {
        Resource minResource = Resource.ANY;
        int minCount = 0;
        for (Resource r : Resource.values()) {
            int contains = 0;
            for (Resource borrow : this.resourcesBorrowed) {
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

        Resource[] resourcesDue = new Resource[this.resourcesBorrowed.length + 1];
        int idx = 0;
        for (Resource r : this.resourcesBorrowed) {
            resourcesDue[idx] = r;
            idx++;
        }
        resourcesDue[idx] = minResource;

        return resourcesDue;
    }
}
