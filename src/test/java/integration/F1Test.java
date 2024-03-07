package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import gamedatastructures.GameBoard;
import gamedatastructures.GameType;
import gamedatastructures.Terrain;
import gamedatastructures.Tile;
import graphs.RoadGraph;
import graphs.VertexGraph;

public class F1Test {
    private static final String LAYOUT_FILE = "src/main/java/gamedatastructures/TileLayout.txt";
    private static final String VERTEX_TO_VERTEX_LAYOUT_FILE = "src/main/java/graphs/VertexToVertexLayout.txt";
    private static final String VERTEX_TO_ROAD_LAYOUT_FILE = "src/main/java/graphs/VertexToRoadLayout.txt";
    private static final String VERTEX_TO_PORT_LAYOUT_FILE = "src/main/java/graphs/VertexToPortLayout.txt";
    private static final String ROAD_TO_ROAD_LAYOUT_FILE = "src/main/java/graphs/RoadToRoadLayout.txt";
    private static final String ROAD_TO_VERTEX_LAYOUT_FILE = "src/main/java/graphs/RoadToVertexLayout.txt";
    @Test 
    public void testGenerateBoard() {
        VertexGraph vertexes = new VertexGraph();
        RoadGraph roads = new RoadGraph();
        vertexes.initializeVertexToVertexAdjacency(VERTEX_TO_VERTEX_LAYOUT_FILE);
        vertexes.initializeVertexToRoadAdjacency(roads, VERTEX_TO_ROAD_LAYOUT_FILE);
        vertexes.initializeVertexToPortAdjacency(VERTEX_TO_PORT_LAYOUT_FILE, GameType.Beginner);
        roads.initializeRoadToRoadAdjacency(ROAD_TO_ROAD_LAYOUT_FILE);
        roads.initializeRoadToVertexAdjacency(vertexes, ROAD_TO_VERTEX_LAYOUT_FILE);

        GameBoard board = new GameBoard(GameType.Beginner,LAYOUT_FILE);
        
        //Expected Terrain Order For Default
        Terrain[] expectedTerrain = {Terrain.MOUNTAINS,Terrain.PASTURE,Terrain.FORREST,Terrain.FIELDS,Terrain.HILLS,Terrain.PASTURE,Terrain.HILLS,
            Terrain.FIELDS,Terrain.FORREST, Terrain.DESERT,Terrain.FORREST,Terrain.MOUNTAINS,Terrain.FORREST,Terrain.MOUNTAINS,
            Terrain.FIELDS,Terrain.PASTURE,Terrain.HILLS,Terrain.FIELDS,Terrain.PASTURE};
        //Expected Die Values for Default
        int[] expectedDieVals =  {10,2,9,12,6,4,10,9,11,7,3,8,8,3,4,5,5,6,11};
        //Expected robber booleans for Default
        boolean[] expectedRobbers = {false,false,false,false,false,false,false,false,false,true,false,false,false,false,false,false,false,false,false};
        //Vetexes with ports
        int[] vertexWithPorts ={0,1,14,15,7,17,26,37,28,38,47,48,50,51,45,46};
       
        int i = 0;
        for(Tile tile : board.getTiles()){
            //Tiles should be ordered in default
            assertEquals(i,tile.getTileNumber());
            assertEquals(tile.getTerrain(), expectedTerrain[i]);
            assertEquals(tile.getDieNumber(), expectedDieVals[i]);
            assertEquals(tile.getHasRobber(), expectedRobbers[i]);
            //Assert that the desert tile starts with the robber
            if(i == 9){
                assertTrue(tile.getHasRobber());
            }
            else assertFalse(tile.getHasRobber());
            i++;
        }
        //Assert that the robber tile is the desert tile
        assertEquals(board.getTiles()[9],board.getRobberTile());
        assertEquals(19,i);


        //Now check each vertex for ports as exepected
        for(int vertex : vertexWithPorts) {
            assertTrue(vertexes.getVertex(vertex).hasPort());
        }
    }
}
