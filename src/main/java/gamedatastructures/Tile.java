package gamedatastructures;

import saving.*;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.File;
import java.util.Arrays;

public class Tile implements Restorable {
    private static final int NUM_VERTEXES = 6;
    private Terrain terrainType;
    private int dieNumber;
    private final int tileNumber;
    private boolean hasRobber;
    private int[] vertexIDs = new int[NUM_VERTEXES];

    public Tile(final Terrain terrain, final int dieNum, final int tileNum, final boolean robber, final int[] vertexs) {
        this.terrainType = terrain;
        this.dieNumber = dieNum;
        this.tileNumber = tileNum;
        this.hasRobber = robber;
        this.vertexIDs = Arrays.copyOf(vertexs, NUM_VERTEXES);
    }
    /**
     * Sets the hasRobber boolean.
     * @param robber is on tile
     */
    // public void setRobber(final boolean robber) {
    //     this.hasRobber = robber;
    // }
    /**
     * Returns the vertex list of the tile
     * @return this.vertexes the list of vertexs
     */
    public int[] getVertexIDs() {
        return Arrays.copyOf(this.vertexIDs, NUM_VERTEXES);
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
    //testing purposes only
    void setDie(final int newDie) {
        this.dieNumber = newDie;
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
        private final int[] vertexIDs;

        // Storage Constants
        private static final String TARGET_FILE_NAME = "tile.txt";

        // Field Keys
        private static final String TERRAIN_TYPE = "TerrainType";
        private static final String DIE_NUMBER = "DieNumber";
        private static final String HAS_ROBBER = "HasRobber";
        private static final String VERTEX_IDS = "VertexIDs";

        private TileMemento() {
            this.terrainType = Tile.this.terrainType;
            this.dieNumber = Tile.this.dieNumber;
            this.hasRobber = Tile.this.hasRobber;
            this.vertexIDs = Tile.this.vertexIDs;
        }

        @SuppressFBWarnings("EI_EXPOSE_REP2")
        public TileMemento(final File folder) {
            // Create a MementoReader for reading memento data
            MementoReader reader = new MementoReader(folder, TARGET_FILE_NAME);

            // Read simple fields from the file
            this.terrainType = Terrain.valueOf(reader.readField(TERRAIN_TYPE));
            this.dieNumber = Integer.parseInt(reader.readField(DIE_NUMBER));
            this.hasRobber = Boolean.parseBoolean(reader.readField(HAS_ROBBER));
            this.vertexIDs = parseVertexIDs(reader.readField(VERTEX_IDS));
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
            writer.writeField(VERTEX_IDS, Arrays.toString(vertexIDs));
        }

        public void restore() {
            // Restore simple fields
            Tile.this.terrainType = this.terrainType;
            Tile.this.dieNumber = this.dieNumber;
            Tile.this.hasRobber = this.hasRobber;
            System.arraycopy(this.vertexIDs, 0, Tile.this.vertexIDs, 0, this.vertexIDs.length);
        }
    }

    @Override
    public Memento createMemento() {
        return new TileMemento();
    }
}