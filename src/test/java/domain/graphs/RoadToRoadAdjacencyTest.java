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

    GameboardGraph gameboardGraph;

    @BeforeEach
    public void setup() {
        gameboardGraph = new GameboardGraph(GameType.Beginner);
        GameLoader.initializeGraphs(gameboardGraph);
    }

    @Test
    public void testGetAdjacentRoadsTo0() {
        Road road = gameboardGraph.getRoad(0);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(gameboardGraph.getRoad(1));
        expectedRoads.add(gameboardGraph.getRoad(6));

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
            Road road = gameboardGraph.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(gameboardGraph.getRoad(id - 1));
            expectedRoads.add(gameboardGraph.getRoad(id + 1));
            expectedRoads.add(gameboardGraph.getRoad(id + (6 - i)));


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
            Road road = gameboardGraph.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(gameboardGraph.getRoad(id - 1));
            expectedRoads.add(gameboardGraph.getRoad(id + 1));
            expectedRoads.add(gameboardGraph.getRoad(id + (5 - i)));


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
        Road road = gameboardGraph.getRoad(5);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(gameboardGraph.getRoad(4));
        expectedRoads.add(gameboardGraph.getRoad(9));


        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo6() {
        Road road = gameboardGraph.getRoad(6);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(gameboardGraph.getRoad(0));
        expectedRoads.add(gameboardGraph.getRoad(10));
        expectedRoads.add(gameboardGraph.getRoad(11));


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
            Road road = gameboardGraph.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(gameboardGraph.getRoad(id - (6 - i)));
            expectedRoads.add(gameboardGraph.getRoad(id - (5 - i)));
            expectedRoads.add(gameboardGraph.getRoad(id + (5 + i)));
            expectedRoads.add(gameboardGraph.getRoad(id + (6 + i)));


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
        Road road = gameboardGraph.getRoad(9);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(gameboardGraph.getRoad(5));
        expectedRoads.add(gameboardGraph.getRoad(16));
        expectedRoads.add(gameboardGraph.getRoad(17));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo10() {
        Road road = gameboardGraph.getRoad(10);


        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(gameboardGraph.getRoad(6));
        expectedRoads.add(gameboardGraph.getRoad(11));
        expectedRoads.add(gameboardGraph.getRoad(18));

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
            Road road = gameboardGraph.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(gameboardGraph.getRoad(id - (5 + i)));
            expectedRoads.add(gameboardGraph.getRoad(id - 1));
            expectedRoads.add(gameboardGraph.getRoad(id + 1));
            expectedRoads.add(gameboardGraph.getRoad(id + (8 - i)));

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
            Road road = gameboardGraph.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(gameboardGraph.getRoad(id - (5 + i)));
            expectedRoads.add(gameboardGraph.getRoad(id - 1));
            expectedRoads.add(gameboardGraph.getRoad(id + 1));
            expectedRoads.add(gameboardGraph.getRoad(id + (7 - i)));

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
        Road road = gameboardGraph.getRoad(17);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(gameboardGraph.getRoad(9));
        expectedRoads.add(gameboardGraph.getRoad(16));
        expectedRoads.add(gameboardGraph.getRoad(22));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo18() {
        Road road = gameboardGraph.getRoad(18);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(gameboardGraph.getRoad(10));
        expectedRoads.add(gameboardGraph.getRoad(23));
        expectedRoads.add(gameboardGraph.getRoad(24));

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
            Road road = gameboardGraph.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(gameboardGraph.getRoad(id - (8 - i)));
            expectedRoads.add(gameboardGraph.getRoad(id - (7 - i)));
            expectedRoads.add(gameboardGraph.getRoad(id + (6 + i)));
            expectedRoads.add(gameboardGraph.getRoad(id + (7 + i)));


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
        Road road = gameboardGraph.getRoad(22);


        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(gameboardGraph.getRoad(17));
        expectedRoads.add(gameboardGraph.getRoad(31));
        expectedRoads.add(gameboardGraph.getRoad(32));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo23() {
        Road road = gameboardGraph.getRoad(23);


        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(gameboardGraph.getRoad(18));
        expectedRoads.add(gameboardGraph.getRoad(24));
        expectedRoads.add(gameboardGraph.getRoad(33));

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
            Road road = gameboardGraph.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(gameboardGraph.getRoad(id - (6 + i)));
            expectedRoads.add(gameboardGraph.getRoad(id - 1));
            expectedRoads.add(gameboardGraph.getRoad(id + 1));
            expectedRoads.add(gameboardGraph.getRoad(id + (10 - i)));


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
            Road road = gameboardGraph.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(gameboardGraph.getRoad(id - (6 + i)));
            expectedRoads.add(gameboardGraph.getRoad(id - 1));
            expectedRoads.add(gameboardGraph.getRoad(id + 1));
            expectedRoads.add(gameboardGraph.getRoad(id + (9 - i)));

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
        Road road = gameboardGraph.getRoad(32);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(gameboardGraph.getRoad(22));
        expectedRoads.add(gameboardGraph.getRoad(31));
        expectedRoads.add(gameboardGraph.getRoad(38));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo33() {
        Road road = gameboardGraph.getRoad(33);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(gameboardGraph.getRoad(23));
        expectedRoads.add(gameboardGraph.getRoad(39));

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
            Road road = gameboardGraph.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(gameboardGraph.getRoad(id - (10 - i)));
            expectedRoads.add(gameboardGraph.getRoad(id - (9 - i)));
            expectedRoads.add(gameboardGraph.getRoad(id + (6 + i)));
            expectedRoads.add(gameboardGraph.getRoad(id + (7 + i)));

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
        Road road = gameboardGraph.getRoad(38);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(gameboardGraph.getRoad(32));
        expectedRoads.add(gameboardGraph.getRoad(48));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo39() {
        Road road = gameboardGraph.getRoad(39);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(gameboardGraph.getRoad(33));
        expectedRoads.add(gameboardGraph.getRoad(40));
        expectedRoads.add(gameboardGraph.getRoad(49));

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
            Road road = gameboardGraph.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(gameboardGraph.getRoad(id - (6 + i)));
            expectedRoads.add(gameboardGraph.getRoad(id - 1));
            expectedRoads.add(gameboardGraph.getRoad(id + 1));
            expectedRoads.add(gameboardGraph.getRoad(id + (9 - i)));

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
            Road road = gameboardGraph.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(gameboardGraph.getRoad(id - (7 + i)));
            expectedRoads.add(gameboardGraph.getRoad(id - 1));
            expectedRoads.add(gameboardGraph.getRoad(id + 1));
            expectedRoads.add(gameboardGraph.getRoad(id + (9 - i)));

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
        Road road = gameboardGraph.getRoad(48);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(gameboardGraph.getRoad(38));
        expectedRoads.add(gameboardGraph.getRoad(47));
        expectedRoads.add(gameboardGraph.getRoad(53));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo49() {
        Road road = gameboardGraph.getRoad(49);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(gameboardGraph.getRoad(39));
        expectedRoads.add(gameboardGraph.getRoad(40));
        expectedRoads.add(gameboardGraph.getRoad(54));

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
            Road road = gameboardGraph.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(gameboardGraph.getRoad(id - (9 - i)));
            expectedRoads.add(gameboardGraph.getRoad(id - (8 - i)));
            expectedRoads.add(gameboardGraph.getRoad(id + (5 + i)));
            expectedRoads.add(gameboardGraph.getRoad(id + (6 + i)));

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
        Road road = gameboardGraph.getRoad(53);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(gameboardGraph.getRoad(47));
        expectedRoads.add(gameboardGraph.getRoad(48));
        expectedRoads.add(gameboardGraph.getRoad(61));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo54() {
        Road road = gameboardGraph.getRoad(54);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(gameboardGraph.getRoad(49));
        expectedRoads.add(gameboardGraph.getRoad(55));
        expectedRoads.add(gameboardGraph.getRoad(62));

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
            Road road = gameboardGraph.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(gameboardGraph.getRoad(id - (5 + i)));
            expectedRoads.add(gameboardGraph.getRoad(id - 1));
            expectedRoads.add(gameboardGraph.getRoad(id + 1));
            expectedRoads.add(gameboardGraph.getRoad(id + (7 - i)));

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
            Road road = gameboardGraph.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(gameboardGraph.getRoad(id - (6 + i)));
            expectedRoads.add(gameboardGraph.getRoad(id - 1));
            expectedRoads.add(gameboardGraph.getRoad(id + 1));
            expectedRoads.add(gameboardGraph.getRoad(id + (7 - i)));

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
        Road road = gameboardGraph.getRoad(61);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(gameboardGraph.getRoad(53));
        expectedRoads.add(gameboardGraph.getRoad(60));
        expectedRoads.add(gameboardGraph.getRoad(65));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo62() {
        Road road = gameboardGraph.getRoad(62);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(gameboardGraph.getRoad(54));
        expectedRoads.add(gameboardGraph.getRoad(55));
        expectedRoads.add(gameboardGraph.getRoad(66));

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
            Road road = gameboardGraph.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(gameboardGraph.getRoad(id - (7 - i)));
            expectedRoads.add(gameboardGraph.getRoad(id - (6 - i)));
            expectedRoads.add(gameboardGraph.getRoad(id + (4 + i)));
            expectedRoads.add(gameboardGraph.getRoad(id + (5 + i)));

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
        Road road = gameboardGraph.getRoad(65);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(gameboardGraph.getRoad(60));
        expectedRoads.add(gameboardGraph.getRoad(61));
        expectedRoads.add(gameboardGraph.getRoad(71));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsTo66() {
        Road road = gameboardGraph.getRoad(66);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(gameboardGraph.getRoad(62));
        expectedRoads.add(gameboardGraph.getRoad(67));

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
            Road road = gameboardGraph.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(gameboardGraph.getRoad(id - (4 + i)));
            expectedRoads.add(gameboardGraph.getRoad(id - 1));
            expectedRoads.add(gameboardGraph.getRoad(id + 1));

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
            Road road = gameboardGraph.getRoad(id);

            List<Road> expectedRoads = new ArrayList<>();
            expectedRoads.add(gameboardGraph.getRoad(id - (5 + i)));
            expectedRoads.add(gameboardGraph.getRoad(id - 1));
            expectedRoads.add(gameboardGraph.getRoad(id + 1));

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
        Road road = gameboardGraph.getRoad(71);

        List<Road> expectedRoads = new ArrayList<>();
        expectedRoads.add(gameboardGraph.getRoad(65));
        expectedRoads.add(gameboardGraph.getRoad(70));

        List<Road> actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.size(), actualRoads.size());
        for (int i = 0; i < expectedRoads.size(); i++) {
            assertEquals(expectedRoads.get(i), actualRoads.get(i));
        }
    }

    @Test
    public void testGetAdjacentRoadsNotInitialized() {
        gameboardGraph = new GameboardGraph(GameType.Beginner);
        Road testRoad = gameboardGraph.getRoad(0); // get any road

        RuntimeException exception = assertThrows(RuntimeException.class, testRoad::getAdjacentRoads);

        String expectedMessage = "Road-to-Road adjacency uninitialized";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
