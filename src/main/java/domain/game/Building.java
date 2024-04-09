package domain.game;

import data.*;
import domain.bank.Resource;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.File;

public class Building implements Restorable {
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

    @Override
    public Memento createMemento() {
        return new BuildingMemento();
    }

    public class BuildingMemento implements Memento {
        private boolean isCity;
        private DistrictType district;

        private static final String TARGET_FILE_NAME = "building.txt";
        private static final String IS_CITY = "IsCity";
        private static final String DISTRICT = "District";

        private BuildingMemento() {
            this.isCity = Building.this.isCity;
            this.district = Building.this.district;
        }

        @SuppressFBWarnings("EI_EXPOSE_REP2")
        public BuildingMemento(final File folder) {
            // Create a MementoReader for reading memento data
            MementoReader reader = new MementoReader(folder, TARGET_FILE_NAME);

            // Read simple fields from the file
            this.isCity = Boolean.parseBoolean(reader.readField(IS_CITY));
            this.district = DistrictType.valueOf(reader.readField(DISTRICT));
        }

        @Override
        public void save(File folder) throws SaveException {
            // Create a MementoWriter for writing memento data
            MementoWriter writer = new MementoWriter(folder, TARGET_FILE_NAME);

            // Write simple fields to the file
            writer.writeField(DISTRICT, district.toString());
            writer.writeField(IS_CITY, Boolean.toString(isCity));
        }

        @Override
        public void restore() {
            Building.this.isCity = this.isCity;
            Building.this.district = this.district;
        }
    }
}
