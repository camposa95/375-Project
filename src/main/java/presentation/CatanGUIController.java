package presentation;

import domain.bank.Resource;
import domain.devcarddeck.DevCard;
import domain.building.DistrictType;
import domain.gameboard.Terrain;
import domain.gameboard.Tile;
import domain.graphs.*;
import domain.player.Player;
import data.GameLoader;
import domain.controller.Controller;
import domain.controller.GameState;
import domain.controller.SuccessCode;
import javafx.stage.StageStyle;
import presentation.popups.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import static data.GameLoader.DEFAULT_ICON_FOLDER_PATH;

public class CatanGUIController {
    // FXML Entity Imports
    @FXML
    public Pane gameboard, gameboardPane;
    @FXML
    public Polygon hex0, hex1, hex2, hex3, hex4, hex5, hex6, hex7, hex8, hex9, hex10, hex11, hex12, hex13, hex14, hex15, hex16, hex17, hex18;
    @FXML
    private Circle vertex0, vertex1, vertex2, vertex3, vertex4, vertex5, vertex6, vertex7, vertex8, vertex9, vertex10, vertex11, vertex12, vertex13, vertex14, vertex15, vertex16, vertex17, vertex18, vertex19, vertex20, vertex21, vertex22, vertex23, vertex24, vertex25, vertex26, vertex27, vertex28, vertex29, vertex30, vertex31, vertex32, vertex33, vertex34, vertex35, vertex36, vertex37, vertex38, vertex39, vertex40, vertex41, vertex42, vertex43, vertex44, vertex45, vertex46, vertex47, vertex48, vertex49, vertex50, vertex51, vertex52, vertex53;
    @FXML
    private Circle road0, road1, road2, road3, road4, road5, road6, road7, road8, road9, road10, road11, road12, road13, road14, road15, road16, road17, road18, road19, road20, road21, road22, road23, road24, road25, road26, road27, road28, road29, road30, road31, road32, road33, road34, road35, road36, road37, road38, road39, road40, road41, road42, road43, road44, road45, road46, road47, road48, road49, road50, road51, road52, road53, road54, road55, road56, road57, road58, road59, road60, road61, road62, road63, road64, road65, road66, road67, road68, road69, road70, road71;
    @FXML
    private Circle port0, port1, port2, port3, port4, port5, port6, port7, port8;
    @FXML
    private Button rollButton, buildSettlementButton, buildRoadButton, buildCityButton, buyDevCardButton, playKnightButton, playMonopolyButton, playRoadBuildingButton, playYearOfPlentyButton, endTurnButton, cancelButton, playerTradeButton, bankTradeButton, pauseButton, undoButton, redoButton, buildDistrictButton;
    @FXML
    private Text number0, number1, number2, number3, number4, number5, number6, number7, number8, number9, number10, number11, number12, number13, number14, number15, number16, number17, number18;
    @FXML
    private Circle numberBackground0, numberBackground1, numberBackground2, numberBackground3, numberBackground4, numberBackground5, numberBackground6, numberBackground7, numberBackground8, numberBackground9, numberBackground10, numberBackground11, numberBackground12, numberBackground13, numberBackground14, numberBackground15, numberBackground16, numberBackground17, numberBackground18;
    @FXML
    private Text player1wood, player1brick, player1wool, player1grain, player1ore, player2wood, player2brick, player2wool, player2grain, player2ore, player3wood, player3brick, player3wool,  player3grain, player3ore, player4wood, player4brick, player4wool, player4grain, player4ore, player1vp, player2vp, player3vp, player4vp, player1name, player2name, player4name, player3name;
    @FXML
    private Circle robber0, robber1, robber2, robber3, robber4, robber5, robber6, robber7, robber8, robber9, robber10, robber11, robber12, robber13, robber14, robber15, robber16, robber17, robber18;
    @FXML
    private Rectangle recipes, woodIcon1, woodIcon2, woodIcon3, woodIcon4, brickIcon1, brickIcon2, brickIcon3, brickIcon4, woolIcon1, woolIcon2, woolIcon3, woolIcon4, grainIcon1, grainIcon2, grainIcon3, grainIcon4, oreIcon1, oreIcon2, oreIcon3, oreIcon4;
    @FXML
    private Text playerTurnText, numVictoryPointsText;
    @FXML
    public
    Text tooltipText;
    @FXML
    private Text turnTitle1, turnTitle2, devCardsTitle, victoryPointsTitle;
    @FXML
    private Polygon settlementTemplate, cityTemplate;
    @FXML
    private Rectangle roadTemplate, robber;
    //Lists
    public Polygon[] hexagonTiles;
    private Circle[] vertices;
    private Circle[] roadMarkers;
    private Circle[] ports;
    private Text[] numbers;
    private Circle[] numberBackgrounds;
    private Text[] victoryPointTexts;
    private Circle[] robberSpots;

    HashMap<Polygon, Integer> settlementToVertexMap = new HashMap<>();
    ResourceBundle messages;
    private String iconFolderPath = DEFAULT_ICON_FOLDER_PATH;
    public int robberId;

    public enum GUIState {
        BUSY, IDLE, GAME_WON
    }
    public GUIState guiState;

    private Controller controller; // facade to main game stuff

    private final List<Popup> popupsOpen = new ArrayList<>();

    public void notifyOfPopupClose(Popup popup) {
        this.popupsOpen.remove(popup);
        this.guiState = GUIState.IDLE;
    }

    private void setupGUIEntityLists(){
        hexagonTiles = new Polygon[]{hex0, hex1, hex2, hex3, hex4, hex5, hex6, hex7, hex8, hex9, hex10, hex11, hex12, hex13, hex14, hex15, hex16, hex17, hex18};
        vertices = new Circle[]{vertex0, vertex1, vertex2, vertex3, vertex4, vertex5, vertex6, vertex7, vertex8, vertex9, vertex10, vertex11, vertex12, vertex13, vertex14, vertex15, vertex16, vertex17, vertex18, vertex19, vertex20, vertex21, vertex22, vertex23, vertex24, vertex25, vertex26, vertex27, vertex28, vertex29, vertex30, vertex31, vertex32, vertex33, vertex34, vertex35, vertex36, vertex37, vertex38, vertex39, vertex40, vertex41, vertex42, vertex43, vertex44, vertex45, vertex46, vertex47, vertex48, vertex49, vertex50, vertex51, vertex52, vertex53};
        roadMarkers = new Circle[]{road0, road1, road2, road3, road4, road5, road6, road7, road8, road9, road10, road11, road12, road13, road14, road15, road16, road17, road18, road19, road20, road21, road22, road23, road24, road25, road26, road27, road28, road29, road30, road31, road32, road33, road34, road35, road36, road37, road38, road39, road40, road41, road42, road43, road44, road45, road46, road47, road48, road49, road50, road51, road52, road53, road54, road55, road56, road57, road58, road59, road60, road61, road62, road63, road64, road65, road66, road67, road68, road69, road70, road71};
        ports = new Circle[]{port0, port1, port2, port3, port4, port5, port6, port7, port8};
        numbers = new Text[]{number0, number1, number2, number3, number4, number5, number6, number7, number8, number9, number10, number11, number12, number13, number14, number15, number16, number17, number18};
        numberBackgrounds = new Circle[]{numberBackground0, numberBackground1, numberBackground2, numberBackground3, numberBackground4, numberBackground5, numberBackground6, numberBackground7, numberBackground8, numberBackground9, numberBackground10, numberBackground11, numberBackground12, numberBackground13, numberBackground14, numberBackground15, numberBackground16, numberBackground17, numberBackground18};
        victoryPointTexts = new Text[]{player1vp, player2vp, player3vp, player4vp};
        robberSpots = new Circle[]{robber0,robber1,robber2,robber3,robber4,robber5,robber6,robber7,robber8,robber9,robber10,robber11,robber12,robber13,robber14,robber15,robber16,robber17,robber18};
    }

    //Initialize the game
    @FXML
    private void initialize() {
        setupGUIEntityLists();  //FIRST
        setAllRoadsVisibility(false);

        settlementTemplate.setVisible(false);
        cityTemplate.setVisible(false);
        roadTemplate.setVisible(false);

        playKnightButton.setDisable(true);
        playMonopolyButton.setDisable(true);
        playRoadBuildingButton.setDisable(true);
        playYearOfPlentyButton.setDisable(true);
        rollButton.setDisable(true);

        player1name.setFill(getPlayerColor(1));
        player2name.setFill(getPlayerColor(2));
        player3name.setFill(getPlayerColor(3));
        player4name.setFill(getPlayerColor(4));

        robber.setVisible(false);
        setAllRobberSpotsVisibility(false);
        guiState = GUIState.IDLE;

        initAllImages();
    }

    public void internationalize(ResourceBundle bundle){
        this.messages = bundle;

        turnTitle1.setText(messages.getString("turnTitle1"));
        turnTitle2.setText(messages.getString("turnTitle2"));
        devCardsTitle.setText(messages.getString("devCardsTitle"));
        victoryPointsTitle.setText(messages.getString("victoryPointsTitle"));

        rollButton.setText(messages.getString("rollButtonText"));
        buildSettlementButton.setText(messages.getString("buildSettlementText"));
        buildRoadButton.setText(messages.getString("buildRoadText"));
        buildCityButton.setText(messages.getString("buildCityText"));
        buildDistrictButton.setText(messages.getString("buildDistrictText"));
        buyDevCardButton.setText(messages.getString("buyDevCardText"));
        playKnightButton.setText(messages.getString("playKnightText"));
        playMonopolyButton.setText(messages.getString("playMonopolyText"));
        playRoadBuildingButton.setText(messages.getString("playRoadBuildingText"));
        playYearOfPlentyButton.setText(messages.getString("playYearOfPlentyText"));
        playerTradeButton.setText(messages.getString("playerTradeText"));
        bankTradeButton.setText(messages.getString("bankTradeText"));
        endTurnButton.setText(messages.getString("endTurnText"));
        cancelButton.setText(messages.getString("cancelText"));

        player1name.setText(messages.getString("player1"));
        player2name.setText(messages.getString("player2"));
        player3name.setText(messages.getString("player3"));
        player4name.setText(messages.getString("player4"));

        undoButton.setText(messages.getString("undoButton"));
        pauseButton.setText(messages.getString("pauseButton"));
        redoButton.setText(messages.getString("redoButton"));
    }

    public Color getPlayerColor(int playerNum){
        return switch (playerNum) {
            case 1 -> Color.BLUE;
            case 2 -> Color.GREEN;
            case 3 -> Color.RED;
            case 4 -> Color.BROWN;
            default -> Color.WHITE;
        };
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    // --------------------------------------------------------
    //
    // Bulk State Rendering
    //
    // --------------------------------------------------------

    public void initializeGameBoard() {
        // Static initialization
        this.initializeTiles();
        this.initializePorts();
        this.hidePlayerStatsIfNeeded();

        // State specific data
        this.updateVertexes();
        this.updateRoads();
        this.updateInfoPane();

        if (controller.getState() == GameState.TURN_START) {
            setAllVerticesVisibility(false);
            rollButton.setDisable(false);
            this.tooltipText.setText(messages.getString("rollDice"));
        }
    }

    // -------------- Static Rendering -----------------------

    private void initializeTiles() {
        Tile[] tiles = GameLoader.getInstance().getTiles();
        for(int i = 0; i < tiles.length; i++){
            Tile current = tiles[i];
            if(current.getTerrain()!= Terrain.DESERT){
                numbers[i].setText(Integer.toString(current.getDieNumber()));
            }else{
                numbers[i].setVisible(false);
                numberBackgrounds[i].setVisible(false);
            }
        }
    }

    private void initializePorts() {
        GameboardGraph gameboardGraph = GameLoader.getInstance().getGameboardGraph();
        for(int p = 0; p < GameboardGraph.NUM_PORTS; p++){
            Port cur = gameboardGraph.getPort(p);
            setPortTrade(ports[p], cur.getResource());
        }
    }

    private void hidePlayerStatsIfNeeded() {
        int numPlayers = GameLoader.getInstance().getNumPlayers();
        if(numPlayers < 3){
            player3wood.setVisible(false);
            player3brick.setVisible(false);
            player3wool.setVisible(false);
            player3grain.setVisible(false);
            player3ore.setVisible(false);
            player3vp.setVisible(false);
            player3name.setVisible(false);
            woodIcon3.setVisible(false);
            brickIcon3.setVisible(false);
            woolIcon3.setVisible(false);
            grainIcon3.setVisible(false);
            oreIcon3.setVisible(false);
        }
        if(numPlayers < 4){
            player4wood.setVisible(false);
            player4brick.setVisible(false);
            player4wool.setVisible(false);
            player4grain.setVisible(false);
            player4ore.setVisible(false);
            player4vp.setVisible(false);
            player4name.setVisible(false);
            woodIcon4.setVisible(false);
            brickIcon4.setVisible(false);
            woolIcon4.setVisible(false);
            grainIcon4.setVisible(false);
            oreIcon4.setVisible(false);
        }
    }

    // State Specific Rendering

    private void updateVertexes() {
        GameboardGraph vertexes = GameLoader.getInstance().getGameboardGraph();
        for (int i = 0; i < GameboardGraph.NUM_VERTICES; i++) {
            Vertex vertex = vertexes.getVertex(i);
            if (vertex.isOccupied()) {
                renderSettlementOnVertex(vertices[i], getPlayerColor(vertex.getOwner().getPlayerNum()));
            }
        }
    }

    private void updateRoads() {
        GameboardGraph roads = GameLoader.getInstance().getGameboardGraph();
        for (int i = 0; i < GameboardGraph.NUM_ROADS; i++) {
            Road road = roads.getRoad(i);
            if (road.isOccupied()) {
                renderRoad(i, road.getOwner().getPlayerNum());
            }
        }
    }

    private void updateInfoPane() {
        //Called in various places to update player data in pane
        Player[] tempPlayers = this.controller.getPlayerArr();
        this.playerTurnText.setText(Integer.toString(this.controller.getCurrentPlayer().playerNum));
        this.tooltipText.setText("");
        Player currentPlayer = tempPlayers[this.controller.getCurrentPlayer().playerNum-1];
        if(this.controller.getDevCardsEnabled()){
            HashMap<DevCard, Integer> devCards = currentPlayer.hand.devCards;
            playKnightButton.setDisable(devCards.get(DevCard.KNIGHT) <= 0);
            playMonopolyButton.setDisable(devCards.get(DevCard.MONOPOLY) <= 0);
            playYearOfPlentyButton.setDisable(devCards.get(DevCard.PLENTY) <= 0);
            playRoadBuildingButton.setDisable(devCards.get(DevCard.ROAD) <= 0);
        }else{
            playKnightButton.setDisable(true);
            playMonopolyButton.setDisable(true);
            playYearOfPlentyButton.setDisable(true);
            playRoadBuildingButton.setDisable(true);
        }

        numVictoryPointsText.setText(Integer.toString(currentPlayer.hand.devCards.get(DevCard.VICTORY)));

        player1wood.setText(Integer.toString(tempPlayers[0].hand.getResourceCount(Resource.LUMBER)));
        player1brick.setText(Integer.toString(tempPlayers[0].hand.getResourceCount(Resource.BRICK)));
        player1wool.setText(Integer.toString(tempPlayers[0].hand.getResourceCount(Resource.WOOL)));
        player1grain.setText(Integer.toString(tempPlayers[0].hand.getResourceCount(Resource.GRAIN)));
        player1ore.setText(Integer.toString(tempPlayers[0].hand.getResourceCount(Resource.ORE)));
        player1vp.setText(Integer.toString(tempPlayers[0].victoryPoints));

        player2wood.setText(Integer.toString(tempPlayers[1].hand.getResourceCount(Resource.LUMBER)));
        player2brick.setText(Integer.toString(tempPlayers[1].hand.getResourceCount(Resource.BRICK)));
        player2wool.setText(Integer.toString(tempPlayers[1].hand.getResourceCount(Resource.WOOL)));
        player2grain.setText(Integer.toString(tempPlayers[1].hand.getResourceCount(Resource.GRAIN)));
        player2ore.setText(Integer.toString(tempPlayers[1].hand.getResourceCount(Resource.ORE)));
        player2vp.setText(Integer.toString(tempPlayers[1].victoryPoints));

        if(tempPlayers.length>=3){
            player3wood.setText(Integer.toString(tempPlayers[2].hand.getResourceCount(Resource.LUMBER)));
            player3brick.setText(Integer.toString(tempPlayers[2].hand.getResourceCount(Resource.BRICK)));
            player3wool.setText(Integer.toString(tempPlayers[2].hand.getResourceCount(Resource.WOOL)));
            player3grain.setText(Integer.toString(tempPlayers[2].hand.getResourceCount(Resource.GRAIN)));
            player3ore.setText(Integer.toString(tempPlayers[2].hand.getResourceCount(Resource.ORE)));
            player3vp.setText(Integer.toString(tempPlayers[2].victoryPoints));
        }

        if(tempPlayers.length==4){
            player4wood.setText(Integer.toString(tempPlayers[3].hand.getResourceCount(Resource.LUMBER)));
            player4brick.setText(Integer.toString(tempPlayers[3].hand.getResourceCount(Resource.BRICK)));
            player4wool.setText(Integer.toString(tempPlayers[3].hand.getResourceCount(Resource.WOOL)));
            player4grain.setText(Integer.toString(tempPlayers[3].hand.getResourceCount(Resource.GRAIN)));
            player4ore.setText(Integer.toString(tempPlayers[3].hand.getResourceCount(Resource.ORE)));
            player4vp.setText(Integer.toString(tempPlayers[3].victoryPoints));
        }
    }

    // ----------------------------------------------------------------
    //
    // Game State Flow Control
    //
    // ----------------------------------------------------------------

    public void pauseButtonPressed() throws IOException {
        if (this.controller.getState() == GameState.TURN_START  && this.guiState == GUIState.IDLE) {
            FXMLLoader fxmlLoader = new FXMLLoader(Catan.class.getResource("PauseMenu.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle(messages.getString("paused"));
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();


            PauseMenuController pauseMenuController = fxmlLoader.getController();
            pauseMenuController.setControllers(this, this.controller);
            pauseMenuController.setMessages(this.messages);
            this.popupsOpen.add(pauseMenuController);

            this.guiState = GUIState.BUSY;
            this.tooltipText.setText(messages.getString("paused"));
        } else {
            this.tooltipText.setText(messages.getString("pauseNotAllowed"));
        }
    }

    public void endTurnButtonPressed() throws IOException {
        // Triggered by End Turn button pressed
        if(this.controller.getState()==GameState.DEFAULT && this.guiState == GUIState.IDLE){
            SuccessCode success = this.controller.endTurn();
            if(success == SuccessCode.SUCCESS){
                this.updateInfoPane();
                this.tooltipText.setText(messages.getString("rollDice"));

                // beginning of turn
                this.controller.setState(GameState.TURN_START);
                this.guiState = GUIState.IDLE; //just in case
                GameLoader.getInstance().notifyOfTurnStart();
            }else if(success == SuccessCode.GAME_WIN){
                applyGameWon();
            }
        }
    }

    public void undo() throws IOException {
        if (GameLoader.getInstance().undo()) {
            repaint();
        } else {
            tooltipText.setText(messages.getString("undoFail"));
        }
    }

    public void redo() throws IOException {
        if (GameLoader.getInstance().redo()) {
            repaint();
        } else {
            tooltipText.setText(messages.getString("redoFail"));
        }
    }

    private void repaint() throws IOException {
        closePopups();

        // close Start Screen window
        Stage stage = (Stage) gameboard.getScene().getWindow();

        // Open up game board window
        FXMLLoader fxmlLoader = new FXMLLoader(Catan.class.getResource("gameboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(messages.getString("catanTitle"));
        stage.setScene(scene);
        stage.show();

        // Link the Gui Controller to the Domain Controller
        CatanGUIController guiController = fxmlLoader.getController();
        guiController.setController(controller);
        changeIconSet(this.iconFolderPath);

        // initialize the game-board
        guiController.internationalize(messages);
        guiController.initializeGameBoard();
    }

    private void closePopups() {
        for (Popup popup: popupsOpen.toArray(new Popup[0])) {
            popup.close();
        }
    }

    public void finishedMove() {
        updateInfoPane();
        this.guiState = CatanGUIController.GUIState.IDLE;
        this.tooltipText.setText("");
    }

    //this method is called to disable everything on the board once a player has won the game
    private void applyGameWon() throws IOException {
        // update the gui state
        this.guiState = GUIState.GAME_WON;
        this.tooltipText.setText(messages.getString("gameOver"));
        //update pane so accurate victory points are shown on the left
        this.updateInfoPane();

        //show a simple game won pane
        FXMLLoader fxmlLoader = new FXMLLoader(Catan.class.getResource("game_won.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(messages.getString("gameOverTitle"));
        stage.setScene(scene);
        stage.show();

        GameWonController gwc = fxmlLoader.getController();
        gwc.setControllers(this, this.controller);
        gwc.setMessages(this.messages);

        // disable buttons to end game
        disableActions();
    }

    private void disableActions() {
        rollButton.setDisable(true);
        endTurnButton.setDisable(true);
        cancelButton.setDisable(true);
        buildSettlementButton.setDisable(true);
        buildRoadButton.setDisable(true);
        buildCityButton.setDisable(true);
        buildDistrictButton.setDisable(true);
        buyDevCardButton.setDisable(true);
        playKnightButton.setDisable(true);
        playMonopolyButton.setDisable(true);
        playRoadBuildingButton.setDisable(true);
        playYearOfPlentyButton.setDisable(true);

        pauseButton.setDisable(true);
    }

    //-------------------------------------------------------------------
    //
    // Click Handlers
    //
    //-------------------------------------------------------------------

    @FXML
    public void handleVertexClick(MouseEvent event) throws IOException {
        //Triggered by clicking Vertex circle
        int currentPlayer = this.controller.getCurrentPlayer().playerNum;
        int vertexId = Integer.parseInt(((Circle)event.getSource()).getId().substring("vertex".length()));
        SuccessCode success = controller.clickedVertex(vertexId);
        if(success == SuccessCode.SUCCESS){
            this.renderSettlementOnVertex(vertices[vertexId], getPlayerColor(currentPlayer));

            if(this.controller.getState()==GameState.FIRST_ROAD || this.controller.getState()==GameState.SECOND_ROAD){
                setAllRoadsVisibility(true);
            }

            setAllVerticesVisibility(false);
            updateInfoPane();
            this.guiState = GUIState.IDLE;

        }else if(success == SuccessCode.INSUFFICIENT_RESOURCES){
            this.tooltipText.setText(messages.getString("notEnoughResourcesOrSettlements"));
        }else if(success == SuccessCode.INVALID_PLACEMENT){
            this.tooltipText.setText(messages.getString("cannotBuildSettlementHere"));
        }else if(success == SuccessCode.GAME_WIN){
            this.renderSettlementOnVertex(vertices[vertexId], getPlayerColor(currentPlayer));
            applyGameWon();
        }
    }

    // ROBBER METHODS
    public void handleRobberClick(MouseEvent event) throws IOException {
        this.robberId = Integer.parseInt(((Circle) event.getSource()).getId().substring("robber".length()));
        SuccessCode success = this.controller.moveRobber(robberId);
        if(success == SuccessCode.SUCCESS){
            renderRobberOnHex(((Circle) event.getSource()).getLayoutX(), ((Circle) event.getSource()).getLayoutY());
            setAllRobberSpotsVisibility(false);

            FXMLLoader fxmlLoader = new FXMLLoader(Catan.class.getResource("RobPlayer.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle(messages.getString("sevenRolledTitle"));
            stage.setScene(scene);
            stage.show();

            RobPlayerController robPlayerController = fxmlLoader.getController();
            robPlayerController.setControllers(this, this.controller);
            robPlayerController.setMessages(this.messages);
            this.popupsOpen.add(robPlayerController);

            this.guiState = GUIState.IDLE;
        } else {
            this.tooltipText.setText(messages.getString("cannotMoveRobberHere"));
        }
    }

    public void handleRoadClick(MouseEvent event) throws IOException {
        int currentPlayer = this.controller.getCurrentPlayer().playerNum;
        GameState currentState = this.controller.getState();
        //Triggered by Road circle click
        int roadId = Integer.parseInt(((Circle) event.getSource()).getId().substring("road".length()));
        SuccessCode success = controller.clickedRoad(roadId);
        if (success == SuccessCode.SUCCESS) {
            //render the road just clicked
            this.renderRoad(roadId, currentPlayer);

            this.updateInfoPane();
            if (this.controller.getState() == GameState.SECOND_SETTLEMENT || this.controller.getState() == GameState.FIRST_SETTLEMENT) { //set up road, not end of set up
                setAllVerticesVisibility(true);
                this.tooltipText.setText("");
            }
            else if(this.controller.getState() == GameState.TURN_START){ //set up road, end of set up
                setAllRoadsVisibility(false);
                rollButton.setDisable(false);
                this.guiState = GUIState.IDLE;
                this.tooltipText.setText("");
            }
            else if(currentState == GameState.ROAD_BUILDING_1){ //placing first road in road building
                if(this.controller.getState() == GameState.ROAD_BUILDING_2){ //go to place second road
                    this.tooltipText.setText(messages.getString("roadBuilding2"));
                    setAllRoadsVisibility(true);
                }else if(this.controller.getState() == GameState.DEFAULT){ //out of roads, back to normal gameplay
                    setAllRoadsVisibility(false);
                    this.tooltipText.setText(messages.getString("outOfRoads"));
                    this.guiState = GUIState.IDLE;
                }
            }else if(currentState == GameState.ROAD_BUILDING_2){ //placing second road in road building
                setAllRoadsVisibility(false);
                this.guiState = GUIState.IDLE;
                this.tooltipText.setText("");
            }else if(this.controller.getState() == GameState.DEFAULT){ //normal turn play
                setAllRoadsVisibility(false);
                this.guiState = GUIState.IDLE;
                this.tooltipText.setText("");
            }
            this.playerTurnText.setText(String.valueOf(this.controller.getCurrentPlayer().playerNum));
        }else if(success == SuccessCode.INSUFFICIENT_RESOURCES){
            this.tooltipText.setText(messages.getString("notEnoughResourcesOrRoads"));
        }else if(success == SuccessCode.INVALID_PLACEMENT){
            this.tooltipText.setText(messages.getString("cannotBuildRoadHere"));
        }else if(success == SuccessCode.GAME_WIN){
            this.renderRoad(roadId, currentPlayer);
            this.applyGameWon();
        }
    }

    public void buildSettlementButtonPress(){
        //Triggered by Build Settlement button pressed
        if(this.controller.getState()==GameState.DEFAULT){
            this.guiState = GUIState.BUSY;
            this.controller.setState(GameState.BUILD_SETTLEMENT);
            setAllVerticesVisibility(true);
        }
    }

    public void buildRoadButtonPress(){
        //Triggered by Build Road button pressed
        if(this.controller.getState()==GameState.DEFAULT) {
            this.guiState = GUIState.BUSY;
            this.controller.setState(GameState.BUILD_ROAD);
            setAllRoadsVisibility(true);
        }
    }

    public void buildCityButtonPress(){
        //Triggered by Buy City button pressed
        if(this.controller.getState()==GameState.DEFAULT) {
            this.guiState = GUIState.BUSY;
            this.tooltipText.setText(messages.getString("selectSettlementToUpgrade"));
            this.controller.setState(GameState.UPGRADE_SETTLEMENT);
        }
    }

    public void buildDistrictButtonPress(){
        //Triggered by Build Settlement button pressed
        if(this.controller.getState()==GameState.DEFAULT){
            this.guiState = GUIState.BUSY;
            this.controller.setState(GameState.BUILD_DISTRICT);
        }
    }

    public void buyDevCardButtonPress() throws IOException {
        //Triggered by Buy Development Card button pressed
        if(this.controller.getState()==GameState.DEFAULT || this.controller.getState() == GameState.TURN_START){
            SuccessCode success = controller.clickedBuyDevCard();
            if(success == SuccessCode.SUCCESS){
                this.tooltipText.setText(messages.getString("purchasedDevCard"));
                this.updateInfoPane();
            }else if(success == SuccessCode.INSUFFICIENT_RESOURCES){
                this.tooltipText.setText(messages.getString("notEnoughResourcesDevCard"));
            }else if(success == SuccessCode.EMPTY_DEV_CARD_DECK){
                this.tooltipText.setText(messages.getString("devCardDeckEmpty"));
            }else if(success == SuccessCode.GAME_WIN){
                applyGameWon();
            }
        }
    }

    public void rollButtonPressed() throws IOException {
        //Triggered by Roll Button pressed
        if(this.controller.getState() == GameState.TURN_START && this.guiState == GUIState.IDLE){
            int dieNumber = this.controller.rollDice();
            this.updateInfoPane();
            this.tooltipText.setText(dieNumber + " " + messages.getString("dieNumberRolled"));
            if (dieNumber == 7) {
                this.doRobber();
            } else if (dieNumber == 12) {
                Controller.WeatherEvent weatherEvent = this.controller.createWeatherEvent();

                // Build the weather event message
                String messagePattern = messages.getString("weatherEvent");
                Object[] params = {messages.getString(weatherEvent.boostType().toString()),
                        messages.getString(weatherEvent.resource().toString()),
                        weatherEvent.forEveryone() ? messages.getString("everyone") : messages.getString("onlyPlayer")};
                String weatherEventMessage = MessageFormat.format(messagePattern, params);

                // display it to the user
                this.tooltipText.setText(weatherEventMessage);
                this.controller.setState(GameState.DEFAULT);
            } else {
                this.controller.setState(GameState.DEFAULT);
            }
        }
    }

    private void doRobber() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Catan.class.getResource("DropCards.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(messages.getString("sevenRolledTitle"));
        stage.setScene(scene);
        stage.show();

        DropCardsController dropCardsController = fxmlLoader.getController();
        dropCardsController.setControllers(this, this.controller);
        dropCardsController.setMessages(this.messages);
        this.popupsOpen.add(dropCardsController);

        this.tooltipText.setText(messages.getString("sevenRolled"));
        this.guiState = GUIState.BUSY;
    }

    public void cancelButtonPressed(){
        if(this.controller.getState()==GameState.BUILD_ROAD
            || this.controller.getState() == GameState.BUILD_SETTLEMENT
            || this.controller.getState() == GameState.UPGRADE_SETTLEMENT
        ){
            this.controller.setState(GameState.DEFAULT);
            setAllVerticesVisibility(false);
            setAllRoadsVisibility(false);
            updateInfoPane();
            this.guiState = GUIState.IDLE;
            this.tooltipText.setText("");
        }
    }

    public void playKnightButtonPressed() throws IOException {
        //Triggered by Play Knight Card pressed
        if(this.controller.getState() == GameState.DEFAULT && this.guiState == GUIState.IDLE){
            SuccessCode success = this.controller.playKnightCard();
            if(success == SuccessCode.SUCCESS){
                setAllRobberSpotsVisibility(true);
                this.tooltipText.setText(messages.getString("moveRobber"));
                this.guiState = GUIState.BUSY;
            }else if(success == SuccessCode.CANNOT_PLAY_CARD){
                this.tooltipText.setText(messages.getString("cannotPlayDevCard"));
            }else if(success == SuccessCode.GAME_WIN){
                applyGameWon();
            }

        }
    }

    public void playMonopolyButtonPressed() throws IOException {
        //Triggered by Play Monopoly card pressed
        if(this.controller.getState()==GameState.DEFAULT && this.guiState == GUIState.IDLE){
            FXMLLoader fxmlLoader = new FXMLLoader(Catan.class.getResource("monopoly.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle(messages.getString("monopolyTitle"));
            stage.setScene(scene);
            stage.show();


            MonopolyController monopolyController = fxmlLoader.getController();
            monopolyController.setControllers(this, this.controller);
            monopolyController.setMessages(this.messages);
            this.popupsOpen.add(monopolyController);

            this.guiState = GUIState.BUSY;
            this.tooltipText.setText(messages.getString("playingMonopoly"));
        }
    }

    public void playRoadBuildingButtonPressed(){
        //Triggered by Play Road Building Card pressed
        if(this.controller.getState() == GameState.DEFAULT && this.guiState == GUIState.IDLE){
            SuccessCode success = controller.useRoadBuildingCard();
            if(success == SuccessCode.SUCCESS){
                setAllRoadsVisibility(true);
                this.tooltipText.setText(messages.getString("playRoadBuilding"));
                this.guiState = GUIState.BUSY;
            }else if(success == SuccessCode.CANNOT_PLAY_CARD){
                this.tooltipText.setText(messages.getString("cannotPlayDevCard"));
            }
        }
    }

    public void playYearOfPlentyButtonPressed() throws IOException {
        //Triggered by Play YearOfPlenty card pressed
        if(this.controller.getState()==GameState.DEFAULT && this.guiState==GUIState.IDLE){
            FXMLLoader fxmlLoader = new FXMLLoader(Catan.class.getResource("year_of_plenty.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle(messages.getString("yearOfPlentyTitle"));
            stage.setScene(scene);
            stage.show();

            YearOfPlentyController yearOfPlentyController = fxmlLoader.getController();
            yearOfPlentyController.setControllers(this, this.controller);
            yearOfPlentyController.setMessages(this.messages);
            this.popupsOpen.add(yearOfPlentyController);

            this.tooltipText.setText(messages.getString("playingYearOfPlenty"));
            this.guiState = GUIState.BUSY;
        }
    }

    public void playerTradeButtonPressed() throws IOException {
        //Triggered by Player Trade button pressed
        if (this.controller.getState() == GameState.DEFAULT && this.guiState == GUIState.IDLE) {
            FXMLLoader fxmlLoader = new FXMLLoader(Catan.class.getResource("PlayerTradeWindow.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle(messages.getString("playerTradeTitle"));
            stage.setScene(scene);
            stage.show();

            PlayerTradeWindowController playerTradeController = fxmlLoader.getController();
            playerTradeController.setControllers(this, this.controller);
            playerTradeController.setMessages(this.messages);
            this.popupsOpen.add(playerTradeController);

            this.tooltipText.setText(messages.getString("playerTrade"));

            this.guiState = GUIState.BUSY;
        }
    }

    public void bankTradeButtonPressed() throws IOException {
        //Triggered by Trade with Bank button pressed
        if(this.controller.getState() == GameState.DEFAULT && this.guiState == GUIState.IDLE) {
            FXMLLoader fxmlLoader = new FXMLLoader(Catan.class.getResource("BankTradeWindow.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle(messages.getString("bankTradeTitle"));
            stage.setScene(scene);
            stage.show();

            BankTradeWindowController bankTradeController = fxmlLoader.getController();
            bankTradeController.setControllers(this, this.controller);
            bankTradeController.setMessages(this.messages);
            this.popupsOpen.add(bankTradeController);

            this.tooltipText.setText(messages.getString("bankTrade"));
            this.guiState = GUIState.BUSY;
        }
    }

    public void handleSettlementClick(MouseEvent event) {
        Polygon clickedSettlement = (Polygon) event.getSource();
        int vertex = settlementToVertexMap.get(clickedSettlement);
        if(this.controller.getState()==GameState.UPGRADE_SETTLEMENT) {
            handleUpgradeSettlement(clickedSettlement, vertex);
        } else if(this.controller.getState()==GameState.BUILD_DISTRICT) {
            try {
                handleBuildDistrict(vertex, clickedSettlement);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleUpgradeSettlement(Polygon clickedSettlement, int vertex) {
        SuccessCode success = this.controller.clickedVertex(vertex);
        if(success==SuccessCode.SUCCESS){
            settlementToVertexMap.remove(clickedSettlement);
            gameboardPane.getChildren().remove(clickedSettlement);
            renderCityOnVertex(clickedSettlement.getLayoutX(), clickedSettlement.getLayoutY(), vertex);
            updateInfoPane();
            this.tooltipText.setText(messages.getString("builtCity"));
            this.guiState = GUIState.IDLE;
        }else if(success == SuccessCode.INVALID_PLACEMENT){
            this.tooltipText.setText(messages.getString("cannotPlaceCity"));
        }else if(success==SuccessCode.INSUFFICIENT_RESOURCES){
            this.tooltipText.setText(messages.getString("notEnoughResourcesCity"));
        }else if(success == SuccessCode.GAME_WIN){
            settlementToVertexMap.remove(clickedSettlement);
            gameboardPane.getChildren().remove(clickedSettlement);
            renderCityOnVertex(clickedSettlement.getLayoutX(), clickedSettlement.getLayoutY(), vertex);
            try {
                applyGameWon();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleBuildDistrict(int vertex, Polygon building) throws IOException {
        //Triggered by Play Monopoly card pressed
        FXMLLoader fxmlLoader = new FXMLLoader(Catan.class.getResource("build_district.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(messages.getString("buildDistrictTitle"));
        stage.setScene(scene);
        stage.show();

        BuildDistrictController buildDistrictController = fxmlLoader.getController();
        buildDistrictController.setControllers(this, this.controller);
        buildDistrictController.setMessages(this.messages);
        buildDistrictController.setSelectedVertex(vertex);
        buildDistrictController.setSelectedBuilding(building);
        this.popupsOpen.add(buildDistrictController);

        this.guiState = GUIState.BUSY;
        this.tooltipText.setText(messages.getString("buildingDistrict"));
    }

    public void setTooltipText(String text) {
        this.tooltipText.setText(text);
    }

    // -----------------------------------------------------------------------
    //
    // Gui rendering related methods
    //
    // -----------------------------------------------------------------------

    private void renderRoad(int index, int playerNum){
        double x = roadMarkers[index].getLayoutX();
        double y = roadMarkers[index].getLayoutY();
        Color color = getPlayerColor(playerNum);

        // Create the road
        Rectangle road = new Rectangle();
        road.setWidth(roadTemplate.getWidth());
        road.setHeight(roadTemplate.getHeight());
        road.setStroke(Color.BLACK);
        road.setFill(color);

        // rotate road, based on which edge of hex it is on
        if (index==0 || index == 2 || index == 4 || index == 10 || index == 12 || index == 14 || index == 16 || index == 23 || index == 25 || index == 27 || index == 29 || index == 31 || index == 40 || index == 42 || index == 44 || index == 46 || index == 48 || index == 55 || index == 57 || index == 59 || index == 61 || index == 67 || index == 69 || index == 71){
            //top left side, bottom right side
            road.setRotate(60);
            y -= 30;
        } else if (index == 1 || index == 3 || index == 5 || index == 11 || index == 13 || index == 15 || index == 17 || index == 24 || index == 26 || index == 28 || index == 30 || index == 32 || index == 39 || index == 41 || index == 43 || index == 45 || index == 47 || index == 54 || index == 56 || index == 58 || index == 60 || index == 66 || index == 68 || index == 70){
            //top right side, bottom left side
            road.setRotate(-60);
            y -= 20;
        } else if (index == 6 || index == 7 || index == 8 || index == 9 || index == 18 || index == 19 || index == 20 || index == 21 || index == 22 || index == 33 || index == 34 || index == 35 || index == 36 || index == 37 || index == 38 || index == 49 || index == 50 || index == 51 || index == 52 || index == 53 || index == 62 || index == 63 || index == 64 || index == 65){
            //middle left side, middle right side
            //road.setRotate(30);
            y -= 20;
        }

        road.setLayoutX(x);
        road.setLayoutY(y);

        gameboardPane.getChildren().add(road);
        roadMarkers[index].setVisible(false);
        roadMarkers[index] = null;
    }

    private void setAllRoadsVisibility(boolean visibility){
        //Called in various places in handleRoadClick and handleVertexClick
        for(Circle road: roadMarkers){
            if(road != null){
                road.setVisible(visibility);
            }
        }
    }

    private void renderCityOnVertex(double x, double y, int vertexId) {
        Color color = getPlayerColor(this.controller.getCurrentPlayer().playerNum);
        //Construct new settlement
        Polygon newCity = new Polygon();
        newCity.getPoints().addAll(cityTemplate.getPoints());
        newCity.setLayoutX(x);
        newCity.setLayoutY(y);
        newCity.setFill(color);
        setDistrictColor(newCity, controller.getDistrictTypeForVertex(vertexId));

        //remove settlement from screen and place city
        gameboardPane.getChildren().add(newCity);
    }

    public void setAllRobberSpotsVisibility(boolean visibility){
        for(Circle robberSpot: robberSpots){
            if(robberSpot!=null){
                robberSpot.setVisible(visibility);
            }
        }
    }

    private void renderRobberOnHex(double x, double y){
        robber.setLayoutX(x-(robber.getWidth()/2));
        robber.setLayoutY(y-(robber.getHeight()/2)+10);
        robber.setVisible(true);
    }

    private void renderSettlementOnVertex(Circle vertex, Color color) {
        double x = vertex.getLayoutX() - 20;
        double y = vertex.getLayoutY() - 20;
        int vertexId = Integer.parseInt(vertex.getId().substring("vertex".length()));

        // Construct new settlement
        Polygon newSettlement = this.createSettlement(x, y, color);
        this.setDistrictColor(newSettlement, controller.getDistrictTypeForVertex(vertexId));

        // add to board
        gameboardPane.getChildren().add(newSettlement);
        this.settlementToVertexMap.put(newSettlement, vertexId);
        vertices[vertexId].setVisible(false);
        vertices[vertexId] = null;
    }

    private Polygon createSettlement(double x, double y, Color color) {
        Polygon newSettlement = new Polygon();
        newSettlement.getPoints().addAll(settlementTemplate.getPoints());
        newSettlement.setLayoutX(x);
        newSettlement.setLayoutY(y);
        newSettlement.setStroke(Color.BLACK);
        newSettlement.setFill(color);
        newSettlement.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleSettlementClick);

        return newSettlement;
    }

    public void setDistrictColor(Polygon building, DistrictType type) {
        Color c = Color.BLACK;
        switch (type) {
            case MINE -> c = Color.rgb(100, 100, 100);
            case GARDEN -> c = Color.rgb(223, 197, 123);
            case BARN -> c = Color.rgb(125, 218, 88);
            case KILN -> c = Color.rgb(124, 22, 23, 1);
            case SAWMILL -> c = Color.rgb(93, 67, 35, 1);
        }

        building.setStroke(c);
        building.setStrokeWidth(2);
    }

    private void setAllVerticesVisibility(boolean visibility){
        //called in various places in handleVertexClick and handleRoadClick
        for(Circle vertex: vertices){
            if(vertex!=null){
                vertex.setVisible(visibility);
            }
        }
    }

    // Changing resource image code
    private void initAllImages() {
        initHexImages();
        setIconImages();
        initializePorts();
        initRobberIcon();
    }

    private void initHexImages() {
        Tile[] tiles = GameLoader.getInstance().getTiles();
        for(int i = 0; i < tiles.length; i++) {
            setHexBackground(hexagonTiles[i], tiles[i].getTerrain());
        }
    }

    private void setHexBackground(Polygon location, Terrain resource){
        //used in initialization
        Image backgroundImage = null;
        switch (resource) {
            case FORREST -> backgroundImage = getIconImage("tile_lumber.png");
            case HILLS -> backgroundImage = getIconImage("tile_brick.png");
            case PASTURE -> backgroundImage = getIconImage("tile_wool.png");
            case FIELDS -> backgroundImage = getIconImage("tile_wheat.png");
            case MOUNTAINS -> backgroundImage = getIconImage("tile_ore.png");
            case DESERT -> backgroundImage = getIconImage("tile_desert.png");
        }
        if(backgroundImage==null){
            return;
        }
        location.setFill(new ImagePattern(backgroundImage));
    }

    private void setIconImages(){
        recipes.setFill(new ImagePattern(getIconImage("recipes.PNG")));

        Image woodIcon = getIconImage("card_lumber.png");
        if (!woodIcon.isError()) {
            woodIcon1.setFill(new ImagePattern(woodIcon));
            woodIcon2.setFill(new ImagePattern(woodIcon));
            woodIcon3.setFill(new ImagePattern(woodIcon));
            woodIcon4.setFill(new ImagePattern(woodIcon));
        }

        Image brickIcon = getIconImage("card_brick.png");
        if (!brickIcon.isError()) {
            brickIcon1.setFill(new ImagePattern(brickIcon));
            brickIcon2.setFill(new ImagePattern(brickIcon));
            brickIcon3.setFill(new ImagePattern(brickIcon));
            brickIcon4.setFill(new ImagePattern(brickIcon));
        }

        Image woolIcon = getIconImage("card_wool.png");
        if (!woolIcon.isError()) {
            woolIcon1.setFill(new ImagePattern(woolIcon));
            woolIcon2.setFill(new ImagePattern(woolIcon));
            woolIcon3.setFill(new ImagePattern(woolIcon));
            woolIcon4.setFill(new ImagePattern(woolIcon));
        }

        Image grainIcon = getIconImage("card_wheat.png");
        if (!grainIcon.isError()) {
            grainIcon1.setFill(new ImagePattern(grainIcon));
            grainIcon2.setFill(new ImagePattern(grainIcon));
            grainIcon3.setFill(new ImagePattern(grainIcon));
            grainIcon4.setFill(new ImagePattern(grainIcon));
        }

        Image oreIcon = getIconImage("card_ore.png");
        if (!oreIcon.isError()) {
            oreIcon1.setFill(new ImagePattern(oreIcon));
            oreIcon2.setFill(new ImagePattern(oreIcon));
            oreIcon3.setFill(new ImagePattern(oreIcon));
            oreIcon4.setFill(new ImagePattern(oreIcon));
        }
    }

    private void setPortTrade(Circle port, Resource tradeType){
        //Called in initialization
        Image backgroundImage = null;
        switch (tradeType) {
            case ANY -> backgroundImage = getIconImage("improved_trade.png");
            case LUMBER -> backgroundImage = getIconImage("card_lumber.png");
            case BRICK -> backgroundImage = getIconImage("card_brick.png");
            case WOOL -> backgroundImage = getIconImage("card_wool.png");
            case GRAIN -> backgroundImage = getIconImage("card_wheat.png");
            case ORE -> backgroundImage = getIconImage("card_ore.png");
        }
        if(backgroundImage==null){
            return;
        }
        port.setFill(new ImagePattern(backgroundImage));
    }

    private void initRobberIcon() {
        robber.setFill(new ImagePattern(getIconImage("robber.png")));
    }

    public void changeIconSet(final String iconFolderPath) {
        this.iconFolderPath = iconFolderPath;
        initAllImages();
    }

    /**
     * Returns a valid path to the desired icon. If the icon file does exist
     * in the given folder, returns a path to that icon. If it doesn't, instead
     * return a path to the default image for that icon
     * @param iconName the name of the icon file (eg. tile_wood.png)
     * @return a valid path to that icon
     */
    private Image getIconImage(String iconName) {
        Path desiredPath = Path.of(iconFolderPath, iconName);
        URL desiredURL = getClass().getClassLoader().getResource(desiredPath.toString());

        if (desiredURL != null) {
            return new Image(desiredURL.toString());
        } else {
            return new Image(getClass().getClassLoader().getResource(Path.of(DEFAULT_ICON_FOLDER_PATH, iconName).toString()).toString());
        }
    }
}
