package gamedatastructures;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void testPurchaseSettlement_emptyHand_returnFalse() {
        Bank.getInstance().resetBank();
        Player player = new Player(1);
        boolean success = player.purchaseSettlement();

        int expectedVictoryPoints = 0;

        assertFalse(success);
        assertEquals(expectedVictoryPoints, player.victoryPoints);
    }

    @Test
    public void testPurchaseSettlement_justEnoughResources_returnTrue() {
        Bank.getInstance().resetBank();
        Player player = new Player(1);
        Hand mockedHand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();

        // Move resources into mocked hand
        Bank.getInstance()
                .removeResources(new Resource[] { Resource.LUMBER, Resource.BRICK, Resource.WOOL, Resource.GRAIN });
        mockedHand.addResources(new Resource[] { Resource.LUMBER, Resource.BRICK, Resource.WOOL, Resource.GRAIN });

        player.hand = mockedHand;

        EasyMock.replay(mockedHand);

        boolean success = player.purchaseSettlement();
        int expectedHandSize = 0;
        int expectedVictoryPoints = 1;
        int actualHandSize = mockedHand.getResourceCardCount();

        assertTrue(success);
        assertEquals(expectedHandSize, actualHandSize);
        assertEquals(expectedVictoryPoints, player.victoryPoints);
        EasyMock.verify(mockedHand);
    }

    @Test
    public void testPurchaseSettlement_missingOneResource_returnFalse() {
        Bank.getInstance().resetBank();
        Player player = new Player(1);
        Hand mockedHand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();

        // Move resources into mocked hand
        Bank.getInstance().removeResources(new Resource[] { Resource.LUMBER, Resource.BRICK, Resource.WOOL });
        mockedHand.addResources(new Resource[] { Resource.LUMBER, Resource.BRICK, Resource.WOOL });
        player.hand = mockedHand;
        EasyMock.replay(mockedHand);

        boolean success = player.purchaseSettlement();
        int expectedHandSize = 3;
        int expectedVictoryPoints = 0;
        int actualHandSize = mockedHand.getResourceCardCount();

        assertFalse(success);
        assertEquals(expectedHandSize, actualHandSize);
        assertEquals(expectedVictoryPoints, player.victoryPoints);
        EasyMock.verify(mockedHand);
    }

    @Test
    public void testPurchaseSettlement_fullHand_returnTrue() {
        Bank.getInstance().resetBank();
        Player player = new Player(1);
        Hand mockedHand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();

        // remove all from bank
        Bank bank = Bank.getInstance();
        bank.removeResource(Resource.LUMBER, 19);
        bank.removeResource(Resource.BRICK, 19);
        bank.removeResource(Resource.WOOL, 19);
        bank.removeResource(Resource.GRAIN, 19);
        bank.removeResource(Resource.ORE, 19);
        // add all to hand
        mockedHand.addResource(Resource.LUMBER, 19);
        mockedHand.addResource(Resource.BRICK, 19);
        mockedHand.addResource(Resource.WOOL, 19);
        mockedHand.addResource(Resource.GRAIN, 19);
        mockedHand.addResource(Resource.ORE, 19);
        player.hand = mockedHand;
        EasyMock.replay(mockedHand);

        boolean success = player.purchaseSettlement();
        int expectedHandSize = 91;
        int expectedVictoryPoints = 1;
        int actualHandSize = mockedHand.getResourceCardCount();

        assertTrue(success);
        assertEquals(expectedHandSize, actualHandSize);
        assertEquals(expectedVictoryPoints, player.victoryPoints);
        EasyMock.verify(mockedHand);
    }

    @Test
    public void testPurchaseSettlement_notEnoughSettlements_returnFalse() {
        Bank.getInstance().resetBank();
        Player player = new Player(1);
        Hand mockedHand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();

        // Move resources into mocked hand
        Bank.getInstance()
                .removeResources(new Resource[] { Resource.LUMBER, Resource.BRICK, Resource.WOOL, Resource.GRAIN });
        mockedHand.addResources(new Resource[] { Resource.LUMBER, Resource.BRICK, Resource.WOOL, Resource.GRAIN });

        player.hand = mockedHand;
        player.numSettlements = 0;

        EasyMock.replay(mockedHand);

        boolean success = player.purchaseSettlement();
        int expectedHandSize = 4;
        int expectedVictoryPoints = 0;
        int actualHandSize = mockedHand.getResourceCardCount();

        assertFalse(success);
        assertEquals(expectedHandSize, actualHandSize);
        assertEquals(expectedVictoryPoints, player.victoryPoints);
        EasyMock.verify(mockedHand);
    }

    @Test
    public void testPurchaseRoad_emptyHand_returnFalse() {
        Bank.getInstance().resetBank();
        Player player = new Player(1);
        boolean success = player.purchaseRoad();

        assertFalse(success);
    }

    @Test
    public void testPurchaseRoad_JustEnoughResources_returnTrue() {
        Bank.getInstance().resetBank();
        Player player = new Player(1);
        Hand mockedHand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();

        // Move resources into mocked hand
        Bank.getInstance().removeResources(new Resource[] { Resource.LUMBER, Resource.BRICK });
        mockedHand.addResources(new Resource[] { Resource.LUMBER, Resource.BRICK });

        player.hand = mockedHand;

        EasyMock.replay(mockedHand);

        boolean success = player.purchaseRoad();
        int expectedHandSize = 0;
        int actualHandSize = mockedHand.getResourceCardCount();

        assertTrue(success);
        assertEquals(expectedHandSize, actualHandSize);
        EasyMock.verify(mockedHand);
    }

    @Test
    public void testPurchaseRoad_OnlyOneLumber_returnFalse() {
        Bank.getInstance().resetBank();
        Player player = new Player(1);
        Hand mockedHand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();

        // Move resources into mocked hand
        Bank.getInstance().removeResources(new Resource[] { Resource.LUMBER });
        mockedHand.addResources(new Resource[] { Resource.LUMBER });
        player.hand = mockedHand;
        EasyMock.replay(mockedHand);

        boolean success = player.purchaseRoad();
        int expectedHandSize = 1;
        int actualHandSize = mockedHand.getResourceCardCount();

        assertFalse(success);
        assertEquals(expectedHandSize, actualHandSize);
        EasyMock.verify(mockedHand);
    }

    @Test
    public void testPurchaseRoad_notEnoughRoads_returnFalse() {
        Bank.getInstance().resetBank();
        Player player = new Player(1);
        Hand mockedHand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();

        // Move resources into mocked hand
        Bank.getInstance().removeResources(new Resource[] { Resource.LUMBER, Resource.BRICK });
        mockedHand.addResources(new Resource[] { Resource.LUMBER, Resource.BRICK });

        player.hand = mockedHand;
        player.numRoads = 0;

        EasyMock.replay(mockedHand);

        boolean success = player.purchaseRoad();
        int expectedHandSize = 2;
        int actualHandSize = mockedHand.getResourceCardCount();

        assertFalse(success);
        assertEquals(expectedHandSize, actualHandSize);
        EasyMock.verify(mockedHand);
    }

    @Test
    public void testTradeResources_withNullPlayer_throwsNullPointerException() {
        Player player1 = new Player(1);
        Player player2 = null;

        String expectedMessage = "Other player cannot be null";

        Exception thrown = assertThrows(IllegalArgumentException.class, () -> {
            player1.tradeResources(player2, new Resource[] {}, new Resource[] {});
        });
        assertEquals(0, player1.hand.getResourceCardCount());
        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    public void testTradeResources_withPlayer_andEmptyCollections_throwsIllegalArgumentException() {
        Player player1 = new Player(1);
        Player player2 = new Player(2);

        Resource[] player1Hand = new Resource[] {
                Resource.LUMBER
        };

        player1.hand.addResources(player1Hand);

        String expectedMessage = "Traded resources must not be empty";

        Exception thrown = assertThrows(IllegalArgumentException.class, () -> {
            player1.tradeResources(player2, player1Hand, new Resource[] {});
        });
        assertEquals(1, player1.hand.getResourceCardCount());
        assertEquals(0, player2.hand.getResourceCardCount());
        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    public void testTradeResources_withPlayer_andPlayer2EmptyCollections_throwsIllegalArgumentException() {
        Player player1 = new Player(1);
        Player player2 = new Player(2);

        Resource[] player2Hand = new Resource[] {
                Resource.LUMBER
        };

        player2.hand.addResources(player2Hand);

        String expectedMessage = "Traded resources must not be empty";

        Exception thrown = assertThrows(IllegalArgumentException.class, () -> {
            player1.tradeResources(player2, new Resource[] {}, player2Hand);
        });
        assertEquals(0, player1.hand.getResourceCardCount());
        assertEquals(1, player2.hand.getResourceCardCount());
        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    public void testTradeResources_withPlayer_withOneResource_returnTrue() {
        Player player1 = new Player(1);
        Player player2 = new Player(2);

        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();
        Hand mockedPlayer2Hand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();

        player1.hand = mockedPlayer1Hand;
        player2.hand = mockedPlayer2Hand;

        mockedPlayer1Hand.addResource(Resource.LUMBER, 1);
        mockedPlayer2Hand.addResource(Resource.BRICK, 1);

        EasyMock.replay(mockedPlayer1Hand, mockedPlayer2Hand);

        boolean success = player1.tradeResources(player2,
                new Resource[] { Resource.LUMBER },
                new Resource[] { Resource.BRICK });

        assertTrue(success);
        assertEquals(1, mockedPlayer1Hand.getResourceCardCount());
        assertEquals(1, mockedPlayer2Hand.getResourceCardCount());
        assertEquals(1, mockedPlayer1Hand.getResourceCardAmount(Resource.BRICK));
        assertEquals(1, mockedPlayer2Hand.getResourceCardAmount(Resource.LUMBER));
        EasyMock.verify(mockedPlayer1Hand, mockedPlayer2Hand);
    }

    @Test
    public void testTradeResources_withPlayer_withTwoResources_returnTrue() {
        Player player1 = new Player(1);
        Player player2 = new Player(2);

        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();
        Hand mockedPlayer2Hand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();

        player1.hand = mockedPlayer1Hand;
        player2.hand = mockedPlayer2Hand;

        mockedPlayer1Hand.addResource(Resource.LUMBER, 1);
        mockedPlayer1Hand.addResource(Resource.BRICK, 1);
        mockedPlayer2Hand.addResource(Resource.GRAIN, 1);

        EasyMock.replay(mockedPlayer1Hand, mockedPlayer2Hand);

        boolean success = player1.tradeResources(player2,
                new Resource[] { Resource.LUMBER, Resource.BRICK },
                new Resource[] { Resource.GRAIN });

        assertTrue(success);
        assertEquals(1, mockedPlayer1Hand.getResourceCardCount());
        assertEquals(2, mockedPlayer2Hand.getResourceCardCount());
        assertEquals(1, mockedPlayer1Hand.getResourceCardAmount(Resource.GRAIN));
        assertEquals(1, mockedPlayer2Hand.getResourceCardAmount(Resource.BRICK));
        assertEquals(1, mockedPlayer2Hand.getResourceCardAmount(Resource.LUMBER));
        EasyMock.verify(mockedPlayer1Hand, mockedPlayer2Hand);
    }

    @Test
    public void testTradeResources_withPlayer_withDuplicateResources_returnTrue() {
        Player player1 = new Player(1);
        Player player2 = new Player(2);

        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();
        Hand mockedPlayer2Hand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();

        player1.hand = mockedPlayer1Hand;
        player2.hand = mockedPlayer2Hand;

        mockedPlayer1Hand.addResource(Resource.LUMBER, 2);
        mockedPlayer2Hand.addResource(Resource.BRICK, 1);

        EasyMock.replay(mockedPlayer1Hand, mockedPlayer2Hand);

        boolean success = player1.tradeResources(player2,
                new Resource[] { Resource.LUMBER, Resource.LUMBER },
                new Resource[] { Resource.BRICK });

        assertTrue(success);
        assertEquals(1, mockedPlayer1Hand.getResourceCardCount());
        assertEquals(2, mockedPlayer2Hand.getResourceCardCount());
        assertEquals(1, mockedPlayer1Hand.getResourceCardAmount(Resource.BRICK));
        assertEquals(2, mockedPlayer2Hand.getResourceCardAmount(Resource.LUMBER));
        EasyMock.verify(mockedPlayer1Hand, mockedPlayer2Hand);
    }

    @Test
    public void testTradeResources_withPlayer_withTooManyResources_returnFalse() {
        Player player1 = new Player(1);
        Player player2 = new Player(2);

        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();
        Hand mockedPlayer2Hand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();

        player1.hand = mockedPlayer1Hand;
        player2.hand = mockedPlayer2Hand;

        mockedPlayer1Hand.addResource(Resource.LUMBER, 1);
        mockedPlayer2Hand.addResource(Resource.BRICK, 1);

        EasyMock.replay(mockedPlayer1Hand, mockedPlayer2Hand);

        boolean success = player1.tradeResources(player2,
                new Resource[] { Resource.LUMBER, Resource.LUMBER },
                new Resource[] { Resource.BRICK });

        assertFalse(success);
        assertEquals(1, mockedPlayer1Hand.getResourceCardCount());
        assertEquals(1, mockedPlayer2Hand.getResourceCardCount());
        assertEquals(1, mockedPlayer1Hand.getResourceCardAmount(Resource.LUMBER));
        assertEquals(1, mockedPlayer2Hand.getResourceCardAmount(Resource.BRICK));
        EasyMock.verify(mockedPlayer1Hand, mockedPlayer2Hand);
    }

    @Test
    public void testTradeResources_withPlayer_withTooManyResourcesPlayer2_returnFalse() {
        Player player1 = new Player(1);
        Player player2 = new Player(2);

        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();
        Hand mockedPlayer2Hand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();

        player1.hand = mockedPlayer1Hand;
        player2.hand = mockedPlayer2Hand;

        mockedPlayer1Hand.addResource(Resource.LUMBER, 1);
        mockedPlayer2Hand.addResource(Resource.BRICK, 1);

        EasyMock.replay(mockedPlayer1Hand, mockedPlayer2Hand);

        boolean success = player1.tradeResources(player2,
                new Resource[] { Resource.LUMBER },
                new Resource[] { Resource.BRICK, Resource.BRICK });

        assertFalse(success);
        assertEquals(1, mockedPlayer1Hand.getResourceCardCount());
        assertEquals(1, mockedPlayer2Hand.getResourceCardCount());
        assertEquals(1, mockedPlayer1Hand.getResourceCardAmount(Resource.LUMBER));
        assertEquals(1, mockedPlayer2Hand.getResourceCardAmount(Resource.BRICK));
        EasyMock.verify(mockedPlayer1Hand, mockedPlayer2Hand);
    }

    @Test
    public void testTrade_withBank_validTrade_returnTrue() {
        Player player = new Player(1);
        Bank bank = Bank.getInstance();

        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();

        // move resources from bank to player
        mockedPlayer1Hand.addResource(Resource.LUMBER, 4);
        bank.removeResource(Resource.LUMBER, 4);
        player.hand = mockedPlayer1Hand;

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.tradeWithBank(Resource.LUMBER, Resource.BRICK);

        assertTrue(success);
        assertEquals(1, mockedPlayer1Hand.getResourceCardAmount(Resource.BRICK));
        assertEquals(0, mockedPlayer1Hand.getResourceCardAmount(Resource.LUMBER));
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testTrade_withBank_withBrickPort_validTrade_returnTrue() {
        Player player = new Player(1);
        Bank bank = Bank.getInstance();

        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();

        // move resources from bank to player
        mockedPlayer1Hand.addResource(Resource.BRICK, 2);
        bank.removeResource(Resource.BRICK, 2);
        player.hand = mockedPlayer1Hand;
        player.addTradeBoost(Resource.BRICK);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.tradeWithBank(Resource.BRICK, Resource.WOOL);

        assertTrue(success);
        assertEquals(0, mockedPlayer1Hand.getResourceCardAmount(Resource.BRICK));
        assertEquals(1, mockedPlayer1Hand.getResourceCardAmount(Resource.WOOL));
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testTrade_withBank_withBetterPort_validTrade_returnTrue() {
        Player player = new Player(1);
        Bank bank = Bank.getInstance();
        bank.resetBank();

        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();

        // move resources from bank to player
        mockedPlayer1Hand.addResource(Resource.BRICK, 3);
        bank.removeResource(Resource.BRICK, 3);
        player.hand = mockedPlayer1Hand;
        player.addTradeBoost(Resource.ANY);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.tradeWithBank(Resource.BRICK, Resource.WOOL);

        assertTrue(success);
        assertEquals(0, mockedPlayer1Hand.getResourceCardAmount(Resource.BRICK));
        assertEquals(1, mockedPlayer1Hand.getResourceCardAmount(Resource.WOOL));
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testTrade_withEmptyBank_returnFalse() {
        Player player = new Player(1);
        Bank bank = Bank.getInstance();

        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();

        // move resources from bank to player
        mockedPlayer1Hand.addResource(Resource.BRICK, 4);
        bank.removeResource(Resource.BRICK, 4);
        player.hand = mockedPlayer1Hand;

        // empty bank
        bank.removeResource(Resource.WOOL, 19);
        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.tradeWithBank(Resource.BRICK, Resource.WOOL);

        assertFalse(success);
        assertEquals(4, mockedPlayer1Hand.getResourceCardAmount(Resource.BRICK));
        assertEquals(0, mockedPlayer1Hand.getResourceCardAmount(Resource.WOOL));
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testTrade_withBank_notEnoughCards_returnFalse() {
        Player player = new Player(1);
        Bank bank = Bank.getInstance();

        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();

        // move resources from bank to player
        mockedPlayer1Hand.addResource(Resource.WOOL, 1);
        bank.removeResource(Resource.WOOL, 1);
        player.hand = mockedPlayer1Hand;

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.tradeWithBank(Resource.WOOL, Resource.GRAIN);

        assertFalse(success);
        assertEquals(1, mockedPlayer1Hand.getResourceCardAmount(Resource.WOOL));
        assertEquals(0, mockedPlayer1Hand.getResourceCardAmount(Resource.GRAIN));
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testUpgradeSettlementToCity_HasCity_CanAfford_returnTrue() {
        Player player = new Player(1);
        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor()
                .addMockedMethod("removeResources").createMock();
        Bank bank = Bank.getInstance();
        bank.resetBank();

        mockedPlayer1Hand.addResources(new Resource[] {
                Resource.ORE,
                Resource.ORE,
                Resource.ORE,
                Resource.GRAIN,
                Resource.GRAIN
        });

        bank.removeResources(new Resource[] {
                Resource.ORE,
                Resource.ORE,
                Resource.ORE,
                Resource.GRAIN,
                Resource.GRAIN
        });

        player.hand = mockedPlayer1Hand;

        EasyMock.expect(mockedPlayer1Hand.removeResources(new Resource[] {
                Resource.ORE,
                Resource.ORE,
                Resource.ORE,
                Resource.GRAIN,
                Resource.GRAIN
        })).andReturn(true);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.canUpgradeSettlementToCity();

        assertTrue(success);
        assertEquals(3, player.numCities);
    }

    @Test
    public void testUpgradeSettlementToCity_HasCity_CannotAfford_returnFalse() {
        Player player = new Player(1);
        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor()
                .addMockedMethod("removeResources").createMock();
        Bank bank = Bank.getInstance();
        bank.resetBank();

        mockedPlayer1Hand.addResources(new Resource[] {
                Resource.ORE,
                Resource.ORE,
                Resource.GRAIN,
        });

        bank.removeResources(new Resource[] {
                Resource.ORE,
                Resource.ORE,
                Resource.GRAIN,
        });

        player.hand = mockedPlayer1Hand;

        EasyMock.expect(mockedPlayer1Hand.removeResources(new Resource[] {
                Resource.ORE,
                Resource.ORE,
                Resource.ORE,
                Resource.GRAIN,
                Resource.GRAIN
        })).andReturn(false);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.canUpgradeSettlementToCity();

        assertFalse(success);
        assertEquals(4, player.numCities);
    }

    @Test
    public void testUpgradeSettlementToCity_HasNoCities_CanAfford_returnFalse() {
        Player player = new Player(1);
        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor()
                .addMockedMethod("removeResources").createMock();
        Bank bank = Bank.getInstance();
        bank.resetBank();

        mockedPlayer1Hand.addResources(new Resource[] {
                Resource.ORE,
                Resource.ORE,
                Resource.ORE,
                Resource.GRAIN,
                Resource.GRAIN
        });

        bank.removeResources(new Resource[] {
                Resource.ORE,
                Resource.ORE,
                Resource.ORE,
                Resource.GRAIN,
                Resource.GRAIN
        });

        player.hand = mockedPlayer1Hand;

        // simulate having played all cities
        player.numCities = 0;

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.canUpgradeSettlementToCity();

        assertFalse(success);
        assertEquals(0, player.numCities);
    }

    @Test
    public void testUpgradeSettlementToCity_HasNoCities_CannotAfford_returnFalse() {
        Player player = new Player(1);
        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor()
                .addMockedMethod("removeResources").createMock();
        Bank bank = Bank.getInstance();
        bank.resetBank();

        mockedPlayer1Hand.addResources(new Resource[] {
                Resource.ORE,
                Resource.ORE,
                Resource.GRAIN,
        });

        bank.removeResources(new Resource[] {
                Resource.ORE,
                Resource.ORE,
                Resource.GRAIN,
        });

        player.hand = mockedPlayer1Hand;

        // simulate having played all cities
        player.numCities = 0;

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.canUpgradeSettlementToCity();

        assertFalse(success);
        assertEquals(0, player.numCities);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_nullCard_throwIllegalArgumentException() {
        Player player = new Player(1);

        String expectedMessage = "Cannot purchase null Development Card";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            player.purchaseDevCard(null);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testPurchaseDevCard_Knight_canAfford_returnTrue() {
        Player player = new Player(1);
        Bank bank = Bank.getInstance();
        bank.resetBank();
        DevCard knight = DevCard.KNIGHT;
        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor()
                .addMockedMethod("removeResources").addMockedMethod("addDevelopmentCard").createMock();
        mockedPlayer1Hand.addResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        bank.removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        player.hand = mockedPlayer1Hand;

        EasyMock.expect(mockedPlayer1Hand.removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        })).andReturn(true);
        EasyMock.expect(mockedPlayer1Hand.addDevelopmentCard(knight)).andReturn(true);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(knight);

        assertTrue(success);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_Knight_cannotAfford_returnFalse() {
        Player player = new Player(1);
        Bank bank = Bank.getInstance();
        bank.resetBank();
        DevCard knight = DevCard.KNIGHT;
        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor()
                .addMockedMethod("removeResources").addMockedMethod("addDevelopmentCard").createMock();
        player.hand = mockedPlayer1Hand;

        EasyMock.expect(mockedPlayer1Hand.removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        })).andReturn(false);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(knight);

        assertFalse(success);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_YearOfPlenty_canAfford_returnTrue() {
        Player player = new Player(1);
        Bank bank = Bank.getInstance();
        bank.resetBank();
        DevCard yop = DevCard.PLENTY;
        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor()
                .addMockedMethod("removeResources").addMockedMethod("addDevelopmentCard").createMock();
        mockedPlayer1Hand.addResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        bank.removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        player.hand = mockedPlayer1Hand;

        EasyMock.expect(mockedPlayer1Hand.removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        })).andReturn(true);
        EasyMock.expect(mockedPlayer1Hand.addDevelopmentCard(yop)).andReturn(true);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(yop);

        assertTrue(success);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_YearOfPlenty_cannotAfford_returnFalse() {
        Player player = new Player(1);
        Bank bank = Bank.getInstance();
        bank.resetBank();
        DevCard yop = DevCard.PLENTY;
        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor()
                .addMockedMethod("removeResources").addMockedMethod("addDevelopmentCard").createMock();
        player.hand = mockedPlayer1Hand;

        EasyMock.expect(mockedPlayer1Hand.removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        })).andReturn(false);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(yop);

        assertFalse(success);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_RoadBuilding_canAfford_returnTrue() {
        Player player = new Player(1);
        Bank bank = Bank.getInstance();
        bank.resetBank();
        DevCard road = DevCard.ROAD;
        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor()
                .addMockedMethod("removeResources").addMockedMethod("addDevelopmentCard").createMock();
        mockedPlayer1Hand.addResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        bank.removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        player.hand = mockedPlayer1Hand;

        EasyMock.expect(mockedPlayer1Hand.removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        })).andReturn(true);
        EasyMock.expect(mockedPlayer1Hand.addDevelopmentCard(road)).andReturn(true);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(road);

        assertTrue(success);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_RoadBuilding_cannotAfford_returnFalse() {
        Player player = new Player(1);
        Bank bank = Bank.getInstance();
        bank.resetBank();
        DevCard road = DevCard.ROAD;
        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor()
                .addMockedMethod("removeResources").addMockedMethod("addDevelopmentCard").createMock();
        player.hand = mockedPlayer1Hand;

        EasyMock.expect(mockedPlayer1Hand.removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        })).andReturn(false);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(road);

        assertFalse(success);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_Monopoly_canAfford_returnTrue() {
        Player player = new Player(1);
        Bank bank = Bank.getInstance();
        bank.resetBank();
        DevCard monopoly = DevCard.MONOPOLY;
        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor()
                .addMockedMethod("removeResources").addMockedMethod("addDevelopmentCard").createMock();
        mockedPlayer1Hand.addResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        bank.removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        player.hand = mockedPlayer1Hand;

        EasyMock.expect(mockedPlayer1Hand.removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        })).andReturn(true);
        EasyMock.expect(mockedPlayer1Hand.addDevelopmentCard(monopoly)).andReturn(true);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(monopoly);

        assertTrue(success);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_Monopoly_cannotAfford_returnFalse() {
        Player player = new Player(1);
        Bank bank = Bank.getInstance();
        bank.resetBank();
        DevCard monopoly = DevCard.MONOPOLY;
        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor()
                .addMockedMethod("removeResources").addMockedMethod("addDevelopmentCard").createMock();
        player.hand = mockedPlayer1Hand;

        EasyMock.expect(mockedPlayer1Hand.removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        })).andReturn(false);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(monopoly);

        assertFalse(success);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_VictoryPoint_canAfford_returnTrue() {
        Player player = new Player(1);
        Bank bank = Bank.getInstance();
        bank.resetBank();
        DevCard victoryPoint = DevCard.VICTORY;
        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor()
                .addMockedMethod("removeResources").addMockedMethod("addDevelopmentCard").createMock();
        mockedPlayer1Hand.addResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        bank.removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        player.hand = mockedPlayer1Hand;

        EasyMock.expect(mockedPlayer1Hand.removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        })).andReturn(true);
        EasyMock.expect(mockedPlayer1Hand.addDevelopmentCard(victoryPoint)).andReturn(true);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(victoryPoint);

        assertTrue(success);
        assertEquals(1, player.victoryPoints);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_VictoryPoint_cannotAfford_returnFalse() {
        Player player = new Player(1);
        Bank bank = Bank.getInstance();
        bank.resetBank();
        DevCard victoryPoint = DevCard.VICTORY;
        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor()
                .addMockedMethod("removeResources").addMockedMethod("addDevelopmentCard").createMock();
        player.hand = mockedPlayer1Hand;

        EasyMock.expect(mockedPlayer1Hand.removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        })).andReturn(false);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(victoryPoint);

        assertFalse(success);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_knight_full_returnFalse() {
        Player player = new Player(1);
        Bank bank = Bank.getInstance();
        bank.resetBank();
        DevCard knight = DevCard.KNIGHT;
        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor()
                .addMockedMethod("removeResources").addMockedMethod("addDevelopmentCard").createMock();
        mockedPlayer1Hand.addResource(Resource.WOOL, 15);
        mockedPlayer1Hand.addResource(Resource.GRAIN, 15);
        mockedPlayer1Hand.addResource(Resource.ORE, 15);
        bank.removeResource(Resource.WOOL, 15);
        bank.removeResource(Resource.GRAIN, 15);
        bank.removeResource(Resource.ORE, 15);
        player.hand = mockedPlayer1Hand;

        EasyMock.expect(mockedPlayer1Hand.removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        })).andReturn(true);
        EasyMock.expect(mockedPlayer1Hand.addDevelopmentCard(knight)).andReturn(false);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(knight);

        assertFalse(success);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_yearOfPlenty_full_returnFalse() {
        Player player = new Player(1);
        Bank bank = Bank.getInstance();
        bank.resetBank();
        DevCard yop = DevCard.PLENTY;
        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor()
                .addMockedMethod("removeResources").addMockedMethod("addDevelopmentCard").createMock();
        mockedPlayer1Hand.addResource(Resource.WOOL, 3);
        mockedPlayer1Hand.addResource(Resource.GRAIN, 3);
        mockedPlayer1Hand.addResource(Resource.ORE, 3);
        bank.removeResource(Resource.WOOL, 3);
        bank.removeResource(Resource.GRAIN, 3);
        bank.removeResource(Resource.ORE, 3);
        player.hand = mockedPlayer1Hand;

        EasyMock.expect(mockedPlayer1Hand.removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        })).andReturn(true);
        EasyMock.expect(mockedPlayer1Hand.addDevelopmentCard(yop)).andReturn(false);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(yop);

        assertFalse(success);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_roadBuilding_full_returnFalse() {
        Player player = new Player(1);
        Bank bank = Bank.getInstance();
        bank.resetBank();
        DevCard roadBuilding = DevCard.ROAD;
        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor()
                .addMockedMethod("removeResources").addMockedMethod("addDevelopmentCard").createMock();
        mockedPlayer1Hand.addResource(Resource.WOOL, 3);
        mockedPlayer1Hand.addResource(Resource.GRAIN, 3);
        mockedPlayer1Hand.addResource(Resource.ORE, 3);
        bank.removeResource(Resource.WOOL, 3);
        bank.removeResource(Resource.GRAIN, 3);
        bank.removeResource(Resource.ORE, 3);
        player.hand = mockedPlayer1Hand;

        EasyMock.expect(mockedPlayer1Hand.removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        })).andReturn(true);
        EasyMock.expect(mockedPlayer1Hand.addDevelopmentCard(roadBuilding)).andReturn(false);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(roadBuilding);

        assertFalse(success);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_monopoly_full_returnFalse() {
        Player player = new Player(1);
        Bank bank = Bank.getInstance();
        bank.resetBank();
        DevCard monopoly = DevCard.MONOPOLY;
        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor()
                .addMockedMethod("removeResources").addMockedMethod("addDevelopmentCard").createMock();
        mockedPlayer1Hand.addResource(Resource.WOOL, 3);
        mockedPlayer1Hand.addResource(Resource.GRAIN, 3);
        mockedPlayer1Hand.addResource(Resource.ORE, 3);
        bank.removeResource(Resource.WOOL, 3);
        bank.removeResource(Resource.GRAIN, 3);
        bank.removeResource(Resource.ORE, 3);
        player.hand = mockedPlayer1Hand;

        EasyMock.expect(mockedPlayer1Hand.removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        })).andReturn(true);
        EasyMock.expect(mockedPlayer1Hand.addDevelopmentCard(monopoly)).andReturn(false);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(monopoly);

        assertFalse(success);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testPurchaseDevCard_victoryPoint_full_returnFalse() {
        Player player = new Player(1);
        Bank bank = Bank.getInstance();
        bank.resetBank();
        DevCard vp = DevCard.VICTORY;
        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor()
                .addMockedMethod("removeResources").addMockedMethod("addDevelopmentCard").createMock();
        mockedPlayer1Hand.addResource(Resource.WOOL, 6);
        mockedPlayer1Hand.addResource(Resource.GRAIN, 6);
        mockedPlayer1Hand.addResource(Resource.ORE, 6);
        bank.removeResource(Resource.WOOL, 6);
        bank.removeResource(Resource.GRAIN, 6);
        bank.removeResource(Resource.ORE, 6);
        player.hand = mockedPlayer1Hand;

        EasyMock.expect(mockedPlayer1Hand.removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        })).andReturn(true);
        EasyMock.expect(mockedPlayer1Hand.addDevelopmentCard(vp)).andReturn(false);

        EasyMock.replay(mockedPlayer1Hand);

        boolean success = player.purchaseDevCard(vp);

        assertFalse(success);
        EasyMock.verify(mockedPlayer1Hand);
    }

    @Test
    public void testCanPlayDevelopmentCard_hasPlayedDevCard_returnFalse() {
        Bank.getInstance().resetBank();
        Player player = new Player(1);
        player.hasPlayedDevCard = true;
        DevCard knight = DevCard.KNIGHT;
        assertFalse(player.canPlayDevelopmentCard(knight));
    }

    @Test
    public void testCanPlayDevelopmentCard_nullCard_throwIllegalArgumentException() {
        Bank.getInstance().resetBank();
        Player player = new Player(1);
        DevCard nullCard = null;

        String expectedMessage = "Cannot attempt to play null Development Card";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            player.canPlayDevelopmentCard(nullCard);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testCanPlayDevelopmentCard_knight_returnTrue() {
        Bank.getInstance().resetBank();
        Player player = new Player(1);
        DevCard knight = DevCard.KNIGHT;

        // make sure player has purchased a Knight
        player.hand.addResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        Bank.getInstance().removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        player.purchaseDevCard(knight);

        // attempt to play knight
        boolean success = player.canPlayDevelopmentCard(knight);

        assertTrue(success);
        assertEquals(1, player.numKnightsPlayed);
    }

    @Test
    public void testCanPlayDevelopmentCard_yearOfPlenty_returnTrue() {
        Bank.getInstance().resetBank();
        Player player = new Player(1);
        DevCard yop = DevCard.PLENTY;

        // make sure player has purchased a Year of plenty card
        player.hand.addResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        Bank.getInstance().removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        player.purchaseDevCard(yop);

        // attempt to play year of plenty
        boolean success = player.canPlayDevelopmentCard(yop);

        assertTrue(success);
    }

    @Test
    public void testCanPlayDevelopmentCard_roadBuilding_returnTrue() {
        Bank.getInstance().resetBank();
        Player player = new Player(1);
        DevCard roadBuilding = DevCard.ROAD;

        // make sure player has purchased a road Building card
        player.hand.addResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        Bank.getInstance().removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        player.purchaseDevCard(roadBuilding);

        // attempt to play road building
        boolean success = player.canPlayDevelopmentCard(roadBuilding);

        assertTrue(success);
    }

    @Test
    public void testCanPlayDevelopmentCard_monopoly_returnTrue() {
        Bank.getInstance().resetBank();
        Player player = new Player(1);
        DevCard monopoly = DevCard.MONOPOLY;

        // make sure player has purchased a Monopoly card
        player.hand.addResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        Bank.getInstance().removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        player.purchaseDevCard(monopoly);

        // attempt to play monopoly
        boolean success = player.canPlayDevelopmentCard(monopoly);

        assertTrue(success);
    }

    @Test
    public void testCanPlayDevelopmentCard_victoryPoint_returnFalse() {
        Bank.getInstance().resetBank();
        Player player = new Player(1);
        DevCard victoryPoint = DevCard.VICTORY;

        // make sure player has purchased a victory point card
        player.hand.addResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        Bank.getInstance().removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        player.purchaseDevCard(victoryPoint);

        // attempt to play victory point (should fail, card is not playable)
        boolean success = player.canPlayDevelopmentCard(victoryPoint);

        assertFalse(success);
    }

    @Test
    public void testCanPlayDevelopmentCard_knight_noKnightInHand_returnFalse() {
        Bank.getInstance().resetBank();
        Player player = new Player(1);
        DevCard knight = DevCard.KNIGHT;

        boolean success = player.canPlayDevelopmentCard(knight);
        assertFalse(success);
    }

    @Test
    public void testCanPlayDevelopmentCard_yearOfPlenty_noYOPInHand_returnFalse() {
        Bank.getInstance().resetBank();
        Player player = new Player(1);
        DevCard yop = DevCard.PLENTY;

        boolean success = player.canPlayDevelopmentCard(yop);
        assertFalse(success);
    }

    @Test
    public void testCanPlayDevelopmentCard_roadBuilding_noRBInHand_returnFalse() {
        Bank.getInstance().resetBank();
        Player player = new Player(1);
        DevCard road = DevCard.ROAD;

        boolean success = player.canPlayDevelopmentCard(road);
        assertFalse(success);
    }

    @Test
    public void testCanPlayDevelopmentCard_monopoly_noMonopolyInHand_returnFalse() {
        Bank.getInstance().resetBank();
        Player player = new Player(1);
        DevCard monopoly = DevCard.MONOPOLY;

        boolean success = player.canPlayDevelopmentCard(monopoly);
        assertFalse(success);
    }

    @Test
    public void testCanPlayDevelopmentCard_attemptToPlay2_returnFalse() {
        Bank.getInstance().resetBank();
        Player player = new Player(1);
        DevCard knight = DevCard.KNIGHT;

        player.hand.addResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE,
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        Bank.getInstance().removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE,
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        player.purchaseDevCard(knight);
        player.purchaseDevCard(knight);

        boolean playFirst = player.canPlayDevelopmentCard(knight);
        boolean playSecond = player.canPlayDevelopmentCard(knight);
        assertTrue(playFirst);
        assertFalse(playSecond);
    }

    // ----------------------------------------------------------------------------
    //
    // Tests for new can play method
    //
    // ----------------------------------------------------------------------------

    @Test
    public void testUseDevCard_nullCard() {
        Bank.getInstance().resetBank();
        Player player = new Player(1);
        DevCard nullCard = null;

        String expectedMessage = "Cannot attempt to play null Development Card";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            player.useDevCard(nullCard);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testUseDevCard_exactEnoughNonBought() {
        Bank.getInstance().resetBank();
        Player player = new Player(1);
        DevCard card = DevCard.PLENTY;

        // make sure player has purchased a Year of plenty card
        player.hand.addResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        Bank.getInstance().removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        player.purchaseDevCard(card);

        // make it so it wasn't bought this turn
        player.addboughtCardsToHand();

        // attempt to play card
        boolean success = player.useDevCard(card);

        assertTrue(success);
    }

    @Test
    public void testUseDevCard_exactEnoughOneBought() {
        Bank.getInstance().resetBank();
        Player player = new Player(1);
        DevCard card = DevCard.PLENTY;

        // make sure player has purchased a card
        player.hand.addResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        Bank.getInstance().removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        player.purchaseDevCard(card);
        // make it so it wasn't bought this turn
        player.addboughtCardsToHand();


        // buy another of the same card
        player.hand.addResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        Bank.getInstance().removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        player.purchaseDevCard(card);
        // this one was bought during the turn

        // attempt to play card
        boolean success = player.useDevCard(card);

        assertTrue(success);
    }

    @Test
    public void testUseDevCard_notEnoughNoneBought() {
        Bank.getInstance().resetBank();
        Player player = new Player(1);
        DevCard card = DevCard.PLENTY;

        // attempt to play card that we don't have
        boolean success = player.useDevCard(card);

        assertFalse(success);
    }

    @Test
    public void testUseDevCard_notEnoughOneBought() {
        Bank.getInstance().resetBank();
        Player player = new Player(1);
        DevCard card = DevCard.PLENTY;

        // make sure player has purchased a card
        player.hand.addResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        Bank.getInstance().removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        player.purchaseDevCard(card);
        // player did buy it this turn

        // attempt to play card
        boolean success = player.useDevCard(card);

        assertFalse(success);
    }

    // false because the card was victory card
    @Test
    public void testUseDevCard_victoryPointCard() {
        Bank.getInstance().resetBank();
        Player player = new Player(1);
        DevCard card = DevCard.VICTORY;

        // make sure player has purchased a victory point card
        player.hand.addResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        Bank.getInstance().removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        player.purchaseDevCard(card);

        // make it so it wasn't bought this turn
        player.addboughtCardsToHand();

        // attempt to play victory point card. note regardless if we have it or not it should fail
        boolean success = player.useDevCard(card);

        assertFalse(success);
    }
}
