package domain.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import data.GameLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.game.GameType;
import domain.bank.Resource;

public class GetPortTest {
    private static final int MIN_LOCATION_ID = 0;
    private static final int MAX_LOCATION_ID = 8;

    GameboardGraph vertexes;

    @BeforeEach
    public void setup() {
        vertexes = new GameboardGraph(GameType.Beginner);
        GameLoader.initializeGraphs(vertexes);
    }

    @Test
    public void getPortWithLocationId0() {
        Port port = vertexes.getPort(MIN_LOCATION_ID);

        int actualId = port.getLocationId();

        Resource expectedResource = Resource.ANY;
        Resource actualResource = port.getResource();

        assertEquals(MIN_LOCATION_ID, actualId);
        assertEquals(expectedResource, actualResource);
    }

    @Test
    public void getPortWithLocationId1() {
        Port port = vertexes.getPort(1);

        int expectedId = 1;
        int actualId = port.getLocationId();

        Resource expectedResource = Resource.GRAIN;
        Resource actualResource = port.getResource();

        assertEquals(expectedId, actualId);
        assertEquals(expectedResource, actualResource);
    }

    @Test
    public void getPortWithLocationId2() {
        Port port = vertexes.getPort(2);

        int expectedId = 2;
        int actualId = port.getLocationId();

        Resource expectedResource = Resource.WOOL;
        Resource actualResource = port.getResource();

        assertEquals(expectedId, actualId);
        assertEquals(expectedResource, actualResource);
    }

    @Test
    public void getPortWithLocationId3() {
        Port port = vertexes.getPort(3);

        int expectedId = 3;
        int actualId = port.getLocationId();

        Resource expectedResource = Resource.ANY;
        Resource actualResource = port.getResource();

        assertEquals(expectedId, actualId);
        assertEquals(expectedResource, actualResource);
    }

    @Test
    public void getPortWithLocationId4() {
        Port port = vertexes.getPort(4);

        int expectedId = 4;
        int actualId = port.getLocationId();

        Resource expectedResource = Resource.ORE;
        Resource actualResource = port.getResource();

        assertEquals(expectedId, actualId);
        assertEquals(expectedResource, actualResource);
    }

    @Test
    public void getPortWithLocationId5() {
        Port port = vertexes.getPort(5);

        int expectedId = 5;
        int actualId = port.getLocationId();

        Resource expectedResource = Resource.ANY;
        Resource actualResource = port.getResource();

        assertEquals(expectedId, actualId);
        assertEquals(expectedResource, actualResource);
    }

    @Test
    public void getPortWithLocationId6() {
        Port port = vertexes.getPort(6);

        int expectedId = 6;
        int actualId = port.getLocationId();

        Resource expectedResource = Resource.ANY;
        Resource actualResource = port.getResource();

        assertEquals(expectedId, actualId);
        assertEquals(expectedResource, actualResource);
    }

    @Test
    public void getPortWithLocationId7() {
        Port port = vertexes.getPort(7);

        int expectedId = 7;
        int actualId = port.getLocationId();

        Resource expectedResource = Resource.BRICK;
        Resource actualResource = port.getResource();

        assertEquals(expectedId, actualId);
        assertEquals(expectedResource, actualResource);
    }

    @Test
    public void getPortWithLocationId8() {
        Port port = vertexes.getPort(8);

        int expectedId = 8;
        int actualId = port.getLocationId();

        Resource expectedResource = Resource.LUMBER;
        Resource actualResource = port.getResource();

        assertEquals(expectedId, actualId);
        assertEquals(expectedResource, actualResource);
    }

    @Test
    public void getPortWithInvalidId9() {
        GameboardGraph vertexes =  new GameboardGraph(GameType.Beginner);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vertexes.getPort(MAX_LOCATION_ID + 1));

        String expectedMessage = "Invalid locationId; Try[0, 8]";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void getPortWithInvalidIdNegative1() {
        GameboardGraph vertexes =  new GameboardGraph(GameType.Beginner);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vertexes.getPort(-1));

        String expectedMessage = "Invalid locationId; Try[0, 8]";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test_RandomPorts() {
        GameboardGraph randomPorts = new GameboardGraph(GameType.Advanced);
        GameLoader.initializeGraphs(randomPorts);

        assertEquals(vertexes.getOrder().length, randomPorts.getOrder().length);
        assertFalse(Arrays.equals(vertexes.getOrder(), randomPorts.getOrder()));
    }
}
