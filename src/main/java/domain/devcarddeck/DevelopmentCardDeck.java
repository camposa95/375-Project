package domain.devcarddeck;

import data.*;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DevelopmentCardDeck implements Restorable {
    private static final DevCard[] DEFAULT_DECK = {
        DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT, DevCard.KNIGHT,
        DevCard.VICTORY, DevCard.VICTORY, DevCard.VICTORY, DevCard.VICTORY, DevCard.VICTORY,
        DevCard.ROAD, DevCard.ROAD,
        DevCard.PLENTY, DevCard.PLENTY,
        DevCard.MONOPOLY, DevCard.MONOPOLY};
    private final ArrayList<DevCard> deck = new ArrayList<>();

    /**
     * Creates a new deck object
     */
    public DevelopmentCardDeck() {
        Collections.shuffle(Arrays.asList(DEFAULT_DECK));
        this.deck.addAll(Arrays.asList(DEFAULT_DECK));
    }

    /**
     * Draws a card from the deck
     * @return DevCard
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
        return new ArrayList<>(deck);
    }

    /**
     * Returns a card to the deck if not used
     */
    public void returnToDeck(final DevCard card) {
        this.deck.add(card);
    }

    // -----------------------------------
    //
    // Restorable implementation
    //
    // -----------------------------------

    public class DevCardDeckMemento implements Memento {
        private final ArrayList<DevCard> deck;

        // Storage Constants
        private static final String TARGET_FILE_NAME = "deck.txt";

        // Field Keys
        private static final String DEV_CARDS = "DevCards";

        private DevCardDeckMemento() {
            this.deck = new ArrayList<>();
            this.deck.addAll(DevelopmentCardDeck.this.deck);
        }

        @SuppressFBWarnings("EI_EXPOSE_REP2")
        public DevCardDeckMemento(final File folder) {
            // Create a MementoReader for reading memento data
            MementoReader reader = new MementoReader(folder, TARGET_FILE_NAME);

            // Read simple fields from the file
            this.deck = parseDevCards(reader.readField(DEV_CARDS));
        }

        private ArrayList<DevCard> parseDevCards(final String devCardsString) {
            String[] devCardArray = devCardsString.substring(1, devCardsString.length() - 1).split(", ");

            ArrayList<DevCard> devCards = new ArrayList<>();
            for (String devCardStr : devCardArray) {
                devCards.add(DevCard.valueOf(devCardStr.trim()));
            }
            return devCards;
        }

        public void save(final File folder) throws IOException {
            // Create a MementoWriter for writing memento data
            MementoWriter writer = new MementoWriter(folder, TARGET_FILE_NAME);

            // Write each DevCard in the deck to the file
            writer.writeField(DEV_CARDS, Arrays.toString(deck.toArray()));
        }

        public void restore() {
            // Deserialize the array of DevCards and replace the deck in DevelopmentCardDeck
            DevelopmentCardDeck.this.deck.clear();
            DevelopmentCardDeck.this.deck.addAll(deck);
        }
    }

    @Override
    public Memento createMemento() {
        return new DevCardDeckMemento();
    }
}
