package gamedatastructures;

import SavingAndLoading.Memento;
import SavingAndLoading.MementoWriter;
import SavingAndLoading.Restorable;

import java.io.File;
import java.util.Arrays;

public class Tile implements Restorable {
    private static final int NUM_VERTEXES = 6;
    private Terrain terrainType;
    private int dieNumber;
    private int tileNumber;
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
        private Terrain terrainType;
        private int dieNumber;
        private int tileNumber;
        private boolean hasRobber;
        private int[] vertexIDs;

        private TileMemento() {
            this.terrainType = Tile.this.terrainType;
            this.dieNumber = Tile.this.dieNumber;
            this.tileNumber = Tile.this.tileNumber;
            this.hasRobber = Tile.this.hasRobber;
            this.vertexIDs = Tile.this.vertexIDs;
        }

        @Override
        public void save(File folder) {
            // Create a MementoWriter for writing memento data
            MementoWriter writer = new MementoWriter(folder, "tile_" + tileNumber + ".txt");

            // Write simple fields to the file
            writer.writeField("TerrainType", terrainType.toString());
            writer.writeField("DieNumber", Integer.toString(dieNumber));
            writer.writeField("TileNumber", Integer.toString(tileNumber));
            writer.writeField("HasRobber", Boolean.toString(hasRobber));
            writer.writeField("VertexIDs", Arrays.toString(vertexIDs));
        }
    }

    @Override
    public Memento createMemento() {
        return new TileMemento();
    }

    @Override
    public void restore(Memento m) {

    }
}