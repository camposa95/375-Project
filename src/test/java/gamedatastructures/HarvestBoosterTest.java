package gamedatastructures;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

public class HarvestBoosterTest {
    @Test
    public void testSingleDoubleBoost() {
        // Set up the boosts
        HarvestBooster booster = new HarvestBooster();
        booster.setBoost(Resource.LUMBER, BoostType.DOUBLE);

        // Tests input
        Resource[] unadjustedResources = {Resource.LUMBER, Resource.GRAIN, Resource.WOOL, Resource.ORE, Resource.BRICK};

        // Expected results
        Map<Resource, Integer> expectedResourceCounts = new HashMap<>();
        expectedResourceCounts.put(Resource.LUMBER, 2);
        expectedResourceCounts.put(Resource.GRAIN, 1);
        expectedResourceCounts.put(Resource.WOOL, 1);
        expectedResourceCounts.put(Resource.ORE, 1);
        expectedResourceCounts.put(Resource.BRICK, 1);

        // Method call
        Resource[] adjustedResources = booster.getAdjustedHarvest(unadjustedResources);
        Map<Resource, Integer> actualResourceCounts = countResources(adjustedResources);

        assertEquals(expectedResourceCounts, actualResourceCounts);
    }

    @Test
    public void testSingleDisableBoost() {
        // Set up the boosts
        HarvestBooster booster = new HarvestBooster();
        booster.setBoost(Resource.WOOL, BoostType.ZERO);

        // Tests input
        Resource[] unadjustedResources = {Resource.LUMBER, Resource.GRAIN, Resource.WOOL, Resource.ORE, Resource.BRICK};

        // Expected results
        Map<Resource, Integer> expectedResourceCounts = new HashMap<>();
        expectedResourceCounts.put(Resource.LUMBER, 1);
        expectedResourceCounts.put(Resource.GRAIN, 1);
        expectedResourceCounts.put(Resource.WOOL, 0);
        expectedResourceCounts.put(Resource.ORE, 1);
        expectedResourceCounts.put(Resource.BRICK, 1);

        // Method call
        Resource[] adjustedResources = booster.getAdjustedHarvest(unadjustedResources);
        Map<Resource, Integer> actualResourceCounts = countResources(adjustedResources);

        assertEquals(expectedResourceCounts, actualResourceCounts);
    }

    @Test
    public void testSingleNoneBoost() {
        // Set up the boosts
        HarvestBooster booster = new HarvestBooster();
        // No boosts added

        // Tests input
        Resource[] unadjustedResources = {Resource.LUMBER, Resource.GRAIN, Resource.WOOL, Resource.ORE, Resource.BRICK};

        // Expected results
        Map<Resource, Integer> expectedResourceCounts = new HashMap<>();
        expectedResourceCounts.put(Resource.LUMBER, 1);
        expectedResourceCounts.put(Resource.GRAIN, 1);
        expectedResourceCounts.put(Resource.WOOL, 1);
        expectedResourceCounts.put(Resource.ORE, 1);
        expectedResourceCounts.put(Resource.BRICK, 1);

        // Method call
        Resource[] adjustedResources = booster.getAdjustedHarvest(unadjustedResources);
        Map<Resource, Integer> actualResourceCounts = countResources(adjustedResources);

        assertEquals(expectedResourceCounts, actualResourceCounts);
    }

    @Test
    public void testVarietyBoost() {
        // Set up the boosts
        HarvestBooster booster = new HarvestBooster();
        booster.setBoost(Resource.LUMBER, BoostType.ZERO);
        booster.setBoost(Resource.GRAIN, BoostType.DOUBLE);
        // none for wool
        booster.setBoost(Resource.ORE, BoostType.ZERO);
        booster.setBoost(Resource.BRICK, BoostType.DOUBLE);


        // Tests input
        Resource[] unadjustedResources = {Resource.LUMBER, Resource.GRAIN, Resource.WOOL, Resource.ORE, Resource.BRICK};

        // Expected results
        Map<Resource, Integer> expectedResourceCounts = new HashMap<>();
        expectedResourceCounts.put(Resource.LUMBER, 0);
        expectedResourceCounts.put(Resource.GRAIN, 2);
        expectedResourceCounts.put(Resource.WOOL, 1);
        expectedResourceCounts.put(Resource.ORE, 0);
        expectedResourceCounts.put(Resource.BRICK, 2);

        // Method call
        Resource[] adjustedResources = booster.getAdjustedHarvest(unadjustedResources);
        Map<Resource, Integer> actualResourceCounts = countResources(adjustedResources);

        assertEquals(expectedResourceCounts, actualResourceCounts);
    }

    @Test
    public void testDuration() {
        HarvestBooster booster = new HarvestBooster();
        // Tests input & output declarations
        Resource[] unadjustedResources = {Resource.LUMBER, Resource.GRAIN, Resource.WOOL, Resource.ORE, Resource.BRICK};
        Map<Resource, Integer> expectedResourceCounts = new HashMap<>();
        Resource[] adjustedResources;
        Map<Resource, Integer> actualResourceCounts;

        // ------------------------------------------------
        // 1st turn
        // -------------------------------------------------
        // Set up the boosts
        booster.setBoost(Resource.LUMBER, BoostType.ZERO);
        // Expected results
        expectedResourceCounts.put(Resource.LUMBER, 0);
        expectedResourceCounts.put(Resource.GRAIN, 1);
        expectedResourceCounts.put(Resource.WOOL, 1);
        expectedResourceCounts.put(Resource.ORE, 1);
        expectedResourceCounts.put(Resource.BRICK, 1);
        // Assertions
        adjustedResources = booster.getAdjustedHarvest(unadjustedResources);
        actualResourceCounts = countResources(adjustedResources);
        assertEquals(expectedResourceCounts, actualResourceCounts);

        // ------------------------------------------------
        // 2nd turn
        // -------------------------------------------------
        // Set up the boosts
        booster.setBoost(Resource.GRAIN, BoostType.DOUBLE);
        // Expected results
        expectedResourceCounts.put(Resource.GRAIN, 2); // additional boost, other should still be active
        // Assertions
        adjustedResources = booster.getAdjustedHarvest(unadjustedResources);
        actualResourceCounts = countResources(adjustedResources);
        assertEquals(expectedResourceCounts, actualResourceCounts);

        // ------------------------------------------------
        // 3rd turn
        // -------------------------------------------------
        // No additional boosts
        // Same results as before
        // Assertions
        adjustedResources = booster.getAdjustedHarvest(unadjustedResources);
        actualResourceCounts = countResources(adjustedResources);
        assertEquals(expectedResourceCounts, actualResourceCounts);

        // ------------------------------------------------
        // 4th turn
        // -------------------------------------------------
        // No additional boosts
        // Same results as before
        // Assertions
        adjustedResources = booster.getAdjustedHarvest(unadjustedResources);
        actualResourceCounts = countResources(adjustedResources);
        assertEquals(expectedResourceCounts, actualResourceCounts);

        // ------------------------------------------------
        // 5th turn
        // -------------------------------------------------
        // No additional boosts
        // Same results as before
        // Assertions
        adjustedResources = booster.getAdjustedHarvest(unadjustedResources);
        actualResourceCounts = countResources(adjustedResources);
        assertEquals(expectedResourceCounts, actualResourceCounts);

        // ------------------------------------------------
        // 6th turn
        // -------------------------------------------------
        // No additional boosts
        // Expected results
        expectedResourceCounts.put(Resource.LUMBER, 1); // 1st boost should have worn off after the 5th turn
        // Assertions
        adjustedResources = booster.getAdjustedHarvest(unadjustedResources);
        actualResourceCounts = countResources(adjustedResources);
        assertEquals(expectedResourceCounts, actualResourceCounts);

        // ------------------------------------------------
        // 7th turn
        // -------------------------------------------------
        // No additional boosts
        // Expected results
        expectedResourceCounts.put(Resource.GRAIN, 1); // 2nd boost should have worn off after the 6th turn
        // Assertions
        adjustedResources = booster.getAdjustedHarvest(unadjustedResources);
        actualResourceCounts = countResources(adjustedResources);
        assertEquals(expectedResourceCounts, actualResourceCounts);
    }

    private Map<Resource, Integer> countResources(Resource[] resources) {
        Map<Resource, Integer> resourceCounts = new HashMap<>();
        resourceCounts.put(Resource.LUMBER, 0);
        resourceCounts.put(Resource.GRAIN, 0);
        resourceCounts.put(Resource.WOOL, 0);
        resourceCounts.put(Resource.ORE, 0);
        resourceCounts.put(Resource.BRICK, 0);

        // count up the resources
        for (Resource resource : resources) {
            resourceCounts.put(resource, resourceCounts.get(resource) + 1);
        }

        return resourceCounts;
    }
}
