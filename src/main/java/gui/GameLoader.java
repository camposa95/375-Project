package gui;

import controller.Controller;
import gamedatastructures.*;
import graphs.RoadGraph;
import graphs.VertexGraph;

public class GameLoader {

    // Text files that need to be loaded:
    private static final String ROAD_TO_ROAD_FILE = "src/main/java/graphs/RoadToRoadLayout.txt";
    private static final String ROAD_TO_VERTEX_FILE = "src/main/java/graphs/RoadToVertexLayout.txt";
    private static final String VERTEX_TO_VERTEX_FILE = "src/main/java/graphs/VertexToVertexLayout.txt";
    private static final String VERTEX_TO_PORT_FILE = "src/main/java/graphs/VertexToPortLayout.txt";
    private static final String VERTEX_TO_ROAD_FILE = "src/main/java/graphs/VertexToRoadLayout.txt";
    private static final String TILE_LAYOUT = "src/main/java/gamedatastructures/TileLayout.txt";

    // temp fields to help with loading stuff.
    private static VertexGraph vertexGraph;
    private static GameBoard gameBoard;

    public static Controller instantiateGameObjects(GameType gameType, int numPlayers) {
        RoadGraph roadGraph = new RoadGraph();
        vertexGraph = new VertexGraph();
        roadGraph.initializeRoadToRoadAdjacency(ROAD_TO_ROAD_FILE);
        vertexGraph.initializeVertexToVertexAdjacency(VERTEX_TO_VERTEX_FILE);
        roadGraph.initializeRoadToVertexAdjacency(vertexGraph, ROAD_TO_VERTEX_FILE);
        vertexGraph.initializeVertexToRoadAdjacency(roadGraph, VERTEX_TO_ROAD_FILE);
        vertexGraph.initializeVertexToPortAdjacency(VERTEX_TO_PORT_FILE, gameType);

        gameBoard = new GameBoard(gameType, TILE_LAYOUT);
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        Game game = new Game(gameBoard, vertexGraph, roadGraph, devCardDeck);

        Player[] players = new Player[numPlayers];
        for(int i = 0; i < numPlayers; i++){
            Player player = new Player(i+1);
            players[i] = player;
        }

       return new Controller(game, players, gameType);
    }

    public static VertexGraph getVertexGraph() {
        return vertexGraph;
    }

    public static Tile[] getTiles() {
        return gameBoard.getTiles();
    }
}
