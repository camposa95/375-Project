package domain.graphs;

import data.*;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import domain.player.Player;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Represents a location on the ma
 */
public class Vertex implements Restorable {

    private final int locationId;
    private List<Vertex> adjacentVertexes;
    private List<Road> adjacentRoads;
    private Port adjacentPort;
    private Player owner;
    private boolean isCity;

    // initialization flags:
    private boolean portInitialized;
    private boolean adjacentRoadsInitialized;
    private boolean adjacentVertexesInitialized;

    // -----------------------------------------------
    // Initialization
    // -----------------------------------------------

    /**
     * Creates a new vertex with the given ID.
     * @param id the locationId of the vertex
     */
    public Vertex(final int id) {
        this.locationId = id;
        this.portInitialized = false;
        this.adjacentVertexesInitialized = false;
        this.adjacentRoadsInitialized = false;
    }

    /**
     * Sets the adjacentPort for this Vertex object. This method is intended
     * to be called only once, after all Port and Vertex objects have been created,
     * to initialize the adjacentPort for all the Vertex objects.
     *
     * @param port the adjacent Port to set for this Vertex
     */
    public void setAdjacentPort(final Port port) {
        if (this.portInitialized) {
            throw new IllegalCallerException("Cannot re-set the port after initialization");
        }
        this.adjacentPort = port;
        this.portInitialized = true;
    }

    /**
     * Sets the adjacentRoads array for this Vertex object. This method is intended
     * to be called only once, after all Road and Vertex objects have been created,
     * to initialize the adjacentRoads array for all the Vertex objects.
     *
     * @param roads the array of adjacent Roads to set for this Vertex
     */
    public void setAdjacentRoads(final List<Road> roads) {
        if (this.adjacentRoadsInitialized) {
            throw new IllegalCallerException("Cannot re-set the adjacent roads after initialization");
        }
        this.adjacentRoads = Collections.unmodifiableList(roads);
        this.adjacentRoadsInitialized = true;
    }

    /**
     * Sets the adjacentVertexes array for this Vertex object. This method is
     * intended to be called only once, after all Vertex objects have been
     * created, to initialize the adjacentVertexes array for all the Vertex
     * objects.
     *
     * @param vertexes the array of adjacent Vertexes to set for this Vertex
     */
    public void setAdjacentVertexes(final List<Vertex> vertexes) {
        if (this.adjacentVertexesInitialized) {
            throw new IllegalCallerException("Cannot re-set the adjacent vertexes after initialization");
        }
        this.adjacentVertexes = Collections.unmodifiableList(vertexes);
        this.adjacentVertexesInitialized = true;
    }

    // -----------------------------------------------
    // End Initialization methods
    // -----------------------------------------------

    /**
     * Returns the locationId of the vertex.
     * @return the locationId of the vertex
     */
    public int getLocationId() {
        return this.locationId;
    }

    /**
     * Gets an unmodifiable list of Vertexes adjacent to this one.
     *
     * @throws RuntimeException if the graph is not initialized correctly for this type of adjacency
     */
    List<Vertex> getAdjacentVertexes() {
        if (!this.adjacentVertexesInitialized) {
            throw new RuntimeException("Vertex-to-Vertex adjacency uninitialized");
        }
        return this.adjacentVertexes;
    }

    /**
     * Gets an unmodifiable list of Roads adjacent to this one.
     *
     * @throws RuntimeException if the graph is not initialized correctly for this type of adjacency
     */
    public List<Road> getAdjacentRoads() {
        if (!this.adjacentRoadsInitialized) {
            throw new RuntimeException("Vertex-to-Road adjacency uninitialized");
        }
        return this.adjacentRoads;
    }

    /**
     * Tells whether this Vertex has a Port adjacent to it or not
     *
     * @return True if the Vertex has an adjacent Port, false otherwise
     */
    public boolean hasPort() {
        if (!this.portInitialized) {
            throw new RuntimeException("Vertex-to-Port adjacency uninitialized");
        }
        return this.adjacentPort != null;
    }

    /**
     * Gives the Port that is adjacent to this Vertex if it exists
     *
     * @return the adjacentPort to this Vertex
     * @throws NullPointerException if the adjacentPort has not been
     *                               initialized
     */
    public Port getAdjacentPort() {
        if (!this.portInitialized) {
            throw new RuntimeException("Vertex-to-Port adjacency uninitialized");
        }

        if (this.adjacentPort == null) {
            throw new NullPointerException("This vertex has no port");
        }
        return this.adjacentPort;
    }

    /**
     * Tells whether a settlement can be built on this Vertex.
     * This is true only if the Vertex has not already been developed,
     * and the distance rule is observed: there can be no other settlements
     * directly adjacent to this Vertex
     * <p>
     * Note: this is a simple check that is meant to be used during setup,
     * later during regular gameplay other checks in conjunction with this
     * one will need to be called as well.
     *
     * @return true if this Vertex can be built upon, false otherwise
     */
    public boolean isBuildable() {
        if (this.isOccupied()) {
            return false;
        }

        return !this.isAdjacentToSettlement();
    }

    /**
     * Tells whether a settlement has already been built next to this Vertex
     *
     * @return true if any of the adjacent Vertexes already have a settlement
     */
    private boolean isAdjacentToSettlement() {
        for (Vertex vertex: this.getAdjacentVertexes()) {
            if (vertex.isOccupied()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tells whether a settlement has already been built on this Vertex
     *
     * @return true if this Vertex has a settlement, false otherwise
     */
    public boolean isOccupied() {
        return this.owner != null;
    }

    /**
     * Builds a settlement on this vertex by first registering this vertex as being owned
     * by the given player. Also adds the trade boost to the player if the vertex was
     * next to a port
     */
    public void build(final Player player) {
        setOwner(player);
        if (hasPort()) {
            player.addTradeBoost(getAdjacentPort().getResource());
        }
    }

    /**
     * Sets the owner of this vertex to the given player
     */
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setOwner(final Player player) {
        this.owner = player;
    }

    /**
     * Gets this Vertexes Owner.
     * <p>
     * Note: this method is meant to be primarily used for testing assertion purposes,
     * and for internal use by the graphs. Beware it will return null if this is unoccupied.
     * Best use is to call isOccupied() first to avoid null pointer exceptions.
     *
     * @return the Player that owns this Vertex
     */
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public Player getOwner() {
        return this.owner;
    }

    /**
     * Tells whether a settlement can be built on this Vertex by the given player
     * in regular play.
     * <p>
     * This is true only if the Vertex has not already been developed,
     * the distance rule is observed: there can be no other settlements
     * directly adjacent to this Vertex, and this Vertex is adjacent to
     * one of the players built roads.
     *
     * @return true if this Vertex can be built upon by the given player, false otherwise
     */
    public boolean isBuildableBy(final Player player) {
        if (!this.isBuildable()) {
            return false;
        }

        return this.isAdjacentToFriendlyRoad(player);
    }

    /**
     * Tells this if this Vertex is adjacent to a Road owned by the given player
     *
     * @return true if this Vertex is adjacent to a friendly Road, false otherwise
     */
    public boolean isAdjacentToFriendlyRoad(final Player player) {

        for (Road road: this.getAdjacentRoads()) {
            if (road.isOccupied()) {
                Player roadOwner = road.getOwner();
                if (player.equals(roadOwner)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Tells this if this Vertex is upgradable by the given player.
     * This is true if the player already owns the vertex, and the vertex is not already a city.
     *
     * @return true if the vertex is upgradable by the player, false otherwise
     */
    public boolean isUpgradableBy(final Player player) {
        if (this.isCity) {
            return false;
        }
        return this.getOwner() == player;
    }

    /**
     * Simple setter to set the isCity flag of this vertex
     *
     * @param b, the value to set for the flag
     */
    public void setIsCity(final boolean b) {
        this.isCity = b;
    }

    /**
     * Simple getter to tell is this vertex is a city or not
     */
    public boolean getIsCity() {
        return this.isCity;
    }

    /**
     * Helper that tells us if this vertex is owned by an enemy of the given player
     */
    public boolean ownedByEnemyOf(final Player player) {

        if (this.getOwner() == null) {
            return false;
        }

        return this.getOwner() != player;
    }

    // -----------------------------------
    //
    // Restorable implementation
    //
    // -----------------------------------

    public class VertexMemento implements Memento {
        private final Player owner; // terminal here
        private final boolean isCity;

        // Storage Constants
        private static final String TARGET_FILE_NAME = "vertex.txt";

        // Field Keys
        private static final String OWNER = "Owner";
        private static final String IS_CITY = "IsCity";

        private VertexMemento() {
            this.isCity = Vertex.this.isCity;
            this.owner = Vertex.this.owner;
        }

        @SuppressFBWarnings("EI_EXPOSE_REP2")
        public VertexMemento(final File folder) {
            // Create a MementoReader for reading memento data
            MementoReader reader = new MementoReader(folder, TARGET_FILE_NAME);

            // Read simple fields from the file
            this.owner = parseOwner(reader.readField(OWNER));
            this.isCity = Boolean.parseBoolean(reader.readField(IS_CITY));
        }

        private Player parseOwner(final String ownerString) {
            // Check if the ownerString represents "None"
            if (ownerString.equals("None")) {
                return null;
            } else {
                // Extract player number from the string representation
                int playerNum = Integer.parseInt(ownerString.substring(ownerString.lastIndexOf(" ") + 1));
                // Retrieve the player using the GameLoader
                return GameLoader.getInstance().getPlayerByNum(playerNum);
            }
        }

        public void save(final File folder) throws IOException {
            // Create a MementoWriter for writing memento data
            MementoWriter writer = new MementoWriter(folder, TARGET_FILE_NAME);

            // Write simple fields to the file
            writer.writeField(OWNER, owner != null ? owner.toString() : "None");
            writer.writeField(IS_CITY, Boolean.toString(isCity));
        }

        public void restore() {
            // Restore simple fields
            Vertex.this.isCity = this.isCity;
            Vertex.this.owner = this.owner;
        }
    }

    @Override
    public Memento createMemento() {
        return new VertexMemento();
    }
}
