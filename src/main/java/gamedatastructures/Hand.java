package gamedatastructures;

import SavingAndLoading.Memento;
import SavingAndLoading.MementoReader;
import SavingAndLoading.MementoWriter;
import SavingAndLoading.Restorable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Hand implements Restorable {
    private HashMap<Resource, Integer> hand = new HashMap<>();
    private static final int MAX_AMOUNT = 19;
    private static final int MAX_KNIGHTS = 14;
    private static final int MAX_YOP = 2;
    private static final int MAX_MONOPOLY = 2;
    private static final int MAX_ROAD = 2;
    private static final int MAX_VP = 5;

    public HashMap<DevCard, Integer> devCards = new HashMap<>();
    public HashMap<DevCard, Integer> devCardsBoughtThisTurn = new HashMap<>();

    public Hand() {
        hand.put(Resource.LUMBER, 0);
        hand.put(Resource.BRICK, 0);
        hand.put(Resource.WOOL, 0);
        hand.put(Resource.GRAIN, 0);
        hand.put(Resource.ORE, 0);

        devCards.put(DevCard.KNIGHT, 0);
        devCards.put(DevCard.ROAD, 0);
        devCards.put(DevCard.MONOPOLY, 0);
        devCards.put(DevCard.PLENTY, 0);
        devCards.put(DevCard.VICTORY, 0);

        devCardsBoughtThisTurn.put(DevCard.KNIGHT, 0);
        devCardsBoughtThisTurn.put(DevCard.ROAD, 0);
        devCardsBoughtThisTurn.put(DevCard.MONOPOLY, 0);
        devCardsBoughtThisTurn.put(DevCard.PLENTY, 0);
        devCardsBoughtThisTurn.put(DevCard.VICTORY, 0);
    }

    public boolean addResource(final Resource type, final int amount) {
        if (amount <= 0 || amount > MAX_AMOUNT) {
            throw new IllegalArgumentException("Resource amount must be between 0 and 19");
        }
        if (hand.get(type) + amount > MAX_AMOUNT) {
            return false;
        }
        hand.put(type, hand.get(type) + amount);
        return true;
    }

    public boolean removeResource(final Resource type, final int amount) {
        if (amount <= 0 || amount > MAX_AMOUNT) {
            throw new IllegalArgumentException("Resource amount must be between 0 and 19");
        }
        if (hand.get(type) < amount) {
            return false;
        }
        hand.put(type, hand.get(type) - amount);
        return true;
    }

    public int getResourceCardCount() {
        return hand.get(Resource.LUMBER)
                + hand.get(Resource.BRICK)
                + hand.get(Resource.WOOL)
                + hand.get(Resource.GRAIN)
                + hand.get(Resource.ORE);
    }

    public boolean addResources(final Resource[] resources) {
        for (int i = 0; i < resources.length; i++) {
            if (this.hand.get(resources[i]) + 1 > MAX_AMOUNT) {
                //remove all resources added previously
                for (int remove = 0; remove < i; remove++) {
                    this.removeResource(resources[remove], 1);
                }
                return false;
            }
            this.hand.put(resources[i], this.hand.get(resources[i]) + 1);
        }
        return true;
    }

    public boolean removeResources(final Resource[] resources) {
        for (int i = 0; i < resources.length; i++) {
            if (this.hand.get(resources[i]) == 0) {
                for (int add = 0; add < i; add++) {
                    this.addResource(resources[add], 1);
                }
                return false;
            }
            this.hand.put(resources[i], this.hand.get(resources[i]) - 1);
        }
        return true;
    }

    public int getResourceCardAmount(final Resource resource) {
        return this.hand.get(resource);
    }

    public boolean addDevelopmentCard(final DevCard card) {
        if (card == null) {
            throw new IllegalArgumentException("Card added to hand cannot be null");
        }
        int amountOfCard = devCards.get(card);

        if (card == DevCard.KNIGHT && amountOfCard == MAX_KNIGHTS) {
            return false;
        } else if (card == DevCard.PLENTY && amountOfCard == MAX_YOP) {
            return false;
        } else if (card == DevCard.MONOPOLY && amountOfCard == MAX_MONOPOLY) {
            return false;
        } else if (card == DevCard.ROAD && amountOfCard == MAX_ROAD) {
            return false;
        } else if (card == DevCard.VICTORY && amountOfCard == MAX_VP) {
            return false;
        }

        this.devCards.put(card, this.devCards.get(card) + 1);
        return true;
    }

    public boolean removeDevelopmentCard(final DevCard card) {
        if (card == null) {
            throw new IllegalArgumentException("Cannot remove null Dev Card");
        }
        if (card == DevCard.VICTORY) {
            return false;
        }
        if (this.devCards.get(card) == 0) {
            return false;
        }
        this.devCards.put(card, this.devCards.get(card) - 1);
        return true;
    }
    //helper method for robbing player
    public Resource[] getResourceTypes() {
        ArrayList<Resource> resources = new ArrayList<Resource>();
        for (java.util.Map.Entry<Resource, Integer> entry : hand.entrySet()) {
            if (entry.getValue() > 0) {
                resources.add(entry.getKey());
            }
        }
        Resource[] rArr = resources.toArray(new Resource[resources.size()]);
        return rArr;
    }

    // -----------------------------------
    //
    // Restorable implementation
    //
    // -----------------------------------

    public class HandMemento implements Memento {

        private final HashMap<Resource, Integer> hand;
        private final HashMap<DevCard, Integer> devCards;
        private final HashMap<DevCard, Integer> devCardsBoughtThisTurn;

        // Storage Constants
        private static final String HAND_FILE_NAME = "Hand.txt";
        private static final String DEVCARDS_FILE_NAME = "DevCards.txt";
        private static final String DEVCARDS_BOUGHT_FILE_NAME = "DevCardsBoughtThisTurn.txt";

        private HandMemento() {
            this.hand = new HashMap<>();
            this.hand.putAll(Hand.this.hand);

            this.devCards = new HashMap<>();
            this.devCards.putAll(Hand.this.devCards);

            this.devCardsBoughtThisTurn = new HashMap<>();
            this.devCardsBoughtThisTurn.putAll(Hand.this.devCardsBoughtThisTurn);
        }

        public HandMemento(File folder) {
            // Initialize the HashMaps
            this.hand = new HashMap<>();
            this.devCards = new HashMap<>();
            this.devCardsBoughtThisTurn = new HashMap<>();

            // Read data from separate files and populate the HashMaps
            readResourceMap(folder, HAND_FILE_NAME, hand);
            readDevCardMap(folder, DEVCARDS_FILE_NAME, devCards);
            readDevCardMap(folder, DEVCARDS_BOUGHT_FILE_NAME, devCardsBoughtThisTurn);
        }

        private void readResourceMap(File folder, String fileName, Map<Resource, Integer> map) {
            MementoReader reader = new MementoReader(folder, fileName);

            for (Map.Entry<String, String> entry : reader.readAllFields().entrySet()) {
                map.put(Resource.valueOf(entry.getKey()),
                        Integer.parseInt(entry.getValue()));
            }
        }

        private void readDevCardMap(File folder, String fileName, Map<DevCard, Integer> map) {
            MementoReader reader = new MementoReader(folder, fileName);

            for (Map.Entry<String, String> entry : reader.readAllFields().entrySet()) {
                map.put(DevCard.valueOf(entry.getKey()),
                        Integer.parseInt(entry.getValue()));
            }
        }

        @Override
        public void save(File folder) {
            // Write the state of the class's attributes to separate files
            writeHashMap(folder, HAND_FILE_NAME, hand);
            writeHashMap(folder, DEVCARDS_FILE_NAME, devCards);
            writeHashMap(folder, DEVCARDS_BOUGHT_FILE_NAME, devCardsBoughtThisTurn);
        }

        // Helper method to write a HashMap to a separate file
        private void writeHashMap(File folder, String fileName, HashMap<?, Integer> hashMap) {
            // Create a MementoWriter for the current map
            MementoWriter writer = new MementoWriter(folder, fileName);

            // Write each entry of the map to the file
            for (Map.Entry<?, Integer> entry : hashMap.entrySet()) {
                writer.writeField(entry.getKey().toString(), entry.getValue().toString());
            }
        }

        @Override
        public void restore() {
            // Restore the state of the hand
            Hand.this.hand.clear();
            Hand.this.hand.putAll(hand);

            // Restore the state of the devCards
            Hand.this.devCards.clear();
            Hand.this.devCards.putAll(devCards);

            // Restore the state of the devCardsBoughtThisTurn
            Hand.this.devCardsBoughtThisTurn.clear();
            Hand.this.devCardsBoughtThisTurn.putAll(devCardsBoughtThisTurn);
        }
    }

    @Override
    public Memento createMemento() {
        return new HandMemento();
    }
}
