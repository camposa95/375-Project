package gamedatastructures;

public class Building {
    private boolean isCity;
    protected DistrictType district;

    public Building() {
        isCity = false;
        district = DistrictType.EMPTY;
    }

    /**
     * Given a resource type, returns the number of resources
     * the player owning this building should receive based on the building's bonuses
     * @param resource the type of resource to be acquired
     * @return the number of resources of the given type that should be acquired
     */
    public int getYield(Resource resource) {
        int baseYield = isCity ? 2 : 1;
        return Math.max(baseYield, district.getYield(resource));
    }

    public void upgradeToCity() {
        this.isCity = true;
    }

    public void buildDistrict(DistrictType type) {
        if (canBuildDistrict()) {
            this.district = type;
        } else {
            throw new IllegalArgumentException("Cannot make a district on a building where one already exists.");
        }
    }

    private boolean canBuildDistrict() {
        return this.district == DistrictType.EMPTY;
    }

    public DistrictType getDistrict() {
        return this.district;
    }

    public boolean isCity() {
        return this.isCity;
    }
}
