package domain.gameboard;

import java.io.File;
import java.io.IOException;
import java.util.*;

import domain.game.GameType;
import data.*;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class GameBoard implements Restorable {
    private static final int NUM_TILES = 19;
    private final Terrain[] terrainOrder = {Terrain.MOUNTAINS, Terrain.PASTURE, Terrain.FORREST, Terrain.FIELDS, Terrain.HILLS, Terrain.PASTURE, Terrain.HILLS, Terrain.FIELDS, Terrain.FORREST,  Terrain.DESERT, Terrain.FORREST, Terrain.MOUNTAINS, Terrain.FORREST, Terrain.MOUNTAINS, Terrain.FIELDS, Terrain.PASTURE, Terrain.HILLS, Terrain.FIELDS, Terrain.PASTURE};
    private final Integer[] dieOrder = {10, 2, 9, 12, 6, 4, 10, 9, 11, 7, 3, 8, 8, 3, 4, 5, 5, 6, 11};
    private Tile robberTile;
    private final Tile[] tiles;

    /**
     * Initializes a new GameBoard with specified game type.
     * @param type the game difficulty
     */
    public GameBoard(final GameType type) {
        if (type == GameType.Advanced) {
            Collections.shuffle(Arrays.asList(this.terrainOrder));
            Collections.shuffle(Arrays.asList(this.dieOrder));
        }

        this.tiles = new Tile[NUM_TILES];
        for (int i = 0; i < NUM_TILES; i++) {
            boolean robber = terrainOrder[i] == Terrain.DESERT;

            tiles[i] = new Tile(terrainOrder[i], dieOrder[i], i, robber);

            if (robber) {
                this.robberTile = tiles[i];
            }
        }
    }

    /**
     * Returns the corner vertexes of a tile
     * @param tileNum the number of the tile with desired vertexes
     * @return int[] the vertex IDs of tile with tileNum
     */
    public List<Integer> getTileVertexIDs(final int tileNum) {
        return this.tiles[tileNum].getVertexIDs();
    }

    public Tile getTile(final int locationId) {

        if (locationId >= NUM_TILES || locationId < 0) {
            throw new IllegalArgumentException("Invalid locationId; Try[0, 18]");
        }

        return this.tiles[locationId];
    }

    /**
     * Returns the tile array
     * @return this.tiles
     */
    public Tile[] getTiles() {
        return Arrays.copyOf(this.tiles, NUM_TILES);
    }

    /**
     * Returns the tile that currently has a robber
     * @return Tile the robber tile
     */
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public Tile getRobberTile() {
        return  this.robberTile;
    }

    /**
     * Sets the robber tile to the inputted tile
     */
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setRobberTile(final Tile tile) {
        this.robberTile = tile;
    }

    // -----------------------------------
    //
    // Restorable implementation
    //
    // -----------------------------------

    public class GameBoardMemento implements Memento {
        // simple fields
        private final Terrain[] terrainOrder;
        private final Integer[] dieOrder;
        private final int robberTileNum;

        // sub mementos
        private final Memento[] tileMementos;

        // Storage Constants
        private static final String TARGET_FILE_NAME = "gameboard.txt";
        private static final String TILE_SUBFOLDER_PREFIX = "Tile";

        // Field Keys
        private static final String TERRAIN_ORDER = "TerrainOrder";
        private static final String DIE_ORDER = "DieOrder";
        private static final String ROBBER_TILE_NUM = "RobberTileNum";

        private GameBoardMemento() {
            this.terrainOrder = Arrays.copyOf(GameBoard.this.terrainOrder, GameBoard.this.terrainOrder.length);
            this.dieOrder = Arrays.copyOf(GameBoard.this.dieOrder, GameBoard.this.dieOrder.length);
            this.robberTileNum = GameBoard.this.robberTile.getTileNumber();

            // sub mementos
            this.tileMementos = new Memento[GameBoard.this.tiles.length];
            for (int i = 0; i < GameBoard.this.tiles.length; i++) {
                this.tileMementos[i] = GameBoard.this.tiles[i].createMemento();
            }
        }

        @SuppressFBWarnings("EI_EXPOSE_REP2")
        public GameBoardMemento(final File folder) {
            // Create a MementoReader for reading memento data
            MementoReader reader = new MementoReader(folder, TARGET_FILE_NAME);

            // Read simple fields from the file
            this.terrainOrder = parseTerrainArr(reader.readField(TERRAIN_ORDER));
            this.dieOrder = parseDieArr(reader.readField(DIE_ORDER));
            this.robberTileNum = Integer.parseInt(reader.readField(ROBBER_TILE_NUM));

            // Read sub-mementos from the appropriate sub-folders
            this.tileMementos = new Memento[GameBoard.this.tiles.length];
            for (int i = 0; i < this.tileMementos.length; i++) {
                File tileSubFolder = reader.getSubFolder(TILE_SUBFOLDER_PREFIX + i);
                this.tileMementos[i] = GameBoard.this.tiles[i].new TileMemento(tileSubFolder);
            }
        }

        private Terrain[] parseTerrainArr(final String terrainOrderString) {
            String[] terrainValues = terrainOrderString.substring(1, terrainOrderString.length() - 1).split(", ");

            Terrain[] terrains = new Terrain[terrainValues.length];
            for (int i = 0; i < terrainValues.length; i++) {
                String terrainValue = terrainValues[i].trim().toUpperCase();
                terrains[i] = Terrain.valueOf(terrainValue);
            }

            return terrains;
        }

        private Integer[] parseDieArr(final String dieOrderString) {
            String[] dieValues = dieOrderString.substring(1, dieOrderString.length() - 1).split(", ");

            Integer[] faces = new Integer[dieValues.length];
            for (int i = 0; i < dieValues.length; i++) {
                faces[i] = Integer.parseInt(dieValues[i].trim());
            }

            return faces;
        }

        public void save(final File folder) throws IOException {
            // Create a MementoWriter for writing memento data
            MementoWriter writer = new MementoWriter(folder, TARGET_FILE_NAME);

            // Write simple fields to the file
            writer.writeField(TERRAIN_ORDER, Arrays.toString(terrainOrder));
            writer.writeField(DIE_ORDER, Arrays.toString(dieOrder));
            writer.writeField(ROBBER_TILE_NUM, Integer.toString(robberTileNum));

            // Save sub mementos' state
            for (int i = 0; i < tileMementos.length; i++) {
                // Create a subfolder for each tile's memento
                File tileSubFolder = writer.getSubFolder(TILE_SUBFOLDER_PREFIX + i);
                tileMementos[i].save(tileSubFolder);
            }
        }

        public void restore() {
            // Restore simple fields
            System.arraycopy(this.terrainOrder, 0, GameBoard.this.terrainOrder, 0, this.terrainOrder.length);
            System.arraycopy(this.dieOrder, 0, GameBoard.this.dieOrder, 0, this.dieOrder.length);

            GameBoard.this.setRobberTile(GameBoard.this.tiles[this.robberTileNum]);

            // Restore sub mementos
            for (Memento tileMemento : tileMementos) {
                tileMemento.restore();
            }
        }
    }

    @Override
    public Memento createMemento() {
        return new GameBoardMemento();
    }
}
