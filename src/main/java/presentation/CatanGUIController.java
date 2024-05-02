package presentation;

import domain.bank.Resource;
import domain.controller.GameState;
import domain.devcarddeck.DevCard;
import domain.building.DistrictType;
import domain.gameboard.Terrain;
import domain.gameboard.Tile;
import domain.graphs.*;
import domain.player.Player;
import data.GameLoader;
import domain.controller.Controller;
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
import static domain.controller.GameState.*;
import static domain.controller.SuccessCode.GAME_WIN;
import static domain.controller.SuccessCode.SUCCESS;
import static presentation.CatanGUIController.GUIState.BUSY;
import static presentation.CatanGUIController.GUIState.IDLE;

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
    private Button rollButton, buildSettlementButton, buildRoadButton, buildCityButton, buyDevCardButton, playKnightButton, playMonopolyButton, playRoadBuildingButton, playYearOfPlentyButton, endTurnButton, cancelButton, playerTradeButton, bankTradeButton, pauseButton, undoButton, redoButton, buildDistrictButton, bankLoanButton;
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
    public int robberId;

    public enum GUIState {
        BUSY, IDLE, GAME_WON
    }
    public GUIState guiState;

    private Controller controller; // facade to main game stuff

    private final List<Popup> popupsOpen = new ArrayList<>();

    public void notifyOfPopupClose(Popup popup) {
        this.popupsOpen.remove(popup);
        this.guiState = IDLE;
        updateInfoPane();
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

    /**
     * Initialize Game Objects, this is a distinct action from setting the visibility of dynamically enabled items.
     */
    @FXML
    private void initialize() {
        setupGUIEntityLists();  //FIRST

        settlementTemplate.setVisible(false);
        cityTemplate.setVisible(false);
        roadTemplate.setVisible(false);

        player1name.setFill(getPlayerColor(1));
        player2name.setFill(getPlayerColor(2));
        player3name.setFill(getPlayerColor(3));
        player4name.setFill(getPlayerColor(4));

        robber.setVisible(false);
        guiState = IDLE;

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
        bankLoanButton.setText(messages.getString("bankLoanText"));
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
        this.initializeRobber();
        this.initializePorts();
        this.hidePlayerStatsIfNeeded();

        // State specific data
        this.updateVertexes();
        this.updateRoads();
        this.updateInfoPane();

        // Visibility
        this.updateCircleVisibility();
        this.updateActionVisibility();
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

    private void initializeRobber() {
        Tile[] tiles = GameLoader.getInstance().getTiles();
        for(int i = 0; i < tiles.length; i++){
            if(tiles[i].getHasRobber()){
                double x = robberSpots[i].getLayoutX();
                double y = robberSpots[i].getLayoutY();

                renderRobberOnHex(x, y);
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

    // TODO: Maybe make this dynamically change based on the turn of the player?
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
        clearTooltipText();
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

        if(tempPlayers.length>=3) {
            player3wood.setText(Integer.toString(tempPlayers[2].hand.getResourceCount(Resource.LUMBER)));
            player3brick.setText(Integer.toString(tempPlayers[2].hand.getResourceCount(Resource.BRICK)));
            player3wool.setText(Integer.toString(tempPlayers[2].hand.getResourceCount(Resource.WOOL)));
            player3grain.setText(Integer.toString(tempPlayers[2].hand.getResourceCount(Resource.GRAIN)));
            player3ore.setText(Integer.toString(tempPlayers[2].hand.getResourceCount(Resource.ORE)));
            player3vp.setText(Integer.toString(tempPlayers[2].victoryPoints));
        }

        if(tempPlayers.length==4) {
            player4wood.setText(Integer.toString(tempPlayers[3].hand.getResourceCount(Resource.LUMBER)));
            player4brick.setText(Integer.toString(tempPlayers[3].hand.getResourceCount(Resource.BRICK)));
            player4wool.setText(Integer.toString(tempPlayers[3].hand.getResourceCount(Resource.WOOL)));
            player4grain.setText(Integer.toString(tempPlayers[3].hand.getResourceCount(Resource.GRAIN)));
            player4ore.setText(Integer.toString(tempPlayers[3].hand.getResourceCount(Resource.ORE)));
            player4vp.setText(Integer.toString(tempPlayers[3].victoryPoints));
        }
    }

    private void updateCircleVisibility() {
        switch (controller.getState()) {
            case TURN_START, DEFAULT -> {
                hideAllVertexes();
                hideAllRoads();
                hideAllRobberSpots();
                guiState = IDLE;
            }
            case FIRST_SETTLEMENT, SECOND_SETTLEMENT, BUILD_SETTLEMENT -> {
                showClickableVertexes();
                hideAllRoads();
                hideAllRobberSpots();
            }
            case FIRST_ROAD, SECOND_ROAD, BUILD_ROAD, ROAD_BUILDING_1, ROAD_BUILDING_2 -> {
                showClickableRoads();
                hideAllVertexes();
                hideAllRobberSpots();
            }
        }
    }

    private void updateActionVisibility() {
        switch (controller.getState()) {
            case TURN_START -> {
                handleStartOfTurnButtonVisibility();
            }
            default -> {
                handleInTurnButtonVisibility();
            }
        }
    }

    private void handleStartOfTurnButtonVisibility() {
        // Roll Button
        rollButton.setDisable(false);

        // Build Buttons
        buildSettlementButton.setDisable(true);
        buildRoadButton.setDisable(true);
        buildCityButton.setDisable(true);
        buildDistrictButton.setDisable(true);
        cancelButton.setDisable(true);

        // Dev Card Buttons
        buyDevCardButton.setDisable(true);
        playKnightButton.setDisable(true);
        playYearOfPlentyButton.setDisable(true);
        playMonopolyButton.setDisable(true);
        playRoadBuildingButton.setDisable(true);

        // Trade Buttons
        playerTradeButton.setDisable(true);
        bankTradeButton.setDisable(true);
        bankLoanButton.setDisable(true);

        // End Turn Button
        endTurnButton.setDisable(true);

        // Pause Button
        pauseButton.setDisable(false);
    }
    private void handleInTurnButtonVisibility() {
        // Roll Button
        rollButton.setDisable(true);

        // Build Buttons
        buildSettlementButton.setDisable(false);
        buildRoadButton.setDisable(false);
        buildCityButton.setDisable(false);
        buildDistrictButton.setDisable(false);
        cancelButton.setDisable(false);

        // Dev Card Buttons
        buyDevCardButton.setDisable(false);
        playKnightButton.setDisable(false);
        playYearOfPlentyButton.setDisable(false);
        playMonopolyButton.setDisable(false);
        playRoadBuildingButton.setDisable(false);

        // Trade Buttons
        playerTradeButton.setDisable(false);
        bankTradeButton.setDisable(false);
        bankLoanButton.setDisable(false);

        // End Turn Button
        endTurnButton.setDisable(false);

        // Pause Button
        pauseButton.setDisable(true);
    }

    // ----------------------------------------------------------------
    //
    // Game State Flow Control
    //
    // ----------------------------------------------------------------

    public void pauseButtonPressed() throws IOException {
        if (controller.getState() == TURN_START  && guiState == IDLE) {
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

            this.guiState = BUSY;
            setTooltipText("paused");
        } else {
            setTooltipText("pauseNotAllowed");
        }
    }

    public void endTurnButtonPressed() throws IOException {
        if (controller.getState() == DEFAULT && guiState == IDLE) {
            switch (controller.endTurn()) {
                case SUCCESS -> {
                    updateInfoPane();
                    setTooltipText("rollDice");

                    controller.setState(TURN_START);
                    GameLoader.getInstance().notifyOfTurnStart();
                    updateActionVisibility();
                }
                case GAME_WIN -> applyGameWon();
            }
        }
    }

    public void undo() throws IOException {
        if (GameLoader.getInstance().undo()) {
            repaint();
        } else {
            setTooltipText("undoFail");
        }
    }

    public void redo() throws IOException {
        if (GameLoader.getInstance().redo()) {
            repaint();
        } else {
            setTooltipText("redoFail");
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

        // initialize the game-board
        guiController.internationalize(messages);
        guiController.initializeGameBoard();

        updateActionVisibility();
    }

    private void closePopups() {
        for (Popup popup: popupsOpen.toArray(new Popup[0])) {
            popup.close();
        }
    }

    public void finishedMove() {
        updateInfoPane();
        guiState = IDLE;
        clearTooltipText();
        updateActionVisibility();
    }

    //this method is called to disable everything on the board once a player has won the game
    private void applyGameWon() throws IOException {
        // update the gui state
        this.guiState = GUIState.GAME_WON;
        setTooltipText("gameOver");
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
        disableAllActions();
    }

    private void disableAllActions() {
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

    public void handleVertexClick(MouseEvent event) throws IOException {
        int currentPlayer = controller.getCurrentPlayer().playerNum;
        int vertexId = Integer.parseInt(((Circle)event.getSource()).getId().substring("vertex".length()));

        switch (controller.clickedVertex(vertexId)) {
            case SUCCESS -> {
                renderSettlementOnVertex(vertices[vertexId], getPlayerColor(currentPlayer));
                updateCircleVisibility();
                updateInfoPane();
            }
            case INSUFFICIENT_RESOURCES -> setTooltipText("notEnoughResourcesOrSettlements");
            case INVALID_PLACEMENT -> setTooltipText("cannotBuildSettlementHere");
            case GAME_WIN -> {
                renderSettlementOnVertex(vertices[vertexId], getPlayerColor(currentPlayer));
                applyGameWon();
            }
        }
    }

    public void handleRoadClick(MouseEvent event) throws IOException {
        int currentPlayer = controller.getCurrentPlayer().playerNum;
        int roadId = Integer.parseInt(((Circle) event.getSource()).getId().substring("road".length()));

        switch (controller.clickedRoad(roadId)) {
            case SUCCESS -> {
                renderRoad(roadId, currentPlayer);
                updateInfoPane();
                updateCircleVisibility();
            }
            case INSUFFICIENT_RESOURCES -> setTooltipText("notEnoughResourcesOrRoads");
            case INVALID_PLACEMENT -> setTooltipText("cannotBuildRoadHere");
            case GAME_WIN -> {
                renderRoad(roadId, currentPlayer);
                applyGameWon();
            }
        }
    }

    // ROBBER METHODS
    public void handleRobberClick(MouseEvent event) throws IOException {
        robberId = Integer.parseInt(((Circle) event.getSource()).getId().substring("robber".length()));

        if (controller.moveRobber(robberId) == SUCCESS) {
            renderRobberOnHex(((Circle) event.getSource()).getLayoutX(), ((Circle) event.getSource()).getLayoutY());

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

            guiState = IDLE;
            controller.setState(DEFAULT);
            updateCircleVisibility();
        } else {
            setTooltipText("cannotMoveRobberHere");
        }
    }

    public void buildSettlementButtonPress() {
        if (controller.getState() == DEFAULT) {
            guiState = BUSY;
            controller.setState(BUILD_SETTLEMENT);
            updateCircleVisibility();
        }
    }

    public void buildRoadButtonPress() {
        if (controller.getState() == DEFAULT) {
            guiState = BUSY;
            controller.setState(BUILD_ROAD);
            updateCircleVisibility();
        }
    }

    public void buildCityButtonPress() {
        if (controller.getState() == DEFAULT) {
            guiState = BUSY;
            setTooltipText("selectSettlementToUpgrade");
            controller.setState(UPGRADE_SETTLEMENT);
        }
    }

    public void buildDistrictButtonPress() {
        if (controller.getState() == DEFAULT) {
            guiState = BUSY;
            controller.setState(BUILD_DISTRICT);
        }
    }

    public void buyDevCardButtonPress() throws IOException {
        //Triggered by Buy Development Card button pressed
        if(this.controller.getState()== DEFAULT || this.controller.getState() == TURN_START){
            SuccessCode success = controller.clickedBuyDevCard();
            if(success == SUCCESS){
                setTooltipText("purchasedDevCard");
                this.updateInfoPane();
            }else if(success == SuccessCode.INSUFFICIENT_RESOURCES){
                setTooltipText("notEnoughResourcesDevCard");
            }else if(success == SuccessCode.EMPTY_DEV_CARD_DECK){
                setTooltipText("devCardDeckEmpty");
            }else if(success == GAME_WIN){
                applyGameWon();
            }
        }
    }

    public void rollButtonPressed() throws IOException {
        if (controller.getState() == TURN_START && guiState == IDLE) {
            int dieNumber = controller.rollDice();
            updateInfoPane();
            tooltipText.setText(dieNumber + " " + messages.getString("dieNumberRolled"));

            switch (dieNumber) {
                case 7 -> doRobber();
                case 12 -> {
                    Controller.WeatherEvent weatherEvent = controller.createWeatherEvent();

                    // Build the weather event message
                    String messagePattern = messages.getString("weatherEvent");
                    Object[] params = {messages.getString(weatherEvent.boostType().toString()),
                            messages.getString(weatherEvent.resource().toString()),
                            weatherEvent.forEveryone() ? messages.getString("everyone") : messages.getString("onlyPlayer")};
                    String weatherEventMessage = MessageFormat.format(messagePattern, params);

                    // display it to the user
                    tooltipText.setText(weatherEventMessage);
                    controller.setState(DEFAULT);
                }
                default -> controller.setState(DEFAULT);
            }

            updateActionVisibility();
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

        setTooltipText("sevenRolled");
        guiState = BUSY;
    }

    public void cancelButtonPressed() {
        switch (controller.getState()) {
            case BUILD_ROAD, BUILD_SETTLEMENT, UPGRADE_SETTLEMENT -> {
                controller.setState(DEFAULT);
                updateCircleVisibility();
                guiState = IDLE;
                clearTooltipText();
            }
            default -> setTooltipText("cannotCancel");
        }
    }

    public void playKnightButtonPressed() throws IOException {
        if (controller.getState() == DEFAULT && guiState == IDLE) {
            switch (controller.playKnightCard()) {
                case SUCCESS -> {
                    showRobberSpots();
                    setTooltipText("moveRobber");
                    guiState = BUSY;
                }
                case CANNOT_PLAY_CARD -> setTooltipText("cannotPlayDevCard");
                case GAME_WIN -> applyGameWon();
            }
        }
    }

    public void playMonopolyButtonPressed() throws IOException {
        //Triggered by Play Monopoly card pressed
        if(this.controller.getState()== DEFAULT && this.guiState == IDLE){
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

            this.guiState = BUSY;
            setTooltipText("playingMonopoly");
        }
    }

    public void playRoadBuildingButtonPressed() {
        if (controller.getState() == DEFAULT && guiState == IDLE) {
            switch (controller.useRoadBuildingCard()) {
                case SUCCESS ->  {
                    updateCircleVisibility();
                    setTooltipText("playRoadBuilding");
                    guiState = BUSY;
                }
                case CANNOT_PLAY_CARD -> setTooltipText("cannotPlayDevCard");
            }
        }
    }

    public void playYearOfPlentyButtonPressed() throws IOException {
        //Triggered by Play YearOfPlenty card pressed
        if(this.controller.getState()== DEFAULT && this.guiState== IDLE){
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

            setTooltipText("playingYearOfPlenty");
            this.guiState = BUSY;
        }
    }

    public void playerTradeButtonPressed() throws IOException {
        //Triggered by Player Trade button pressed
        if (this.controller.getState() == DEFAULT && this.guiState == IDLE) {
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

            setTooltipText("playerTrade");

            this.guiState = BUSY;
        }
    }

    public void bankTradeButtonPressed() throws IOException {
        //Triggered by Trade with Bank button pressed
        if(this.controller.getState() == DEFAULT && this.guiState == IDLE) {
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

            setTooltipText("bankTrade");
            this.guiState = BUSY;
        }
    }

    public void bankLoanButtonPressed() throws IOException {
        if(this.controller.getState() == GameState.DEFAULT && this.guiState == GUIState.IDLE) {
            FXMLLoader fxmlLoader = new FXMLLoader(Catan.class.getResource("BankLoanWindow.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle(messages.getString("bankTradeTitle"));
            stage.setScene(scene);
            stage.show();

            BankLoanWindowController bankLoanController = fxmlLoader.getController();
            bankLoanController.setControllers(this, this.controller);
            bankLoanController.setMessages(this.messages);
            this.popupsOpen.add(bankLoanController);

            this.tooltipText.setText(messages.getString("bankLoanGameTooltip"));
            this.guiState = GUIState.BUSY;
        }
    }

    public void handleSettlementClick(MouseEvent event) {
        Polygon clickedSettlement = (Polygon) event.getSource();
        int vertex = settlementToVertexMap.get(clickedSettlement);

        switch (controller.getState()) {
            case UPGRADE_SETTLEMENT -> {
                try {
                    handleUpgradeSettlement(clickedSettlement, vertex);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case BUILD_DISTRICT -> {
                try {
                    handleBuildDistrict(vertex, clickedSettlement);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void handleUpgradeSettlement(Polygon clickedSettlement, int vertex) throws IOException {
        switch (controller.clickedVertex(vertex)) {
            case SUCCESS -> {
                settlementToVertexMap.remove(clickedSettlement);
                gameboardPane.getChildren().remove(clickedSettlement);
                renderCityOnVertex(clickedSettlement.getLayoutX(), clickedSettlement.getLayoutY(), vertex);
                updateInfoPane();
                setTooltipText("builtCity");
                guiState = IDLE;
            }
            case INVALID_PLACEMENT -> setTooltipText("cannotPlaceCity");
            case INSUFFICIENT_RESOURCES -> setTooltipText("notEnoughResourcesCity");
            case GAME_WIN -> {
                settlementToVertexMap.remove(clickedSettlement);
                gameboardPane.getChildren().remove(clickedSettlement);
                renderCityOnVertex(clickedSettlement.getLayoutX(), clickedSettlement.getLayoutY(), vertex);
                applyGameWon();
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

        this.guiState = BUSY;
        setTooltipText("buildingDistrict");
    }

    public void setTooltipText(String in18Key) {
        this.tooltipText.setText(messages.getString(in18Key));
    }

    public void clearTooltipText() {
        this.tooltipText.setText("");
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

    private void hideAllRoads() {
        //Called in various places in handleRoadClick and handleVertexClick
        for (Circle road: roadMarkers) {
            if (road != null) {
                road.setVisible(false);
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

    private void hideAllRobberSpots() {
        for (Circle robberSpot: robberSpots) {
            if (robberSpot != null) {
                robberSpot.setVisible(false);
            }
        }
    }

    public void showRobberSpots() {
        hideAllRobberSpots();
        for (Integer i : controller.getValidRobberSpots()) {
            System.out.println(i);
            robberSpots[i].setVisible(true);
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
        Polygon newSettlement = createSettlement(x, y, color);
        setDistrictColor(newSettlement, controller.getDistrictTypeForVertex(vertexId));

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
            case MINE -> c = Color.rgb(140, 140, 140);
            case GARDEN -> c = Color.rgb(223, 197, 123);
            case BARN -> c = Color.rgb(125, 218, 88);
            case KILN -> c = Color.rgb(124, 22, 23, 1);
            case SAWMILL -> c = Color.rgb(93, 67, 35, 1);
        }

        building.setStroke(c);
        building.setStrokeWidth(2);
    }

    private void hideAllVertexes() {
        // called in various places in handleVertexClick and handleRoadClick
        for (Circle vertex: vertices) {
            if (vertex != null) {
                vertex.setVisible(false);
            }
        }
    }

    private void showClickableVertexes() {
        hideAllVertexes();
        for (Integer i : controller.getBuildableVertexes()) {
            vertices[i].setVisible(true);
        }
    }

    private void showClickableRoads() {
        hideAllRoads();
        for (Integer i : controller.getBuildableRoads()) {
            roadMarkers[i].setVisible(true);
        }
    }

    // Changing resource image code
    public void initAllImages() {
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

    private void setHexBackground(Polygon location, Terrain resource) {
        //used in initialization
        ImagePattern backgroundImage = null;
        switch (resource) {
            case FORREST -> backgroundImage = GameLoader.getInstance().getImage("tile_lumber.png");
            case HILLS -> backgroundImage = GameLoader.getInstance().getImage("tile_brick.png");
            case PASTURE -> backgroundImage = GameLoader.getInstance().getImage("tile_wool.png");
            case FIELDS -> backgroundImage = GameLoader.getInstance().getImage("tile_wheat.png");
            case MOUNTAINS -> backgroundImage = GameLoader.getInstance().getImage("tile_ore.png");
            case DESERT -> backgroundImage = GameLoader.getInstance().getImage("tile_desert.png");
        }
        if(backgroundImage==null){
            return;
        }
        location.setFill(backgroundImage);
    }

    private void setIconImages(){
        recipes.setFill(GameLoader.getInstance().getImage("recipes.PNG"));

        ImagePattern woodIcon = GameLoader.getInstance().getImage("card_lumber.png");
        woodIcon1.setFill(woodIcon);
        woodIcon2.setFill(woodIcon);
        woodIcon3.setFill(woodIcon);
        woodIcon4.setFill(woodIcon);

        ImagePattern brickIcon = GameLoader.getInstance().getImage("card_brick.png");
        brickIcon1.setFill(brickIcon);
        brickIcon2.setFill(brickIcon);
        brickIcon3.setFill(brickIcon);
        brickIcon4.setFill(brickIcon);

        ImagePattern woolIcon = GameLoader.getInstance().getImage("card_wool.png");
        woolIcon1.setFill(woolIcon);
        woolIcon2.setFill(woolIcon);
        woolIcon3.setFill(woolIcon);
        woolIcon4.setFill(woolIcon);

        ImagePattern grainIcon = GameLoader.getInstance().getImage("card_wheat.png");
        grainIcon1.setFill(grainIcon);
        grainIcon2.setFill(grainIcon);
        grainIcon3.setFill(grainIcon);
        grainIcon4.setFill(grainIcon);

        ImagePattern oreIcon = GameLoader.getInstance().getImage("card_ore.png");
        oreIcon1.setFill(oreIcon);
        oreIcon2.setFill(oreIcon);
        oreIcon3.setFill(oreIcon);
        oreIcon4.setFill(oreIcon);
    }

    private void setPortTrade(Circle port, Resource tradeType){
        //Called in initialization
        ImagePattern backgroundImage = null;
        switch (tradeType) {
            case ANY -> backgroundImage = GameLoader.getInstance().getImage("improved_trade.png");
            case LUMBER -> backgroundImage = GameLoader.getInstance().getImage("card_lumber.png");
            case BRICK -> backgroundImage = GameLoader.getInstance().getImage("card_brick.png");
            case WOOL -> backgroundImage = GameLoader.getInstance().getImage("card_wool.png");
            case GRAIN -> backgroundImage = GameLoader.getInstance().getImage("card_wheat.png");
            case ORE -> backgroundImage = GameLoader.getInstance().getImage("card_ore.png");
        }
        if(backgroundImage==null){
            return;
        }
        port.setFill(backgroundImage);
    }

    private void initRobberIcon() {
        robber.setFill(GameLoader.getInstance().getImage("robber.png"));
    }
}
