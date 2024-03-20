package gamedatastructures;

import java.util.Arrays;

import SavingAndLoading.Memento;
import SavingAndLoading.Restorable;
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
    public int victoryPoints;
    public boolean hasPlayedDevCard;
    public int numKnightsPlayed;

    int numSettlements;
    int numRoads;
    int numCities;
    private Resource[] tradeBoosts;
    private int numTradeBoosts;
    private boolean hasLongestRoadCard;
    private boolean hasLargestArmy;

    public Player(final int num) {
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
        Bank.getInstance().addResources(new Resource[]{
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
        Bank.getInstance().addResources(new Resource[]{
                Resource.LUMBER,
                Resource.BRICK,
        });

        this.numRoads--;

        return true;
    }

    /**
     * Gets the array of trade boosts this player has
     * Note: ANY type means that is 3:1 for any resourse type.
     * Specific boosts mean 2:1 for the indicated resourse
     *
     * @return array of trade boosts.
     */
    public Resource[] getTradeBoosts() {
        return Arrays.copyOf(tradeBoosts, numTradeBoosts);
    }

    /**
     * Method called from vertex to add the ports trade boosts
     * to this player
     *
     * @param resourse
     */
    public void addTradeBoost(final Resource resourse) {
        this.tradeBoosts[numTradeBoosts] = resourse;
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
            this.hand.addResources(resourcesGiven);
            return false;
        }

        otherPlayer.hand.addResources(resourcesGiven);
        this.hand.addResources(resourcesReceived);

        return true;
    }

    public boolean tradeWithBank(final Resource resourceGiven, final Resource resourceTaken) {
        //determine trade boost for resourceGiven
        int amountToGive = MAX_AMOUNT;
        for (int i = 0; i < tradeBoosts.length; i++) {
            if (tradeBoosts[i] == resourceGiven) {
                amountToGive = RESOURCE_PORT;
            } else if (tradeBoosts[i] == Resource.ANY) {
                amountToGive = Math.min(amountToGive, BUFF_AMOUNT);
            }
        }
        Bank bank = Bank.getInstance();
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

    public boolean upgradeSettlementToCity() {
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

        Bank bank = Bank.getInstance();
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
        Bank bank = Bank.getInstance();
        bank.addResources(new Resource[]{
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        boolean purchasedDevelopmentCard = this.hand.addDevelopmentCard(card);
        if (!purchasedDevelopmentCard) {
            this.hand.addResources(new Resource[]{
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
    public void addboughtCardsToHand() {
        hand.devCardsBoughtThisTurn.put(DevCard.KNIGHT, 0);
        hand.devCardsBoughtThisTurn.put(DevCard.ROAD, 0);
        hand.devCardsBoughtThisTurn.put(DevCard.MONOPOLY, 0);
        hand.devCardsBoughtThisTurn.put(DevCard.PLENTY, 0);
        hand.devCardsBoughtThisTurn.put(DevCard.VICTORY, 0);
    }

    /**
     * This is the new version of canPlayDevelopmentCard
     * This method will check if the hand has an available card of the given type
     * that was not bought this turn. If there is one avialable, it will then remove the
     * card from the hand. Will return false on victory point cards because these are non playerable.
     *
     * @param card
     * @return
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

    /**
     * Note this is the old method.
     * Do not use
     *
     * @param card
     * @return
     */
    public boolean canPlayDevelopmentCard(final DevCard card) {
        if (card == null) {
            throw new IllegalArgumentException("Cannot attempt to play null Development Card");
        }
        if (hasPlayedDevCard) {
            return false;
        }
        if (card == DevCard.VICTORY) {
            return false;
        }

        boolean canPlay = this.hand.removeDevelopmentCard(card);
        if (!canPlay) {
            return false;
        }

        //can play
        this.hasPlayedDevCard = true;
        if (card == DevCard.KNIGHT) {
            this.numKnightsPlayed++;
            //later: add check for largest army here?
        }
        return true;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    /**
     * This gets the number of settlment pieces left in thier possesion for use.
     * Not the number of settlments they built.
     *
     * @return
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
     * Helper method that manually places the settlment for setup.
     *
     * Note: This does not do resource checking because it is assumeed to
     * not matter during setup
     */
    public void placeSettlementSetup() {
        this.numSettlements--;
        this.victoryPoints++;
    }

    /**
     * Helper method that manually places the road for setup.
     *
     * Note: This does not do resource checking because it is assumeed to
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
     * @param points
     */
    public void setVictoryPoints(final int points) {
        this.victoryPoints = points;
    }

    /**
     * Simple setter for integration testing purposes
     * @param numRoads
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

    public class PlayerMemento implements Memento {

        // simple fields
        private final int victoryPoints;
        private final boolean hasPlayedDevCard;
        private final int numKnightsPlayed;
        private final int numSettlements;
        private final int numRoads;
        private final int numCities;
        private final Resource[] tradeBoosts;
        private final int numTradeBoosts;
        private final boolean hasLongestRoadCard;
        private final boolean hasLargestArmy;

        // sub mementos
        private final Memento handMemento;

        private PlayerMemento() {
            // simple fields
            this.victoryPoints = Player.this.victoryPoints;
            this.hasPlayedDevCard = Player.this.hasPlayedDevCard;
            this.numKnightsPlayed = Player.this.numKnightsPlayed;
            this.numSettlements = Player.this.numSettlements;
            this.numRoads = Player.this.numRoads;
            this.numCities = Player.this.numCities;
            this.tradeBoosts = Arrays.copyOf(Player.this.tradeBoosts, Player.this.tradeBoosts.length);
            this.numTradeBoosts = Player.this.numTradeBoosts;
            this.hasLongestRoadCard = Player.this.hasLongestRoadCard;
            this.hasLargestArmy = Player.this.hasLargestArmy;

            // sub mementos
            this.handMemento = Player.this.hand.createMemento();
        }

        @Override
        public void save() {

        }
    }

    @Override
    public Memento createMemento() {
        return new PlayerMemento();
    }

    @Override
    public void restore(Memento m) {

    }
}
