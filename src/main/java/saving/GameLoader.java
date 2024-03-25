package saving;

import controller.Controller;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import gamedatastructures.*;
import graphs.RoadGraph;
import graphs.VertexGraph;

import java.io.File;
import java.util.ResourceBundle;

public class GameLoader {

    // Text files that need to be loaded for instantiation;
    private static final String ROAD_TO_ROAD_FILE = "src/main/java/graphs/RoadToRoadLayout.txt";
    private static final String ROAD_TO_VERTEX_FILE = "src/main/java/graphs/RoadToVertexLayout.txt";
    private static final String VERTEX_TO_VERTEX_FILE = "src/main/java/graphs/VertexToVertexLayout.txt";
    private static final String VERTEX_TO_PORT_FILE = "src/main/java/graphs/VertexToPortLayout.txt";
    private static final String VERTEX_TO_ROAD_FILE = "src/main/java/graphs/VertexToRoadLayout.txt";
    private static final String TILE_LAYOUT = "src/main/java/gamedatastructures/TileLayout.txt";


    // Storage constants
    private String savedGamesPath = "src/main/java/saving/slots";
    private static final String SLOT_PREFIX = "slot";
    private static final String EXTENSION = ".txt";
    private static final String GAME_TYPE = "gameType";
    private static final String NUM_PLAYERS = "numPlayers";
    private static final String LANGUAGE = "language";

    private static GameLoader uniqueInstance = null;

    private VertexGraph vertexGraph;
    private RoadGraph roadGraph;
    private GameBoard gameBoard;
    private Controller controller;
    private Player[] players;
    private GameType gameType;
    private int numPlayers;

    private String language;

    private GameLoader() {
        // restricts access
    }

    @SuppressFBWarnings("EI_EXPOSE_REP")
    public static synchronized GameLoader getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new GameLoader();
        }
        return uniqueInstance;
    }

    /**
     * Only for testing purposes to set where to save and load from
     */
    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    public void setSlotsPath(final String slotsPath) {
        this.savedGamesPath = slotsPath;
    }

    private void instantiateGameObjects(final GameType gameMode, final int playerCount) {
        this.gameType = gameMode;
        this.numPlayers = playerCount;

        roadGraph = new RoadGraph();
        vertexGraph = new VertexGraph();
        roadGraph.initializeRoadToRoadAdjacency(ROAD_TO_ROAD_FILE);
        vertexGraph.initializeVertexToVertexAdjacency(VERTEX_TO_VERTEX_FILE);
        roadGraph.initializeRoadToVertexAdjacency(vertexGraph, ROAD_TO_VERTEX_FILE);
        vertexGraph.initializeVertexToRoadAdjacency(roadGraph, VERTEX_TO_ROAD_FILE);
        vertexGraph.initializeVertexToPortAdjacency(VERTEX_TO_PORT_FILE, this.gameType);

        gameBoard = new GameBoard(gameMode, TILE_LAYOUT);
        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        Game game = new Game(gameBoard, vertexGraph, roadGraph, devCardDeck);

        this.players = new Player[this.numPlayers];
        for (int i = 0; i < this.numPlayers; i++) {
            Player player = new Player(i + 1);
            players[i] = player;
        }

        this.controller = new Controller(game, players, gameMode);
    }

    public ResourceBundle getMessageBundle() {
        if (this.language.equals("English")) {
            return ResourceBundle.getBundle("i18n/messages");
        } else { // Must be spanish
            return ResourceBundle.getBundle("i18n/messages_es");
        }
    }

    public boolean saveGame() {
        // Create a File object representing the base folder
        File baseFolder = new File(savedGamesPath + "/" + SLOT_PREFIX + 1);
        if (!baseFolder.exists()) {
            if (!baseFolder.mkdirs()) {
                return false;
            }
        }

        // Create a MementoWriter for writing memento data
        try {
            // write the basic game info to the file
            MementoWriter writer = new MementoWriter(baseFolder, SLOT_PREFIX + EXTENSION);
            writer.writeField(GAME_TYPE, String.valueOf(this.gameType));
            writer.writeField(NUM_PLAYERS, String.valueOf(this.numPlayers));
            writer.writeField(LANGUAGE, this.language);

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

    public boolean hasSavedSlot() {
        File folder = new File(savedGamesPath);
        File[] files = folder.listFiles();

        // Check if the folder is has files other than the .gitkeep
        return (files != null && files.length > 1);
    }

    @SuppressFBWarnings("EI_EXPOSE_REP")
    public Controller loadGame() {
        // Create a File object representing the base folder
        File baseFolder = new File(savedGamesPath + "/" + SLOT_PREFIX + 1);

        // Create a MementoReader for restoring the basic game info
        MementoReader reader = new MementoReader(baseFolder, SLOT_PREFIX + EXTENSION);
        GameType restoredGameType = GameType.valueOf(reader.readField(GAME_TYPE));
        int restoredNumPlayers = Integer.parseInt(reader.readField(NUM_PLAYERS));
        this.language = reader.readField(LANGUAGE);

        // re-instantiate the game objects based on the basic game info
        this.instantiateGameObjects(restoredGameType, restoredNumPlayers);

        // Now restore all the mementos stating with the controller
        File controllerFolder = reader.getSubFolder("Controller");
        Controller.ControllerMemento controllerMemento = this.controller.new ControllerMemento(controllerFolder);
        controllerMemento.restore();

        // and bank root objects
        File bankFolder = reader.getSubFolder("Bank");
        Bank.BankMemento bankMemento = Bank.getInstance().new BankMemento(bankFolder);
        bankMemento.restore();

        return this.controller;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP")
    public Controller createNewGame(final GameType gameTypeSelected, final int playerCount, final String languageSelected) {
        this.language = languageSelected;
        this.instantiateGameObjects(gameTypeSelected, playerCount);
        return this.controller;
    }

    // ---------------------------------------------------------------
    //
    // Reference Getters
    //
    // ----------------------------------------------------------------

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

    public int getNumPlayers() {
        return this.numPlayers;
    }

    public RoadGraph getRoadGraph() {
        return roadGraph;
    }
}
