package gamedatastructures;

import gamedatastructures.Resource;

public class Building {
    protected int brickResourceBonus;
    protected int grainResourceBonus;
    protected int lumberResourceBonus;
    protected int oreResourceBonus;
    protected int woolResourceBonus;
    private boolean isCity;

    public Building() {
        brickResourceBonus = 1;
        grainResourceBonus = 1;
        lumberResourceBonus = 1;
        oreResourceBonus = 1;
        woolResourceBonus = 1;
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

    public void upgradeToCity() {
        this.brickResourceBonus = getUpgradedResourceBonus(brickResourceBonus);
        this.grainResourceBonus = getUpgradedResourceBonus(grainResourceBonus);
        this.lumberResourceBonus = getUpgradedResourceBonus(lumberResourceBonus);
        this.oreResourceBonus = getUpgradedResourceBonus(oreResourceBonus);
        this.woolResourceBonus = getUpgradedResourceBonus(woolResourceBonus);

        this.isCity = true;
    }

    public boolean isCity() {
        return this.isCity;
    }

    private int getUpgradedResourceBonus(int originalBonus) {
        return (originalBonus <= 1) ? originalBonus + 1 : originalBonus;
    }
}
