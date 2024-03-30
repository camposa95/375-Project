package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import data.GameLoader;
import org.junit.jupiter.api.Test;

import domain.gameboard.GameBoard;
import domain.game.GameType;
import domain.gameboard.Terrain;
import domain.gameboard.Tile;
import domain.graphs.RoadGraph;
import domain.graphs.VertexGraph;

public class F1Test {
    @Test 
    public void testGenerateBoard() {
        GameType gameType = GameType.Beginner;
        VertexGraph vertexes = new VertexGraph(gameType);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        
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
        for(Tile tile : gameBoard.getTiles()){
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
        assertEquals(gameBoard.getTiles()[9], gameBoard.getRobberTile());
        assertEquals(19,i);


        //Now check each vertex for ports as exepected
        for(int vertex : vertexWithPorts) {
            assertTrue(vertexes.getVertex(vertex).hasPort());
        }
    }
}
