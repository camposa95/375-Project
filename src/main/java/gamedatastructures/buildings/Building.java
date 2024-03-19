package gamedatastructures.buildings;

import gamedatastructures.Resource;

public abstract class Building {
    protected int brickResourceBonus = 1;
    protected int grainResourceBonus = 1;
    protected int lumberResourceBonus = 1;
    protected int oreResourceBonus = 1;
    protected int woolResourceBonus = 1;

    public Building() {

    }

    /**
     * Given a resource type, returns the number of resources
     * the player owning this building should receive based on the building's bonuses
     * @param resource the type of resource to be acquired
     * @return the number of resources of the given type that should be acquired
     */
    public int getYield(Resource resource) {
        switch (resource) {
            case BRICK:
                return brickResourceBonus;
            case GRAIN:
                return grainResourceBonus;
            case LUMBER:
                return lumberResourceBonus;
            case ORE:
                return oreResourceBonus;
            case WOOL:
                return woolResourceBonus;
            default:
                return 0;
        }
    }
}
