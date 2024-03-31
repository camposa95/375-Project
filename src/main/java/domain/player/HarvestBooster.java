package domain.player;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import domain.bank.Resource;
import data.*;

import java.io.File;
import java.util.*;

public class HarvestBooster implements Restorable {
    private static final int DURATION = 5;
    private static final int DONE_TIME = 1;

    private final Map<Resource, BoostType> boosts;
    private final Map<Resource, Integer> durations;

    public HarvestBooster() {
        this.boosts = new HashMap<>();
        this.boosts.put(Resource.BRICK, BoostType.NONE);
        this.boosts.put(Resource.LUMBER, BoostType.NONE);
        this.boosts.put(Resource.ORE, BoostType.NONE);
        this.boosts.put(Resource.GRAIN, BoostType.NONE);
        this.boosts.put(Resource.WOOL, BoostType.NONE);

        this.durations = new HashMap<>();
        this.durations.put(Resource.BRICK, DONE_TIME);
        this.durations.put(Resource.LUMBER, DONE_TIME);
        this.durations.put(Resource.ORE, DONE_TIME);
        this.durations.put(Resource.GRAIN, DONE_TIME);
        this.durations.put(Resource.WOOL, DONE_TIME);
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
        advanceClock();
        return adjustedHarvest.toArray(new Resource[0]);
    }

    public void setBoost(final Resource resource, final BoostType boostType) {
        this.boosts.put(resource, boostType);
        this.durations.put(resource, DURATION);
    }

    private void advanceClock() {
        // advance the clock for all of them
        for (Map.Entry<Resource, Integer> boost : this.durations.entrySet()) {
            Resource resource = boost.getKey();
            Integer duration = boost.getValue();
            this.durations.put(resource, Math.max(DONE_TIME, duration - 1));
            if (duration == DONE_TIME) { // if they wore out change remove the boost
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
            readBoostMap(folder, this.boosts);
            readDurationMap(folder, this.durations);
        }

        private void readBoostMap(final File folder, final Map<Resource, BoostType> map) {
            MementoReader reader = new MementoReader(folder, HarvestBoosterMemento.BOOSTS_FILE_NAME);

            for (Map.Entry<String, String> entry : reader.readAllFields().entrySet()) {
                map.put(Resource.valueOf(entry.getKey()),
                        BoostType.valueOf(entry.getValue()));
            }
        }

        private void readDurationMap(final File folder, final Map<Resource, Integer> map) {
            MementoReader reader = new MementoReader(folder, HarvestBoosterMemento.DURATIONS_FILE_NAME);

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
