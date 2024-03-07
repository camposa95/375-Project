package graphs;

import gamedatastructures.Resource;

public class Port {

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

}
