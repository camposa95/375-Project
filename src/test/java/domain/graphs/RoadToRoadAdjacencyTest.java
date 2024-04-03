package domain.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import data.GameLoader;
import domain.game.GameType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class RoadToRoadAdjacencyTest {

    VertexGraph vertexes;
    RoadGraph roads;

    @BeforeEach
    public void setup() {
        vertexes = new VertexGraph(GameType.Beginner);
        roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);
    }

    @Test
    public void testGetAdjacentRoadsTo0() {
        Road road = roads.getRoad(0);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(roads.getRoad(1));
        expectedRoads.add(roads.getRoad(6));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn1and3() {
        final int[] ids = {1, 3};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(roads.getRoad(id - 1));
            expectedRoads.add(roads.getRoad(id + 1));
            expectedRoads.add(roads.getRoad(id + (6 - i)));


            List<Road> actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.size(), actualRoads.size());
            for (int j = 0; j < expectedRoads.size(); j++) {
                assertEquals(expectedRoads.get(j), actualRoads.get(j));
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn2and4() {
        final int[] ids = {2, 4};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(roads.getRoad(id - 1));
            expectedRoads.add(roads.getRoad(id + 1));
            expectedRoads.add(roads.getRoad(id + (5 - i)));


            List<Road> actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.size(), actualRoads.size());
            for (int j = 0; j < expectedRoads.size(); j++) {
                assertEquals(expectedRoads.get(j), actualRoads.get(j));
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo5() {
        Road road = roads.getRoad(5);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(roads.getRoad(4));
        expectedRoads.add(roads.getRoad(9));


        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo6() {
        Road road = roads.getRoad(6);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(roads.getRoad(0));
        expectedRoads.add(roads.getRoad(10));
        expectedRoads.add(roads.getRoad(11));


        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn7and8() {
        final int[] ids = {7, 8};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(roads.getRoad(id - (6 - i)));
            expectedRoads.add(roads.getRoad(id - (5 - i)));
            expectedRoads.add(roads.getRoad(id + (5 + i)));
            expectedRoads.add(roads.getRoad(id + (6 + i)));


            List<Road> actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.size(), actualRoads.size());
            for (int j = 0; j < expectedRoads.size(); j++) {
                assertEquals(expectedRoads.get(j), actualRoads.get(j));
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo9() {
        Road road = roads.getRoad(9);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(roads.getRoad(5));
        expectedRoads.add(roads.getRoad(16));
        expectedRoads.add(roads.getRoad(17));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo10() {
        Road road = roads.getRoad(10);


        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(roads.getRoad(6));
        expectedRoads.add(roads.getRoad(11));
        expectedRoads.add(roads.getRoad(18));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn11and13and15() {
        final int[] ids = {11, 13, 15};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(roads.getRoad(id - (5 + i)));
            expectedRoads.add(roads.getRoad(id - 1));
            expectedRoads.add(roads.getRoad(id + 1));
            expectedRoads.add(roads.getRoad(id + (8 - i)));

            List<Road> actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.size(), actualRoads.size());
            for (int j = 0; j < expectedRoads.size(); j++) {
                assertEquals(expectedRoads.get(j), actualRoads.get(j));
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn12and14and16() {
        final int[] ids = {12, 14, 16};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(roads.getRoad(id - (5 + i)));
            expectedRoads.add(roads.getRoad(id - 1));
            expectedRoads.add(roads.getRoad(id + 1));
            expectedRoads.add(roads.getRoad(id + (7 - i)));

            List<Road> actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.size(), actualRoads.size());
            for (int j = 0; j < expectedRoads.size(); j++) {
                assertEquals(expectedRoads.get(j), actualRoads.get(j));
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo17() {
        Road road = roads.getRoad(17);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(roads.getRoad(9));
        expectedRoads.add(roads.getRoad(16));
        expectedRoads.add(roads.getRoad(22));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo18() {
        Road road = roads.getRoad(18);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(roads.getRoad(10));
        expectedRoads.add(roads.getRoad(23));
        expectedRoads.add(roads.getRoad(24));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn19and20and21() {
        final int[] ids = {19, 20, 21};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(roads.getRoad(id - (8 - i)));
            expectedRoads.add(roads.getRoad(id - (7 - i)));
            expectedRoads.add(roads.getRoad(id + (6 + i)));
            expectedRoads.add(roads.getRoad(id + (7 + i)));


            List<Road> actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.size(), actualRoads.size());
            for (int j = 0; j < expectedRoads.size(); j++) {
                assertEquals(expectedRoads.get(j), actualRoads.get(j));
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo22() {
        Road road = roads.getRoad(22);


        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(roads.getRoad(17));
        expectedRoads.add(roads.getRoad(31));
        expectedRoads.add(roads.getRoad(32));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo23() {
        Road road = roads.getRoad(23);


        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(roads.getRoad(18));
        expectedRoads.add(roads.getRoad(24));
        expectedRoads.add(roads.getRoad(33));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn24and26and28and30() {
        final int[] ids = {24, 26, 28, 30};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(roads.getRoad(id - (6 + i)));
            expectedRoads.add(roads.getRoad(id - 1));
            expectedRoads.add(roads.getRoad(id + 1));
            expectedRoads.add(roads.getRoad(id + (10 - i)));


            List<Road> actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.size(), actualRoads.size());
            for (int j = 0; j < expectedRoads.size(); j++) {
                assertEquals(expectedRoads.get(j), actualRoads.get(j));
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn25and27and29and31() {
        final int[] ids = {25, 27, 29, 31};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(roads.getRoad(id - (6 + i)));
            expectedRoads.add(roads.getRoad(id - 1));
            expectedRoads.add(roads.getRoad(id + 1));
            expectedRoads.add(roads.getRoad(id + (9 - i)));

            List<Road> actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.size(), actualRoads.size());
            for (int j = 0; j < expectedRoads.size(); j++) {
                assertEquals(expectedRoads.get(j), actualRoads.get(j));
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo32() {
        Road road = roads.getRoad(32);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(roads.getRoad(22));
        expectedRoads.add(roads.getRoad(31));
        expectedRoads.add(roads.getRoad(38));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo33() {
        Road road = roads.getRoad(33);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(roads.getRoad(23));
        expectedRoads.add(roads.getRoad(39));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn34and35and36and37() {
        final int[] ids = {34, 35, 36, 37};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(roads.getRoad(id - (10 - i)));
            expectedRoads.add(roads.getRoad(id - (9 - i)));
            expectedRoads.add(roads.getRoad(id + (6 + i)));
            expectedRoads.add(roads.getRoad(id + (7 + i)));

            List<Road> actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.size(), actualRoads.size());
            for (int j = 0; j < expectedRoads.size(); j++) {
                assertEquals(expectedRoads.get(j), actualRoads.get(j));
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo38() {
        Road road = roads.getRoad(38);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(roads.getRoad(32));
        expectedRoads.add(roads.getRoad(48));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo39() {
        Road road = roads.getRoad(39);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(roads.getRoad(33));
        expectedRoads.add(roads.getRoad(40));
        expectedRoads.add(roads.getRoad(49));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn40and42and44and46() {
        final int[] ids = {40, 42, 44, 46};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(roads.getRoad(id - (6 + i)));
            expectedRoads.add(roads.getRoad(id - 1));
            expectedRoads.add(roads.getRoad(id + 1));
            expectedRoads.add(roads.getRoad(id + (9 - i)));

            List<Road> actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.size(), actualRoads.size());
            for (int j = 0; j < expectedRoads.size(); j++) {
                assertEquals(expectedRoads.get(j), actualRoads.get(j));
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn41and43and45and47() {
        final int[] ids = {41, 43, 45, 47};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(roads.getRoad(id - (7 + i)));
            expectedRoads.add(roads.getRoad(id - 1));
            expectedRoads.add(roads.getRoad(id + 1));
            expectedRoads.add(roads.getRoad(id + (9 - i)));

            List<Road> actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.size(), actualRoads.size());
            for (int j = 0; j < expectedRoads.size(); j++) {
                assertEquals(expectedRoads.get(j), actualRoads.get(j));
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo48() {
        Road road = roads.getRoad(48);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(roads.getRoad(38));
        expectedRoads.add(roads.getRoad(47));
        expectedRoads.add(roads.getRoad(53));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo49() {
        Road road = roads.getRoad(49);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(roads.getRoad(39));
        expectedRoads.add(roads.getRoad(40));
        expectedRoads.add(roads.getRoad(54));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn50and51and52() {
        final int[] ids = {50, 51, 52};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(roads.getRoad(id - (9 - i)));
            expectedRoads.add(roads.getRoad(id - (8 - i)));
            expectedRoads.add(roads.getRoad(id + (5 + i)));
            expectedRoads.add(roads.getRoad(id + (6 + i)));

            List<Road> actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.size(), actualRoads.size());
            for (int j = 0; j < expectedRoads.size(); j++) {
                assertEquals(expectedRoads.get(j), actualRoads.get(j));
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo53() {
        Road road = roads.getRoad(53);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(roads.getRoad(47));
        expectedRoads.add(roads.getRoad(48));
        expectedRoads.add(roads.getRoad(61));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo54() {
        Road road = roads.getRoad(54);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(roads.getRoad(49));
        expectedRoads.add(roads.getRoad(55));
        expectedRoads.add(roads.getRoad(62));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn55and57and59() {
        final int[] ids = {55, 57, 59};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(roads.getRoad(id - (5 + i)));
            expectedRoads.add(roads.getRoad(id - 1));
            expectedRoads.add(roads.getRoad(id + 1));
            expectedRoads.add(roads.getRoad(id + (7 - i)));

            List<Road> actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.size(), actualRoads.size());
            for (int j = 0; j < expectedRoads.size(); j++) {
                assertEquals(expectedRoads.get(j), actualRoads.get(j));
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn56and58and60() {
        final int[] ids = {56, 58, 60};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(roads.getRoad(id - (6 + i)));
            expectedRoads.add(roads.getRoad(id - 1));
            expectedRoads.add(roads.getRoad(id + 1));
            expectedRoads.add(roads.getRoad(id + (7 - i)));

            List<Road> actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.size(), actualRoads.size());
            for (int j = 0; j < expectedRoads.size(); j++) {
                assertEquals(expectedRoads.get(j), actualRoads.get(j));
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo61() {
        Road road = roads.getRoad(61);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(roads.getRoad(53));
        expectedRoads.add(roads.getRoad(60));
        expectedRoads.add(roads.getRoad(65));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo62() {
        Road road = roads.getRoad(62);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(roads.getRoad(54));
        expectedRoads.add(roads.getRoad(55));
        expectedRoads.add(roads.getRoad(66));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn63and64() {
        final int[] ids = {63, 64};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(roads.getRoad(id - (7 - i)));
            expectedRoads.add(roads.getRoad(id - (6 - i)));
            expectedRoads.add(roads.getRoad(id + (4 + i)));
            expectedRoads.add(roads.getRoad(id + (5 + i)));

            List<Road> actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.size(), actualRoads.size());
            for (int j = 0; j < expectedRoads.size(); j++) {
                assertEquals(expectedRoads.get(j), actualRoads.get(j));
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo65() {
        Road road = roads.getRoad(65);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(roads.getRoad(60));
        expectedRoads.add(roads.getRoad(61));
        expectedRoads.add(roads.getRoad(71));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo66() {
        Road road = roads.getRoad(66);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(roads.getRoad(62));
        expectedRoads.add(roads.getRoad(67));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn67and69() {
        final int[] ids = {67, 69};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(roads.getRoad(id - (4 + i)));
            expectedRoads.add(roads.getRoad(id - 1));
            expectedRoads.add(roads.getRoad(id + 1));

            List<Road> actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.size(), actualRoads.size());
            for (int j = 0; j < expectedRoads.size(); j++) {
                assertEquals(expectedRoads.get(j), actualRoads.get(j));
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn68and70() {
        final int[] ids = {68, 70};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(roads.getRoad(id - (5 + i)));
            expectedRoads.add(roads.getRoad(id - 1));
            expectedRoads.add(roads.getRoad(id + 1));

            List<Road> actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.size(), actualRoads.size());
            for (int j = 0; j < expectedRoads.size(); j++) {
                assertEquals(expectedRoads.get(j), actualRoads.get(j));
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo71() {
        Road road = roads.getRoad(71);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(roads.getRoad(65));
        expectedRoads.add(roads.getRoad(70));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsNotInitialized() {
        RoadGraph roads = new RoadGraph();
        Road testRoad = roads.getRoad(0); // get any road

        RuntimeException exception = assertThrows(RuntimeException.class, testRoad::getAdjacentRoads);

        String expectedMessage = "Road-to-Road adjacency uninitialized";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
