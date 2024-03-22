package graphs;

import SavingAndLoading.Memento;
import SavingAndLoading.MementoReader;
import SavingAndLoading.MementoWriter;
import SavingAndLoading.Restorable;
import gamedatastructures.Resource;

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

        public PortMemento(File folder) {
            // Create a MementoReader for reading memento data
            MementoReader reader = new MementoReader(folder, TARGET_FILE_NAME);

            // Read simple fields from the file
            this.resource = Resource.valueOf(reader.readField(RESOURCE));
        }


        @Override
        public void save(File folder) {
            // Create a MementoWriter for writing memento data
            MementoWriter writer = new MementoWriter(folder, TARGET_FILE_NAME);

            // Write simple fields to the file
            writer.writeField(RESOURCE, resource.toString());
        }

        @Override
        public void restore() {
            Port.this.resource = this.resource;
        }
    }

    @Override
    public Memento createMemento() {
        return new PortMemento();
    }
}
