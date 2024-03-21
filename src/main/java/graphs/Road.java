package graphs;

import java.io.File;
import java.util.Arrays;

import SavingAndLoading.Memento;
import SavingAndLoading.MementoWriter;
import SavingAndLoading.Restorable;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import gamedatastructures.Player;

/**
 * Represents a road on the map.
 */
public class Road implements Restorable {

    private static final int MAX_ADJACENT_ROADS = 4;
    private final int locationId;
    private Road[] adjacentRoads;
    private Vertex[] adjacentVertexes;
    private Player owner;

    /**
     * Creates a new road with the given location ID.
     *
     * @param id the locationId of the road on the map
     */
    public Road(final int id) {
        this.locationId = id;
    }

    /**
     * Gets locationId of this Road
     *
     * @return the location ID
     */
    public int getLocationId() {
        return this.locationId;
    }

    /**
     * Gets an array of Roads adjacent to this one. Returns a clone of the internal
     * array, so that outside methods cannot modify the internal array directly.
     * However, the Road objects themselves are still the same objects as in the
     * original array, so modifications to a specific Road object in the cloned
     * array will be reflected in the original array.
     *
     * @return a clone of the adjacentRoads array
     * @throws RuntimeException if the graph is not initialized correctly for this type of adjacency
     */
    public Road[] getAdjacentRoads() {
        if (this.adjacentRoads == null) {
            throw new RuntimeException("Road-to-Road adjacency uninitialized");
        }
        return this.adjacentRoads.clone();
    }

    /**
     * Sets the adjacentRoads array for this Road object. This method is intended to
     * be called only once, after all Road objects have been created, to initialize
     * the adjacentRoads array for all the Road objects.
     *
     * @param roads the array of adjacent Roads to set for this Road
     */
    protected void setAdjacentRoads(final Road[] roads) {
        this.adjacentRoads = roads;
    }

    /**
     * Sets the adjacentVertexes array for this Road object. This method is intended
     * to be called only once, after all Road and Vertex objects have been created.
     *
     * @param vertexes the array of adjacent Vertexes to set for this Road
     */
    protected void setAdjacentVertexes(final Vertex[] vertexes) {
        this.adjacentVertexes = vertexes;
    }

    /**
     * Gets an array of Vertexes adjacent to this one. Returns a clone of the
     * internal array, so that outside methods cannot modify the internal array
     * directly. However, the Vertex objects themselves are still the same objects
     * as in the internal array, so modifications to a specific Vertex object in
     * the cloned array will be reflected in the original array.
     *
     * @return a clone of the adjacentVertexes array
     * @throws RuntimeException if the graph is not initialized correctly for this type of adjacency
     */
    public Vertex[] getAdjacentVertexes() {
        if (this.adjacentVertexes == null) {
            throw new RuntimeException("Road-to-Vertex adjacency uninitialized");
        }

        return this.adjacentVertexes.clone();
    }

    /**
     * Sets the owner field for this Road. The owner is the player who built
     * this Road.
     */
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setOwner(final Player player) {
        this.owner = player;
    }

    /**
     * Tells whether this Road has been built yet
     *
     * @return true if this Road has been built, false otherwise
     */
    public boolean isOccupied() {
        return this.owner != null;
    }

    /**
     * Gets this Roads Owner.
     *
     * @return the Player that owns this Road
     */
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public Player getOwner() {
        return this.owner;
    }

    /**
     * Tells whether any Player can build on this Road.
     * This is true only if the Road is currently unoccupied.
     * Note: this is a simple check that is mean to be used during setup,
     * later during regular gameplay other checks in conjunciton with this
     * one will need to ba called as well.
     *
     * @return true is this Road is unoccupied, false otherwise
     */
    public boolean isBuildable() {
        return this.owner == null;
    }

    /**
     * Tells whether the given Vertex is adjacent to this Road,
     * and is meant to be used during setup in conjunction with
     * isBuildable to determine buildability. The vertex passed
     * in should be the last Vertex placed by the setup rules.
     * Note: does not give information about settlements
     *
     * @return true if the Vertex is adjacent to this Road, false otherwise
     */
    public boolean isAdjacentTo(final Vertex vertex) {
        for (Vertex neighbor: this.getAdjacentVertexes()) {
            if (vertex.equals(neighbor)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Tells whether the given Player can build on this Road.
     * This is based off the rules for regular gameplay:
     * https://www.catan.com/sites/default/files/2021-06/catan_base_rules_2020_200707.pdf
     *
     * @return true is this Road can be built by the player, false otherwise
     */
    public boolean isBuildableBy(final Player player) {
        if (this.isOccupied()) {
            return false;
        }
        if (existsAdjacentFriendlyRoadNotAdjacentToEnemyVertex(player)) {
            return true;
        }

        return false;
    }

    /**
     * Tells whether there exists a friendly road adjacent to this road that
     * is not adjacent to a vertex owned by an enemy of the given player.
     *
     * @return true if any of the friendly roads is not next to an enemies vertex, false otherwise
     */
    private boolean existsAdjacentFriendlyRoadNotAdjacentToEnemyVertex(final Player player) {

        for (Road road: getFriendlyRoads(player)) {
            if (!road.isAdjacentToEnemyVertex(player)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tells this road is adjacent to an vertex owned by an enemy of the give player
     *
     * @return true if this road is adjacent to an enemies vertex, false otherwise
     */
    private boolean isAdjacentToEnemyVertex(final Player player) {
        for (Vertex vertex: this.getAdjacentVertexes()) {
            if (vertex.isOccupied()) {
                Player cityOwner = vertex.getOwner();
                if (!player.equals(cityOwner)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the Roads adjacent to this one that are also owned by the given player
     *
     * @return An array of roads adjacent to this one owned by the given player
     */
    private Road[] getFriendlyRoads(final Player player) {
        Road[] friendlyRoads = new Road[MAX_ADJACENT_ROADS];
        int numFriendlyRoads = 0;

        for (Road road: this.getAdjacentRoads()) {
            if (road.isOccupied()) {
                Player roadOwner = road.getOwner();
                if (player.equals(roadOwner)) {
                    friendlyRoads[numFriendlyRoads] = road;
                    numFriendlyRoads++;
                }
            }
        }

        return Arrays.copyOf(friendlyRoads, numFriendlyRoads);
    }

    /**
     * Returns the vertex in this.adjacent vertexes that is not the given vertex
     *
     * @param origin
     * @return
     */
    public Vertex getTail(final Vertex origin) {
        for (Vertex vertex: this.getAdjacentVertexes()) {
            if (vertex != origin) {
                return vertex;
            }
        }

        throw new IllegalStateException("This road only has one adjacent vertex?");
    }

    // -----------------------------------
    //
    // Restorable implementation
    //
    // -----------------------------------

    public class RoadMemento implements Memento {
        private final Player owner; // terminal

        private RoadMemento() {
            this.owner = Road.this.owner;
        }

        @Override
        public void save(File folder) {
            // Create a MementoWriter for writing memento data
            MementoWriter writer = new MementoWriter(folder, "road.txt");

            // Write simple fields to the file
            writer.writeField("Owner", owner != null ? owner.toString() : "None");
        }

        @Override
        public void restore() {
            // Restore the owner of the road
            Road.this.owner = this.owner;
        }
    }

    @Override
    public Memento createMemento() {
        return new RoadMemento();
    }
}
