package graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import SavingAndLoading.Memento;
import SavingAndLoading.MementoWriter;
import SavingAndLoading.Restorable;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import gamedatastructures.GameType;
import gamedatastructures.Resource;

/**
 * Graph of the locations on the map
 */
public class VertexGraph implements Restorable {

    private static final int NUM_VERTICES = 54;
    private static final int NUM_PORTS = 9;
    private static final int MAX_ADJACENT_VERTEXES = 3;
    private static final int MAX_ADJACENT_ROADS = 3;
    private final Resource[] portResources = {Resource.ANY, Resource.GRAIN, Resource.WOOL, Resource.ANY, Resource.ORE, Resource.ANY, Resource.ANY, Resource.BRICK, Resource.LUMBER};

    private Vertex[] vertexes;
    private Port[] ports;

    /**
     * Creates a new vertex graph with the default number of vertices.
     *
     * Note: Adjacency must be initialized separatly by calling
     * the appropriate initializer methods
     */
    public VertexGraph() {
        this.vertexes = new Vertex[NUM_VERTICES];
        this.ports = new Port[NUM_PORTS];

        for (int i = 0; i < NUM_VERTICES; i++) {
            this.vertexes[i] = new Vertex(i);
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
     * Reads though the layout file and initializes the graph with the defined
     * adjacency relationships
     */
    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    public void initializeVertexToVertexAdjacency(final String filePath) {
        File vertexLayout = new File(filePath);
        try (Scanner scanner = new Scanner(vertexLayout, StandardCharsets.UTF_8.name())) {
            int vertexToInitializeId = 0;
            Vertex vertexToInitialize;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(" ");
                Vertex[] adjacentVertexesBuffer = new Vertex[MAX_ADJACENT_VERTEXES];

                for (int i = 0; i < values.length; i++) {
                    int vertexId = Integer.parseInt(values[i]);
                    adjacentVertexesBuffer[i] = getVertex(vertexId);
                }

                vertexToInitialize = getVertex(vertexToInitializeId);
                Vertex[] adjacentVertexes = Arrays.copyOf(adjacentVertexesBuffer, values.length);
                vertexToInitialize.setAdjacentVertexes(adjacentVertexes);
                vertexToInitializeId++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Incorrect filename/path when initialized");
        }
    }

    /**
     * Reads though the VertexToRoadlayout file and initializes the graph with the defined
     * adjacency relationships
     */
    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    public void initializeVertexToRoadAdjacency(final RoadGraph roads, final String filePath) {
        File vertexToRoadLayout = new File(filePath);
        try (Scanner scanner = new Scanner(vertexToRoadLayout, StandardCharsets.UTF_8.name())) {
            int vertexToInitializeId = 0;
            Vertex vertexToInitialize;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(" ");
                Road[] adjacentRoadsBuffer = new Road[MAX_ADJACENT_ROADS];

                for (int i = 0; i < values.length; i++) {
                    int roadId = Integer.parseInt(values[i]);
                    adjacentRoadsBuffer[i] = roads.getRoad(roadId);
                }

                vertexToInitialize = getVertex(vertexToInitializeId);
                Road[] adjacentRoads = Arrays.copyOf(adjacentRoadsBuffer, values.length);
                vertexToInitialize.setAdjacentRoads(adjacentRoads);
                vertexToInitializeId++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Incorrect filename/path when initialized");
        }
    }

    /**
     * Reads though the VertexToPortlayout file and initializes the graph with the defined
     * adjacency relationships
     * @param gameType the type of game to initialize for, it beginer then the default orientation
     * will be used. If advanced the ports will be randomized
     */
    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    public void initializeVertexToPortAdjacency(final String filePath, final GameType gameType) {
        if (gameType == GameType.Advanced) {
            Collections.shuffle(Arrays.asList(this.portResources));
        }

        for (int i = 0; i < NUM_PORTS; i++) {
            this.ports[i] = new Port(i, portResources[i]);
        }

        File vertexToRoadLayout = new File(filePath);
        try (Scanner scanner = new Scanner(vertexToRoadLayout, StandardCharsets.UTF_8.name())) {
            int vertexToInitializeId = 0;
            Vertex vertexToInitialize;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(" ");
                try {
                    int portId = Integer.parseInt(values[0]);
                    Port adjacentPort = getPort(portId);
                    vertexToInitialize = getVertex(vertexToInitializeId);
                    vertexToInitialize.setAdjacentPort(adjacentPort);
                } catch (NumberFormatException e) {
                    // incomplete line or blank line indicating no port
                }
                vertexToInitializeId++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Incorrect filename/path when initialized");
        }

        for (int i = 0; i < NUM_VERTICES; i++) {
            getVertex(i).setPortInitialized();
        }
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

        @Override
        public void save(File folder) {
            // Create a MementoWriter for writing memento data
            MementoWriter writer = new MementoWriter(folder, "vertexgraph.txt");

            // Write simple fields to the file
            writer.writeField("PortResources", Arrays.toString(portResources));

            // Save sub mementos' state
            for (int i = 0; i < vertexMementos.length; i++) {
                // Create a subfolder for each vertex's memento
                File vertexSubFolder = writer.getSubFolder("Vertex" + i);
                vertexMementos[i].save(vertexSubFolder);
            }

            for (int i = 0; i < portMementos.length; i++) {
                // Create a subfolder for each port's memento
                File portSubFolder = writer.getSubFolder("Port" + i);
                portMementos[i].save(portSubFolder);
            }
        }

        @Override
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
