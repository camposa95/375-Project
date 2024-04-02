package domain.player;

import domain.bank.Bank;
import domain.bank.Resource;
import domain.devcarddeck.DevCard;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void testPurchaseSettlement_emptyHand_returnFalse() {
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        boolean success = player.purchaseSettlement();

        int expectedVictoryPoints = 0;

        assertFalse(success);
        assertEquals(expectedVictoryPoints, player.victoryPoints);
    }

    @Test
    public void testPurchaseSettlement_justEnoughResources_returnTrue() {
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        Hand mockedHand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();

        // Move resources into mocked hand
        bank.removeResources(new Resource[] { Resource.LUMBER, Resource.BRICK, Resource.WOOL, Resource.GRAIN });
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
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        Hand mockedHand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();

        // Move resources into mocked hand
        bank.removeResources(new Resource[] { Resource.LUMBER, Resource.BRICK, Resource.WOOL });
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
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        Hand mockedHand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();

        // remove all from bank
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
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        Hand mockedHand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();

        // Move resources into mocked hand
        bank.removeResources(new Resource[] { Resource.LUMBER, Resource.BRICK, Resource.WOOL, Resource.GRAIN });
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
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        boolean success = player.purchaseRoad();

        assertFalse(success);
    }

    @Test
    public void testPurchaseRoad_JustEnoughResources_returnTrue() {
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        Hand mockedHand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();

        // Move resources into mocked hand
        bank.removeResources(new Resource[] { Resource.LUMBER, Resource.BRICK });
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
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        Hand mockedHand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();

        // Move resources into mocked hand
        bank.removeResources(new Resource[] { Resource.LUMBER });
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
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        Hand mockedHand = EasyMock.createMockBuilder(Hand.class).withConstructor().createMock();

        // Move resources into mocked hand
        bank.removeResources(new Resource[] { Resource.LUMBER, Resource.BRICK });
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
        Bank bank = new Bank();
        Player player = new Player(1, bank);

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
        Bank bank = new Bank();
        Player player = new Player(1, bank);

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
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        bank.reset();

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
        Bank bank = new Bank();
        Player player = new Player(1, bank);

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
        Bank bank = new Bank();
        Player player = new Player(1, bank);

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
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor()
                .addMockedMethod("removeResources").createMock();
        bank.reset();

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
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor()
                .addMockedMethod("removeResources").createMock();
        bank.reset();

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
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor()
                .addMockedMethod("removeResources").createMock();
        bank.reset();

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
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        Hand mockedPlayer1Hand = EasyMock.createMockBuilder(Hand.class).withConstructor()
                .addMockedMethod("removeResources").createMock();
        bank.reset();

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
        Bank bank = new Bank();
        Player player = new Player(1, bank);

        String expectedMessage = "Cannot purchase null Development Card";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            player.purchaseDevCard(null);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testPurchaseDevCard_Knight_canAfford_returnTrue() {
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        bank.reset();
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
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        bank.reset();
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
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        bank.reset();
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
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        bank.reset();
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
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        bank.reset();
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
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        bank.reset();
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
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        bank.reset();
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
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        bank.reset();
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
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        bank.reset();
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
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        bank.reset();
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
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        bank.reset();
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
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        bank.reset();
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
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        bank.reset();
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
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        bank.reset();
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
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        bank.reset();
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

    // ----------------------------------------------------------------------------
    //
    // Tests for new can play method
    //
    // ----------------------------------------------------------------------------

    @Test
    public void testUseDevCard_nullCard() {
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        DevCard nullCard = null;

        String expectedMessage = "Cannot attempt to play null Development Card";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            player.useDevCard(nullCard);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testUseDevCard_exactEnoughNonBought() {
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        DevCard card = DevCard.PLENTY;

        // make sure player has purchased a Year of plenty card
        player.hand.addResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        bank.removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        player.purchaseDevCard(card);

        // make it so it wasn't bought this turn
        player.addBoughtCardsToHand();

        // attempt to play card
        boolean success = player.useDevCard(card);

        assertTrue(success);
    }

    @Test
    public void testUseDevCard_exactEnoughOneBought() {
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        DevCard card = DevCard.PLENTY;

        // make sure player has purchased a card
        player.hand.addResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        bank.removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        player.purchaseDevCard(card);
        // make it so it wasn't bought this turn
        player.addBoughtCardsToHand();


        // buy another of the same card
        player.hand.addResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        bank.removeResources(new Resource[] {
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
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        DevCard card = DevCard.PLENTY;

        // attempt to play card that we don't have
        boolean success = player.useDevCard(card);

        assertFalse(success);
    }

    @Test
    public void testUseDevCard_notEnoughOneBought() {
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        DevCard card = DevCard.PLENTY;

        // make sure player has purchased a card
        player.hand.addResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        bank.removeResources(new Resource[] {
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
        Bank bank = new Bank();
        Player player = new Player(1, bank);
        DevCard card = DevCard.VICTORY;

        // make sure player has purchased a victory point card
        player.hand.addResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        bank.removeResources(new Resource[] {
                Resource.WOOL, Resource.GRAIN, Resource.ORE
        });
        player.purchaseDevCard(card);

        // make it so it wasn't bought this turn
        player.addBoughtCardsToHand();

        // attempt to play victory point card. note regardless if we have it or not it should fail
        boolean success = player.useDevCard(card);

        assertFalse(success);
    }
}
