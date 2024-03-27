package domain.graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import data.*;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import domain.game.GameType;
import domain.bank.Resource;

/**
 * Graph of the locations on the map
 */
public class VertexGraph implements Restorable {

    public static final int NUM_VERTICES = 54;
    public static final int NUM_PORTS = 9;
    private final Resource[] portResources = {Resource.ANY, Resource.GRAIN, Resource.WOOL, Resource.ANY, Resource.ORE, Resource.ANY, Resource.ANY, Resource.BRICK, Resource.LUMBER};
    private final Vertex[] vertexes;
    private final Port[] ports;

    /**
     * Creates a new vertex graph with the default number of vertices.
     *
     * Note: Adjacency must be initialized separatly by calling
     * the appropriate initializer methods
     */
    public VertexGraph(final GameType gameType) {
        this.vertexes = new Vertex[NUM_VERTICES];
        this.ports = new Port[NUM_PORTS];

        for (int i = 0; i < NUM_VERTICES; i++) {
            this.vertexes[i] = new Vertex(i);
        }

        if (gameType == GameType.Advanced) {
            Collections.shuffle(Arrays.asList(this.portResources));
        }

        for (int i = 0; i < NUM_PORTS; i++) {
            this.ports[i] = new Port(i, portResources[i]);
        }
    }

    /**
     * Returns the vertex with the specified ID.
     * @param locationId the ID of the vertex to retrieve
     * @return the vertex with the specified ID
     * @throws IllegalArgumentException if the specified ID is not within the valid
     *                                  range
     */
    public Vertex getVertex(final int locationId) {

        if (locationId >= NUM_VERTICES || locationId < 0) {
            throw new IllegalArgumentException("Invalid locationId; Try[0, 53]");
        }

        return this.vertexes[locationId];
    }

    /**
     * Returns the Port with the specified ID.
     * @param locationId the ID of the Port to retrieve
     * @return the Port with the specified ID
     * @throws IllegalArgumentException if the specified ID is not within the valid
     *                                  range
     */
    public Port getPort(final int locationId) {

        if (locationId >= NUM_PORTS || locationId < 0) {
            throw new IllegalArgumentException("Invalid locationId; Try[0, 8]");
        }

        return this.ports[locationId];
    }

    /**
     * Returns the order of the ports' resources.
     *
     * Mainly used to help determine in testing if the ports were shuffled.
     *
     * @return and array of the resource of the ports in order according to the game diagram
     */
    public Resource[] getOrder() {

        Resource[] resourceOrder = new Resource[NUM_PORTS];
        for (int i = 0; i < NUM_PORTS; i++) {
            resourceOrder[i] = this.ports[i].getResourse();
        }

        return resourceOrder;
    }

    // -----------------------------------
    //
    // Restorable implementation
    //
    // -----------------------------------

    public class VertexGraphMemento implements Memento {

        // simple fields
        private final Resource[] portResources;

        // sub mementos
        private final Memento[] vertexMementos;
        private final Memento[] portMementos;

        // Storage Constants
        private static final String TARGET_FILE_NAME = "vertexgraph.txt";
        private static final String VERTEX_SUBFOLDER_PREFIX = "Vertex";
        private static final String PORT_SUBFOLDER_PREFIX = "Port";

        // Field Keys
        private static final String PORT_RESOURCES = "PortResources";

        private VertexGraphMemento() {
            // simple fields
            this.portResources = Arrays.copyOf(VertexGraph.this.portResources, VertexGraph.this.portResources.length);

            // sub mementos
            this.vertexMementos = new Memento[VertexGraph.this.vertexes.length];
            for (int i = 0; i < VertexGraph.this.vertexes.length; i++) {
                this.vertexMementos[i] = VertexGraph.this.vertexes[i].createMemento();
            }

            this.portMementos = new Memento[VertexGraph.this.ports.length];
            for (int i = 0; i < VertexGraph.this.ports.length; i++) {
                this.portMementos[i] = VertexGraph.this.ports[i].createMemento();
            }
        }

        @SuppressFBWarnings("EI_EXPOSE_REP2")
        public VertexGraphMemento(final File folder) {
            // Create a MementoReader for reading memento data
            MementoReader reader = new MementoReader(folder, TARGET_FILE_NAME);

            // Read simple fields from the file
            this.portResources = parsePortResources(reader.readField(PORT_RESOURCES));

            // Read sub-mementos from the appropriate subfolders
            this.vertexMementos = new Memento[VertexGraph.this.vertexes.length];
            for (int i = 0; i < this.vertexMementos.length; i++) {
                File vertexSubFolder = reader.getSubFolder(VERTEX_SUBFOLDER_PREFIX + i);
                this.vertexMementos[i] = VertexGraph.this.vertexes[i].new VertexMemento(vertexSubFolder);
            }

            this.portMementos = new Memento[VertexGraph.this.ports.length];
            for (int i = 0; i < this.portMementos.length; i++) {
                File portSubFolder = reader.getSubFolder(PORT_SUBFOLDER_PREFIX + i);
                this.portMementos[i] = VertexGraph.this.ports[i].new PortMemento(portSubFolder);
            }
        }

        // Helper method to parse port resources
        private Resource[] parsePortResources(final String portResourcesString) {
            String[] resourceNames = portResourcesString.substring(1, portResourcesString.length() - 1).split(", ");

            Resource[] resources = new Resource[resourceNames.length];
            for (int i = 0; i < resourceNames.length; i++) {
                resources[i] = Resource.valueOf(resourceNames[i].trim());
            }

            return resources;
        }

        public void save(final File folder) throws SaveException {
            // Create a MementoWriter for writing memento data
            MementoWriter writer = new MementoWriter(folder, TARGET_FILE_NAME);

            // Write simple fields to the file
            writer.writeField(PORT_RESOURCES, Arrays.toString(portResources));

            // Save sub mementos' state
            for (int i = 0; i < vertexMementos.length; i++) {
                // Create a subfolder for each vertex's memento
                File vertexSubFolder = writer.getSubFolder(VERTEX_SUBFOLDER_PREFIX + i);
                vertexMementos[i].save(vertexSubFolder);
            }

            for (int i = 0; i < portMementos.length; i++) {
                // Create a subfolder for each port's memento
                File portSubFolder = writer.getSubFolder(PORT_SUBFOLDER_PREFIX + i);
                portMementos[i].save(portSubFolder);
            }
        }

        public void restore() {
            // Restore simple fields
            System.arraycopy(this.portResources, 0, VertexGraph.this.portResources, 0, this.portResources.length);

            // Restore sub mementos
            for (Memento vertexMemento : vertexMementos) {
                vertexMemento.restore();
            }

            for (Memento portMemento : portMementos) {
                portMemento.restore();
            }
        }
    }

    @Override
    public Memento createMemento() {
        return new VertexGraphMemento();
    }
}
