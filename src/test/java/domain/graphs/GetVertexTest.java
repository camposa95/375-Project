package domain.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import data.GameLoader;
import domain.game.GameType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GetVertexTest {

    private static final int MIN_LOCATION_ID = 0;
    private static final int MAX_LOCATION_ID = 53;

    VertexGraph vertexes;

    @BeforeEach
    public void setup() {
        vertexes = new VertexGraph(GameType.Beginner);
    }

    @Test
    public void getVertexWithLocationId0() {
        Vertex vertex = vertexes.getVertex(MIN_LOCATION_ID);

        int actualId = vertex.getLocationId();

        assertEquals(MIN_LOCATION_ID, actualId);
    }

    @Test
    public void getVertexWithLocationId1() {
        Vertex vertex = vertexes.getVertex(1);

        int expectedId = 1;
        int actualId = vertex.getLocationId();

        assertEquals(expectedId, actualId);
    }

    @Test
    public void getVertexNWithN_2to53() {
        VertexGraph vertexes =  new VertexGraph(GameType.Beginner);

        for (int i = 2; i <= MAX_LOCATION_ID; i++) {
            Vertex vertex = vertexes.getVertex(i);

            int actualId = vertex.getLocationId();

            assertEquals(i, actualId);
        }
    }

    @Test
    public void getVertexWithInvalidId54() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vertexes.getVertex(MAX_LOCATION_ID + 1));

        String expectedMessage = "Invalid locationId; Try[0, 53]";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void getVertexWithInvalidIdNegative1() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> vertexes.getVertex(-1));

        String expectedMessage = "Invalid locationId; Try[0, 53]";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
