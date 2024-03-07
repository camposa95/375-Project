package graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class RoadToRoadAdjacencyTest {
    private static final String LAYOUT_FILE = "src/main/java/graphs/RoadToRoadLayout.txt";

    @Test
    public void testGetAdjacentRoadsTo0() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        Road road = roads.getRoad(0);

        Road[] expectedRoads = {roads.getRoad(1),
                                roads.getRoad(6)};
        Road[] actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.length);
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads[i]);
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn1and3() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        final int[] ids = {1, 3};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            Road[] expectedRoads = {roads.getRoad(id - 1),
                                    roads.getRoad(id + 1),
                                    roads.getRoad(id + (6 - i))};
            Road[] actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.length);
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads[j]);
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn2and4() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        final int[] ids = {2, 4};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            Road[] expectedRoads = {roads.getRoad(id - 1),
                                    roads.getRoad(id + 1),
                                    roads.getRoad(id + (5 - i))};
            Road[] actualRoads = road.getAdjacentRoads();
            
            assertEquals(expectedRoads.length, actualRoads.length);
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads[j]);
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo5() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        Road road = roads.getRoad(5);

        Road[] expectedRoads = {roads.getRoad(4),
                                roads.getRoad(9)};
        Road[] actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.length);
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads[i]);
        }
    }

    @Test
    public void testGetAdjacentRoadsTo6() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        Road road = roads.getRoad(6);

        Road[] expectedRoads = {roads.getRoad(0),
                                roads.getRoad(10),
                                roads.getRoad(11),};
        Road[] actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.length);
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads[i]);
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn7and8() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        final int[] ids = {7, 8};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            Road[] expectedRoads = {roads.getRoad(id - (6 - i)),
                                    roads.getRoad(id - (5 - i)),
                                    roads.getRoad(id + (5 + i)),
                                    roads.getRoad(id + (6 + i))};
            Road[] actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.length);
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads[j]);
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo9() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        Road road = roads.getRoad(9);

        Road[] expectedRoads = {roads.getRoad(5),
                                roads.getRoad(16),
                                roads.getRoad(17)};
        Road[] actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.length);
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads[i]);
        }
    }

    @Test
    public void testGetAdjacentRoadsTo10() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        Road road = roads.getRoad(10);

        Road[] expectedRoads = {roads.getRoad(6),
                                roads.getRoad(11),
                                roads.getRoad(18)};
        Road[] actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.length);
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads[i]);
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn11and13and15() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        final int[] ids = {11, 13, 15};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            Road[] expectedRoads = {roads.getRoad(id - (5 + i)),
                                    roads.getRoad(id - 1),
                                    roads.getRoad(id + 1),
                                    roads.getRoad(id + (8 - i))};
            Road[] actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.length);
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads[j]);
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn12and14and16() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        final int[] ids = {12, 14, 16};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            Road[] expectedRoads = {roads.getRoad(id - (5 + i)),
                                    roads.getRoad(id - 1),
                                    roads.getRoad(id + 1),
                                    roads.getRoad(id + (7 - i))};
            Road[] actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.length);
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads[j]);
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo17() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        Road road = roads.getRoad(17);

        Road[] expectedRoads = {roads.getRoad(9),
                                roads.getRoad(16),
                                roads.getRoad(22)};
        Road[] actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.length);
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads[i]);
        }
    }

    @Test
    public void testGetAdjacentRoadsTo18() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        Road road = roads.getRoad(18);

        Road[] expectedRoads = {roads.getRoad(10),
                                roads.getRoad(23),
                                roads.getRoad(24)};
        Road[] actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.length);
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads[i]);
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn19and20and21() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        final int[] ids = {19, 20, 21};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            Road[] expectedRoads = {roads.getRoad(id - (8 - i)),
                                    roads.getRoad(id - (7 - i)),
                                    roads.getRoad(id + (6 + i)),
                                    roads.getRoad(id + (7 + i))};
            Road[] actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.length);
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads[j]);
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo22() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        Road road = roads.getRoad(22);

        Road[] expectedRoads = {roads.getRoad(17),
                                roads.getRoad(31),
                                roads.getRoad(32)};
        Road[] actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.length);
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads[i]);
        }
    }

    @Test
    public void testGetAdjacentRoadsTo23() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        Road road = roads.getRoad(23);

        Road[] expectedRoads = {roads.getRoad(18),
                                roads.getRoad(24),
                                roads.getRoad(33)};
        Road[] actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.length);
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads[i]);
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn24and26and28and30() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        final int[] ids = {24, 26, 28, 30};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            Road[] expectedRoads = {roads.getRoad(id - (6 + i)),
                                    roads.getRoad(id - 1),
                                    roads.getRoad(id + 1),
                                    roads.getRoad(id + (10 - i))};
            Road[] actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.length);
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads[j]);
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn25and27and29and31() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        final int[] ids = {25, 27, 29, 31};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            Road[] expectedRoads = {roads.getRoad(id - (6 + i)),
                                    roads.getRoad(id - 1),
                                    roads.getRoad(id + 1),
                                    roads.getRoad(id + (9 - i))};
            Road[] actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.length);
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads[j]);
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo32() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        Road road = roads.getRoad(32);

        Road[] expectedRoads = {roads.getRoad(22),
                                roads.getRoad(31),
                                roads.getRoad(38)};
        Road[] actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.length);
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads[i]);
        }
    }

    @Test
    public void testGetAdjacentRoadsTo33() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        Road road = roads.getRoad(33);

        Road[] expectedRoads = {roads.getRoad(23),
                                roads.getRoad(39)};
        Road[] actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.length);
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads[i]);
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn34and35and36and37() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        final int[] ids = {34, 35, 36, 37};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            Road[] expectedRoads = {roads.getRoad(id - (10 - i)),
                                    roads.getRoad(id - (9 - i)),
                                    roads.getRoad(id + (6 + i)),
                                    roads.getRoad(id + (7 + i))};
            Road[] actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.length);
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads[j]);
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo38() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        Road road = roads.getRoad(38);

        Road[] expectedRoads = {roads.getRoad(32),
                                roads.getRoad(48)};
        Road[] actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.length);
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads[i]);
        }
    }

    @Test
    public void testGetAdjacentRoadsTo39() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        Road road = roads.getRoad(39);

        Road[] expectedRoads = {roads.getRoad(33),
                                roads.getRoad(40),
                                roads.getRoad(49)};
        Road[] actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.length);
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads[i]);
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn40and42and44and46() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        final int[] ids = {40, 42, 44, 46};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            Road[] expectedRoads = {roads.getRoad(id - (6 + i)),
                                    roads.getRoad(id - 1),
                                    roads.getRoad(id + 1),
                                    roads.getRoad(id + (9 - i))};
            Road[] actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.length);
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads[j]);
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn41and43and45and47() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        final int[] ids = {41, 43, 45, 47};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            Road[] expectedRoads = {roads.getRoad(id - (7 + i)),
                                    roads.getRoad(id - 1),
                                    roads.getRoad(id + 1),
                                    roads.getRoad(id + (9 - i))};
            Road[] actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.length);
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads[j]);
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo48() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        Road road = roads.getRoad(48);

        Road[] expectedRoads = {roads.getRoad(38),
                                roads.getRoad(47),
                                roads.getRoad(53)};
        Road[] actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.length);
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads[i]);
        }
    }

    @Test
    public void testGetAdjacentRoadsTo49() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        Road road = roads.getRoad(49);

        Road[] expectedRoads = {roads.getRoad(39),
                                roads.getRoad(40),
                                roads.getRoad(54)};
        Road[] actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.length);
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads[i]);
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn50and51and52() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        final int[] ids = {50, 51, 52};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            Road[] expectedRoads = {roads.getRoad(id - (9 - i)),
                                    roads.getRoad(id - (8 - i)),
                                    roads.getRoad(id + (5 + i)),
                                    roads.getRoad(id + (6 + i))};
            Road[] actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.length);
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads[j]);
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo53() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        Road road = roads.getRoad(53);

        Road[] expectedRoads = {roads.getRoad(47),
                                roads.getRoad(48),
                                roads.getRoad(61)};
        Road[] actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.length);
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads[i]);
        }
    }

    @Test
    public void testGetAdjacentRoadsTo54() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        Road road = roads.getRoad(54);

        Road[] expectedRoads = {roads.getRoad(49),
                                roads.getRoad(55),
                                roads.getRoad(62)};
        Road[] actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.length);
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads[i]);
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn55and57and59() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        final int[] ids = {55, 57, 59};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            Road[] expectedRoads = {roads.getRoad(id - (5 + i)),
                                    roads.getRoad(id - 1),
                                    roads.getRoad(id + 1),
                                    roads.getRoad(id + (7 - i))};
            Road[] actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.length);
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads[j]);
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn56and58and60() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        final int[] ids = {56, 58, 60};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            Road[] expectedRoads = {roads.getRoad(id - (6 + i)),
                                    roads.getRoad(id - 1),
                                    roads.getRoad(id + 1),
                                    roads.getRoad(id + (7 - i))};
            Road[] actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.length);
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads[j]);
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo61() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        Road road = roads.getRoad(61);

        Road[] expectedRoads = {roads.getRoad(53),
                                roads.getRoad(60),
                                roads.getRoad(65)};
        Road[] actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.length);
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads[i]);
        }
    }

    @Test
    public void testGetAdjacentRoadsTo62() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        Road road = roads.getRoad(62);

        Road[] expectedRoads = {roads.getRoad(54),
                                roads.getRoad(55),
                                roads.getRoad(66)};
        Road[] actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.length);
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads[i]);
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn63and64() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        final int[] ids = {63, 64};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            Road[] expectedRoads = {roads.getRoad(id - (7 - i)),
                                    roads.getRoad(id - (6 - i)),
                                    roads.getRoad(id + (4 + i)),
                                    roads.getRoad(id + (5 + i))};
            Road[] actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.length);
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads[j]);
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo65() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        Road road = roads.getRoad(65);

        Road[] expectedRoads = {roads.getRoad(60),
                                roads.getRoad(61),
                                roads.getRoad(71)};
        Road[] actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.length);
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads[i]);
        }
    }

    @Test
    public void testGetAdjacentRoadsTo66() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        Road road = roads.getRoad(66);

        Road[] expectedRoads = {roads.getRoad(62),
                                roads.getRoad(67)};
        Road[] actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.length);
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads[i]);
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn67and69() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        final int[] ids = {67, 69};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            Road[] expectedRoads = {roads.getRoad(id - (4 + i)),
                                    roads.getRoad(id - 1),
                                    roads.getRoad(id + 1)};
            Road[] actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.length);
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads[j]);
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsToNIn68and70() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        final int[] ids = {68, 70};

        int i = 0;
        for (int id: ids) {
            Road road = roads.getRoad(id);

            Road[] expectedRoads = {roads.getRoad(id - (5 + i)),
                                    roads.getRoad(id - 1),
                                    roads.getRoad(id + 1)};
            Road[] actualRoads = road.getAdjacentRoads();

            assertEquals(expectedRoads.length, actualRoads.length);
            for (int j = 0; j < expectedRoads.length; j++) {
                assertEquals(expectedRoads[j], actualRoads[j]);
            }

            i++;
        }
    }

    @Test
    public void testGetAdjacentRoadsTo71() {
        RoadGraph roads = new RoadGraph();
        roads.initializeRoadToRoadAdjacency(LAYOUT_FILE);
        Road road = roads.getRoad(71);

        Road[] expectedRoads = {roads.getRoad(65),
                                roads.getRoad(70)};
        Road[] actualRoads = road.getAdjacentRoads();

        assertEquals(expectedRoads.length, actualRoads.length);
        for (int i = 0; i < expectedRoads.length; i++) {
            assertEquals(expectedRoads[i], actualRoads[i]);
        }
    }

    @Test
    public void testGetAdjacentRoadsInitializedIncorrectly() {
        RoadGraph roads = new RoadGraph();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            roads.initializeRoadToRoadAdjacency("blah blah blah");
        });

        String expectedMessage = "Incorrect filename/path when initialized";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetAdjacentRoadsNotInitialized() {
        RoadGraph roads = new RoadGraph();
        Road testRoad = roads.getRoad(0); // get any road

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            testRoad.getAdjacentRoads();
        });

        String expectedMessage = "Road-to-Road adjacency uninitialized";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
