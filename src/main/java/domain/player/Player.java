package domain.player;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import domain.bank.Bank;
import domain.devcarddeck.DevCard;
import domain.bank.Resource;
import data.*;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class Player implements Restorable {
    private static final int MAX_SETTLEMENTS = 5;
    private static final int MAX_ROADS = 15;
    private static final int TOTAL_PORTS = 9;
    private static final int MAX_AMOUNT = 4;
    private static final int RESOURCE_PORT = 2;
    private static final int BUFF_AMOUNT = 3;
    private static final int MAX_CITIES = 4;

    @SuppressFBWarnings("URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD") //warning is temporary
    public final int playerNum;
    public Hand hand;
    public final HarvestBooster harvestBooster;
    public final Bank bank;
    public int victoryPoints;
    public boolean hasPlayedDevCard;
    public int numKnightsPlayed;

    int numSettlements;
    int numRoads;
    int numCities;
    private final Resource[] tradeBoosts;
    private int numTradeBoosts;
    private boolean hasLongestRoadCard;
    private boolean hasLargestArmy;

    /**
     * Production constructor
     */
    public Player(final int num, final HarvestBooster booster, final Bank resourceBank) {
        this.playerNum = num;
        this.hand = new Hand();
        this.numSettlements = MAX_SETTLEMENTS;
        this.numRoads = MAX_ROADS;
        this.numCities = MAX_CITIES;
        this.victoryPoints = 0;
        this.tradeBoosts = new Resource[TOTAL_PORTS];
        this.numTradeBoosts = 0;
        this.hasPlayedDevCard = false;
        this.numKnightsPlayed = 0;
        this.harvestBooster = booster;
        this.bank = resourceBank;
    }

    // Test Constructors provide simpler syntax when certain fields are unnecessary
    public Player(final int num) {
        this(num, null, null);
    }
    public Player(final int num, final HarvestBooster booster) {
        this(num, booster, null);
    }
    public Player(final int num, final Bank resourceBank) {
        this(num, null, resourceBank);
    }
    public Player() {
        this(1);
    }

    public boolean purchaseSettlement() {
        if (numSettlements == 0) {
            return false;
        }
        //check if the player can afford to purchase the settlement
        if (!this.hand.removeResources(new Resource[]{
                Resource.LUMBER,
                Resource.BRICK,
                Resource.WOOL,
                Resource.GRAIN
        })) {
            return false;
        }
        //return the cards to the bank
        this.bank.addResources(new Resource[]{
           Resource.LUMBER,
           Resource.BRICK,
           Resource.WOOL,
           Resource.GRAIN
        });

        this.numSettlements--;

        this.victoryPoints++;

        return true;
    }

    public boolean purchaseRoad() {
        if (this.numRoads == 0) {
            return false;
        }
        if (!this.hand.removeResources(new Resource[] {
                Resource.LUMBER,
                Resource.BRICK,
        })) {
            return false;
        }
        //return the cards to the bank
        this.bank.addResources(new Resource[]{
                Resource.LUMBER,
                Resource.BRICK,
        });

        this.numRoads--;

        return true;
    }

    public boolean addResources(final Resource[] resourcesToAdd) {
        boolean ret = this.hand.addResources(resourcesToAdd);
        if (this.bank != null) {
            this.bank.payLoanIfDue(this);
        }
        return ret;
    }

    /**
     * Gets the array of trade boosts this player has
     * Note: ANY type means that is 3:1 for any resource type.
     * Specific boosts mean 2:1 for the indicated resource
     *
     * @return array of trade boosts.
     */
    public Resource[] getTradeBoosts() {
        return Arrays.copyOf(tradeBoosts, numTradeBoosts);
    }

    /**
     * Method called from vertex to add the ports trade boosts
     * to this player
     */
    public void addTradeBoost(final Resource resource) {
        this.tradeBoosts[numTradeBoosts] = resource;
        this.numTradeBoosts++;
    }

    public boolean tradeResources(final Player otherPlayer, final Resource[] resourcesGiven, final Resource[] resourcesReceived) {
        if (otherPlayer == null) {
            throw new IllegalArgumentException("Other player cannot be null");
        }
        if (resourcesReceived.length == 0) {
            throw new IllegalArgumentException("Traded resources must not be empty");
        }
        if (resourcesGiven.length == 0) {
            throw new IllegalArgumentException("Traded resources must not be empty");
        }

        boolean p1CanTrade = this.hand.removeResources(resourcesGiven);
        if (!p1CanTrade) {
            return false;
        }
        boolean p2CanTrade = otherPlayer.hand.removeResources(resourcesReceived);
        if (!p2CanTrade) {
            this.addResources(resourcesGiven);
            return false;
        }

        otherPlayer.addResources(resourcesGiven);
        this.addResources(resourcesReceived);

        return true;
    }

    public boolean tradeWithBank(final Resource resourceGiven, final Resource resourceTaken) {
        //determine trade boost for resourceGiven
        int amountToGive = MAX_AMOUNT;
        for (Resource tradeBoost : tradeBoosts) {
            if (tradeBoost == resourceGiven) {
                amountToGive = RESOURCE_PORT;
            } else if (tradeBoost == Resource.ANY) {
                amountToGive = Math.min(amountToGive, BUFF_AMOUNT);
            }
        }

        boolean playerCanTrade = this.hand.removeResource(resourceGiven, amountToGive);
        if (!playerCanTrade) {
            return false;
        }
        boolean bankCanTrade = bank.removeResource(resourceTaken, 1);
        if (!bankCanTrade) {
            this.hand.addResource(resourceGiven, amountToGive);
            return false;
        }

        this.hand.addResource(resourceTaken, 1);
        bank.addResource(resourceGiven, amountToGive);

        return true;
    }

    protected int getResourceCount(final Resource resource) {
        return this.hand.getResourceCount(resource);
    }

    public boolean canUpgradeSettlementToCity() {
        if (numCities == 0) {
            return false;
        }

        boolean playerCanPurchase = this.hand.removeResources(new Resource[]{
                Resource.ORE,
                Resource.ORE,
                Resource.ORE,
                Resource.GRAIN,
                Resource.GRAIN
        });
        if (!playerCanPurchase) {
            return false;
        }

        bank.addResources(new Resource[]{
                Resource.ORE,
                Resource.ORE,
                Resource.ORE,
                Resource.GRAIN,
                Resource.GRAIN
        });

        this.numSettlements++;
        this.numCities--;
        this.victoryPoints++;

        return true;
    }

    public boolean purchaseDevCard(final DevCard card) {
        if (card == null) {
            throw new IllegalArgumentException("Cannot purchase null Development Card");
        }
        if (!this.hand.removeResources(new Resource[]{
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        })) {
            return false;
        }

        bank.addResources(new Resource[]{
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        boolean purchasedDevelopmentCard = this.hand.addDevelopmentCard(card);
        if (!purchasedDevelopmentCard) {
            this.addResources(new Resource[]{
                    Resource.WOOL, Resource.GRAIN, Resource.ORE
            });
            bank.removeResources(new Resource[]{
                    Resource.WOOL, Resource.GRAIN, Resource.ORE
            });
            return false;
        }
        if (card == DevCard.VICTORY) {
            this.victoryPoints++;
            //later: add end game check here (?)
        }

        // increment the number of dev cards bought this turn
        this.hand.devCardsBoughtThisTurn.put(card, this.hand.devCardsBoughtThisTurn.get(card) + 1);
        return true;
    }

    /**
     * Method that makes it so dev cards bought during the turn are
     * now playable. This is called during end turn
     */
    public void addBoughtCardsToHand() {
        hand.devCardsBoughtThisTurn.put(DevCard.KNIGHT, 0);
        hand.devCardsBoughtThisTurn.put(DevCard.ROAD, 0);
        hand.devCardsBoughtThisTurn.put(DevCard.MONOPOLY, 0);
        hand.devCardsBoughtThisTurn.put(DevCard.PLENTY, 0);
        hand.devCardsBoughtThisTurn.put(DevCard.VICTORY, 0);
    }

    /**
     * This is the new version of canPlayDevelopmentCard
     * This method will check if the hand has an available card of the given type
     * that was not bought this turn. If there is one available, it will then remove the
     * card from the hand. Will return false on victory point cards because these are non-playable.
     */
    public boolean useDevCard(final DevCard card) {
        if (card == null) {
            throw new IllegalArgumentException("Cannot attempt to play null Development Card");
        }

        if (card == DevCard.VICTORY) {
            return false;
        }

        if (hand.devCards.get(card) - hand.devCardsBoughtThisTurn.get(card) <= 0) {
            return false;
        }

        hand.removeDevelopmentCard(card);

        return true;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    /**
     * This gets the number of settlement pieces left in their possession for use.
     * Not the number of settlements they built.
     */
    public int getNumSettlements() {
        return numSettlements;
    }

    public int getNumRoads() {
        return numRoads;
    }

    public int getNumCities() {
        return numCities;
    }

    /**
     * Helper method that manually places the settlement for setup.
     * <p>
     * Note: This does not do resource checking because it is assumed to
     * not matter during setup
     */
    public void placeSettlementSetup() {
        this.numSettlements--;
        this.victoryPoints++;
    }

    /**
     * Helper method that manually places the road for setup.
     * <p>
     * Note: This does not do resource checking because it is assumed to
     * not matter during setup
     */
    public void placeRoadSetup() {
        this.numRoads--;
    }

    // ------------------------------------------------------------------
    //
    // Longest Road card related methods. Don't touch
    //
    // ------------------------------------------------------------------

    /**
     * Method that gives this player the longest Road card by incrementing this
     * players victory points and setting the internal flag to true
     */
    public void giveLongestRoadCard() {
        this.victoryPoints += 2;
        this.hasLongestRoadCard = true;
    }

    public boolean hasLongestRoad() {
        return this.hasLongestRoadCard;
    }

    public void removeLongestRoadCard() {
        this.victoryPoints -= 2;
        this.hasLongestRoadCard = false;
    }

    public int getVictoryPoints() {
        return this.victoryPoints;
    }

    /**
     * Simple setter used during testing
     * Warning: do not use outside of testing to establish state of world
     */
    public void setVictoryPoints(final int points) {
        this.victoryPoints = points;
    }

    /**
     * Simple setter for integration testing purposes
     */
    public void setNumRoads(final int num) {
        this.numRoads = num;
    }

    // ------------------------------------------------------------------
    //
    // Largest Army card related methods. Don't touch
    //
    // ------------------------------------------------------------------

    /**
     * Simply setter type method that increments the number of knights the player has
     * so that we can see who has the largest army later
     */
    public void incrementNumKnights() {
        this.numKnightsPlayed++;
    }

    public Boolean hasLargestArmy() {
        return hasLargestArmy;
    }

    public int getNumKnightsPlayed() {
        return numKnightsPlayed;
    }

    public void giveLargestArmyCard() {
        this.victoryPoints += 2;
        this.hasLargestArmy = true;
    }

    public void removeLargestArmyCard() {
        this.victoryPoints -= 2;
        this.hasLargestArmy = false;
    }

    // -----------------------------------
    //
    // Restorable implementation
    //
    // -----------------------------------

    @Override
    public String toString() {
        return "Player " + this.playerNum;
    }

    public class PlayerMemento implements Memento {

        // simple fields
        private final int victoryPoints;
        private final boolean hasPlayedDevCard;
        private final int numKnightsPlayed;
        private final int numSettlements;
        private final int numRoads;
        private final int numCities;
        private final int numTradeBoosts;
        private final boolean hasLongestRoadCard;
        private final boolean hasLargestArmy;
        private final Resource[] tradeBoosts;

        // sub mementos
        private final Memento handMemento;
        private final Memento harvestBoosterMemento;

        // Storage Constants
        private static final String TARGET_FILE_NAME = "player.txt";
        private static final String HAND_SUBFOLDER_NAME = "Hand";
        private static final String HARVEST_BOOSTER_SUBFOLDER_NAME = "HarvestBooster";

        // Field Keys
        private static final String VICTORY_POINTS = "VictoryPoints";
        private static final String HAS_PLAYED_DEV_CARD = "HasPlayedDevCard";
        private static final String NUM_KNIGHTS_PLAYED = "NumKnightsPlayed";
        private static final String NUM_SETTLEMENTS = "NumSettlements";
        private static final String NUM_ROADS = "NumRoads";
        private static final String NUM_CITIES = "NumCities";
        private static final String NUM_TRADE_BOOSTS = "NumTradeBoosts";
        private static final String HAS_LONGEST_ROAD_CARD = "HasLongestRoadCard";
        private static final String HAS_LARGEST_ARMY = "HasLargestArmy";
        private static final String TRADE_BOOSTS = "TradeBoosts";

        private PlayerMemento() {
            // simple fields
            this.victoryPoints = Player.this.victoryPoints;
            this.hasPlayedDevCard = Player.this.hasPlayedDevCard;
            this.numKnightsPlayed = Player.this.numKnightsPlayed;
            this.numSettlements = Player.this.numSettlements;
            this.numRoads = Player.this.numRoads;
            this.numCities = Player.this.numCities;
            this.numTradeBoosts = Player.this.numTradeBoosts;
            this.hasLongestRoadCard = Player.this.hasLongestRoadCard;
            this.hasLargestArmy = Player.this.hasLargestArmy;
            this.tradeBoosts = Arrays.copyOf(Player.this.tradeBoosts, Player.this.tradeBoosts.length);

            // sub mementos
            this.handMemento = Player.this.hand.createMemento();
            this.harvestBoosterMemento = Player.this.harvestBooster.createMemento();
        }

        @SuppressFBWarnings("EI_EXPOSE_REP2")
        public PlayerMemento(final File folder) {
            // Create a MementoReader for reading memento data
            MementoReader reader = new MementoReader(folder, TARGET_FILE_NAME);

            // Read simple fields from the file
            this.victoryPoints = Integer.parseInt(reader.readField(VICTORY_POINTS));
            this.hasPlayedDevCard = Boolean.parseBoolean(reader.readField(HAS_PLAYED_DEV_CARD));
            this.numKnightsPlayed = Integer.parseInt(reader.readField(NUM_KNIGHTS_PLAYED));
            this.numSettlements = Integer.parseInt(reader.readField(NUM_SETTLEMENTS));
            this.numRoads = Integer.parseInt(reader.readField(NUM_ROADS));
            this.numCities = Integer.parseInt(reader.readField(NUM_CITIES));
            this.numTradeBoosts = Integer.parseInt(reader.readField(NUM_TRADE_BOOSTS));
            this.hasLongestRoadCard = Boolean.parseBoolean(reader.readField(HAS_LONGEST_ROAD_CARD));
            this.hasLargestArmy = Boolean.parseBoolean(reader.readField(HAS_LARGEST_ARMY));

            // Read trade boosts from the file
            String tradeBoostsString = reader.readField(TRADE_BOOSTS);
            this.tradeBoosts = parseTradeBoosts(tradeBoostsString);

            // Restore hand state using its memento
            File handFolder = reader.getSubFolder(HAND_SUBFOLDER_NAME);
            this.handMemento = Player.this.hand.new HandMemento(handFolder);

            // Restore harvest booster state using its memento
            File harvestBoosterFolder = reader.getSubFolder(HARVEST_BOOSTER_SUBFOLDER_NAME);
            this.harvestBoosterMemento = Player.this.harvestBooster.new HarvestBoosterMemento(harvestBoosterFolder);
        }

        private Resource[] parseTradeBoosts(final String tradeBoostsString) {
            String[] boostTokens = tradeBoostsString.substring(1, tradeBoostsString.length() - 1).split(", ");

            Resource[] boosts = new Resource[boostTokens.length];
            for (int i = 0; i < boostTokens.length; i++) {
                String token = boostTokens[i].trim();
                if (token.equals("null")) {
                    boosts[i] = null;
                } else {
                    boosts[i] = Resource.valueOf(token);
                }
            }
            return boosts;
        }

        public void save(final File folder) throws IOException {
            // Create a MementoWriter for writing memento data
            MementoWriter writer = new MementoWriter(folder, TARGET_FILE_NAME);

            // Write simple fields to the file
            writer.writeField(VICTORY_POINTS, Integer.toString(victoryPoints));
            writer.writeField(HAS_PLAYED_DEV_CARD, Boolean.toString(hasPlayedDevCard));
            writer.writeField(NUM_KNIGHTS_PLAYED, Integer.toString(numKnightsPlayed));
            writer.writeField(NUM_SETTLEMENTS, Integer.toString(numSettlements));
            writer.writeField(NUM_ROADS, Integer.toString(numRoads));
            writer.writeField(NUM_CITIES, Integer.toString(numCities));
            writer.writeField(NUM_TRADE_BOOSTS, Integer.toString(numTradeBoosts));
            writer.writeField(HAS_LONGEST_ROAD_CARD, Boolean.toString(hasLongestRoadCard));
            writer.writeField(HAS_LARGEST_ARMY, Boolean.toString(hasLargestArmy));

            // Write trade boosts to the file
            writer.writeField(TRADE_BOOSTS, Arrays.toString(tradeBoosts));

            // Delegate saving of the hand to its own memento
            File handFolder = writer.getSubFolder(HAND_SUBFOLDER_NAME);
            handMemento.save(handFolder);

            // Delegate saving of the hand to its own memento
            File harvestBoosterFolder = writer.getSubFolder(HARVEST_BOOSTER_SUBFOLDER_NAME);
            harvestBoosterMemento.save(harvestBoosterFolder);
        }

        public void restore() {
            // Restore simple fields
            Player.this.victoryPoints = this.victoryPoints;
            Player.this.hasPlayedDevCard = this.hasPlayedDevCard;
            Player.this.numKnightsPlayed = this.numKnightsPlayed;
            Player.this.numSettlements = this.numSettlements;
            Player.this.numRoads = this.numRoads;
            Player.this.numCities = this.numCities;
            Player.this.numTradeBoosts = this.numTradeBoosts;
            Player.this.hasLongestRoadCard = this.hasLongestRoadCard;
            Player.this.hasLargestArmy = this.hasLargestArmy;

            // Restore trade boosts
            System.arraycopy(this.tradeBoosts, 0, Player.this.tradeBoosts, 0, this.tradeBoosts.length);

            // Restore hand state using its memento
            handMemento.restore();

            // Restore harvestBooster using its memento
            harvestBoosterMemento.restore();
        }
    }

    @Override
    public Memento createMemento() {
        return new PlayerMemento();
    }
}
