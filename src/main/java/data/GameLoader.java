package data;

import domain.bank.Bank;
import domain.controller.Controller;
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


    // Storage constants
    private String savedGamesPath = "src/main/java/data/slots";
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
    public void setSlotsPath(final String slotsPath) {
        this.savedGamesPath = slotsPath;
    }

    private void instantiateGameObjects(final GameType gameMode, final int playerCount) {
        this.gameType = gameMode;
        this.numPlayers = playerCount;

        roadGraph = new RoadGraph();
        vertexGraph = new VertexGraph(gameMode);
        initializeGraphs(roadGraph, vertexGraph);

        gameBoard = new GameBoard(gameMode);
        initializeGameBoard(gameBoard);

        Bank bank = new Bank();

        DevelopmentCardDeck devCardDeck = new DevelopmentCardDeck();
        Game game = new Game(gameBoard, vertexGraph, roadGraph, devCardDeck, bank);

        this.players = new Player[this.numPlayers];
        for (int i = 0; i < this.numPlayers; i++) {
            Player player = new Player(i + 1, new HarvestBooster(), bank);
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

    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
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
        } catch (SaveException e) {
            return false;
        }

        return true;
    }

    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    public boolean hasSavedSlot() {
        File folder = new File(savedGamesPath);
        File[] files = folder.listFiles();

        // Check if the folder is has files other than the .git-keep
        return (files != null && files.length > 1);
    }

    @SuppressFBWarnings({"PATH_TRAVERSAL_IN", "EI_EXPOSE_REP"})
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

    public static void initializeGraphs(final RoadGraph roadGraph, final VertexGraph vertexGraph) {
        initializeRoadToRoadAdjacency(roadGraph);
        initializeVertexToVertexAdjacency(vertexGraph);
        initializeRoadToVertexAdjacency(roadGraph, vertexGraph);
        initializeVertexToRoadAdjacency(vertexGraph, roadGraph);
        initializeVertexToPortAdjacency(vertexGraph);
    }

    public static void initializeGameBoard(final GameBoard gameBoard) {
        initializeTileToVertexAdjacency(gameBoard);
    }

    // --------------------------------------------------------
    //
    // Initialization of VertexGraph
    //
    // --------------------------------------------------------

    private static void initializeVertexToVertexAdjacency(final VertexGraph vertexGraph) {
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
                    adjacentVertexes.add(vertexGraph.getVertex(vertexId));
                }

                vertexToInitialize = vertexGraph.getVertex(vertexToInitializeId);
                vertexToInitialize.setAdjacentVertexes(adjacentVertexes);
                vertexToInitializeId++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Incorrect filename/path when initialized");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initializeVertexToRoadAdjacency(final VertexGraph vertexGraph, final RoadGraph roadGraph) {
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
                    adjacentRoads.add(roadGraph.getRoad(roadId));
                }

                vertexToInitialize = vertexGraph.getVertex(vertexToInitializeId);
                vertexToInitialize.setAdjacentRoads(adjacentRoads);
                vertexToInitializeId++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Incorrect filename/path when initialized");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initializeVertexToPortAdjacency(final VertexGraph vertexGraph) {
        File vertexToRoadLayout = new File(GameLoader.VERTEX_TO_PORT_FILE);
        try (Scanner scanner = new Scanner(vertexToRoadLayout, StandardCharsets.UTF_8)) {
            int vertexToInitializeId = 0;
            Vertex vertexToInitialize;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(" ");
                vertexToInitialize = vertexGraph.getVertex(vertexToInitializeId);

                // Parse the port id into an actual port
                Port adjacentPort = null;
                try {
                    int portId = Integer.parseInt(values[0]);
                    adjacentPort = vertexGraph.getPort(portId);
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

    // --------------------------------------------------------
    //
    // Initialization of RoadGraph
    //
    // --------------------------------------------------------

    /**
     * Reads through the layout file and initializes the graph with the defined
     * adjacency relationships
     */
    private static void initializeRoadToRoadAdjacency(final RoadGraph roadGraph) {
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
                    adjacentRoads.add(roadGraph.getRoad(roadId));
                }

                roadToInitialize = roadGraph.getRoad(roadToInitializeId);
                roadToInitialize.setAdjacentRoads(adjacentRoads);
                roadToInitializeId++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Incorrect filename/path when initialized");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads through the RoadToVertexLayout file and initializes the graph with the defined
     * adjacency relationships
     */
    private static void initializeRoadToVertexAdjacency(final RoadGraph roadGraph, final VertexGraph vertexGraph) {
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
                    adjacentVertexesBuffer.add(vertexGraph.getVertex(vertexId));
                }

                roadToInitialize = roadGraph.getRoad(roadToInitializeId);
                roadToInitialize.setAdjacentVertexes(adjacentVertexesBuffer);
                roadToInitializeId++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Incorrect filename/path when initialized");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // --------------------------------------------------------
    //
    // Initialization of GameBoard
    //
    // --------------------------------------------------------

    /**
     * Generates from the layout file and initializes the tile array
     */
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
}
