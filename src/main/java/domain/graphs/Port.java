package domain.graphs;

import data.*;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import domain.bank.Resource;

import java.io.File;

public class Port implements Restorable {

    private final int locationId;
    private Resource resource;

    /**
     * Creates a new Port with the given location ID.
     *
     * @param id the locationId of the Port on the map
     * @param resourceType
     */
    public Port(final int id, final Resource resourceType) {
        this.locationId = id;
        this.resource = resourceType;
    }

    /**
     * Gets locationId of this Port
     *
     * @return the location ID
     */
    public int getLocationId() {
        return this.locationId;
    }

    public Resource getResourse() {
        return this.resource;
    }

    // -----------------------------------
    //
    // Restorable implementation
    //
    // -----------------------------------

    public class PortMemento implements Memento {
        private final Resource resource;

        // Storage Constants
        private static final String TARGET_FILE_NAME = "port.txt";

        // Field Keys
        private static final String RESOURCE = "Resource";

        private PortMemento() {
            this.resource = Port.this.resource;
        }

        @SuppressFBWarnings("EI_EXPOSE_REP2")
        public PortMemento(final File folder) {
            // Create a MementoReader for reading memento data
            MementoReader reader = new MementoReader(folder, TARGET_FILE_NAME);

            // Read simple fields from the file
            this.resource = Resource.valueOf(reader.readField(RESOURCE));
        }

        public void save(final File folder) throws SaveException {
            // Create a MementoWriter for writing memento data
            MementoWriter writer = new MementoWriter(folder, TARGET_FILE_NAME);

            // Write simple fields to the file
            writer.writeField(RESOURCE, resource.toString());
        }

        public void restore() {
            Port.this.resource = this.resource;
        }
    }

    @Override
    public Memento createMemento() {
        return new PortMemento();
    }
}
