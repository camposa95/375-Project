package domain.gameboard;

import domain.bank.Resource;
import data.*;
import domain.graphs.Vertex;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Tile implements Restorable {
    private Terrain terrainType;
    private int dieNumber;
    private final int tileNumber;
    private boolean hasRobber;
    private List<Integer> vertexIDs;

    // initialization flags:
    private boolean vertexIDsInitialized;

    // -----------------------------------------------
    // Initialization
    // -----------------------------------------------

    public Tile(final Terrain terrain, final int dieNum, final int tileNum, final boolean robber) {
        this.terrainType = terrain;
        this.dieNumber = dieNum;
        this.tileNumber = tileNum;
        this.hasRobber = robber;
    }

    public void setAdjacentVertexes(final List<Integer> vertexIds) {
        if (this.vertexIDsInitialized) {
            throw new IllegalCallerException("Cannot re-set the adjacent vertexes after initialization");
        }
        this.vertexIDs = Collections.unmodifiableList(vertexIds);
        this.vertexIDsInitialized = true;
    }

    // -----------------------------------------------
    // End Initialization methods
    // -----------------------------------------------

    /**
     * Returns the vertex list of the tile
     * @return this.vertexes the list of vertexs
     */
    public List<Integer> getVertexIDs() {
        return this.vertexIDs;
    }

    /**
     * Returns the terrain type of the tile
     * @return this.terrainType
     */
    public Terrain getTerrain() {
        return this.terrainType;
    }

    /**
     * Returns the resource based on terrain type
     * @return Resource
     */
    public Resource getResource() {
        switch (this.terrainType) {
        default: return null;
        case HILLS: return Resource.BRICK;
        case FORREST: return Resource.LUMBER;
        case MOUNTAINS: return Resource.ORE;
        case FIELDS: return Resource.GRAIN;
        case PASTURE: return Resource.WOOL;
        }
    }

    /**
     * Returns the die number of the tile
     * @return this.dieNumber
     */
    public int getDieNumber() {
        return this.dieNumber;
    }

    /**
     * Returns the tile number that corresponds to the schema
     * @return this.tileNumber the id of the tile
     */
    public int getTileNumber() {
        return this.tileNumber;
    }

    /**
     * Returns if the tile has the robber on it
     * @return this.hasRobber
     */
    public boolean getHasRobber() {
        return this.hasRobber;
    }

    /**
     * Sets the robber field true or false based on input
     * @param has
     */
    public void setRobber(final Boolean has) {
        this.hasRobber = has;
    }

    // -----------------------------------
    //
    // Restorable implementation
    //
    // -----------------------------------

    public class TileMemento implements Memento {
        private final Terrain terrainType;
        private final int dieNumber;
        private final boolean hasRobber;

        // Storage Constants
        private static final String TARGET_FILE_NAME = "tile.txt";

        // Field Keys
        private static final String TERRAIN_TYPE = "TerrainType";
        private static final String DIE_NUMBER = "DieNumber";
        private static final String HAS_ROBBER = "HasRobber";

        private TileMemento() {
            this.terrainType = Tile.this.terrainType;
            this.dieNumber = Tile.this.dieNumber;
            this.hasRobber = Tile.this.hasRobber;
        }

        @SuppressFBWarnings("EI_EXPOSE_REP2")
        public TileMemento(final File folder) {
            // Create a MementoReader for reading memento data
            MementoReader reader = new MementoReader(folder, TARGET_FILE_NAME);

            // Read simple fields from the file
            this.terrainType = Terrain.valueOf(reader.readField(TERRAIN_TYPE));
            this.dieNumber = Integer.parseInt(reader.readField(DIE_NUMBER));
            this.hasRobber = Boolean.parseBoolean(reader.readField(HAS_ROBBER));
        }

        private int[] parseVertexIDs(final String vertexIDsString) {
            String[] vertexIDValues = vertexIDsString.substring(1, vertexIDsString.length() - 1).split(", ");

            int[] ids = new int[vertexIDValues.length];
            for (int i = 0; i < vertexIDValues.length; i++) {
                ids[i] = Integer.parseInt(vertexIDValues[i].trim());
            }

            return ids;
        }

        public void save(final File folder) throws SaveException {
            // Create a MementoWriter for writing memento data
            MementoWriter writer = new MementoWriter(folder, TARGET_FILE_NAME);

            // Write simple fields to the file
            writer.writeField(TERRAIN_TYPE, terrainType.toString());
            writer.writeField(DIE_NUMBER, Integer.toString(dieNumber));
            writer.writeField(HAS_ROBBER, Boolean.toString(hasRobber));
        }

        public void restore() {
            // Restore simple fields
            Tile.this.terrainType = this.terrainType;
            Tile.this.dieNumber = this.dieNumber;
            Tile.this.hasRobber = this.hasRobber;
        }
    }

    @Override
    public Memento createMemento() {
        return new TileMemento();
    }
}