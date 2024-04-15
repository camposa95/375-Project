package gamedatastructures;

import domain.bank.Resource;
import domain.game.Building;
import domain.game.DistrictType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BuildingTests {

    @Test
    void testGetYield_withSettlementEmptyDistrictGettingOre_expectOneOre() {
        int expectedYield = 1;

        Building building = new Building();
        Assertions.assertEquals(expectedYield, building.getYield(Resource.ORE));
    }

    @Test
    void testGetYield_withCityEmptyDistrictGettingOre_expectTwoOre() {
        int expectedYield = 2;

        Building building = new Building();
        building.upgradeToCity();
        Assertions.assertEquals(expectedYield, building.getYield(Resource.ORE));
    }

    @Test
    void testGetYield_withSettlementMineGettingOre_expectThreeOre() {
        int expectedYield = 3;

        Building building = new Building();
        building.buildDistrict(DistrictType.MINE);
        Assertions.assertEquals(expectedYield, building.getYield(Resource.ORE));
    }

    @Test
    void testGetYield_withSettlementMineGettingWool_expect1Wool() {
        int expectedYield = 1;

        Building building = new Building();
        building.buildDistrict(DistrictType.MINE);
        Assertions.assertEquals(expectedYield, building.getYield(Resource.WOOL));
    }

    @Test
    void testGetYield_withCityMineGettingOre_expectThreeOre() {
        int expectedYield = 3;

        Building building = new Building();
        building.upgradeToCity();
        building.buildDistrict(DistrictType.MINE);
        Assertions.assertEquals(expectedYield, building.getYield(Resource.ORE));
    }

    @Test
    void testGetYield_withSettlementKilnGettingBrick_expectThreeBrick() {
        int expectedYield = 3;

        Building building = new Building();
        building.buildDistrict(DistrictType.KILN);
        Assertions.assertEquals(expectedYield, building.getYield(Resource.BRICK));
    }

    @Test
    void testGetYield_withSettlementKilnGettingOre_expectOneOre() {
        int expectedYield = 1;

        Building building = new Building();
        building.buildDistrict(DistrictType.KILN);
        Assertions.assertEquals(expectedYield, building.getYield(Resource.ORE));
    }

    @Test
    void testGetYield_withCityKilnGettingBrick_expectThreeBrick() {
        int expectedYield = 3;

        Building building = new Building();
        building.upgradeToCity();
        building.buildDistrict(DistrictType.KILN);
        Assertions.assertEquals(expectedYield, building.getYield(Resource.BRICK));
    }

    @Test
    void testGetYield_withSettlementSawmillGettingLumber_expectThreeLumber() {
        int expectedYield = 3;

        Building building = new Building();
        building.buildDistrict(DistrictType.SAWMILL);
        Assertions.assertEquals(expectedYield, building.getYield(Resource.LUMBER));
    }

    @Test
    void testGetYield_withSettlementSawmillGettingOre_expectOneOre() {
        int expectedYield = 1;

        Building building = new Building();
        building.buildDistrict(DistrictType.SAWMILL);
        Assertions.assertEquals(expectedYield, building.getYield(Resource.ORE));
    }

    @Test
    void testGetYield_withCitySawmillGettingLumber_expectThreeLumber() {
        int expectedYield = 3;

        Building building = new Building();
        building.upgradeToCity();
        building.buildDistrict(DistrictType.SAWMILL);
        Assertions.assertEquals(expectedYield, building.getYield(Resource.LUMBER));
    }

    @Test
    void testGetYield_withSettlementGardenGettingGrain_expectThreeGrain() {
        int expectedYield = 3;

        Building building = new Building();
        building.buildDistrict(DistrictType.GARDEN);
        Assertions.assertEquals(expectedYield, building.getYield(Resource.GRAIN));
    }

    @Test
    void testGetYield_withSettlementGardenGettingOre_expectOneOre() {
        int expectedYield = 1;

        Building building = new Building();
        building.buildDistrict(DistrictType.GARDEN);
        Assertions.assertEquals(expectedYield, building.getYield(Resource.ORE));
    }

    @Test
    void testGetYield_withCityGardenGettingGrain_expectThreeGrain() {
        int expectedYield = 3;

        Building building = new Building();
        building.upgradeToCity();
        building.buildDistrict(DistrictType.GARDEN);
        Assertions.assertEquals(expectedYield, building.getYield(Resource.GRAIN));
    }

    @Test
    void testGetYield_withSettlementBarnGettingWool_expectThreeWool() {
        int expectedYield = 3;

        Building building = new Building();
        building.buildDistrict(DistrictType.BARN);
        Assertions.assertEquals(expectedYield, building.getYield(Resource.WOOL));
    }

    @Test
    void testGetYield_withSettlementBarnGettingOre_expectOneOre() {
        int expectedYield = 1;

        Building building = new Building();
        building.buildDistrict(DistrictType.BARN);
        Assertions.assertEquals(expectedYield, building.getYield(Resource.ORE));
    }

    @Test
    void testGetYield_withCityBarnGettingWool_expectThreeWool() {
        int expectedYield = 3;

        Building building = new Building();
        building.upgradeToCity();
        building.buildDistrict(DistrictType.BARN);
        Assertions.assertEquals(expectedYield, building.getYield(Resource.WOOL));
    }

    @Test
    void testBuildDistrict_withExistingDistrictNewMine_expectIllegalArgumentException() {
        Building building = new Building();
        building.buildDistrict(DistrictType.BARN);

        Assertions.assertThrows(IllegalArgumentException.class, () -> building.buildDistrict(DistrictType.MINE));
    }

    @Test
    void testBuildDistrict_withEmptyDistrictNewMine_expectMineCreated() {
        Building building = new Building();
        building.buildDistrict(DistrictType.MINE);

        Assertions.assertEquals(DistrictType.MINE, building.getDistrict());
    }
}
