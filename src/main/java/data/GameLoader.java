package data;

import domain.bank.Bank;
import domain.controller.Controller;
import domain.controller.GameState;
import domain.graphs.*;
import domain.player.HarvestBooster;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import domain.devcarddeck.DevelopmentCardDeck;
import domain.game.Game;
import domain.game.GameType;
import domain.gameboard.GameBoard;
import domain.gameboard.Tile;
import domain.player.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class GameLoader {

    // Text files that need to be loaded for instantiation;
    private static final String INIT_PATH = "src/main/java/data/init/";
    private static final String ROAD_TO_ROAD_FILE = INIT_PATH + "RoadToRoadLayout.txt";
    private static final String ROAD_TO_VERTEX_FILE = INIT_PATH + "RoadToVertexLayout.txt";
    private static final String VERTEX_TO_VERTEX_FILE = INIT_PATH + "VertexToVertexLayout.txt";
    private static final String VERTEX_TO_PORT_FILE = INIT_PATH + "VertexToPortLayout.txt";
    private static final String VERTEX_TO_ROAD_FILE = INIT_PATH + "VertexToRoadLayout.txt";
    private static final String TILE_LAYOUT = INIT_PATH + "TileLayout.txt";
    public static final String DEFAULT_ICON_FOLDER_PATH = "images/default";


    // Storage constants
    private String savedGamesPath = "src/main/java/data/slots"; // default
    private static final String GLOBAL_CONFIG = "globalConfig";
    private static final String SLOT_PREFIX = "slot";
    private Integer slotNumber;
    private static final int MAXIMUM_SLOTS = 4;
    private static final String EXTENSION = ".txt";
    private static final String GAME_TYPE = "gameType";
    private static final String NUM_PLAYERS = "numPlayers";
    private static final String LANGUAGE = "language";
    private static final String MOST_RECENT_LANGUAGE = "mostRecentLanguage";
    private static final String ICON_PATH = "iconPath";

    private static GameLoader uniqueInstance = null;

    private GameboardGraph gameboardGraph;
    private GameBoard gameBoard;
    private Controller controller;
    private Player[] players;
    private GameType gameType;
    private Integer numPlayers;
    private String language;
    private String iconFolderPath;

    // Core Undo Redo stuff
    private Stack<Memento> undoStack;
    private Stack<Memento> redoStack;

    private GameLoader() {
        // restricts access
    }

    @SuppressFBWarnings("MS_EXPOSE_REP")
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
    void setSlotsPath(final String slotsPath) {
        this.savedGamesPath = slotsPath;
    }

    private void instantiateGameObjects(final GameType gameMode, final int playerCount) {
        // Game setup
        this.gameType = gameMode;
        this.numPlayers = playerCount;

        gameboardGraph = new GameboardGraph(gameMode);
        initializeGraphs(gameboardGraph);

        gameBoard = new GameBoard(gameMode);
        initializeGameBoard(gameBoard);

        Bank bank = new Bank();

        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        Game game = new Game(gameBoard, gameboardGraph, devCardDeck, bank);

        this.players = new Player[this.numPlayers];
        for (int i = 0; i < this.numPlayers; i++) {
            Player player = new Player(i + 1, new HarvestBooster(), bank);
            players[i] = player;
        }

        this.controller = new Controller(game, players, gameMode);

        // Core undo-redo setup
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    public ResourceBundle getMessageBundle() {
        if (this.language == null) {
            // Create a File object representing the base folder
            File baseFolder = new File(savedGamesPath);

            // Create a MementoReader for reading most recently used language
            MementoReader reader = new MementoReader(baseFolder, GLOBAL_CONFIG + EXTENSION);
            this.language = reader.readField(MOST_RECENT_LANGUAGE);
        }

        if (this.language.equals("English")) {
            return ResourceBundle.getBundle("i18n/messages");
        } else { // Must be spanish
            return ResourceBundle.getBundle("i18n/messages_es");
        }
    }

    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    public ResourceBundle setLanguage(final String languageSelected) throws IOException {
        this.language = languageSelected;

        // Create a File object representing the base folder
        File baseFolder = new File(savedGamesPath);

        // Create a MementoWriter for writing most recently used language
        MementoWriter writer = new MementoWriter(baseFolder, GLOBAL_CONFIG + EXTENSION);
        writer.writeField(MOST_RECENT_LANGUAGE, this.language);

        return this.getMessageBundle();
    }

    public void setIconFolderPath(final String path) throws IOException {
        this.iconFolderPath = path;
    }

    public String getIconFolderPath() {
        return this.iconFolderPath;
    }

    public void setSlot(final int slot) {

        if (slot < 1 || slot > MAXIMUM_SLOTS) {
            throw new IllegalArgumentException("Slot can only 1 - 4");
        }

        this.slotNumber = slot;
    }

    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    public boolean isSlotEmpty(final int slotNum) {
        return !(new File(savedGamesPath + "/" + SLOT_PREFIX + slotNum).exists());
    }

    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    public boolean deleteGame() throws IOException {
        assertSlotSelected();

        File slot = new File(savedGamesPath + "/" + SLOT_PREFIX + slotNumber);
        return delete(slot);
    }

    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    public static boolean delete(final File file) {
        if (!file.delete()) {
            for (File subFile : Objects.requireNonNull(file.listFiles())) {
                delete(subFile);
            }
        }

        // Now the directory is empty, so delete it
        return file.delete();
    }

    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    public boolean saveGame() throws IOException {
        assertSlotSelected();

        // Create a File object representing the base folder
        File baseFolder = new File(savedGamesPath + "/" + SLOT_PREFIX + slotNumber);
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
            writer.writeField(ICON_PATH, this.iconFolderPath);

            // Save the controllerMemento memento in the Controller folder
            File controllerFolder = writer.getSubFolder("Controller");
            undoStack.peek().save(controllerFolder);
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    public boolean hasSavedSlot() {

        for (int i = 1; i <= MAXIMUM_SLOTS; i++) {
            if (!isSlotEmpty(i)) {
                return true;
            }
        }

        return false;
    }

    @SuppressFBWarnings({"PATH_TRAVERSAL_IN", "EI_EXPOSE_REP"})
    public Controller loadGame() throws IOException {
        assertSlotSelected();

        // Create a File object representing the base folder
        File baseFolder = new File(savedGamesPath + "/" + SLOT_PREFIX + slotNumber);

        // Create a MementoReader for restoring the basic game info
        MementoReader reader = new MementoReader(baseFolder, SLOT_PREFIX + EXTENSION);
        GameType restoredGameType = GameType.valueOf(reader.readField(GAME_TYPE));
        int restoredNumPlayers = Integer.parseInt(reader.readField(NUM_PLAYERS));
        this.setLanguage(reader.readField(LANGUAGE));
        this.setIconFolderPath(reader.readField(ICON_PATH));

        // re-instantiate the game objects based on the basic game info
        this.instantiateGameObjects(restoredGameType, restoredNumPlayers);

        // Now restore all the mementos stating with the controller
        File controllerFolder = reader.getSubFolder("Controller");
        Controller.ControllerMemento controllerMemento = this.controller.new ControllerMemento(controllerFolder);
        controllerMemento.restore();

        notifyOfTurnStart();
        return this.controller;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP")
    public Controller createNewGame(final GameType gameTypeSelected, final int playerCount, final String languageSelected) throws IOException {
        assertSlotSelected();

        this.setLanguage(languageSelected);
        this.instantiateGameObjects(gameTypeSelected, playerCount);

        notifyOfTurnStart();
        return this.controller;
    }

    private void assertSlotSelected() throws IOException {
        if (slotNumber == null) {
            throw new IOException("Slot # must be set before calling IO operations");
        }
    }

    // ---------------------------------------------------------------
    //
    // Reference Getters
    //
    // ----------------------------------------------------------------

    public GameboardGraph getGameboardGraph() {
        return gameboardGraph;
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

    // --------------------------------------------------------
    //
    // Static Initialization
    //
    // --------------------------------------------------------

    public static void initializeGraphs(final GameboardGraph gameboardGraph) {
        initializeRoadToRoadAdjacency(gameboardGraph);
        initializeVertexToVertexAdjacency(gameboardGraph);
        initializeRoadToVertexAdjacency(gameboardGraph);
        initializeVertexToRoadAdjacency(gameboardGraph);
        initializeVertexToPortAdjacency(gameboardGraph);
    }

    public static void initializeGameBoard(final GameBoard gameBoard) {
        initializeTileToVertexAdjacency(gameBoard);
    }

    // Initialization of VertexGraph
    private static void initializeVertexToVertexAdjacency(final GameboardGraph gameboardGraph) {
        File vertexLayout = new File(GameLoader.VERTEX_TO_VERTEX_FILE);
        try (Scanner scanner = new Scanner(vertexLayout, StandardCharsets.UTF_8)) {
            int vertexToInitializeId = 0;
            Vertex vertexToInitialize;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(" ");
                List<Vertex> adjacentVertexes = new ArrayList<>();

                for (String value : values) {
                    int vertexId = Integer.parseInt(value);
                    adjacentVertexes.add(gameboardGraph.getVertex(vertexId));
                }

                vertexToInitialize = gameboardGraph.getVertex(vertexToInitializeId);
                vertexToInitialize.setAdjacentVertexes(adjacentVertexes);
                vertexToInitializeId++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Incorrect filename/path when initialized");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initializeVertexToRoadAdjacency(final GameboardGraph gameboardGraph) {
        File vertexToRoadLayout = new File(GameLoader.VERTEX_TO_ROAD_FILE);
        try (Scanner scanner = new Scanner(vertexToRoadLayout, StandardCharsets.UTF_8)) {
            int vertexToInitializeId = 0;
            Vertex vertexToInitialize;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(" ");
                List<Road> adjacentRoads = new ArrayList<>();

                for (String value : values) {
                    int roadId = Integer.parseInt(value);
                    adjacentRoads.add(gameboardGraph.getRoad(roadId));
                }

                vertexToInitialize = gameboardGraph.getVertex(vertexToInitializeId);
                vertexToInitialize.setAdjacentRoads(adjacentRoads);
                vertexToInitializeId++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Incorrect filename/path when initialized");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initializeVertexToPortAdjacency(final GameboardGraph gameboardGraph) {
        File vertexToRoadLayout = new File(GameLoader.VERTEX_TO_PORT_FILE);
        try (Scanner scanner = new Scanner(vertexToRoadLayout, StandardCharsets.UTF_8)) {
            int vertexToInitializeId = 0;
            Vertex vertexToInitialize;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(" ");
                vertexToInitialize = gameboardGraph.getVertex(vertexToInitializeId);

                // Parse the port id into an actual port
                Port adjacentPort = null;
                try {
                    int portId = Integer.parseInt(values[0]);
                    adjacentPort = gameboardGraph.getPort(portId);
                } catch (NumberFormatException e) {
                    // else no port indicated so we leave it null
                }

                vertexToInitialize.setAdjacentPort(adjacentPort);
                vertexToInitializeId++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Incorrect filename/path when initialized");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Initialization of RoadGraph
    private static void initializeRoadToRoadAdjacency(final GameboardGraph gameboardGraph) {
        File roadLayout = new File(GameLoader.ROAD_TO_ROAD_FILE);
        try (Scanner scanner = new Scanner(roadLayout, StandardCharsets.UTF_8)) {
            int roadToInitializeId = 0;
            Road roadToInitialize;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(" ");
                List<Road> adjacentRoads = new ArrayList<>();

                for (String value : values) {
                    int roadId = Integer.parseInt(value);
                    adjacentRoads.add(gameboardGraph.getRoad(roadId));
                }

                roadToInitialize = gameboardGraph.getRoad(roadToInitializeId);
                roadToInitialize.setAdjacentRoads(adjacentRoads);
                roadToInitializeId++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Incorrect filename/path when initialized");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initializeRoadToVertexAdjacency(final GameboardGraph gameboardGraph) {
        File roadToVertexLayout = new File(GameLoader.ROAD_TO_VERTEX_FILE);
        try (Scanner scanner = new Scanner(roadToVertexLayout, StandardCharsets.UTF_8)) {
            int roadToInitializeId = 0;
            Road roadToInitialize;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(" ");
                List<Vertex> adjacentVertexesBuffer = new ArrayList<>();

                for (String value : values) {
                    int vertexId = Integer.parseInt(value);
                    adjacentVertexesBuffer.add(gameboardGraph.getVertex(vertexId));
                }

                roadToInitialize = gameboardGraph.getRoad(roadToInitializeId);
                roadToInitialize.setAdjacentVertexes(adjacentVertexesBuffer);
                roadToInitializeId++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Incorrect filename/path when initialized");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Initialization of GameBoard
    private static void initializeTileToVertexAdjacency(final GameBoard gameBoard) {
        File tileLayout = new File(GameLoader.TILE_LAYOUT);
        try (Scanner scanner = new Scanner(tileLayout, StandardCharsets.UTF_8)) {
            int tileNum = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(" ");
                List<Integer> cornerVertexIDs = new ArrayList<>();

                for (String value : values) {
                    int vertexID = Integer.parseInt(value);
                    cornerVertexIDs.add(vertexID);
                }

                gameBoard.getTile(tileNum).setAdjacentVertexes(cornerVertexIDs);
                tileNum++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Incorrect filename/path when initialized");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // -----------------------------------------------------------
    //
    // Core Undo Repo Functionality
    //
    // -----------------------------------------------------------

    /**
     * Tells the loader that a turn has been started.
     * Will save the turn, so it can be undone later.
     * Also clears out the redo-able turns since the timeline changed.
     */
    public void notifyOfTurnStart() {
        undoStack.add(controller.createMemento());
        redoStack.clear();
    }

    /**
     * Restores the game to the beginning of the turn if in the middle of it.
     * <p>
     * Otherwise, restores the game to the beginning of the most recently completed turn,
     * and adds the turn to the redo-able turns
     *
     * @return true if there was a turn restored, false otherwise.
     */
    public boolean undo() {
        if (undoStack.size() == 1 && controller.getState() == GameState.TURN_START) {
            return false;
        }

        if (controller.getState() == GameState.TURN_START) {
            redoStack.push(undoStack.pop());
        }
        undoStack.peek().restore();
        return true;
    }

    /**
     * Restores the game to the beginning of the last turn undone.
     * Adds the restored turn to the restorable turns.
     *
     * @return true if there was a turn restored, false otherwise.
     */
    public boolean redo() {
        if (redoStack.empty()) {
            return false;
        }

        undoStack.push(redoStack.pop());
        undoStack.peek().restore();
        return true;
    }
}
