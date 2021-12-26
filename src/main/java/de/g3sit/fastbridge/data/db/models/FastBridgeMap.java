package de.g3sit.fastbridge.data.db.models;

import org.bukkit.Location;

public class FastBridgeMap {

    private final long id;
    private String name;
    /**
     * minimum location of the region which is used as a template for each new region
     */
    private Location rootRegionMin;
    /**
     * maximum location of the region which is used as a template for each new region
     */
    private Location rootRegionMax;
    /**
     * Location where the player spawns
     */
    private Location spawnLocation;

    public FastBridgeMap(long id, String name, Location rootRegionMin, Location rootRegionMax, Location spawnLocation) {
        this.id = id;
        this.name = name;
        this.rootRegionMin = rootRegionMin;
        this.rootRegionMax = rootRegionMax;
        this.spawnLocation = spawnLocation;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getRootRegionMin() {
        return rootRegionMin;
    }

    public void setRootRegionMin(Location rootRegionMin) {
        this.rootRegionMin = rootRegionMin;
    }

    public Location getRootRegionMax() {
        return rootRegionMax;
    }

    public void setRootRegionMax(Location rootRegionMax) {
        this.rootRegionMax = rootRegionMax;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }
}
