package domain.gameboard;
import data.GameLoader;
import domain.bank.Resource;
import domain.game.GameType;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class GameBoardTest {
    @Test 
    public void test_GenerateDefaultTiles(){
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);
        
        Terrain[] expectedTerrain = {Terrain.MOUNTAINS,Terrain.PASTURE,Terrain.FORREST,Terrain.FIELDS,Terrain.HILLS,Terrain.PASTURE,Terrain.HILLS,
            Terrain.FIELDS,Terrain.FORREST, Terrain.DESERT,Terrain.FORREST,Terrain.MOUNTAINS,Terrain.FORREST,Terrain.MOUNTAINS,
            Terrain.FIELDS,Terrain.PASTURE,Terrain.HILLS,Terrain.FIELDS,Terrain.PASTURE};
        int[] expectedDieNumbers =  {10,2,9,12,6,4,10,9,11,7,3,8,8,3,4,5,5,6,11};
        boolean[] expectedRobbers = {false,false,false,false,false,false,false,false,false,true,false,false,false,false,false,false,false,false,false};
       
        int i = 0;
        for(Tile tile : gameBoard.getTiles()){
            assertEquals(i,tile.getTileNumber());
            assertEquals(tile.getTerrain(), expectedTerrain[i]);
            assertEquals(tile.getDieNumber(), expectedDieNumbers[i]);
            assertEquals(tile.getHasRobber(), expectedRobbers[i]);
            if(i == 9){
                assertTrue(tile.getHasRobber());
            }
            else assertFalse(tile.getHasRobber());
            i++;
        }
        assertEquals(gameBoard.getTiles()[9], gameBoard.getRobberTile());
        assertEquals(19,i);

    }
    
    @Test
    public void test_GenerateRandomTiles(){
        GameBoard basicBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(basicBoard);

        GameBoard randomBoard = new GameBoard(GameType.Advanced);
        GameLoader.initializeGameBoard(randomBoard);

        assertEquals(basicBoard.getTiles().length, randomBoard.getTiles().length);
        boolean sameTiles = true;
        boolean sameDie = true;
        for(int i = 0;i<basicBoard.getTiles().length;i++){
            Tile basic = basicBoard.getTiles()[i];
            Tile random = randomBoard.getTiles()[i];
            if(basic.getTerrain() != random.getTerrain()){
               sameTiles = false;
            }
            if(basic.getDieNumber() != random.getDieNumber()){
                sameDie = false;
            }

        }
        assertFalse(sameTiles);
        assertFalse(sameDie);
    }
    
    @Test
    public void test_GetCornerVertexIDs() {
    int[][] expectedIDs = {{0, 1, 2, 8, 9, 10},
    {2, 3, 4, 10,11, 12},{4, 5, 6, 12,13, 14},{7, 8, 9, 7, 18, 19},
    {9, 10,  11, 19, 20, 21},{11, 12, 13, 21, 22, 23},{13, 14, 15, 23, 24, 25},
    {16, 17, 18, 27, 28, 29},{18, 19, 20, 29, 30, 31},{20, 21, 22, 31, 32, 33},
    {22, 23, 24, 33, 34, 35},{24, 25, 26, 35, 36, 37},{28, 29, 30, 38, 39, 40},
    {30, 31, 32, 40, 41, 42},{32, 33, 34, 42, 43, 44},{34, 35, 36, 44, 45, 46},
    {39, 40, 41, 47, 48, 49},{41, 42, 43, 49, 50, 51},{43, 44, 45, 51, 52, 53}};

    GameBoard gameBoard = new GameBoard(GameType.Beginner);
    GameLoader.initializeGameBoard(gameBoard);
    for (int tileNum = 0; tileNum < 19; tileNum++){
        List<Integer> ActualIDs = gameBoard.getTileVertexIDs(tileNum);
        //If needed: System.out.println("TILE["+tileNum+"]: "+ Arrays.toString(ActualIDs));

        for(int i = 0; i < 6 ; i++){
            assertEquals(expectedIDs[tileNum][i], ActualIDs.get(i));
        }
    }}

    @Test
    public void test_getResourceFromTerrain(){
        Tile tile1 = new Tile(Terrain.DESERT, 0, 0, false);
        Tile tile2 = new Tile(Terrain.FIELDS, 0, 0, false);
        Tile tile3= new Tile(Terrain.FORREST, 0, 0, false);
        Tile tile4 = new Tile(Terrain.HILLS, 0, 0, false);
        Tile tile5 = new Tile(Terrain.MOUNTAINS, 0, 0, false);
        Tile tile6 = new Tile(Terrain.PASTURE, 0, 0, false);
        assertNull(tile1.getResource());
        assertEquals(Resource.GRAIN,tile2.getResource());
        assertEquals(Resource.LUMBER,tile3.getResource());
        assertEquals(Resource.BRICK,tile4.getResource());
        assertEquals(Resource.ORE,tile5.getResource());
        assertEquals(Resource.WOOL,tile6.getResource());
    }

    @Test void testSetRobberTile() {
        Tile tile = new Tile(Terrain.DESERT, 0, 0, false);
        assertFalse(tile.getHasRobber());
        tile.setRobber(true);
        assertTrue(tile.getHasRobber());
        tile.setRobber(false);
        assertFalse(tile.getHasRobber());
    }

    @Test
    public void robberTests() {
        GameBoard gameBoard = new GameBoard(GameType.Beginner);
        GameLoader.initializeGameBoard(gameBoard);

        Tile mockedTile = EasyMock.createStrictMock(Tile.class);
        gameBoard.setRobberTile(mockedTile);
        assertEquals(mockedTile,gameBoard.getRobberTile());
    }

}
