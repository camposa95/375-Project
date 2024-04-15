package integration;

import static domain.gameboard.Terrain.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import data.GameLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.gameboard.GameBoard;
import domain.game.GameType;
import domain.gameboard.Terrain;
import domain.gameboard.Tile;
import domain.graphs.RoadGraph;
import domain.graphs.VertexGraph;

public class F1Test {

    VertexGraph vertexes;
    GameBoard gameBoard;

    @BeforeEach
    public void setUpGameObjects() {
        GameType gameType = GameType.Beginner;
        vertexes = new VertexGraph(gameType);
        RoadGraph roads = new RoadGraph();
        GameLoader.initializeGraphs(roads, vertexes);

        gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
    }

    @Test 
    public void testGenerateBoard() {
        // Tile Expectations
        Terrain[] expectedTerrain = {MOUNTAINS, PASTURE, FORREST, FIELDS, HILLS, PASTURE, HILLS, FIELDS, FORREST, DESERT, FORREST, MOUNTAINS, FORREST, MOUNTAINS, FIELDS, PASTURE, HILLS, FIELDS, PASTURE};
        int[] expectedDieNumbers =  {10, 2, 9, 12, 6, 4, 10, 9, 11, 7, 3, 8, 8, 3, 4, 5, 5, 6, 11};
        boolean[] expectedRobbers = {false, false, false, false, false, false, false, false, false,true,false, false, false, false, false, false, false, false, false};
        // Vertexes with ports
        int[] vertexWithPorts ={0, 1, 14, 15, 7, 17, 26, 37, 28, 38, 47, 48, 50, 51, 45, 46};
       
        int i = 0;
        for (Tile tile : gameBoard.getTiles()) { // Tiles should be ordered in default
            assertEquals(i, tile.getTileNumber());
            assertEquals(tile.getTerrain(), expectedTerrain[i]);
            assertEquals(tile.getDieNumber(), expectedDieNumbers[i]);
            assertEquals(tile.getHasRobber(), expectedRobbers[i]);
            // Assert that the desert tile starts with the robber
            assertEquals(i == 9, tile.getHasRobber());
            i++;
        }
        assertEquals(19, i);

        // Assert that the robber tile is the desert tile
        assertEquals(gameBoard.getTiles()[9], gameBoard.getRobberTile());

        // Now check each vertex for ports as expected
        for (int vertex : vertexWithPorts) {
            assertTrue(vertexes.getVertex(vertex).hasPort());
        }
    }
}
