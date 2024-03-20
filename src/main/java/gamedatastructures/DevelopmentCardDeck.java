package gamedatastructures;

import SavingAndLoading.Memento;
import SavingAndLoading.Restorable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DevelopmentCardDeck implements Restorable {
    private DevCard[] defaultDeck = {
        DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT,
        DevCard.VICTORY, DevCard.VICTORY, DevCard.VICTORY, DevCard.VICTORY, DevCard.VICTORY,
        DevCard.ROAD, DevCard.ROAD,
        DevCard.PLENTY, DevCard.PLENTY,
        DevCard.MONOPOLY, DevCard.MONOPOLY};
    private ArrayList<DevCard> deck = new ArrayList<DevCard>();

    /**
     * Creates a new deck object
     */
    public DevelopmentCardDeck() {
        Collections.shuffle(Arrays.asList(this.defaultDeck));
        for (DevCard card : defaultDeck) {
            this.deck.add(card);
        }
    }

    /**
     * Draws a card from the deck
     * @return DevCard
     * @throws EmptyDevCardDeckException
     */
    public DevCard draw() throws EmptyDevCardDeckException {
        if (this.deck.size() > 0) {
            return this.deck.remove(0);
        }
        throw new EmptyDevCardDeckException();
    }
    /**
     * Returns the deck
     * @return ArrayList<DevCard>
     */
    public ArrayList<DevCard> getDeck() {
        ArrayList<DevCard> clone = new ArrayList<DevCard>();
        clone.addAll(deck);
        return clone;
    }
    /**
     * Returns a card to the deck if not used
     */
    public void returnToDeck(final DevCard card) {
        this.deck.add(card);
    }

    //testing method
    //emptys out the deck
    void empty() {
        this.deck =  new ArrayList<DevCard>();
    }

    // -----------------------------------
    //
    // Restorable implementation
    //
    // -----------------------------------

    public class DevCardDeckMemento implements Memento {
        private final ArrayList<DevCard> deck;

        private DevCardDeckMemento() {
            this.deck = new ArrayList<>();
            this.deck.addAll(DevelopmentCardDeck.this.deck);
        }

        @Override
        public void save() {

        }
    }

    @Override
    public Memento createMemento() {
        return new DevCardDeckMemento();
    }

    @Override
    public void restore(Memento m) {

    }
}
