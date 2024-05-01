package domain.building;

import domain.bank.Resource;

import java.util.Arrays;

public enum DistrictType {
    EMPTY(new Resource[0], Resource.ANY),
    MINE(new Resource[]{Resource.ORE, Resource.ORE, Resource.LUMBER}, Resource.ORE),
    KILN(new Resource[]{Resource.BRICK, Resource.BRICK, Resource.ORE}, Resource.BRICK),
    SAWMILL(new Resource[]{Resource.LUMBER, Resource.LUMBER, Resource.ORE}, Resource.LUMBER),
    GARDEN(new Resource[]{Resource.GRAIN, Resource.GRAIN, Resource.ORE}, Resource.GRAIN),
    BARN(new Resource[]{Resource.WOOL, Resource.WOOL, Resource.LUMBER}, Resource.WOOL);

    private final int bonusNumber = 3;
    Resource[] districtCost;
    Resource type;
    DistrictType(final Resource[] cost, final Resource bonusType) {
        this.districtCost = cost;
        this.type = bonusType;
    }

    public int getYield(final Resource resource) {
        return (resource == type) ? bonusNumber : 0;
    }

    public Resource[] getDistrictCost() {
        return Arrays.copyOf(this.districtCost, this.districtCost.length);
    }
}
