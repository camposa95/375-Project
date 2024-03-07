package graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import gamedatastructures.GameType;
import gamedatastructures.Resource;

public class GetPortTest {
    private static final int MIN_LOCATION_ID = 0;
    private static final int MAX_LOCATION_ID = 8;
    private static final String VERTEX_TO_PORT_LAYOUT_FILE = "src/main/java/graphs/VertexToPortLayout.txt";

    @Test
    public void getPortWithLocationId0() {
        VertexGraph vertexes =  new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);
        Port port = vertexes.getPort(MIN_LOCATION_ID);

        int expectedId = MIN_LOCATION_ID;
        int actualId = port.getLocationId();

        Resource expectedResource = Resource.ANY;
        Resource actualResource = port.getResourse();

        assertEquals(expectedId, actualId);
        assertEquals(expectedResource, actualResource);
    }

    @Test
    public void getPortWithLocationId1() {
        VertexGraph vertexes =  new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);
        Port port = vertexes.getPort(1);

        int expectedId = 1;
        int actualId = port.getLocationId();

        Resource expectedResource = Resource.GRAIN;
        Resource actualResource = port.getResourse();

        assertEquals(expectedId, actualId);
        assertEquals(expectedResource, actualResource);
    }

    @Test
    public void getPortWithLocationId2() {
        VertexGraph vertexes =  new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);
        Port port = vertexes.getPort(2);

        int expectedId = 2;
        int actualId = port.getLocationId();

        Resource expectedResource = Resource.WOOL;
        Resource actualResource = port.getResourse();

        assertEquals(expectedId, actualId);
        assertEquals(expectedResource, actualResource);
    }

    @Test
    public void getPortWithLocationId3() {
        VertexGraph vertexes =  new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);
        Port port = vertexes.getPort(3);

        int expectedId = 3;
        int actualId = port.getLocationId();

        Resource expectedResource = Resource.ANY;
        Resource actualResource = port.getResourse();

        assertEquals(expectedId, actualId);
        assertEquals(expectedResource, actualResource);
    }

    @Test
    public void getPortWithLocationId4() {
        VertexGraph vertexes =  new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);
        Port port = vertexes.getPort(4);

        int expectedId = 4;
        int actualId = port.getLocationId();

        Resource expectedResource = Resource.ORE;
        Resource actualResource = port.getResourse();

        assertEquals(expectedId, actualId);
        assertEquals(expectedResource, actualResource);
    }
    
    @Test
    public void getPortWithLocationId5() {
        VertexGraph vertexes =  new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);
        Port port = vertexes.getPort(5);

        int expectedId = 5;
        int actualId = port.getLocationId();

        Resource expectedResource = Resource.ANY;
        Resource actualResource = port.getResourse();

        assertEquals(expectedId, actualId);
        assertEquals(expectedResource, actualResource);
    }

    @Test
    public void getPortWithLocationId6() {
        VertexGraph vertexes =  new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);
        Port port = vertexes.getPort(6);

        int expectedId = 6;
        int actualId = port.getLocationId();

        Resource expectedResource = Resource.ANY;
        Resource actualResource = port.getResourse();

        assertEquals(expectedId, actualId);
        assertEquals(expectedResource, actualResource);
    }

    @Test
    public void getPortWithLocationId7() {
        VertexGraph vertexes =  new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);
        Port port = vertexes.getPort(7);

        int expectedId = 7;
        int actualId = port.getLocationId();

        Resource expectedResource = Resource.BRICK;
        Resource actualResource = port.getResourse();

        assertEquals(expectedId, actualId);
        assertEquals(expectedResource, actualResource);
    }

    @Test
    public void getPortWithLocationId8() {
        VertexGraph vertexes =  new VertexGraph();
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);
        Port port = vertexes.getPort(8);

        int expectedId = 8;
        int actualId = port.getLocationId();

        Resource expectedResource = Resource.LUMBER;
        Resource actualResource = port.getResourse();

        assertEquals(expectedId, actualId);
        assertEquals(expectedResource, actualResource);
    }

    @Test
    public void getPortWithInvalidId9() {
        VertexGraph vertexes =  new VertexGraph();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            vertexes.getPort(MAX_LOCATION_ID + 1);
        });

        String expectedMessage = "Invalid locationId; Try[0, 8]";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void getPortWithInvalidIdNegative1() {
        VertexGraph vertexes =  new VertexGraph();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            vertexes.getPort(-1);
        });

        String expectedMessage = "Invalid locationId; Try[0, 8]";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void test_RandomPorts(){
        VertexGraph basicPorts = new VertexGraph();
        basicPorts.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);
        VertexGraph randomPorts = new VertexGraph();
        randomPorts.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Advanced);

        assertEquals(basicPorts.getOrder().length, randomPorts.getOrder().length);
        assertFalse(Arrays.equals(basicPorts.getOrder(), randomPorts.getOrder()));
    }
}
