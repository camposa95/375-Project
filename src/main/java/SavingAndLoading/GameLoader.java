package SavingAndLoading;

import controller.Controller;
import gamedatastructures.*;
import graphs.RoadGraph;
import graphs.VertexGraph;

import java.io.File;

public class GameLoader {

    // Text files that need to be loaded for instantiation;
    private static final String ROAD_TO_ROAD_FILE = "src/main/java/graphs/RoadToRoadLayout.txt";
    private static final String ROAD_TO_VERTEX_FILE = "src/main/java/graphs/RoadToVertexLayout.txt";
    private static final String VERTEX_TO_VERTEX_FILE = "src/main/java/graphs/VertexToVertexLayout.txt";
    private static final String VERTEX_TO_PORT_FILE = "src/main/java/graphs/VertexToPortLayout.txt";
    private static final String VERTEX_TO_ROAD_FILE = "src/main/java/graphs/VertexToRoadLayout.txt";
    private static final String TILE_LAYOUT = "src/main/java/gamedatastructures/TileLayout.txt";

    private static GameLoader uniqueInstance = null;

    private VertexGraph vertexGraph;
    private GameBoard gameBoard;
    private Controller controller;

    private GameLoader() {
        // restricts access
    }

    public static synchronized GameLoader getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new GameLoader();
        }
        return uniqueInstance;
    }

    public Controller instantiateGameObjects(GameType gameType, int numPlayers) {
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

        controller = new Controller(game, players, gameType);

        return controller;
    }

    public void saveGame() {
        // Define the base folder path
        String baseFolderPath = "src/main/java/SavingAndLoading/SavedGames/slot1";

        // Create a File object representing the base folder
        File baseFolder = new File(baseFolderPath);

        // Create a MementoWriter for writing memento data
        MementoWriter writer = new MementoWriter(baseFolder, "slot1.txt");

        // Save the root memento in the root folder
        File rootFolder = writer.getSubFolder("Controller");
        Memento root = this.controller.createMemento();
        root.save(rootFolder);

        // Save the bank memento in the bank folder
        File bankFolder = writer.getSubFolder("Bank");
        Memento bank = Bank.getInstance().createMemento();
        bank.save(bankFolder);
    }

    public void loadGame() {

    }

    public VertexGraph getVertexGraph() {
        return vertexGraph;
    }

    public Tile[] getTiles() {
        return gameBoard.getTiles();
    }
}
