package graphs;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import gamedatastructures.Player;
import gamedatastructures.Resource;
import gamedatastructures.buildings.Building;
import gamedatastructures.buildings.City;
import gamedatastructures.buildings.Settlement;

/**
 * Represents a location on the ma
 */
public class Vertex {

    private final int locationId;
    private Vertex[] adjacentVertexes;
    private Road[] adjacentRoads;
    private Port adjacentPort;
    private Player owner;
    private boolean portsInitialized;
    private Building building;

    /**
     * Creates a new vertex with the given ID.
     * @param id the locationId of the vertex
     */
    public Vertex(final int id) {
        this.locationId = id;
        this.portsInitialized = false;
    }

    /**
     * Returns the locationId of the vertex.
     * @return the locationId of the vertex
     */
    public int getLocationId() {
        return this.locationId;
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
            throw new RuntimeException("Vertex-to-Vertex adjacency uninitialized");
        }
        return this.adjacentVertexes.clone();
    }

    /**
     * Sets the adjacentVertexes array for this Vertex object. This method is
     * intended to be called only once, after all Vertex objects have been
     * created, to initialize the adjacentVertexes array for all the Vertex
     * objects.
     *
     * @param vertexes the array of adjacent Vertexes to set for this Vertex
     */
    protected void setAdjacentVertexes(final Vertex[] vertexes) {
        this.adjacentVertexes = vertexes;
    }

    /**
     * Gets an array of Roads adjacent to this one. Returns a clone of the
     * internal array, so that outside methods cannot modify the internal array
     * directly. However, the Road objects themselves are still the same objects
     * as in the internal array, so modifications to a specific Road object in
     * the cloned array will be reflected in the original array.
     *
     * @throws RuntimeException if the graph is not initialized correctly for this type of adjacency
     * @return a clone of the adjacentRoads array
     */
    public Road[] getAdjacentRoads() {
        if (this.adjacentRoads == null) {
            throw new RuntimeException("Vertex-to-Road adjacency uninitialized");
        }
        return this.adjacentRoads.clone();
    }

    /**
     * Sets the adjacentRoads array for this Vertex object. This method is intended
     * to be called only once, after all Road and Vertex objects have been created,
     * to initialize the adjacentRoads array for all the Vertex objects.
     *
     * @param roads the array of adjacent Roads to set for this Vertex
     */
    protected void setAdjacentRoads(final Road[] roads) {
        this.adjacentRoads = roads;
    }

    /**
     * Tells whether this Vertex has a Port adjacent to it or not
     *
     * @return True if the Vertex has an adjacent Port, false otherwise
     */
    public boolean hasPort() {
        if (!this.portsInitialized) {
            throw new RuntimeException("Vertex-to-Port adjacency uninitialized");
        }
        return this.adjacentPort != null;
    }

    /**
     * Sets the adjacentPort for this Vertex object. This method is intended
     * to be called only once, after all Port and Vertex objects have been created,
     * to initialize the adjacentPort for all the Vertex objects.
     *
     * @param port the adjacent Port to set for this Vertex
     */
    protected void setAdjacentPort(final Port port) {
        this.adjacentPort = port;
    }

    /**
     * Sets the initialization flag for the port to true to ensure proper protections
     * against missuse.
     */
    protected void setPortInitialized() {
        this.portsInitialized = true;
    }

    /**
     * Gives the Port that is adjacent to this Vertex if it exists
     *
     * @return the adjacentPort to this Vertex
     * @throws NullPointerException if the adjacentPort has not been
     *                               initialized
     */
    public Port getAdjacentPort() {
        if (!this.portsInitialized) {
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
     * and the distance rule is observed: there can be no other settlments
     * directly adjacent to this Vertex
     *
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

        if (this.isAdjacentToSettlement()) {
            return false;
        }
        return true;
    }

    /**
     * Tells whether a settlement has already been built next to this Vertex
     *
     * @return true if any of the adjacent Vertexes already have a settlement
     */
    private boolean isAdjacentToSettlement() {
        Vertex[] neighbors = this.getAdjacentVertexes();

        for (Vertex vertex: neighbors) {
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
     * Builds a settlment on this vertex by first registering this vertex as being owned
     * by the given player and updates the building field to hold a new settlement.
     * Also adds a trade boost to the player if the vertex was next to a port
     */
    public void build(final Player player) {
        setOwner(player);
        this.building = new Settlement();
        if (hasPort()) {
            player.addTradeBoost(getAdjacentPort().getResourse());
        }
    }

    public int getYield(Resource resource) {
        return (building == null) ? 0 : building.getYield(resource);
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
     *
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
     *
     * This is true only if the Vertex has not already been developed,
     * the distance rule is observed: there can be no other settlments
     * directly adjacent to this Vertex, and this Vertex is adjacent to
     * one of the players built roads.
     *
     * @return true if this Vertex can be built upon by the given player, false otherwise
     */
    public boolean isBuildableBy(final Player player) {
        if (!this.isBuildable()) {
            return false;
        }

        if (!this.isAdjacentToFriendlyRoad(player)) {
            return false;
        }

        return true;
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

    public boolean isCity() {
        return building.getClass() == City.class;
    }

    /**
     * Tells this if this Vertex is upgradable by the given player.
     * This is true if the player already owns the vertex, and the vertex is not already a city.
     *
     * @return true if the vertex is upgradable by the player, false otherwise
     */
    public boolean isUpgradableBy(final Player player) {
        if (this.isCity()) {
            return false;
        }
        if (this.getOwner() != player) {
            return false;
        }

        return true;
    }

    public void upgradeToCity(final Player player) {
        if (isUpgradableBy(player)) {
            this.building = new City((Settlement) this.building);
        }
    }

    // ONLY FOR TESTING PURPOSES
    public void setBuildingToCity() {
        this.building = new City();
    }

    /**
     * Helper that tells us if this vertex is owned by an enemy of the given player
     * @param player
     * @return
     */
    public boolean ownedByEnemyOf(final Player player) {

        if (this.getOwner() == null) {
            return false;
        }

        return this.getOwner() != player;
    }
}
