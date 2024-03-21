package graphs;

import SavingAndLoading.Memento;
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

        private PortMemento() {
            this.resource = Port.this.resource;
        }

        @Override
        public void save(File folder) {
            // Create a MementoWriter for writing memento data
            MementoWriter writer = new MementoWriter(folder, "port.txt");

            // Write simple fields to the file
            writer.writeField("Resource", resource.toString());
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
