package gamedatastructures;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import saving.*;

import java.io.File;
import java.util.*;

public class HarvestBooster implements Restorable {
    private static final int DURATION = 5;

    private final Map<Resource, BoostType> boosts;
    private final Map<Resource, Integer> durations;

    public HarvestBooster() {
        this.boosts = new HashMap<Resource, BoostType>();
        this.boosts.put(Resource.BRICK, BoostType.NONE);
        this.boosts.put(Resource.LUMBER, BoostType.NONE);
        this.boosts.put(Resource.ORE, BoostType.NONE);
        this.boosts.put(Resource.GRAIN, BoostType.NONE);
        this.boosts.put(Resource.WOOL, BoostType.NONE);

        this.durations = new HashMap<Resource, Integer>();
        this.durations.put(Resource.BRICK, 0);
        this.durations.put(Resource.LUMBER, 0);
        this.durations.put(Resource.ORE, 0);
        this.durations.put(Resource.GRAIN, 0);
        this.durations.put(Resource.WOOL, 0);
    }

    public Resource[] getAdjustedHarvest(final Resource[] resources) {
        List<Resource> adjustedHarvest = new LinkedList<>();

        for (Resource resource : resources) {
            BoostType boost = this.boosts.get(resource);
            if (boost == BoostType.NONE) {
                adjustedHarvest.add(resource);
            } else if (boost == BoostType.DOUBLE) {
                adjustedHarvest.add(resource);
                adjustedHarvest.add(resource);
            } // else don't add it since it disabled
        }

        return adjustedHarvest.toArray(new Resource[0]);
    }

    public void setBoost(final Resource resource, final BoostType boostType) {
        this.boosts.put(resource, boostType);
        this.durations.put(resource, DURATION);
    }

    public void notifyOfTurn() {
        // advance the clock for all of them
        for (Map.Entry<Resource, Integer> boost : this.durations.entrySet()) {
            Resource resource = boost.getKey();
            Integer duration = boost.getValue();
            this.durations.put(resource, Math.max(0, duration - 1));
            if (duration == 0) { // if they wore out change remove the boost
                this.boosts.put(resource, BoostType.NONE);
            }
        }
    }

    // -----------------------------------
    //
    // Restorable implementation
    //
    // -----------------------------------

    public class HarvestBoosterMemento implements Memento {

        private final Map<Resource, BoostType> boosts;
        private final Map<Resource, Integer> durations;

        // Storage Constants
        private static final String BOOSTS_FILE_NAME = "Boosts.txt";
        private static final String DURATIONS_FILE_NAME = "Durations.txt";

        private HarvestBoosterMemento() {
            this.boosts = new HashMap<>();
            this.boosts.putAll(HarvestBooster.this.boosts);

            this.durations = new HashMap<>();
            this.durations.putAll(HarvestBooster.this.durations);
        }

        @SuppressFBWarnings("EI_EXPOSE_REP2")
        public HarvestBoosterMemento(final File folder) {
            // Initialize the HashMaps
            this.boosts = new HashMap<>();
            this.durations = new HashMap<>();

            // Read data from separate files and populate the HashMaps
            readBoostMap(folder, BOOSTS_FILE_NAME, this.boosts);
            readDurationMap(folder, DURATIONS_FILE_NAME, this.durations);
        }

        private void readBoostMap(final File folder, final String fileName, final Map<Resource, BoostType> map) {
            MementoReader reader = new MementoReader(folder, fileName);

            for (Map.Entry<String, String> entry : reader.readAllFields().entrySet()) {
                map.put(Resource.valueOf(entry.getKey()),
                        BoostType.valueOf(entry.getValue()));
            }
        }

        private void readDurationMap(final File folder, final String fileName, final Map<Resource, Integer> map) {
            MementoReader reader = new MementoReader(folder, fileName);

            for (Map.Entry<String, String> entry : reader.readAllFields().entrySet()) {
                map.put(Resource.valueOf(entry.getKey()),
                        Integer.parseInt(entry.getValue()));
            }
        }

        public void save(final File folder) throws SaveException {
            // Write the state of the class's attributes to separate files
            writeHashMap(folder, BOOSTS_FILE_NAME, this.boosts);
            writeHashMap(folder, DURATIONS_FILE_NAME, this.durations);
        }

        // Helper method to write a HashMap to a separate file
        private void writeHashMap(final File folder, final String fileName, final Map<?, ?> hashMap) throws SaveException {
            // Create a MementoWriter for the current map
            MementoWriter writer = new MementoWriter(folder, fileName);

            // Write each entry of the map to the file
            for (Map.Entry<?, ?> entry : hashMap.entrySet()) {
                writer.writeField(entry.getKey().toString(), entry.getValue().toString());
            }
        }

        @Override
        public void restore() {
            HarvestBooster.this.boosts.clear();
            HarvestBooster.this.boosts.putAll(this.boosts);

            // Restore the state of the devCards
            HarvestBooster.this.durations.clear();
            HarvestBooster.this.durations.putAll(this.durations);
        }
    }

    @Override
    public Memento createMemento() {
        return new HarvestBoosterMemento();
    }
}
