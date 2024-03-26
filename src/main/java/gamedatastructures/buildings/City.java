package gamedatastructures.buildings;

public class City extends Building {
    public City(Settlement originalSettlement) {
        this.brickResourceBonus = getUpgradedResourceBonus(originalSettlement.brickResourceBonus);
        this.grainResourceBonus = getUpgradedResourceBonus(originalSettlement.grainResourceBonus);
        this.lumberResourceBonus = getUpgradedResourceBonus(originalSettlement.lumberResourceBonus);
        this.oreResourceBonus = getUpgradedResourceBonus(originalSettlement.oreResourceBonus);
        this.woolResourceBonus = getUpgradedResourceBonus(originalSettlement.woolResourceBonus);
    }

    public City() {
        this.brickResourceBonus = 2;
        this.grainResourceBonus = 2;
        this.lumberResourceBonus = 2;
        this.oreResourceBonus = 2;
        this.woolResourceBonus = 2;
    }

    /**
     * Upgrades resource bonus to 2, unless it has been upgraded already,
     * in which case the original bonus doesn't change
     *
     * @param originalBonus the bonus on the original settlement the city is upgraded from
     * @return the new bonus for the city
     */
    private int getUpgradedResourceBonus(int originalBonus) {
        return (originalBonus <= 1) ? originalBonus + 1 : originalBonus;
    }
}
