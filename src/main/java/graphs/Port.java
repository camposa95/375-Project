package graphs;

import SavingAndLoading.Memento;
import SavingAndLoading.Restorable;
import gamedatastructures.Resource;

public class Port implements Restorable {

    private final int locationId;
    private Resource resource;

    /**
     * Creates a new Port with the given location ID.
     *
     * @param id the locationId of the Port on the map
     * @param portResources
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
        public void save() {

        }
    }

    @Override
    public Memento createMemento() {
        return new PortMemento();
    }

    @Override
    public void restore(Memento m) {

    }
}
