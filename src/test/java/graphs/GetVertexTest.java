package graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class GetVertexTest {

    private static final int MIN_LOCATION_ID = 0;
    private static final int MAX_LOCATION_ID = 53;

    @Test
    public void getVertexWithLocationId0() {
        VertexGraph vertexes =  new VertexGraph();
        Vertex vertex = vertexes.getVertex(MIN_LOCATION_ID);

        int expectedId = MIN_LOCATION_ID;
        int actualId = vertex.getLocationId();

        assertEquals(expectedId, actualId);
    }

    @Test
    public void getVertexWithLocationId1() {
        VertexGraph vertexes =  new VertexGraph();
        Vertex vertex = vertexes.getVertex(1);

        int expectedId = 1;
        int actualId = vertex.getLocationId();

        assertEquals(expectedId, actualId);
    }

    @Test
    public void getVertexNWithNfrom2to53() {
        VertexGraph vertexes =  new VertexGraph();

        for (int i = 2; i <= MAX_LOCATION_ID; i++) {
            Vertex vertex = vertexes.getVertex(i);

            int expectedId = i;
            int actualId = vertex.getLocationId();

            assertEquals(expectedId, actualId);
        }
    }

    @Test
    public void getVertexWithInvalidId54() {
        VertexGraph vertexes =  new VertexGraph();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            vertexes.getVertex(MAX_LOCATION_ID + 1);
        });

        String expectedMessage = "Invalid locationId; Try[0, 53]";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void getVertexWithInvalidIdNegative1() {
        VertexGraph vertexes =  new VertexGraph();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            vertexes.getVertex(-1);
        });

        String expectedMessage = "Invalid locationId; Try[0, 53]";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
