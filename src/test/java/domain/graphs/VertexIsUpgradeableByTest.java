package domain.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.player.Player;

public class VertexIsUpgradeableByTest {

    Vertex testVertex;
    Player mockedPlayer, mockedEnemy;

    @BeforeEach
    public void setup() {
        // Note: the functionality of the method under test
        // is independent of the graphs holding the vertexes,
        // so we can just make a vertex by itself
        testVertex = new Vertex(0);
        mockedPlayer = EasyMock.createStrictMock(Player.class);
        mockedEnemy = EasyMock.createStrictMock(Player.class);
    }

    @Test
    public void testIsUpgradeableBy1() {
        // make vertex owned by the player
        testVertex.setOwner(mockedPlayer);
        // vertex is a settlement

        EasyMock.replay(mockedPlayer);
        boolean actualResult = testVertex.isUpgradableBy(mockedPlayer);
        EasyMock.verify(mockedPlayer);

        boolean expectedResult = true;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsUpgradeableBy2() {
        // make vertex owned by the player
        testVertex.setOwner(mockedPlayer);
        // vertex is a city
        testVertex.upgradeToCity(mockedPlayer);

        EasyMock.replay(mockedPlayer);
        boolean actualResult = testVertex.isUpgradableBy(mockedPlayer);
        EasyMock.verify(mockedPlayer);

        boolean expectedResult = false;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsUpgradeableBy3() {
        Player mockedEnemy = EasyMock.createStrictMock(Player.class);

        // vertex not owned by the player
        // vertex is a settlement owned by someone else
        testVertex.setOwner(mockedEnemy);

        EasyMock.replay(mockedPlayer, mockedEnemy);
        boolean actualResult = testVertex.isUpgradableBy(mockedPlayer);
        EasyMock.verify(mockedPlayer, mockedEnemy);

        boolean expectedResult = false;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsUpgradeableBy4() {
        // vertex not owned by the player
        // vertex is a settlement owned by someone else
        testVertex.setOwner(mockedEnemy);
        testVertex.upgradeToCity(mockedPlayer);

        EasyMock.replay(mockedPlayer, mockedEnemy);
        boolean actualResult = testVertex.isUpgradableBy(mockedPlayer);
        EasyMock.verify(mockedPlayer, mockedEnemy);

        boolean expectedResult = false;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsUpgradeableBy5() {
        // vertex not owned by the player
        // vertex is undeveloped

        EasyMock.replay(mockedPlayer, mockedEnemy);
        boolean actualResult = testVertex.isUpgradableBy(mockedPlayer);
        EasyMock.verify(mockedPlayer, mockedEnemy);

        boolean expectedResult = false;
        assertEquals(expectedResult, actualResult);
    }
}
