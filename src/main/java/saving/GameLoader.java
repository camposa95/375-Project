package saving;

import controller.Controller;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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


    // Basic save slot for now
    private static final String BASE_FOLDER_PATH = "src/main/java/SavingAndLoading/SavedGames/slot1";

    private static GameLoader uniqueInstance = null;

    private VertexGraph vertexGraph;
    private GameBoard gameBoard;
    private Controller controller;
    private Player[] players;

    private GameLoader() {
        // restricts access
    }

    public static synchronized GameLoader getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new GameLoader();
        }
        return uniqueInstance;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP")
    public Controller instantiateGameObjects(final GameType gameType, final int numPlayers) {
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

        this.players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player(i + 1);
            players[i] = player;
        }

        controller = new Controller(game, players, gameType);

        return controller;
    }

    public boolean saveGame() {
        // Create a File object representing the base folder
        File baseFolder = new File(BASE_FOLDER_PATH);

        // Create a MementoWriter for writing memento data
        MementoWriter writer = null;
        try {
            writer = new MementoWriter(baseFolder, "slot1.txt");

            // Save the controllerMemento memento in the Controller folder
            File controllerFolder = writer.getSubFolder("Controller");
            Memento controllerMemento = this.controller.createMemento();
            controllerMemento.save(controllerFolder);

            // Save the bank memento in the Bank folder
            File bankFolder = writer.getSubFolder("Bank");
            Memento bank = Bank.getInstance().createMemento();
            bank.save(bankFolder);
        } catch (SaveException e) {
            return false;
        }

        return true;
    }

    public void loadGame() {
        // Create a File object representing the base folder
        File baseFolder = new File(BASE_FOLDER_PATH);

        // Create a MementoReader for reading memento data
        MementoReader reader = new MementoReader(baseFolder, "slot1.txt");

        // Save the controllerMemento memento in the Controller folder
        File controllerFolder = reader.getSubFolder("Controller");
        Controller.ControllerMemento controllerMemento = this.controller.new ControllerMemento(controllerFolder);
        controllerMemento.restore();

        // Save the bank memento in the Bank folder
        File bankFolder = reader.getSubFolder("Bank");
        Bank.BankMemento bankMemento = Bank.getInstance().new BankMemento(bankFolder);
        bankMemento.restore();
    }

    public VertexGraph getVertexGraph() {
        return vertexGraph;
    }

    public Tile[] getTiles() {
        return gameBoard.getTiles();
    }

    public Player getPlayerByNum(final int num) {
        for (Player player : this.players) {
            if (player.getPlayerNum() == num) {
                return player;
            }
        }
        throw new IllegalArgumentException("Player" + num + " not found.");
    }
}
