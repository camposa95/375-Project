package gamedatastructures;

import gamedatastructures.Resource;

public enum DistrictType {
    EMPTY(new Resource[0], Resource.ANY),
    MINE(new Resource[]{Resource.ORE, Resource.ORE, Resource.LUMBER}, Resource.ORE),
    KILN(new Resource[]{Resource.BRICK, Resource.BRICK, Resource.ORE}, Resource.BRICK),
    SAWMILL(new Resource[]{Resource.LUMBER, Resource.LUMBER, Resource.ORE}, Resource.LUMBER),
    GARDEN(new Resource[]{Resource.GRAIN, Resource.GRAIN, Resource.ORE}, Resource.GRAIN),
    BARN(new Resource[]{Resource.WOOL, Resource.WOOL, Resource.LUMBER}, Resource.WOOL);

    private final int BONUS_NUMBER = 3;
    Resource[] cost;
    Resource bonusType;
    DistrictType(Resource[] cost, Resource bonusType) {
        this.cost = cost;
        this.bonusType = bonusType;
    }

    public int getYield(Resource resource) {
        return (resource == bonusType) ? BONUS_NUMBER : 0;
    }
}
