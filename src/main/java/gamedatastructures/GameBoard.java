package gamedatastructures;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import saving.*;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class GameBoard implements Restorable {
    private static final int NUM_TILES = 19;
    private final Terrain[] terrainOrder = {Terrain.MOUNTAINS, Terrain.PASTURE, Terrain.FORREST, Terrain.FIELDS, Terrain.HILLS, Terrain.PASTURE, Terrain.HILLS, Terrain.FIELDS, Terrain.FORREST,  Terrain.DESERT, Terrain.FORREST, Terrain.MOUNTAINS, Terrain.FORREST, Terrain.MOUNTAINS, Terrain.FIELDS, Terrain.PASTURE, Terrain.HILLS, Terrain.FIELDS, Terrain.PASTURE};
    private final Integer[] dieOrder = {10, 2, 9, 12, 6, 4, 10, 9, 11, 7, 3, 8, 8, 3, 4, 5, 5, 6, 11};
    private static final int NUM_VERTEXES = 6;
    private Tile robberTile;
    private Tile[] tiles;
    /**
     * Initializes a new gameboard with specified game type.
     * @param type the game difficulty
     */
    public GameBoard(final GameType type, final String filepath) {
        if (type == GameType.Advanced) {
            Collections.shuffle(Arrays.asList(this.terrainOrder));
            Collections.shuffle(Arrays.asList(this.dieOrder));
        }
        generateTiles(filepath);
    }
    /**
     * Generates from the layout file and initializes the tile array
     */
    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    private void generateTiles(final String filePath) {
        Tile[] tilesTemp = new Tile[NUM_TILES];
        File tileLayout = new File(filePath);
        try (Scanner scanner = new Scanner(tileLayout, StandardCharsets.UTF_8.name());) {
            int tileNum = 0;
            while (scanner.hasNextLine()) {
                boolean robber = terrainOrder[tileNum] == Terrain.DESERT;
                String line = scanner.nextLine();
                String[] values = line.split(" ");
                int[] cornerVertexIDs = new int[NUM_VERTEXES];

                for (int i = 0; i < values.length; i++) {
                    int vertexID = Integer.parseInt(values[i]);
                    cornerVertexIDs[i] = vertexID;
                }
                tilesTemp[tileNum] = new Tile(terrainOrder[tileNum], dieOrder[tileNum], tileNum, robber, Arrays.copyOf(cornerVertexIDs, NUM_VERTEXES));
                if (robber) {
                    this.robberTile = tilesTemp[tileNum];
                }
                tileNum++;
            }
            this.tiles = tilesTemp;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Incorrect filename/path when initialized");
        }
    }
    /**
     * Returns the corner vertexs of a tile
     * @param tileNum the number of the tile with desired vertexes
     * @return int[] the vertex IDs of tile with tileNum
     */
    public int[] getTileVertexIDs(final int tileNum) {
        return this.tiles[tileNum].getVertexIDs();
    }
    /**
     * Returns the tile array
     * @return this.tiles
     */
    public Tile[] getTiles() {
        return Arrays.copyOf(this.tiles, NUM_TILES);
    }
    /**
     * Returns the tile that currentley has a robber
     * @return Tile the robber tile
     */
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public Tile getRobberTile() {
        return  this.robberTile;
    }
    /**
     * Sets the robber tile to the inputed tile
     * @param tile
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

            // Read sub-mementos from the appropriate subfolders
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

        public void save(final File folder) throws SaveException {
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
