package graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import gamedatastructures.Building;
import gamedatastructures.DistrictType;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import gamedatastructures.Player;

public class VertexUpgradesTest {
    @Test
    public void testIsUpgradeableBy1() {
        // Note: the functionality of the method under test
        // is independent of the graphs holding the vertexes
        // so we can just made a vertex by itself
        Vertex testVertex = new Vertex(0);

        Player mockedPlayer = EasyMock.createStrictMock(Player.class);

        // make vertex owned by the player
        testVertex.setOwner(mockedPlayer);
        // vertex is a settlment

        EasyMock.replay(mockedPlayer);
        boolean actualresult = testVertex.isUpgradableBy(mockedPlayer);
        EasyMock.verify(mockedPlayer);

        boolean expectedResult = true;
        assertEquals(expectedResult, actualresult);
    }

    @Test
    public void testIsUpgradeableBy2() {
        // Note: the functionality of the method under test
        // is independent of the graphs holding the vertexes
        // so we can just made a vertex by itself
        Vertex testVertex = new Vertex(0);

        Player mockedPlayer = EasyMock.createStrictMock(Player.class);

        // make vertex owned by the player
        testVertex.setOwner(mockedPlayer);
        // vertex is a city
        testVertex.setBuildingToCity();

        EasyMock.replay(mockedPlayer);
        boolean actualresult = testVertex.isUpgradableBy(mockedPlayer);
        EasyMock.verify(mockedPlayer);

        boolean expectedResult = false;
        assertEquals(expectedResult, actualresult);
    }

    @Test
    public void testIsUpgradeableBy3() {
        // Note: the functionality of the method under test
        // is independent of the graphs holding the vertexes
        // so we can just made a vertex by itself
        Vertex testVertex = new Vertex(0);

        Player mockedPlayer = EasyMock.createStrictMock(Player.class);
        Player mockedEnemy = EasyMock.createStrictMock(Player.class);

        // vertex not owned by the player
        // vertex is a settlment owned by someone else
        testVertex.setOwner(mockedEnemy);

        EasyMock.replay(mockedPlayer, mockedEnemy);
        boolean actualresult = testVertex.isUpgradableBy(mockedPlayer);
        EasyMock.verify(mockedPlayer, mockedEnemy);

        boolean expectedResult = false;
        assertEquals(expectedResult, actualresult);
    }

    @Test
    public void testIsUpgradeableBy4() {
        // Note: the functionality of the method under test
        // is independent of the graphs holding the vertexes
        // so we can just made a vertex by itself
        Vertex testVertex = new Vertex(0);

        Player mockedPlayer = EasyMock.createStrictMock(Player.class);
        Player mockedEnemy = EasyMock.createStrictMock(Player.class);

        // vertex not owned by the player
        // vertex is a settlment owned by someone else
        testVertex.setOwner(mockedEnemy);
        testVertex.setBuildingToCity();

        EasyMock.replay(mockedPlayer, mockedEnemy);
        boolean actualresult = testVertex.isUpgradableBy(mockedPlayer);
        EasyMock.verify(mockedPlayer, mockedEnemy);

        boolean expectedResult = false;
        assertEquals(expectedResult, actualresult);
    }

    @Test
    public void testIsUpgradeableBy5() {
        // Note: the functionality of the method under test
        // is independent of the graphs holding the vertexes
        // so we can just made a vertex by itself
        Vertex testVertex = new Vertex(0);

        Player mockedPlayer = EasyMock.createStrictMock(Player.class);
        Player mockedEnemy = EasyMock.createStrictMock(Player.class);

        // vertex not owned by the player
        // vertex is undeveloped

        EasyMock.replay(mockedPlayer, mockedEnemy);
        boolean actualresult = testVertex.isUpgradableBy(mockedPlayer);
        EasyMock.verify(mockedPlayer, mockedEnemy);

        boolean expectedResult = false;
        assertEquals(expectedResult, actualresult);
    }

    @Test
    public void testBuildDistrict_withVertexOwnedByPlayer_expectSuccess() {
        // Note: the functionality of the method under test
        // is independent of the graphs holding the vertexes
        // so we can just made a vertex by itself
        Vertex testVertex = new Vertex(0);

        Player mockedPlayer = EasyMock.createStrictMock(Player.class);
        Player mockedEnemy = EasyMock.createStrictMock(Player.class);

        testVertex.setOwner(mockedPlayer);
        testVertex.building = new Building();

        EasyMock.replay(mockedPlayer, mockedEnemy);
        testVertex.buildDistrict(mockedPlayer, DistrictType.MINE);
        EasyMock.verify(mockedPlayer, mockedEnemy);

        assertEquals(DistrictType.MINE, testVertex.building.getDistrict());
    }

    @Test
    public void testBuildDistrict_withVertexNotOwnedByPlayer_expectIllegalArgumentException() {
        // Note: the functionality of the method under test
        // is independent of the graphs holding the vertexes
        // so we can just made a vertex by itself
        Vertex testVertex = new Vertex(0);

        Player mockedPlayer = EasyMock.createStrictMock(Player.class);
        Player mockedEnemy = EasyMock.createStrictMock(Player.class);

        testVertex.setOwner(mockedEnemy);
        testVertex.building = new Building();

        EasyMock.replay(mockedPlayer, mockedEnemy);
        Assertions.assertThrows(IllegalArgumentException.class, () -> testVertex.buildDistrict(mockedPlayer, DistrictType.MINE));
        EasyMock.verify(mockedPlayer, mockedEnemy);
    }
}
